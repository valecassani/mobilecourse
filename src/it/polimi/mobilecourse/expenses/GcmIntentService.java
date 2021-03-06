package it.polimi.mobilecourse.expenses;

/**
 * Created by Matteo on 30/05/2015.
 */
import com.google.android.gms.gcm.GoogleCloudMessaging;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

public class GcmIntentService extends IntentService{
    private Context context;
    private String msg;
    private String tipo;
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;
    public static final String TAG = "GCM Demo";
    private Intent receivedIntent;

    public GcmIntentService() {
        super("GcmIntentService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        JSONObject obj;
        receivedIntent = intent;
        Bundle extras = intent.getExtras();
        msg = intent.getStringExtra("message");
        tipo = intent.getStringExtra("type");
        String mex=null;
        String type = null;
        System.out.println(msg);
        try {

            obj = new JSONObject(msg);
            mex=obj.getString("message");

        } catch (Throwable t) {
        }

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(),null);
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString(),null);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.

                // Post notification of received message.
                //sendNotification("Received: " + extras.toString());
                sendNotification(msg,tipo);
                screenOn();
                Log.i(TAG, "Received: " + extras.toString());


            }
        }
    }
    private void sendNotification(String msg, String type) {


        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent myintent = null;
        PendingIntent contentIntent = null;
        if (type != null) {


            if (type.equals("prenotazione")) {

                myintent = new Intent(this, PrenotazioniDettagliActivity.class);
                myintent.putExtra("message", msg);
                myintent.putExtra("id", receivedIntent.getStringExtra("id_prenotazione"));
                contentIntent = PendingIntent.getActivity(this, 0,
                        myintent, PendingIntent.FLAG_UPDATE_CURRENT);



            } else {
                if (type.equals("richiesta")) {
                    Log.d(TAG, "Notification Richiesta ricevuta");
                    myintent = new Intent(this, RichiestaNotificaActivity.class);
                    myintent.putExtra("message", msg);
                    myintent.putExtra("tutor_id", receivedIntent.getStringExtra("tutor_id"));
                    contentIntent = PendingIntent.getActivity(this, 0,
                            myintent, PendingIntent.FLAG_UPDATE_CURRENT);

                }
            }


            //l'iconcina te l'ho messa nella cartella e va in drawable-hdpi
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.gmc_img)
                            .setContentTitle("Tutored")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(msg))
                            .setContentText(msg);

            Notification note = mBuilder.build();

            note.defaults |= Notification.DEFAULT_VIBRATE;
            note.defaults |= Notification.DEFAULT_SOUND;





            mBuilder.setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, note);
        }
    }

    private void screenOn(){



        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn = pm.isScreenOn();

        Log.i("screen on", ""+isScreenOn);

        if(isScreenOn==false)
        {

            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");

            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        }


    }
}
