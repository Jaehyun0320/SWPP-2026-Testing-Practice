package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {

    @Test
    fun dueDateLabel_completedUpcoming_returnsCLEAR() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-10-22")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, true)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_completedUndated_returnsCLEAR() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = null

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, true)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_activeUndated_returnsEmpty() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = null

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("", label)
    }

    @Test
    fun dueDateLabel_activeThreeDaysAhead_returnsDMinus3() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-10-22")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-3", label)
    }

    @Test
    fun dueDateLabel_dueToday_returnsDDay() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-10-19")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_activeThreeDaysOverdue_returnsDPlus3() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-10-16")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D+3", label)
    }

    @Test
    fun dueDateLabel_hundredDaysAhead_returnsDMinus100() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2027-01-27")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-100", label)
    }

    @Test
    fun dueDateLabel_oneDayOverdue_returnsDPlus1() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-10-18")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D+1", label)
    }

    @Test
    fun dueDateLabel_todayBoundary_returnsDDay() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-10-19")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_oneDayAhead_returnsDMinus1() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-10-20")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_nextMonthOneDayAhead_returnsDMinus1() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-31")
        val dueDate = LocalDate.parse("2026-11-01")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_monthBoundaryMinusOne_returnsDMinus30() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-11-18")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-30", label)
    }

    @Test
    fun dueDateLabel_exactMonthAhead_returnsDMinus31() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-11-19")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-31", label)
    }

    @Test
    fun dueDateLabel_monthBoundaryPlusOne_returnsDMinus32() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-11-20")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-32", label)
    }

    @Test
    fun dueDateLabel_commonFebruary_returnsDMinus28() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-02-01")
        val dueDate = LocalDate.parse("2026-03-01")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-28", label)
    }

    @Test
    fun dueDateLabel_leapFebruary_returnsDMinus29() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2028-02-01")
        val dueDate = LocalDate.parse("2028-03-01")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-29", label)
    }

    @Test
    fun dueDateLabel_januaryEndThroughFebruary_returnsDMinus29() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-01-31")
        val dueDate = LocalDate.parse("2026-03-01")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-29", label)
    }

    @Test
    fun dueDateLabel_commonFebruaryEnd_returnsDMinus1() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-02-28")
        val dueDate = LocalDate.parse("2026-03-01")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_leapDayAhead_returnsDMinus1() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2028-02-28")
        val dueDate = LocalDate.parse("2028-02-29")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_leapMarchAhead_returnsDMinus2() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2028-02-28")
        val dueDate = LocalDate.parse("2028-03-01")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-2", label)
    }

    @Test
    fun dueDateLabel_nextYearOneDayAhead_returnsDMinus1() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-12-31")
        val dueDate = LocalDate.parse("2027-01-01")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_commonYearAhead_returnsDMinus365() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2027-10-19")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-365", label)
    }

    @Test
    fun dueDateLabel_leapYearAhead_returnsDMinus366() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2027-10-19")
        val dueDate = LocalDate.parse("2028-10-19")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D-366", label)
    }

    @Test
    fun dueDateLabel_overdueMonthBoundaryMinusOne_returnsDPlus29() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-09-20")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D+29", label)
    }

    @Test
    fun dueDateLabel_exactMonthOverdue_returnsDPlus30() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-09-19")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D+30", label)
    }

    @Test
    fun dueDateLabel_overdueMonthBoundaryPlusOne_returnsDPlus31() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-09-18")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D+31", label)
    }

    @Test
    fun dueDateLabel_previousMonthOneDayOverdue_returnsDPlus1() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-01")
        val dueDate = LocalDate.parse("2026-09-30")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D+1", label)
    }

    @Test
    fun dueDateLabel_previousYearOneDayOverdue_returnsDPlus1() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-01-01")
        val dueDate = LocalDate.parse("2025-12-31")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D+1", label)
    }

    @Test
    fun dueDateLabel_commonYearOverdue_returnsDPlus365() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2025-10-19")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("D+365", label)
    }

    @Test
    fun dueDateLabel_completedOverdue_returnsCLEAR() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-10-16")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, true)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_completedToday_returnsCLEAR() {
        // GIVEN - The supplied calendar dates and completion state
        val today = LocalDate.parse("2026-10-19")
        val dueDate = LocalDate.parse("2026-10-19")

        // WHEN - The task label is calculated
        val label = dueDateLabel(dueDate, today, true)

        // THEN - The exact label from the reviewed specification is returned
        assertEquals("CLEAR", label)
    }
}
