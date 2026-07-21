package com.controlself.edu.domain.model.parent

import com.controlself.edu.domain.model.motivation.AchievementBadge
import com.controlself.edu.domain.model.stats.AttemptStat
import com.controlself.edu.domain.model.stats.CourseStatBar
import com.controlself.edu.domain.model.stats.StudentStatsDashboard

data class LinkedChild(
    val id: String,
    val displayName: String,
    val linkCode: String
) {
    companion object {
        val DEMO = LinkedChild(
            id = "estudiante",
            displayName = "Estudiante Demo",
            linkCode = "DEMO-001"
        )
    }
}

data class DayUsagePoint(
    val dayIso: String,
    val label: String,
    val studyMinutes: Int,
    val entertainmentMinutes: Int
)

data class ParentDashboard(
    val child: LinkedChild = LinkedChild.DEMO,
    val stats: StudentStatsDashboard = StudentStatsDashboard(),
    val badges: List<AchievementBadge> = emptyList(),
    val weekly: List<DayUsagePoint> = emptyList(),
    val recentAttempts: List<AttemptStat> = emptyList(),
    val courseBars: List<CourseStatBar> = emptyList(),
    val entertainmentWeekTotal: Int = 0
)
