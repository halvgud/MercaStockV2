package mx.mercatto.mercastock;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;


public class PushNotificationService extends GcmListenerService {
    public static ArrayList<String> Xrray;
    public static String idSucursal="0";
        public static String host="";
        public static String Sucursal="0";
    @Override
    public void onMessageReceived(String from, Bundle data) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            String message = data.getString("message");
            Log.d("data received",data.toString());
            Sucursal = data.getString("descripcionSucursal");
            host=data.getString("host");

            editor.putString("NOMBRE_AMISTOSO", Sucursal);
            editor.putString("URL_REST", host);

            editor.apply();
            /*try {
                    Xrray = new ArrayList<>();
                    JSONArray jar = new JSONArray(data.getString("data"));
                    for (int i=0;i<jar.length();i++){
                            Xrray.add(jar.get(i).toString());
                    }
                    HashSet<String> hashSet = new HashSet<String>();
                    hashSet.addAll(Xrray);
                    Xrray.clear();
                    Xrray.addAll(hashSet);
            }catch(JSONException e){
                    Log.d("",e.getMessage());
            }*/
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_mstockicon)
                            .setContentTitle("MercaStock! Sucursal:"+Sucursal)
                            .setContentText(message)
                    .setAutoCancel(true);

            Intent resultIntent = new Intent(this, PantallaPrincipal.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(PantallaPrincipal.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(300);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
            Log.d("mess", message);
    }
}
