package br.com.uarini.pogapp.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import br.com.uarini.pogapp.PokemonNotificationActivity;
import br.com.uarini.pogapp.R;

/**
 * Created by marcos on 28/08/16.
 */
public class MyNotificationManager {
    public static void pin(Context mContext){

        final Intent resultIntent = new Intent(mContext, PokemonNotificationActivity.class);
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_notification))
                        .setSmallIcon(R.drawable.ic_notification_small)
                        .setContentTitle(mContext.getString(R.string.app_name))
                        .setContentText(mContext.getString(R.string.notification_subtitle));

        final PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        final NotificationManager mMyNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mMyNotificationManager.notify(0, mBuilder.build());
    }
}
