# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return the task-list D-day label using calendar-day distance from `today` to `dueDate`. Completion takes precedence over the due date.

**Pre-conditions**
- `today` is the current local calendar date supplied by the caller; `dueDate` may be `null`.
- `LocalDate` inputs are valid calendar dates.

**Post-conditions**
- Success cases
  - A completed task returns `"CLEAR"` even with a due date. `[Unit]`
    - Tip: Given `dueDate=2026-10-22`, `today=2026-10-19`, `isCompleted=true` / When `dueDateLabel` is called / Then it returns `"CLEAR"`.
  - A completed task returns `"CLEAR"` with no due date. `[Unit]`
    - Tip: Given `dueDate=null`, `today=2026-10-19`, `isCompleted=true` / When `dueDateLabel` is called / Then it returns `"CLEAR"`.
  - An active task with no due date returns the empty string. `[Unit]`
    - Tip: Given `dueDate=null`, `today=2026-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `""`.
  - An active task due in the future returns `"D-N"`, where N is the total number of calendar days until the date. `[Unit]`
    - Tip: Given `dueDate=2026-10-22`, `today=2026-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `"D-3"`.
  - An active task due today returns `"D-Day"`. `[Unit]`
    - Tip: Given `dueDate=2026-10-19`, `today=2026-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `"D-Day"`.
  - An active overdue task returns `"D+N"`, where N is the total number of calendar days overdue. `[Unit]`
    - Tip: Given `dueDate=2026-10-16`, `today=2026-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `"D+3"`.
- Error cases
  - None. The declared input types include all allowed states. `[Unit]`
- Edge cases
  - The three dates immediately around today produce the three different label forms. `[Unit]`
    - Tip: Given `today=2026-10-19`, `isCompleted=false`, and `dueDate` of `2026-10-18`, `2026-10-19`, then `2026-10-20` / When each is labeled / Then the results are `"D+1"`, `"D-Day"`, then `"D-1"`.
  - Crossing a month boundary still counts one calendar day. `[Unit]`
    - Tip: Given `today=2026-10-31`, `dueDate=2026-11-01`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `"D-1"`.
  - A due date more than one month away uses the total day count rather than the day component of a calendar period. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-11-20`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `"D-32"`.
  - Crossing a year boundary still counts one calendar day. `[Unit]`
    - Tip: Given `today=2026-12-31`, `dueDate=2027-01-01`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `"D-1"`.
  - A leap day contributes one calendar day to the count. `[Unit]`
    - Tip: Given `today=2028-02-28`, `dueDate=2028-03-01`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `"D-2"`.

**Undefined behavior** (optional)
- None.
