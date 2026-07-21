package com.controlself.edu.domain.parent

import com.controlself.edu.domain.model.parent.DayUsagePoint
import com.controlself.edu.domain.model.parent.LinkedChild
import com.controlself.edu.domain.model.parent.ParentDashboard
import com.controlself.edu.domain.model.stats.AttemptStat
import com.controlself.edu.domain.repository.AchievementRepository
import com.controlself.edu.domain.repository.ScreenTimeRepository
import com.controlself.edu.domain.repository.StatsRepository
import com.controlself.edu.domain.stats.StatsAggregator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Vista padre del hijo vinculado (MVP local: datos del dispositivo;
 * identidad demo alineada con seed auth `padre` → `estudiante`).
 */
class ParentDashboardAggregator(
    private val statsRepository: StatsRepository,
    private val screenTimeRepository: ScreenTimeRepository,
    private val achievementRepository: AchievementRepository
) {
    fun observeDashboard(): Flow<ParentDashboard> = combine(
        statsRepository.observeAttempts(),
        screenTimeRepository.observeTodayMinutes(),
        screenTimeRepository.observeWeeklyEntertainmentMinutes(),
        achievementRepository.observeSnapshot()
    ) { attempts, entertainmentToday, weeklyEntertainment, motivation ->
        val stats = StatsAggregator.build(attempts, entertainmentToday, motivation)
        val weekly = buildWeekly(attempts, weeklyEntertainment)
        ParentDashboard(
            child = LinkedChild.DEMO,
            stats = stats,
            badges = motivation.badges,
            weekly = weekly,
            recentAttempts = attempts.sortedByDescending { it.timestampMillis }.take(10),
            courseBars = stats.courseBars,
            entertainmentWeekTotal = weekly.sumOf { it.entertainmentMinutes }
        )
    }

    companion object {
        fun buildWeekly(
            attempts: List<AttemptStat>,
            entertainmentByDay: Map<String, Int>
        ): List<DayUsagePoint> {
            val zone = ZoneId.systemDefault()
            val studyByDay = attempts.groupBy { attempt ->
                Instant.ofEpochMilli(attempt.timestampMillis)
                    .atZone(zone)
                    .toLocalDate()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE)
            }.mapValues { (_, list) -> list.sumOf { it.durationMinutes } }

            val dayFmt = DateTimeFormatter.ofPattern("EEE", Locale("es", "PE"))
            val today = LocalDate.now()
            return (6 downTo 0).map { offset ->
                val day = today.minusDays(offset.toLong())
                val iso = day.format(DateTimeFormatter.ISO_LOCAL_DATE)
                DayUsagePoint(
                    dayIso = iso,
                    label = day.format(dayFmt).replaceFirstChar { it.uppercase() },
                    studyMinutes = studyByDay[iso] ?: 0,
                    entertainmentMinutes = entertainmentByDay[iso] ?: 0
                )
            }
        }
    }
}
