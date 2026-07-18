package com.controlself.edu.domain.stats

import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.motivation.AchievementId
import com.controlself.edu.domain.model.motivation.MotivationSnapshot
import com.controlself.edu.domain.model.stats.AttemptStat
import com.controlself.edu.domain.model.stats.CourseStatBar
import com.controlself.edu.domain.model.stats.ScorePoint
import com.controlself.edu.domain.model.stats.StudentStatsDashboard
import com.controlself.edu.domain.repository.AchievementRepository
import com.controlself.edu.domain.repository.ScreenTimeRepository
import com.controlself.edu.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Une repositorios existentes sin duplicar la fuente de verdad (PRP-10).
 */
class StatsAggregator(
    private val statsRepository: StatsRepository,
    private val screenTimeRepository: ScreenTimeRepository,
    private val achievementRepository: AchievementRepository
) {
    fun observeDashboard(): Flow<StudentStatsDashboard> = combine(
        statsRepository.observeAttempts(),
        screenTimeRepository.observeTodayMinutes(),
        achievementRepository.observeSnapshot()
    ) { attempts, entertainmentMinutes, motivation ->
        build(attempts, entertainmentMinutes, motivation)
    }

    companion object {
        fun build(
            attempts: List<AttemptStat>,
            entertainmentMinutes: Int,
            motivation: MotivationSnapshot
        ): StudentStatsDashboard {
            val ordered = attempts.sortedBy { it.timestampMillis }
            val studyMinutes = ordered.sumOf { it.durationMinutes }
            val average = motivation.averageScore
                ?: ordered.takeIf { it.isNotEmpty() }?.let { list ->
                    list.sumOf { it.correctCount }.toDouble() / list.size
                }

            val recent = ordered.takeLast(8).mapIndexed { index, stat ->
                ScorePoint(
                    label = "${index + 1}",
                    score = stat.correctCount,
                    maxScore = stat.total
                )
            }

            val byCourse = Course.entries.map { course ->
                val courseAttempts = ordered.filter { it.courseId == course.id }
                CourseStatBar(
                    courseId = course.id,
                    label = shortCourseLabel(course),
                    passedCount = courseAttempts.count { it.passed },
                    attemptCount = courseAttempts.size
                )
            }

            val distinctPassed = ordered
                .filter { it.passed }
                .map { it.courseId }
                .toSet()
                .size

            return StudentStatsDashboard(
                evaluationsCount = motivation.completedAttempts.coerceAtLeast(ordered.size),
                averageScore = average,
                studyMinutesTotal = studyMinutes,
                entertainmentMinutesToday = entertainmentMinutes,
                coursesPassedDistinct = distinctPassed,
                achievementsUnlocked = motivation.unlocked.size,
                achievementsTotal = AchievementId.ALL.size,
                currentStreak = motivation.streak.currentDays,
                maxStreak = motivation.maxStreakDays.coerceAtLeast(motivation.streak.currentDays),
                recentScores = recent,
                courseBars = byCourse,
                hasAttemptData = ordered.isNotEmpty()
            )
        }

        private fun shortCourseLabel(course: Course): String = when (course) {
            Course.MATH -> "Mate"
            Course.COMMS -> "Comms"
            Course.SCIENCE -> "CyT"
        }
    }
}
