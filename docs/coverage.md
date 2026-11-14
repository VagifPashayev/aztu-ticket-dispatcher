# Tələblərin yoxlanması

| Tələb | Kod və sübut |
|---|---|
| Array / Dynamic Array | FixedArray.java, DynamicArray.java; sequence-rounds.csv və sequence-summary.csv |
| Singly / Doubly Linked List | Hər iki list sinfi, SequenceTest; eyni CSV-də yeddi əməliyyat |
| Stack / Queue / Deque | SimpleStack, SimpleQueue, SimpleDeque; TicketDesk və --demo |
| Chaining hash table | ChainedHashTable; collision və resize testləri; demoda chain=3 |
| BST insert/search/delete və zəifləmə | BinarySearchTree, StructuresTest; bst-rounds.csv |

## Nəzəri mürəkkəblik

| Struktur / əməliyyat | Vaxt |
|---|---|
| FixedArray get / sona əlavə (yer varsa) | O(1) |
| DynamicArray get / sona əlavə | O(1) / amortizasiya olunmuş O(1), böyümə zamanı O(n) |
| Massivlərdə əvvələ əlavə/silmə, axtarış | O(n) |
| Singly list əvvələ və sona əlavə, əvvəldən silmə | O(1) |
| Singly list sondan silmə, indeksə çatma və axtarış | O(n) |
| Doubly list hər iki uca əlavə/silmə | O(1) |
| Doubly list indeksə çatma | O(min(i, n−1−i)); axtarış O(n) |
| Stack push/pop | Amortizasiya olunmuş O(1) / O(1) |
| Queue enqueue/dequeue, Deque ucları | O(1) |
| Hash get/put/remove | Yaxşı paylanmada gözlənilən amortizasiya olunmuş O(1); pis toqquşmada O(n) |
| BST insert/search/delete | O(h), balanssız pis halda O(n) |
| BST in-order və height | O(n) |

Siyahıya daxili node istinadı verilmədiyi üçün ixtiyari indeksdə əlavə/silmə əvvəlcə node-u tapır. Buna görə bu əməliyyatları ümumi halda O(1) adlandırmaq düzgün deyil. Tətbiqdə bir sorğunun silinməsi kataloqda axtarış və sürüşdürmə səbəbindən O(n)-dir; hash indeksinin olması bütün workflow-u O(1) etmir. Silinmiş növbə elementlərinin ötürülməsi də tək çağırışı uzada bilər.

Strukturlar üçün yaddaş O(n)-dir. FixedArray ayrıca ayrılmış tutum qədər yer tutur; hash üçün bucket massivi də nəzərə alınır. Undo tarixinin və hələ növbədən çıxmamış silinən istinadların yaddaşı tarixçənin uzunluğundan asılıdır.
