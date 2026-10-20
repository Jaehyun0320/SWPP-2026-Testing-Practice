# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?): Unit`

**Intent**: Show the task's current D-day label in the supplied `TextView`. When the task has no label, clear and hide the view so a reused task-list row cannot show a stale label.

**Pre-conditions**
- `textView` is a valid task-list label view; `task` may be `null`.
- The call runs on the Android main thread.

**Post-conditions**
- Success cases
  - A completed task with a due date displays `"CLEAR"` and makes the view visible. `[Integration]`
    - Tip: Given a `TextView`, and `Task(title="Task", description="Description", isCompleted=true, id="completed-dated", dueDate=2026-10-22)` / When `setDueDateLabel` is called / Then `textView.text` is `"CLEAR"` and `visibility` is `View.VISIBLE`.
  - A completed task without a due date displays `"CLEAR"` and makes the view visible. `[Integration]`
    - Tip: Given a `TextView`, and `Task(title="Task", description="Description", isCompleted=true, id="completed-undated", dueDate=null)` / When `setDueDateLabel` is called / Then `textView.text` is `"CLEAR"` and `visibility` is `View.VISIBLE`.
  - An active task due on the caller's current date displays `"D-Day"` and makes the view visible. `[Integration]`
    - Tip: Given a `TextView`, and `Task(title="Task", description="Description", isCompleted=false, id="active-today", dueDate=LocalDate.now())` / When `setDueDateLabel` is called on that same date / Then `textView.text` is `"D-Day"` and `visibility` is `View.VISIBLE`.
  - An active task with no due date leaves no label visible. `[Integration]`
    - Tip: Given a `TextView`, and `Task(title="Task", description="Description", isCompleted=false, id="active-undated", dueDate=null)` / When `setDueDateLabel` is called / Then `textView.text` is `""` and `visibility` is `View.GONE`.
  - A `null` task clears and hides the label. `[Integration]`
    - Tip: Given a `TextView` containing `"CLEAR"` with `View.VISIBLE`, and `task=null` / When `setDueDateLabel` is called / Then `textView.text` is `""` and `visibility` is `View.GONE`.
- Error cases
  - None. All `Task?` inputs are valid. `[Integration]`
- Edge cases
  - Reusing a row for a task that changes from completed to active with no due date clears the previous `"CLEAR"` label. `[Integration]`
    - Tip: Given one `TextView` and `Task(title="Task", description="Description", isCompleted=true, id="same-id", dueDate=null)` followed by the same task with `isCompleted=false` / When `setDueDateLabel` is called for each state in order / Then after the second call `textView.text` is `""` and `visibility` is `View.GONE`.
  - Reusing a row for an active undated task after a completed dated task also clears the previous label. `[Integration]`
    - Tip: Given one `TextView`, a completed `Task(id="first", dueDate=2026-10-22)` and then an active `Task(id="second", dueDate=null)` / When `setDueDateLabel` is called for each in order / Then the first call shows `"CLEAR"` and the second leaves `textView.text` as `""` with `visibility` `View.GONE`.

**Undefined behavior** (optional)
- None.
