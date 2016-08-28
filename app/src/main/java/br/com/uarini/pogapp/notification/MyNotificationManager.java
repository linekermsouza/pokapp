package br.com.uarini.pogapp.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import br.com.uarini.pogapp.PokemonNotificationActivity;
import br.com.uarini.pogapp.R;

/**
 * Created by marcos on 28/08/16.
 */
public class MyNotificationManager {
    public static void pin(Context mContext){

        Intent resultIntent = new Intent(mContext, PokemonNotificationActivity.class);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(mContext.getString(R.string.app_name));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        stackBuilder.addParentStack(PokemonNotificationActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mMyNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mMyNotificationManager.notify(0, mBuilder.build());
    }
}
