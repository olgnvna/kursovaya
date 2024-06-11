package com.example.kursovaya;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BookingReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.createNotification("Скоро ваше бронирование!", "Ваше бронирование начнется завтра.");
    }
}
