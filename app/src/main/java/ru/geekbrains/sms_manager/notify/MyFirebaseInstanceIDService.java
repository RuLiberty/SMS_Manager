package ru.geekbrains.sms_manager.notify;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;

// Получение ключа установки
public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private final String TAG = "PushIDService";

    @Override
    public void onNewToken(String token) {
        // Получить ключ установки приложения на устройство
        Log.d(TAG, "Refreshed token: " + token);
        // Здесь надо связать ключ с данными пользователя в базе данных
        // По этому ключу можно будет идентифицировать устройство
        // и отсылать сообщения определенному пользователю.
        // Предполагается, что у вас будет где-то хранится база с этими данными
        sendRegistrationToServer(token);
    }

    // Метод отправки ключа в вашу БД
    private void sendRegistrationToServer(String refreshedToken){

    }

}

