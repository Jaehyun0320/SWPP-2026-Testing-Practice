# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?): Unit`

**Intent**: Display the current task-list label immediately after task completion or due-date changes. A reused list row must clear both its text and visibility when the current task has no label, rather than retaining an earlier task's label.

**Pre-conditions**
- `textView` is created with `InstrumentationRegistry.getInstrumentation().targetContext`; direct adapter calls execute on the main thread using `runOnMainSync`.
- A fresh `TextView` begins with `text=""` and `visibility=View.VISIBLE`; `task_item.xml` does not set an initial visibility for `R.id.due_date_text`.
- Each test captures `today=LocalDate.now()` once at its start. Every relative `dueDate` and its expected label use that same captured `today` and a fixed day offset.
- `task` may be `null`. In UI-flow tests, `FakeAndroidTestRepository` supplies the task to `TasksFragment`.

**Post-conditions**
- Success cases
  - A completed task with a due date shows exactly `"CLEAR"` and is visible. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=true, id="completed-dated", dueDate=today.plusDays(3))` / When `setDueDateLabel` is called on the main thread / Then `textView.text="CLEAR"` and `visibility=View.VISIBLE`.
  - A completed task without a due date also shows exactly `"CLEAR"` and is visible. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=true, id="completed-undated", dueDate=null)` / When `setDueDateLabel` is called on the main thread / Then `textView.text="CLEAR"` and `visibility=View.VISIBLE`.
  - An active task due today shows exactly `"D-Day"` and is visible. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=false, id="active-today", dueDate=today)` / When `setDueDateLabel` is called on the main thread on the captured date / Then `textView.text="D-Day"` and `visibility=View.VISIBLE`.
  - An active task due in three calendar days shows `"D-3"` and is visible. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=false, id="active-future-3", dueDate=today.plusDays(3))` / When `setDueDateLabel` is called on the main thread / Then `textView.text="D-3"` and `visibility=View.VISIBLE`.
  - An active task overdue by three calendar days shows `"D+3"` and is visible. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=false, id="active-past-3", dueDate=today.minusDays(3))` / When `setDueDateLabel` is called on the main thread / Then `textView.text="D+3"` and `visibility=View.VISIBLE`.
  - A future due date beyond a month uses the full 40-day distance. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=false, id="active-future-40", dueDate=today.plusDays(40))` / When `setDueDateLabel` is called on the main thread / Then `textView.text="D-40"` and `visibility=View.VISIBLE`.
  - An overdue date beyond a month uses the full 40-day distance. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=false, id="active-past-40", dueDate=today.minusDays(40))` / When `setDueDateLabel` is called on the main thread / Then `textView.text="D+40"` and `visibility=View.VISIBLE`.
  - An active task without a due date has no visible or retained label. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=false, id="active-undated", dueDate=null)` / When `setDueDateLabel` is called on the main thread / Then `textView.text=""` and `visibility=View.GONE`, not `View.INVISIBLE`.
  - A `null` task clears and hides a previously displayed label. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a reused `TextView(text="CLEAR", visibility=View.VISIBLE)`, and `task=null` / When `setDueDateLabel` is called on the main thread / Then `textView.text=""` and `visibility=View.GONE`, not `View.INVISIBLE`.
- Error cases
  - None. Every `Task?` input has a defined display result.
