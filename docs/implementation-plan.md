# Ticket Dispatcher Implementation Plan

Goal: satisfy all five LMS topics in one executable Java application.
Spec: design.md. Native execution in a new isolated repository; no existing application files changed.

Global constraints: Java 21; own data structures; deterministic correctness checks; actual benchmark output; public repository authorised by user; presentation deferred.

Review focus: bounds and empty containers; hash collisions/rehashing; BST deletion and skew; stale tickets after deletion; undo consistency; fair timing and explicit limitations; no personal/private files in Git.

- [ ] Task 1: public Sequence API and array/list tests first, then implementations; run Maven tests.
- [ ] Task 2: stack/queue/deque/hash/BST tests and implementations, including deterministic oracle sequences and deep sorted BST.
- [ ] Task 3: TicketDesk workflow tests, CLI/script/demo, EOF and invalid input handling.
- [ ] Task 4: measured benchmark harness, CSV round samples and summaries, BST degeneration, validate results.
- [ ] Task 5: README/coverage/demonstration notes, IntelliJ actual-run screenshots, clean build and fresh final review.
- [ ] Task 6: publish public GitHub repository, verify remote commit and workflow, sync project context.

Test seams chosen for this authorised complete implementation: public structure operations, TicketDesk workflow, Main CLI, benchmark output contract. Tests must cover behavior, not private node layout. User's request to do everything now overrides extra skill check-in gates. No standalone per-feature reviewer; final independent review only.
