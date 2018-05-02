package br.com.paulosalvatore.codelab_android_ocean_a11;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by paulo on 01/05/2018.
 */

public class NotificationPublisher extends BroadcastReceiver {

	public static String NOTIFICATION_ID = "notification-id";
	public static String NOTIFICATION = "notification";

	public void onReceive(Context context, Intent intent) {
		if (intent.getAction() != null && context != null) {
			if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
				LocalData localData = new LocalData(context);
				NotificationScheduler.setReminder(context, NotificationPublisher.class,
						localData.get_hour(), localData.get_min());
				return;
			}
		}

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = intent.getParcelableExtra(NOTIFICATION);
		int id = intent.getIntExtra(NOTIFICATION_ID, 0);

		if (notificationManager != null) {
			notificationManager.notify(id, notification);
		}
	}
}