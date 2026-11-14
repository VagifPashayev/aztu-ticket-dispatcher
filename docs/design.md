# Quruluş

Java 21 konsol proqramı, Maven, yalnız yaddaşda saxlanan məlumatlar. Runtime asılılığı yoxdur.

`Sequence` indeksli ümumi interfeysdir. FixedArray sabit tutumludur, DynamicArray dolduqda tutumu ikiqat artırır. SinglyLinkedList head/tail, DoublyLinkedList head/tail və ikiistiqamətli node əlaqələrini saxlayır.

Stack dinamik massiv, Queue biristiqamətli siyahı, Deque ikiistiqamətli siyahı üzərində qurulub. Hash Table bucket-lərdə əlaqəli zəncirlərdən istifadə edir və 0.75 yük əmsalından sonra böyüyür. BST adi balanssız ağacdır; dərin ağaclarda çağırış steki dolmasın deyə əməliyyatlar iterativdir.

`TicketDesk` kataloq, hash və BST indekslərini birlikdə yeniləyir. Yeni sorğu FIFO növbəsinə düşür. Təcili planlaşdırma deque-nin əvvəlinə, adi planlaşdırma sonuna əlavə edir. Başlıq dəyişikliyi stack-də əvvəlki qiyməti saxlayır.

Silmə hər üç indeksdən sorğunu çıxarır və DELETED statusu qoyur. Queue, Deque və undo tarixində qalan köhnə istinadlar istifadə zamanı ötürülür. ID təkrar verilmir, undo silinmiş sorğunu bərpa etmir. Məlumatlar proqram bağlandıqda saxlanılmır.
