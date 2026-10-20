# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return the task-list label from completion state and the total number of calendar days between `today` and `dueDate`. Month lengths, year boundaries, and leap days contribute their actual number of days; a calendar period's day component is not the distance.

**Pre-conditions**
- `today` is the caller-supplied local calendar date; `dueDate` may be `null`.
- Both non-null date inputs are valid `LocalDate` values within the defined range below.

**Post-conditions**
- Success cases
  - Completion takes precedence over an upcoming due date and returns exactly uppercase `"CLEAR"`. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-10-22`, `isCompleted=true` / When `dueDateLabel` is called / Then it returns exactly `"CLEAR"`.
  - Completion with no due date also returns exactly uppercase `"CLEAR"`. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=null`, `isCompleted=true` / When `dueDateLabel` is called / Then it returns exactly `"CLEAR"`.
  - An active task with no due date returns no label. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=null`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `""`.
  - An active task due in N future calendar days returns `"D-N"`; N is an unpadded base-10 integer without spaces after the exact `"D-"` prefix. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-10-22`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-3"`.
  - An active task due today returns exactly case-sensitive `"D-Day"`, never `"D-0"` or `"D+0"`. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-Day"` and neither `"D-0"` nor `"D+0"`.
  - An active task overdue by N calendar days returns `"D+N"`; N is an unpadded base-10 integer without spaces after the exact `"D+"` prefix. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-10-16`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D+3"`.
  - A three-digit distance retains every decimal digit with no padding or spaces. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2027-01-27`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-100"`.
- Error cases
  - None. Within the pre-conditions, every combination of nullable due date and completion state has a defined result.
- Edge cases
  - One day before today is overdue by exactly one calendar day. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-10-18`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D+1"`.
  - The boundary date itself uses the special today label. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-Day"`, not `"D-0"` or `"D+0"`.
  - One day after today is due in exactly one calendar day. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-10-20`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-1"`.
  - An upcoming date directly across a month boundary is one day away. `[Unit]`
    - Tip: Given `today=2026-10-31`, `dueDate=2026-11-01`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-1"`.
  - The date immediately before the exact one-month boundary is 30 days away. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-11-18`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-30"`.
  - The exact one-month boundary is 31 days away in this calendar span. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-11-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-31"`.
  - The date immediately after the exact one-month boundary is 32 days away. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-11-20`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-32"`.
  - A common-year February contains 28 calendar days. `[Unit]`
    - Tip: Given `today=2026-02-01`, `dueDate=2026-03-01`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-28"`.
  - A leap-year February contains 29 calendar days. `[Unit]`
    - Tip: Given `today=2028-02-01`, `dueDate=2028-03-01`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-29"`.
  - Counting from the end of January through a common-year February uses 29 total days. `[Unit]`
    - Tip: Given `today=2026-01-31`, `dueDate=2026-03-01`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-29"`.
  - Common-year February 28 is one day before March 1. `[Unit]`
    - Tip: Given `today=2026-02-28`, `dueDate=2026-03-01`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-1"`.
  - Leap-year February 28 is one day before February 29. `[Unit]`
    - Tip: Given `today=2028-02-28`, `dueDate=2028-02-29`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-1"`.
  - Leap-year February 28 is two days before March 1. `[Unit]`
    - Tip: Given `today=2028-02-28`, `dueDate=2028-03-01`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-2"`.
  - Crossing New Year's Day still counts one day. `[Unit]`
    - Tip: Given `today=2026-12-31`, `dueDate=2027-01-01`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-1"`.
  - A one-year future span without a leap day is 365 days. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2027-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-365"`.
  - A one-year future span containing February 29 is 366 days. `[Unit]`
    - Tip: Given `today=2027-10-19`, `dueDate=2028-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D-366"`.
  - The overdue date immediately nearer than the exact one-month boundary is 29 days ago. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-09-20`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D+29"`.
  - The exact one-month-old date is 30 days ago in this calendar span. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-09-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D+30"`.
  - The overdue date immediately farther than the exact one-month boundary is 31 days ago. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-09-18`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D+31"`.
  - Crossing backward into the preceding month still counts one overdue day. `[Unit]`
    - Tip: Given `today=2026-10-01`, `dueDate=2026-09-30`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D+1"`.
  - Crossing backward into the preceding year still counts one overdue day. `[Unit]`
    - Tip: Given `today=2026-01-01`, `dueDate=2025-12-31`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D+1"`.
  - A one-year overdue span without a leap day is 365 days. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2025-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns exactly `"D+365"`.
  - Completion takes precedence over an overdue date. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-10-16`, `isCompleted=true` / When `dueDateLabel` is called / Then it returns exactly `"CLEAR"`.
  - Completion takes precedence over a due date that is today. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-10-19`, `isCompleted=true` / When `dueDateLabel` is called / Then it returns exactly `"CLEAR"`.

**Undefined behavior** (optional)
- Time-zone selection and conversion are outside this function's scope because the caller supplies `today` as an already chosen local date.
- Distances involving dates near `LocalDate.MIN` or `LocalDate.MAX` are left undefined because extreme-range arithmetic and output size are outside the task-list feature's intended input range.
