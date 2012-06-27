package uk.co.mpkdashx.CBTT;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		buildNotification(context);
	}

	private void buildNotification(Context context){
		  NotificationManager notificationManager 
		  = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		  NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		  
		  Intent intent = new Intent(context, MainActivity.class);
		  PendingIntent pendingIntent 
		  = PendingIntent.getActivity(context, 0, intent, 0);
		  
		  builder
		  .setDefaults(Notification.DEFAULT_ALL)
		  .setSmallIcon(R.drawable.bus)
		  .setContentTitle("ContentTitle")
		  .setContentText("ContentText")
		  .setContentInfo("ContentInfo")
		  .setTicker("Ticker")
		  .setContentIntent(pendingIntent)
		  .setAutoCancel(true);
		  
		  Notification notification = builder.getNotification();
		  
		  notificationManager.notify(R.drawable.bus, notification);
		 }

}
