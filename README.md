 Dağıtık Abonelik Sistemi

Bu proje, sistem programlama dersinin bir ödevi olarak geliştirilmiş bir dağıtık abonelik sistemidir. Sistem, TCP soketleri aracılığıyla iletişim kurarak abonelik işlemlerini yönetir ve sunucular arasındaki doluluk oranlarını takip eder.

----------

  Proje Özeti

Projenin temel amacı, 3 farklı sunucunun (Server1, Server2, Server3) aynı anda çalışarak gelen abonelik taleplerini işleyip, doluluk oranlarını yönetmesidir. Ayrıca, admin.rb komut dosyası, sunucuları başlatmak, kapasite sorguları göndermek ve anlık doluluk bilgilerini toplamak için kullanılır. plotter.py ise sunuculardan gelen kapasite verilerini kullanarak dinamik bir grafik oluşturur.

----------

   Sistem Bileşenleri

admin.rb: Sunucuları başlatmak için komut gönderir ve sunuculardan gelen yanıtları yönetir.

Server1.java, Server2.java, Server3.java: Sunucular, TCP soketleri ile birbirlerine bağlanır ve abonelik taleplerini işler. Ayrıca, admin.rb tarafından gönderilen kapasite sorgularına cevap verir.

plotter.py: Sunuculardan alınan kapasite verilerini kullanarak dinamik bir doluluk grafiği oluşturur.

dist_subs.conf: Sistem yapılandırmasını içeren konfigürasyon dosyasıdır.

---------

   Özellikler

Fault Tolerance: Hata tolerans seviyeleri dist_subs.conf dosyasından okunur ve sistemin dayanıklılığı sağlanır.

Abonelik Yönetimi: Abone olma (SUBS), abonelikten çıkma (DEL), ve güncelleme işlemleri desteklenir.

Dinamik Grafik: Sunuculardan alınan kapasite bilgileri 5 saniyede bir güncellenen dinamik bir grafikle görselleştirilir.

----------

  Gereksinimler
  
Ruby 3.x

Java 8 veya daha yüksek bir sürüm

Python 3.x

----------

   Kurulum

1.Java Sunucularını Başlatma

Sunucuları başlatmak için her bir sunucu sınıfını çalıştırın:

    java Server1
    java Server2
    java Server3

2.admin.rb Çalıştırma

admin.rb dosyasını çalıştırarak sunucuları başlatma ve kapasite sorgusu gönderme işlemlerini başlatabilirsiniz:

    ruby admin.rb    

3.Plotter.py Çalıştırma

Kapasite verilerini görselleştirecek olan plotter.py dosyasını çalıştırın:

    python plotter.py

-------------

  Yapılandırma

dist_subs.conf dosyasında fault_tolerance_level parametresi ayarlanarak hata tolerans seviyesi belirlenebilir.

---------------

  Kullanım

1.Abonelik İşlemleri:

Abone Olma (SUBS): Abonelik işlemi başlatılır.

Abonelikten Çıkma (DEL): Abonelik iptal edilir.

2.Kapasite Sorgusu: Admin, her 5 saniyede bir sunuculardan kapasite verilerini talep eder ve plotter.py sunucusuna gönderir.

-----------

  Katkıda Bulunma

Bu projeye katkıda bulunmak için, yeni özellikler ekleyebilir veya mevcut kodu geliştirebilirsiniz.

Pull request göndererek katkı sağlayabilirsiniz.
