package com.controlself.edu.domain.repository

import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.model.teacher.ClassroomStudent
import com.controlself.edu.domain.model.teacher.QuestionDifficulty
import com.controlself.edu.domain.model.teacher.TeacherDashboard
import kotlinx.coroutines.flow.Flow

interface QuestionAnalyticsRepository {
    fun observeDifficulties(): Flow<List<QuestionDifficulty>>

    suspend fun recordAttemptAnswers(attempt: QuizAttempt)
}

interface ClassroomRepository {
    fun observeDashboard(): Flow<TeacherDashboard>

    fun getStudent(id: String): ClassroomStudent?

    fun observeStudents(): Flow<List<ClassroomStudent>>
}
