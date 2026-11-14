# Tələblərin yoxlanması

Yenidən yoxlama: **14 noyabr 2026**. Əsas: KOICA/LMS-də oxunmuş Sərbəst İş №1 şərti, proqram kodu, JUnit testləri və saxlanılan ölçmələr. Beş texniki hissənin hamısı yerinə yetirilib; təqdimat və fərdi yükləmə ayrıca qalıb.

## Beş texniki hissə

| № | Tələb | Kodda yoxlanılan hissə | Nəticə və sübut |
|---|---|---|---|
| 1 | Array və Dynamic Array; müxtəlif ölçülərdə əlavə, silmə və axtarış; nəticələrin müqayisəsi | `FixedArray.insert/remove/indexOf`; `DynamicArray.insert/remove/indexOf`, tutumun ikiqat böyüməsi | **Tamamdır.** n=1000/10000/50000, 7 əməliyyat, 5 raund. Hər massiv üçün 105 ölçmə; README-də bütün medianlar və izahları. `SequenceTest` qarışıq əməliyyatlar, tutum, sərhədlər və null yoxlamaları. |
| 2 | Öz Singly və Doubly Linked List strukturları; əsas əməliyyatların müqayisəsi | Ayrı `Node` sinifləri; singly-də `next`, doubly-də `previous/next`, hər ikisində head/tail; insert/remove/indexOf | **Tamamdır.** Hər siyahı üçün 105 ölçmə, massivlərlə eyni ölçü və əməliyyatlar; sondan silmədə fərq ayrıca izah olunur. `SequenceTest` hər ikisini müstəqil `ArrayList` nəticələri ilə yoxlayır. |
| 3 | Stack, Queue və Deque-nin uyğun tətbiq funksiyalarında istifadəsi | `SimpleStack` → başlıq üçün undo; `SimpleQueue` → yeni sorğuların FIFO növbəsi; `SimpleDeque` → təcili sorğu əvvələ, adi sorğu sona | **Tamamdır.** `TicketDesk.rename/undoRename`, `create/triageNext`, `triageNext/processNext`; `StructuresTest.lifoFifoAndBothDequeEnds` və `TicketDeskTest.incomingIsFifoAndUrgentGoesBeforeOrdinary`. Demo və ilk ekran görüntüsü də icra ardıcıllığını göstərir. |
| 4 | Öz Hash Table strukturu; kolliziyaların chaining ilə həlli | `ChainedHashTable.Entry.next`, bucket massivi, `put/get/remove`, 0.75 yük əmsalından sonra rehash | **Tamamdır.** Demo-da tutum 4, açarlar 1/5/9, zəncir 3. `collisionsUpdatesResizeAndRemovalPreserveOtherKeys` 100 eyni hash-li açarı, yeniləmə və silməni yoxlayır; ayrıca 4000 qarışıq əməliyyat `HashMap` ilə tutuşdurulur. |
| 5 | BST əlavə, axtarış, silmə; sıralanmış girişin formaya və axtarışa təsiri | `BinarySearchTree.put/get/remove`, `keysInOrder/height/searchComparisons`; `TicketDesk`-də əlavə/silmə zamanı BST indeksinin yenilənməsi və `ordered` ilə sıralı çıxış | **Tamamdır.** n=1000/3000/6000, iki giriş növü, 5 raund: 30 ölçmə. README-də qurma/axtarış vaxtı, müqayisələr və hündürlüklər; `StructuresTest` yarpaq, bir/iki uşaqlı node və kökün silinməsini, təsadüfi əməliyyatları və 5000 node-luq zənciri yoxlayır. |

Kod keçidləri: [strukturlar](../src/main/java/az/edu/aztu/ticketdesk/structures), [TicketDesk](../src/main/java/az/edu/aztu/ticketdesk/TicketDesk.java), [ölçmə kodu](../src/main/java/az/edu/aztu/ticketdesk/Benchmarks.java), [testlər](../src/test/java/az/edu/aztu/ticketdesk).

CSV keçidləri: [420 ilkin ölçmə](../benchmarks/results/sequence-rounds.csv), [84 xülasə sətri](../benchmarks/results/sequence-summary.csv), [30 BST ölçməsi](../benchmarks/results/bst-rounds.csv). Hash, Stack, Queue və Deque üçün vaxt cədvəli tələb olunmur; onlar funksional testlərlə və tətbiq ssenarisi ilə yoxlanılıb. BST üçün vaxtlar qurma və uğurlu axtarışa aiddir; silmənin düzgünlüyü ayrıca test edilib.

## Tətbiq və təhvil hissəsi

| Şərt | Vəziyyət |
|---|---|
| Kiçik işlək konsol tətbiqi | **Tamamdır.** `Main`, interaktiv əmrlər, `--demo` və `--benchmark`; Maven ilə JAR yığılır. |
| Komanda 2–4 nəfər | **Tamamdır.** README-də istifadəçinin bildirdiyi dörd komanda üzvü var. Fərdi töhfə bölgüsü təsdiqlənməyib. |
| Kod və ya layihə üçün GitHub keçidi | **Hazırdır.** [Açıq repozitoriya](https://github.com/VagifPashayev/aztu-ticket-dispatcher). |
| Təqdimatda funksiyalar, struktur seçiminin səbəbi və ölçmələr | **Qalıb.** Materiallar README-də və ekran görüntülərində var, təqdimat faylı hələ hazırlanmayıb. |
| Hər üzvün öz hesabından təqdimat və kod/GitHub keçidini yükləməsi | **Qalıb.** Bu layihə hazırlanarkən KOICA-ya göndərilməyib. |

Yekun: **proqramın beş texniki bəndi tamamdır; tapşırığın bütövlükdə təhvil verilməsi hələ tamamlanmayıb.** Bu yoxlama qiymət və ya müəllimin qəbul qərarı deyil.

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
