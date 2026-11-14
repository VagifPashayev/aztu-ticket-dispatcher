# Ticket Dispatcher

User selected the helpdesk theme, Java, public GitHub repository and full implementation now. Presentation is a later task. Team supplied by user: Vaqif Paşayev, Riad Qurbanov, Baloğlan Əhmədov, Ayxan Əliyev. Do not invent individual contributions or commit history.

Java 21 console program, Maven, no runtime dependencies, in-memory data. Menu and README in Azerbaijani. Eight custom structures: fixed array, dynamic array, singly/doubly lists, stack, queue, deque, chaining hash table and unbalanced BST (array pair and list pair count as two implementations each). Queue wraps singly list; deque wraps doubly list; stack wraps dynamic array. All required list operations remain independently testable and benchmarked.

Catalog stores Ticket references in dynamic array, ID hash index and BST. New tickets enter FIFO incoming queue. Triage moves next live ticket into work deque: urgent first, ordinary last. Processing marks next planned ticket done. Stack undoes title edits only. Deletion removes from all indexes; stale queue/deque/history references are skipped, never resurrected. IDs never reused. No persistence across restarts; show this explicitly.

Compare same array/list operations and payloads at several sizes, warm-up and repeated batches, deterministic seeds, setup/validation and I/O outside timings, observable checksums and independent rounds. CSV contains per-round measurements and summary, not universal complexity proof. Separately compare sorted/random BST insertion by height and search comparisons, with timings qualified. Standard collection classes may be used as independent test oracles, not hidden implementations.

Start from correctness tests; build workflow and CLI; run benchmarks; document real results; capture actual IntelliJ runs; publish only project folder. Alternatives considered: library (extra lending rules), standalone data-structure lab (weaker application scenario). User selected dispatcher. User approved doing everything at once, so execute without another design/plan permission gate.
