# gezinti
Getir &amp; BiTaksi Hackathon
Bu repo, getir-bitaksi hackathon 2017 süresince gerçekleştirilmiş `Gezinti` ekibinin backend, frontend ve android/ios uygulamalarını içerir.

### Ekip Üyeleri
- Eren
- Necil 
- Mert
- Yasin
#### Commit Standartı
`[platform][branch][feature][commit message]`
şeklindedir. Geliştirme branch'i `dev` branchidir.

## Backend
NodeJs ve MongoDB, ve Mongoose kullanılarak REST API geliştirildi.
#### Teknolojiler;
- NodeJS
- MongoDB
- ExpressJs
- Mongoose
#### Gerçekleştirilenler;
Genel tasklar:
- Error handling
- Promise kullanımı

Etkinlik taskları:
- Etkinlik oluşturulması
- Kullanıcıların etkinlie katılabilmeleri ve çıkabilmeleri
- Etkinliklerin tek olarak görüntülenebilmesi
- Etkinlik aramasında yakınlığa göre etkinliklerin seçilmesi
- Etkinliklerin oluşturan kullanıcı tarafından silinebilmesi
- Etkinliklere yorum yapılması
- Yapılan yorumların yapan kişi tarafından silinebilmesi


Kullanıcı taskları
- Kullanıcıların bilgilerinin görüntülenebilmesi
- Kullanıcı kaydetme
- Kullanıcının profil bilgisini görüntüleme
- Kullanıcıyı seçmek
## Frontend
AngularJs ve NodeJs tabanlı single page application geliştirildi.
#### Teknolojiler;
- AngularJs
- NodeJs
- Material Design Lite
- Jquery
- Material Select
- Material Icons
#### Gerçekleştirilenler;
- Kullanıcının bulunduğu konumun alınması
- Bu konuma göre arama yapılması
- Etkinliklerin tek tek görüntülenmesi
- Etkinlik oluşturma
- Harita üstünden seçilen bir noktadan etkinliğe gitme
- Etkinliğe katılma
- Etkinlikten Çıkma
## Android
Android hakkındaki gerçekler

#### Kütüphaneler
```java
    compile 'com.android.support:design:25.3.0'
    compile 'com.android.support:recyclerview-v7:25.3.0'
    compile 'com.android.volley:volley:1.0.0'
    compile 'com.google.code.gson:gson:2.7'
    compile 'com.google.android.gms:play-services-maps:10.2.0'
    compile 'com.google.android.gms:play-services-location:10.2.0'
    compile 'com.google.android.gms:play-services-places:10.2.0'
```

#### Servis Çağrıları

Tek class üzerinden gerçekleşir. Jenerik sınıflar ile dönüş response tipi bilinir ve cast edilmesine gerek kalmaz. Request kendi responsunun tipine sahip olur.
`get` ve `post` methodları birer tane fakat içerisine aldıklarını abstract BaseRequest sayesinde response tipini belirtmek zorunda olur.
`SuccessListener<T>` şeklinde jenerik bir sınıf alır. Bu sınıf override edilip dönen `onSuccess` methodunun dönüş tipini belirler. 
Örnek
```java
yourInstance.post("yourUrl", request, new SuccessListener<EventCreateResponse>() {
    @Override
    public void onSuccess(EventCreateResponse response) {
    ...
    }
 },
 new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
    ...
    }
});
                
```
#### Hata yorumlamaları
`Volley` ile dönen `VolleyError` türündeki objeler türüne göre bir mesaj türetilerek, UI 'a paslanır. Base sınıf hata türüne göre generate edilmiş hatayı kullanıcıya gösterir.

#### View - Interactor
Çok fazla servis çağrısı olmadığı için presenter yapısı araya koyulması gereksiz görüldü.
Interactor -> servis çağrısı yapar ve sonucu UI'lara interfaceler ile paslar. Çağıran sınıf interface tanımlamaları yaptıkları için çağırdıkları yerde ya da instance oluşturarak gelen cevaplar ile `UI update` işlemlerini yapabilir.
`BaseActivity` , dialog gösterir ve error gösterme işlerini yapar. Böylelikle her fragment/activity içerisinde kod tekrarı yapılmaz.

#### Kullanıcı etkileşimi
Ana fonksiyonun harita üzerinde olmasından dolayı, harita açık iken yapılacak etkileşimleri alttan açılan menu şeklinde `BottomSheet` kullanılarak yapıldı.
Bu sayede kullanıcı haritayı görmek istediğinde UX problemi çekmeden haritaya ulaşabilir, etkileşime geçmek istediğinde haritadan kopmadan devam edebilir.