- Edge cases
  - Reusing a view from a completed undated task for that same active undated task clears `"CLEAR"`. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=true, id="same-undated", dueDate=null)` / When the adapter is called on the main thread, then called again for the same task with `isCompleted=false` / Then after the first call `textView.text="CLEAR"` and `visibility=View.VISIBLE`, and after the second `textView.text=""` and `visibility=View.GONE` (not `View.INVISIBLE`).
  - Reusing a hidden view from an active undated task for that same completed task restores visibility. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=false, id="same-undated-reverse", dueDate=null)` / When the adapter is called on the main thread, then called again for the same task with `isCompleted=true` / Then after the first call `textView.text=""` and `visibility=View.GONE`, and after the second `textView.text="CLEAR"` and `visibility=View.VISIBLE`.
  - Unchecking a completed dated task replaces `"CLEAR"` with the active D-day label. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=true, id="same-dated", dueDate=today.plusDays(3))` / When the adapter is called on the main thread, then called again for the same task with `isCompleted=false` / Then after the first call `textView.text="CLEAR"` and `visibility=View.VISIBLE`, and after the second `textView.text="D-3"` and `visibility=View.VISIBLE`.
  - Clearing a due date from an active task removes its previously visible `"D-3"`. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=false, id="clear-date", dueDate=today.plusDays(3))` / When the adapter is called on the main thread, then called again for the same task with `dueDate=null` / Then after the first call `textView.text="D-3"` and `visibility=View.VISIBLE`, and after the second `textView.text=""` and `visibility=View.GONE` (not `View.INVISIBLE`).
  - Removing the task after a visible `"D-3"` also clears the reused view. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=false, id="remove-task", dueDate=today.plusDays(3))` / When the adapter is called on the main thread, then called again with `task=null` / Then after the first call `textView.text="D-3"` and `visibility=View.VISIBLE`, and after the second `textView.text=""` and `visibility=View.GONE` (not `View.INVISIBLE`).
  - Adding a due date to an active undated task immediately restores a visible D-day label. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, and `Task(title="Task", description="Description", isCompleted=false, id="set-date", dueDate=null)` / When the adapter is called on the main thread, then called again for the same task with `dueDate=today.plusDays(3)` / Then after the first call `textView.text=""` and `visibility=View.GONE`, and after the second `textView.text="D-3"` and `visibility=View.VISIBLE`.
  - Reusing a completed dated row for a different active undated task clears the first row's label. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a fresh `TextView(text="", visibility=View.VISIBLE)`, `Task(title="First", description="Description", isCompleted=true, id="first", dueDate=today.plusDays(3))`, and `Task(title="Second", description="Description", isCompleted=false, id="second", dueDate=null)` / When the adapter is called on the main thread for each task in order / Then after the first call `textView.text="CLEAR"` and `visibility=View.VISIBLE`, and after the second `textView.text=""` and `visibility=View.GONE` (not `View.INVISIBLE`).
  - Checking and unchecking a task without a due date updates the actual task-list row immediately. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a `R.id.due_date_text` view created by `task_item.xml` with initial `text=""` and `visibility=View.VISIBLE`, and `FakeAndroidTestRepository` containing `Task(title="TITLE1", description="DESCRIPTION1", isCompleted=false, id="id1", dueDate=null)` / When `launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)` displays the list, then `R.id.complete_checkbox` in the `"TITLE1"` row is clicked and clicked again / Then before the first click that row's `R.id.due_date_text` has `text=""` and `visibility=View.GONE`, after the first click it has `text="CLEAR"` and `visibility=View.VISIBLE`, and after the second click it has `text=""` and `visibility=View.GONE` (not `View.INVISIBLE`), with no retained `"CLEAR"`.
  - Checking and unchecking a task due in three days updates the actual task-list row immediately. `[Integration]`
    - Tip: Given `today=LocalDate.now()`, a `R.id.due_date_text` view created by `task_item.xml` with initial `text=""` and `visibility=View.VISIBLE`, and `FakeAndroidTestRepository` containing `Task(title="TITLE1", description="DESCRIPTION1", isCompleted=false, id="id1", dueDate=today.plusDays(3))` / When `launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)` displays the list, then `R.id.complete_checkbox` in the `"TITLE1"` row is clicked and clicked again / Then before the first click that row's `R.id.due_date_text` has `text="D-3"` and `visibility=View.VISIBLE`, after the first click it has `text="CLEAR"` and `visibility=View.VISIBLE`, and after the second click it has `text="D-3"` and `visibility=View.VISIBLE`, with no retained `"CLEAR"`.

**Undefined behavior** (optional)
- A test that crosses local midnight between capturing `today` and the adapter or fragment reading the current date is undefined because the two calls can refer to different local dates.
