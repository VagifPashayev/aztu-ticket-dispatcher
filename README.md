# Ticket Dispatcher — sorğu dispetçeri

AzTU, M656aS · Müasir data strukturları və alqoritmləri · Sərbəst iş №1

Komanda: **Vaqif Paşayev, Riad Qurbanov, Baloğlan Əhmədov, Ayxan Əliyev**.

Texniki dəstəyə gələn sorğuları qəbul edən kiçik Java konsol proqramıdır. Sorğu əvvəl növbəyə düşür, sonra adi və ya təcili kimi iş planına keçir. Təcili sorğu birinci icra olunur. Başlıq dəyişikliyini geri qaytarmaq, ID ilə axtarmaq və sorğunu silmək mümkündür.

## İşə salmaq

JDK **21** lazımdır. Maven Wrapper layihəyə daxildir; ilk build üçün internet lazımdır.

Windows PowerShell:

```powershell
.\mvnw.cmd -B clean verify
java -jar target/ticket-dispatcher-1.0.0.jar --demo
java -jar target/ticket-dispatcher-1.0.0.jar
```

Linux/macOS: `./mvnw -B clean verify`, sonra eyni `java -jar ...` əmrləri.

IntelliJ IDEA-da layihə qovluğunu Maven layihəsi kimi açın, Project SDK-ni JDK 21 seçin və importun bitməsini gözləyin. `.run` qovluğundakı **Demo**, **Console** və **Measurements** konfiqurasiyaları hazırdır. Testlər `src/test/java` qovluğundadır.

## Kiçik sınaq

```text
new Printer işləmir
new Şəbəkə bağlantısı yoxdur
triage normal
triage urgent
process
edit 1 Printer kabelini yoxla
undo
find 1
ordered
stats
quit
```

Burada `process` əvvəl ikinci sorğunu tamamlayır: o, təcili kimi deque-nin əvvəlinə əlavə olunub. `undo` yalnız son başlıq dəyişikliyini geri qaytarır. `help` bütün əmrləri göstərir.

## Tapşırığın hissələri

| Hissə | Reallaşdırma və istifadə |
|---|---|
| Array / Dynamic Array | `FixedArray`, `DynamicArray`; müxtəlif ölçülərdə əlavə, silmə və axtarış ölçülür. Dinamik massiv bütün sorğuların kataloqudur. |
| Singly / Doubly Linked List | Hər ikisi öz node-ları ilə yazılıb; eyni əməliyyatlar müqayisə olunur. |
| Stack / Queue / Deque | Başlığı geri qaytarmaq / yeni sorğuların FIFO növbəsi / təcili və adi iş planı. Hər biri yuxarıdakı öz strukturlarımız üzərindədir. |
| Hash Table | Ayrı zəncirləmə (chaining), yük əmsalına görə böyümə; ID ilə axtarış. Demo-da 1, 5, 9 açarları eyni bucket-ə düşür. |
| Binary Search Tree | Əlavə, axtarış, silmə, in-order; sorğuları ID sırası ilə göstərir. Sıralanmış girişdə balanssız ağacın zəifləməsi ayrıca ölçülür. |

Strukturlar `src/main/java/az/edu/aztu/ticketdesk/structures` qovluğundadır. Onların daxilində hazır `ArrayList`, `LinkedList`, `HashMap` və `TreeMap` istifadə olunmur. Testlərdə JDK kolleksiyaları nəticəni müstəqil yoxlamaq üçün işlədilir.

## Ölçmələr və yoxlama

```powershell
java -jar target/ticket-dispatcher-1.0.0.jar --benchmark benchmarks/results
```

- [Ölçmə qaydası və məhdudiyyətlər](docs/benchmark-method.md)
- [Massiv və siyahı nəticələri](benchmarks/results/sequence-summary.csv), [bütün raundlar](benchmarks/results/sequence-rounds.csv)
- [BST raundları](benchmarks/results/bst-rounds.csv), [icra mühiti](benchmarks/results/environment.txt)
- [Tələblərin yoxlanması və mürəkkəblik](docs/coverage.md)
- [IntelliJ ekran görüntüləri](docs/screenshots)

JUnit testləri sərhəd halları, təsadüfi əməliyyat ardıcıllığı, toqquşma və rehash, BST silinməsi, növbədə qalmış silinən sorğular, undo, səhv konsol girişi və CSV çıxışını yoxlayır.

Ölçmələr tədris üçündür, JMH səviyyəli mikrobenchmark deyil. JVM isinməsi, GC və başqa proqramlar nəticəyə təsir edir. CSV-dəki nanosaniyələr başqa kompüter üçün sabit göstərici sayılmır. Sıralanmış 6000 açar üçün adi BST-nin hündürlüyü 6000 olur; təsadüfi girişin hündürlüyü toxumdan asılıdır.

Proqram məlumatları yalnız yaddaşda saxlayır; bağlandıqda sorğular itir. Şəbəkə xidməti və verilənlər bazası yoxdur. Ardıcıl ID-lər istifadə edildiyinə görə tətbiqin adi BST-si də zamanla zəncirə çevrilir. Praktik sistemdə balanslı ağac və ya başqa indeks seçmək lazım olardı.
