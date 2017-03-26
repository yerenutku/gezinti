# gezinti
Getir &amp; BiTaksi Hackathon
Bu repo, getir-bitaksi hackathon 2017 süresince gerçekleştirilmiş `Gezinti` ekibinin backend, frontend ve android/ios uygulamalarını içerir.

### Ekip Üyeleri
- Eren
- Necil 
- Mert
- Yasin

## Backend
Backend hakkında söylenecek bir çift söz
#### Teknolojiler;

#### Gerçekleştirilenler;


## Frontend
Frontend hakkında söylenecek iki çift laf
#### Teknolojiler;

#### Gerçekleştirilenler;


## Android/iOS
Android/iOS hakkındaki gerçekler

#### Kütüphaneler
- play-services
- volley
- gson

#### Servis Çağrıları

Tek class üzerinden gerçekleşir. Jenerik sınıflar ile dönüş response tipi bilinir ve cast edilmesine gerek kalmaz. Request kendi responsunun tipine sahip olur.
`get` ve `post` methodları birer tane fakat içerisine aldıklarını abstract BaseRequest sayesinde response tipini belirtmek zorunda olur.
Örnek
```java
VolleyClientRequests.getInstance(mContext).post("yourUrl", request, new SuccessListener<EventCreateResponse>() {
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

#### 
