# Ölçmə qaydası

`Benchmarks.java` eyni tam ədədlərlə dörd strukturun əməliyyatlarını ölçür. Əsas icrada ölçülər 1000, 10000 və 50000-dir. Hər kombinasiya üçün 2 isinmə və 5 saxlanılan raund var. Strukturların icra sırası raundlar arasında dövr edir. `System.nanoTime()` ilə paket vaxtı ölçülüb əməliyyat sayına bölünür. Xülasədə 5 raundun medianı, minimumu və maksimumu verilir.

## Əməliyyatın mənası

- `grow_append`: boş strukturda n element əlavə edilir. FixedArray üçün yer əvvəlcədən ayrılır, DynamicArray isə tutum 1-dən başlayıb ikiqat böyüyür. İlkin konstruktor vaxtdan kənardır, sonrakı böyümələr vaxtın içindədir. Bu sınaq tutum siyasətlərinin fərqini də əhatə edir.
- `append`: əvvəlcədən n element olan strukturun sonuna 128 əlavə; massivlərdə əlavə tutum əvvəlcədən var. Bu sınaqda dynamic array böyümür.
- `insert_front`: əvvələ 128 əlavə.
- `remove_front`, `remove_back`: əvvəldən və sondan 128 silmə. Silmələr zamanı ölçü azalır.
- `search_hit`: eyni toxumla seçilən 128 mövcud elementin `indexOf` axtarışı.
- `search_miss`: mövcud olmayan 128 mənfi ədədin axtarışı.

Məlumatın hazırlanması, Integer obyektlərinin yaradılması, sorğuların seçilməsi, nəticənin yoxlanması və fayla yazma vaxtdan kənardadır. Silmə və axtarış nəticələrinin checksum-u hesablanır. Əlavələrdən sonra ölçü və ilk/son elementdən hesablanan checksum gözlənilən qiymətlə yoxlanır. Nəticə volatile sahəyə yazılır. Bu sadə tədbirlər tam JIT optimallaşdırma zəmanəti vermir.

## BST

1000, 3000 və 6000 açar sıralanmış və toxumla qarışdırılmış ardıcıllıqla daxil edilir. Qurma vaxtı, 128 uğurlu axtarışın orta vaxtı, hündürlük və müqayisə sayı saxlanılır. Hər iki giriş üçün axtarış açarları eynidir. Hündürlük node sayı ilə ölçülür: boş ağac 0, tək node 1. Sıralanmış girişdə h=n olması vaxt ölçüsündən asılı olmayan zəifləmə nümunəsidir. Ağac balanslaşdırılmır.

## Nəticəni necə oxumalı

Bir JVM prosesi işləyir; ayrıca fork-lar, GC profilləri və etibar intervalları yoxdur. İsinmə sayı məhduddur. Xüsusən qısa əməliyyatlarda JIT, keş, timer və sistem yükü böyük fərq yarada bilər. Bir ölçüdə gözlənilməz nəticə alınması nəzəri mürəkkəbliyi dəyişmir. Siyahılarda node ayırma, massivlərdə kopyalama və yaddaşın yerləşməsi də vaxtı dəyişir. Rəqəmlər həmin icranın müşahidəsidir; performans zəmanəti deyil.

CSV-nin `round` sütunu sıfırdan başlayır; isinmə raundları CSV-yə düşmür. `environment.txt` vaxtı UTC ilə və əsas JVM/OS məlumatını saxlayır. Yenidən icra həmin çıxış qovluğunu yeniləyir. `--benchmark başqa-qovluq` əvvəlki nəticələri saxlamağa imkan verir. Testlərdəki kiçik quick rejimi təqdim olunan tam ölçmələrin əvəzi deyil.
