package com.example.swpp.tododapp.statistics

import com.example.swpp.tododapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticsUtilsTest {

    @Test
    fun getActiveAndCompletedStats_two_return50f50f() {
        // GIVEN - One completed task and one active task
        val tasks = listOf(
            Task("First", "Description", true, "completed-id"),
            Task("Second", "Description", false, "active-id")
        )

        // WHEN - Compute the statistics
        val result = getActiveAndCompletedStats(tasks)

        // THEN - Both percentages are 50%
        assertEquals(50f, result.activeTasksPercent)
        assertEquals(50f, result.completedTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_empty_return0f0f() {
        // GIVEN - No tasks
        val tasks = emptyList<Task>()

        // WHEN - Compute the statistics
        val result = getActiveAndCompletedStats(tasks)

        // THEN - Both percentages are 0%
        assertEquals(0f, result.activeTasksPercent)
        assertEquals(0f, result.completedTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_twoCompleted_return0f100f() {
        // GIVEN - Two completed tasks
        val tasks = listOf(
            Task("First", "Description", true, "completed-id-1"),
            Task("Second", "Description", true, "completed-id-2")
        )

        // WHEN - Compute the statistics
        val result = getActiveAndCompletedStats(tasks)

        // THEN - Active is 0% and completed is 100%
        assertEquals(0f, result.activeTasksPercent)
        assertEquals(100f, result.completedTasksPercent)
    }
}
