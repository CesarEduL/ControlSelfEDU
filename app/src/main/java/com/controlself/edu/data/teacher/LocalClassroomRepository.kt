package com.controlself.edu.data.teacher

import com.controlself.edu.domain.model.motivation.MotivationSnapshot
import com.controlself.edu.domain.model.stats.AttemptStat
import com.controlself.edu.domain.model.teacher.ClassroomStudent
import com.controlself.edu.domain.model.teacher.QuestionDifficulty
import com.controlself.edu.domain.model.teacher.TeacherDashboard
import com.controlself.edu.domain.repository.AchievementRepository
import com.controlself.edu.domain.repository.ClassroomRepository
import com.controlself.edu.domain.repository.LockRepository
import com.controlself.edu.domain.repository.QuestionAnalyticsRepository
import com.controlself.edu.domain.repository.QuizRepository
import com.controlself.edu.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Salón local MVP: estudiantes mock + fila del Estudiante Demo con datos reales del dispositivo.
 */
class LocalClassroomRepository(
    private val statsRepository: StatsRepository,
    private val achievementRepository: AchievementRepository,
    private val lockRepository: LockRepository,
    private val quizRepository: QuizRepository,
    private val analyticsRepository: QuestionAnalyticsRepository
) : ClassroomRepository {

    private val mockStudents = listOf(
        ClassroomStudent(
            id = "mock-ana",
            displayName = "Ana Pérez",
            lastScores = listOf(18, 16, 15),
            averageScore = 16.3,
            evaluationsCount = 3,
            isLocked = false
        ),
        ClassroomStudent(
            id = "mock-luis",
            displayName = "Luis Gómez",
            lastScores = listOf(12, 14, 11),
            averageScore = 12.3,
            evaluationsCount = 3,
            isLocked = true
        ),
        ClassroomStudent(
            id = "mock-maria",
            displayName = "María Ruiz",
            lastScores = listOf(19, 17),
            averageScore = 18.0,
            evaluationsCount = 2,
            isLocked = false
        )
    )

    private var latestStudents: List<ClassroomStudent> = mockStudents

    override fun observeStudents(): Flow<List<ClassroomStudent>> =
        combine(
            statsRepository.observeAttempts(),
            achievementRepository.observeSnapshot(),
            lockRepository.observeLocked()
        ) { attempts, motivation, locked ->
            val demo = buildDemoStudent(attempts, motivation, locked)
            (listOf(demo) + mockStudents).also { latestStudents = it }
        }

    override fun observeDashboard(): Flow<TeacherDashboard> =
        combine(
            observeStudents(),
            quizRepository.observeBankStatus(),
            analyticsRepository.observeDifficulties()
        ) { students, bank, difficulties ->
            val averages = students.mapNotNull { it.averageScore }
            TeacherDashboard(
                studentCount = students.size,
                classroomAverage = averages.takeIf { it.isNotEmpty() }?.average(),
                publishedCourses = bank.count { it.isReady },
                hardTopicsPreview = difficulties.filter { it.attempts > 0 }.take(3),
                students = students
            )
        }

    override fun getStudent(id: String): ClassroomStudent? =
        latestStudents.find { it.id == id } ?: mockStudents.find { it.id == id }

    private fun buildDemoStudent(
        attempts: List<AttemptStat>,
        motivation: MotivationSnapshot,
        locked: Boolean
    ): ClassroomStudent {
        val scores = attempts.takeLast(5).map { it.correctCount }
        return ClassroomStudent(
            id = DEMO_STUDENT_ID,
            displayName = "Estudiante Demo",
            lastScores = scores,
            averageScore = motivation.averageScore
                ?: scores.takeIf { it.isNotEmpty() }?.average(),
            evaluationsCount = motivation.completedAttempts.coerceAtLeast(attempts.size),
            isLocked = locked,
            isLocalDemo = true
        )
    }

    companion object {
        const val DEMO_STUDENT_ID = "local-demo-student"
    }
}
