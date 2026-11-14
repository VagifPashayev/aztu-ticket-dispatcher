# Yerli icranın nəticəsi — 14 noyabr 2026

Tam CSV nəticələri IntelliJ IDEA-dakı **Measurements** konfiqurasiyasının icrasındandır. Mühit `environment.txt`-dədir: Windows 11, OpenJDK 27.0.1.0.3; proqram Java 21 hədəfi ilə yığılır. Maven yoxlaması ayrıca Temurin 21.0.7 ilə aparılıb. Fərqli JVM-lərin vaxtları bir cədvəldə qarışdırılmayıb.

n=50000 üçün median ns/əməliyyat:

| Əməliyyat | FixedArray | DynamicArray | SinglyList | DoublyList |
|---|---:|---:|---:|---:|
| Sondan silmə | 48.438 | 34.375 | 59264.063 | 34.375 |
| Mövcud elementi axtarma | 12826.563 | 12499.219 | 41189.844 | 42504.688 |
| Olmayan elementi axtarma | 26971.875 | 26667.188 | 83800.781 | 85969.531 |

Biristiqamətli siyahıda sondan silmə zamanı əvvəlki node tapılmalıdır. İkiistiqamətli siyahı `tail.prev` vasitəsilə bunu birbaşa edir. Axtarışda isə hər dörd struktur elementləri ardıcıl yoxlayır; massivlərdə yaddaşın yanaşı yerləşməsi bu icrada üstünlük verib.

BST-də 6000 sıralanmış açar bütün raundlarda hündürlüyü 6000 olan zəncir yaradıb. Son ölçülən təsadüfi raundda hündürlük 29-dur. Eyni 128 axtarış üçün orta müqayisə sayı uyğun olaraq 3231.031 və 15.547-dir. Təsadüfi ağacın forması raunddan raunda dəyişir; bütün rəqəmlər `bst-rounds.csv`-də saxlanılıb.

Bu müşahidələr nəzəri izahı dəstəkləyir, lakin bir kompüterdəki qısa ölçmələr ümumi performans zəmanəti vermir. Qısa əməliyyatların minimum və maksimumları arasında xeyli fərq var. Metodun digər məhdudiyyətləri `../../docs/benchmark-method.md` faylında göstərilib.
