package com.example.swpp.tododapp.tasks

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.R
import com.example.swpp.tododapp.ServiceLocator
import com.example.swpp.tododapp.data.Task
import com.example.swpp.tododapp.data.source.FakeAndroidTestRepository
import java.time.LocalDate
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {
    private val instrumentation get() = InstrumentationRegistry.getInstrumentation()

    private fun newLabelView(): TextView {
        lateinit var label: TextView
        instrumentation.runOnMainSync { label = TextView(instrumentation.targetContext) }
        return label
    }

    private fun assertLabel(label: TextView, expectedText: String, expectedVisibility: Int) {
        instrumentation.runOnMainSync {
            assertEquals(expectedText, label.text.toString())
            assertEquals(expectedVisibility, label.visibility)
        }
    }

    private fun bindAndAssert(label: TextView, task: Task?, text: String, visibility: Int) {
        instrumentation.runOnMainSync { setDueDateLabel(label, task) }
        assertLabel(label, text, visibility)
    }

    @Test
    fun setDueDateLabel_completedDated_showsCLEAR() {
        // GIVEN - A fresh visible label and a task with the specified state
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val task = Task("Task", "Description", true, "completed-dated", today.plusDays(3))

        // WHEN - The binding adapter receives that task
        // THEN - Its text and visibility match the reviewed specification
        bindAndAssert(label, task, "CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_completedUndated_showsCLEAR() {
        // GIVEN - A fresh visible label and a task with the specified state
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val task = Task("Task", "Description", true, "completed-undated", null)

        // WHEN - The binding adapter receives that task
        // THEN - Its text and visibility match the reviewed specification
        bindAndAssert(label, task, "CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeToday_showsDDay() {
        // GIVEN - A fresh visible label and a task with the specified state
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val task = Task("Task", "Description", false, "active-today", today)

        // WHEN - The binding adapter receives that task
        // THEN - Its text and visibility match the reviewed specification
        bindAndAssert(label, task, "D-Day", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeFutureThree_showsDMinus3() {
        // GIVEN - A fresh visible label and a task with the specified state
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val task = Task("Task", "Description", false, "active-future-3", today.plusDays(3))

        // WHEN - The binding adapter receives that task
        // THEN - Its text and visibility match the reviewed specification
        bindAndAssert(label, task, "D-3", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activePastThree_showsDPlus3() {
        // GIVEN - A fresh visible label and a task with the specified state
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val task = Task("Task", "Description", false, "active-past-3", today.minusDays(3))

        // WHEN - The binding adapter receives that task
        // THEN - Its text and visibility match the reviewed specification
        bindAndAssert(label, task, "D+3", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeFutureForty_showsDMinus40() {
        // GIVEN - A fresh visible label and a task with the specified state
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val task = Task("Task", "Description", false, "active-future-40", today.plusDays(40))

        // WHEN - The binding adapter receives that task
        // THEN - Its text and visibility match the reviewed specification
        bindAndAssert(label, task, "D-40", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activePastForty_showsDPlus40() {
        // GIVEN - A fresh visible label and a task with the specified state
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val task = Task("Task", "Description", false, "active-past-40", today.minusDays(40))

        // WHEN - The binding adapter receives that task
        // THEN - Its text and visibility match the reviewed specification
        bindAndAssert(label, task, "D+40", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeUndated_clearsAndHides() {
        // GIVEN - A fresh visible label and a task with the specified state
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val task = Task("Task", "Description", false, "active-undated", null)

        // WHEN - The binding adapter receives that task
        // THEN - Its text and visibility match the reviewed specification
        bindAndAssert(label, task, "", View.GONE)
    }

    @Test
    fun setDueDateLabel_nullTask_clearsAndHides() {
        // GIVEN - A reused view showing CLEAR and no task
        val today = LocalDate.now()
        val label = newLabelView()
        instrumentation.runOnMainSync { label.text = "CLEAR" }
        assertLabel(label, "CLEAR", View.VISIBLE)

        // WHEN - The binding adapter receives null
        // THEN - It clears the text and uses GONE rather than INVISIBLE
        bindAndAssert(label, null, "", View.GONE)
    }

    @Test
    fun setDueDateLabel_completedUndatedToActive_clearsAndHides() {
        // GIVEN - A fresh view reused for two states of the same task
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val original = Task("Task", "Description", true, "same-undated", null)
        val changed = original.copy(isCompleted = false)

        // WHEN - Each state is bound in order
        // THEN - Text and visibility are correct after each call
        bindAndAssert(label, original, "CLEAR", View.VISIBLE)
        bindAndAssert(label, changed, "", View.GONE)
    }

    @Test
    fun setDueDateLabel_activeUndatedToCompleted_showsCLEAR() {
        // GIVEN - A fresh view reused for two states of the same task
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val original = Task("Task", "Description", false, "same-undated-reverse", null)
        val changed = original.copy(isCompleted = true)

        // WHEN - Each state is bound in order
        // THEN - Text and visibility are correct after each call
        bindAndAssert(label, original, "", View.GONE)
        bindAndAssert(label, changed, "CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_completedDatedToActive_showsDMinus3() {
        // GIVEN - A fresh view reused for two states of the same task
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val original = Task("Task", "Description", true, "same-dated", today.plusDays(3))
        val changed = original.copy(isCompleted = false)

        // WHEN - Each state is bound in order
        // THEN - Text and visibility are correct after each call
        bindAndAssert(label, original, "CLEAR", View.VISIBLE)
        bindAndAssert(label, changed, "D-3", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeDatedToUndated_clearsAndHides() {
        // GIVEN - A fresh view reused for two states of the same task
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val original = Task("Task", "Description", false, "clear-date", today.plusDays(3))
        val changed = original.copy(dueDate = null)

        // WHEN - Each state is bound in order
        // THEN - Text and visibility are correct after each call
        bindAndAssert(label, original, "D-3", View.VISIBLE)
        bindAndAssert(label, changed, "", View.GONE)
    }

    @Test
    fun setDueDateLabel_activeDatedToNull_clearsAndHides() {
        // GIVEN - A fresh view reused for two states of the same task
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val original = Task("Task", "Description", false, "remove-task", today.plusDays(3))
        val changed = null

        // WHEN - Each state is bound in order
        // THEN - Text and visibility are correct after each call
        bindAndAssert(label, original, "D-3", View.VISIBLE)
        bindAndAssert(label, changed, "", View.GONE)
    }

    @Test
    fun setDueDateLabel_activeUndatedToDated_showsDMinus3() {
        // GIVEN - A fresh view reused for two states of the same task
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val original = Task("Task", "Description", false, "set-date", null)
        val changed = original.copy(dueDate = today.plusDays(3))

        // WHEN - Each state is bound in order
        // THEN - Text and visibility are correct after each call
        bindAndAssert(label, original, "", View.GONE)
        bindAndAssert(label, changed, "D-3", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_completedDatedToDifferentUndated_clearsAndHides() {
        // GIVEN - A fresh view reused for two different tasks
        val today = LocalDate.now()
        val label = newLabelView()
        assertLabel(label, "", View.VISIBLE)
        val first = Task("First", "Description", true, "first", today.plusDays(3))
        val second = Task("Second", "Description", false, "second", null)

        // WHEN - Each task is bound in order
        // THEN - CLEAR is removed for the active undated task
        bindAndAssert(label, first, "CLEAR", View.VISIBLE)
        bindAndAssert(label, second, "", View.GONE)
    }
}

@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsUiFlowTest {
    private lateinit var repository: FakeAndroidTestRepository

    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanupRepository() = runTest {
        ServiceLocator.resetRepository()
    }

    private fun taskLabel() = onView(
        allOf(withId(R.id.due_date_text), withParent(hasDescendant(withText("TITLE1"))))
    )

    private fun clickTaskCheckbox() {
        onView(allOf(withId(R.id.complete_checkbox), withParent(hasDescendant(withText("TITLE1")))))
            .perform(click())
    }

    private fun assertRowLabel(expectedText: String, expectedVisibility: Visibility) {
        taskLabel().check(matches(withText(expectedText)))
        taskLabel().check(matches(withEffectiveVisibility(expectedVisibility)))
    }

    @Test
    fun setDueDateLabel_clickUndatedTask_showsCLEARThenHides() = runTest {
        // GIVEN - The row starts with empty visible label text before binding.
        val today = LocalDate.now()
        val task = Task("TITLE1", "DESCRIPTION1", false, "id1", null)
        repository.saveTask(task)
        launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)
        assertRowLabel("", Visibility.GONE)

        // WHEN - The task is checked
        clickTaskCheckbox()
        // THEN - The row immediately shows CLEAR
        assertRowLabel("CLEAR", Visibility.VISIBLE)

        // WHEN - The same task is unchecked
        clickTaskCheckbox()
        // THEN - CLEAR is removed and the row is GONE, not INVISIBLE
        assertRowLabel("", Visibility.GONE)
    }

    @Test
    fun setDueDateLabel_clickDatedTask_showsCLEARThenDMinus3() = runTest {
        // GIVEN - The row starts with empty visible label text before binding.
        val today = LocalDate.now()
        val task = Task("TITLE1", "DESCRIPTION1", false, "id1", today.plusDays(3))
        repository.saveTask(task)
        launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)
        assertRowLabel("D-3", Visibility.VISIBLE)

        // WHEN - The task is checked
        clickTaskCheckbox()
        // THEN - The row immediately shows CLEAR
        assertRowLabel("CLEAR", Visibility.VISIBLE)

        // WHEN - The same task is unchecked
        clickTaskCheckbox()
        // THEN - CLEAR is replaced with the active D-day label
        assertRowLabel("D-3", Visibility.VISIBLE)
    }
}
