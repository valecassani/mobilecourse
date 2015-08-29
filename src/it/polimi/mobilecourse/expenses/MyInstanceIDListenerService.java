package it.polimi.mobilecourse.expenses;

import android.accounts.AccountManager;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyInstanceIDListenerService extends IntentService {

    private static final String TAG = MyInstanceIDListenerService.class.getSimpleName();

    private String userId;
    private String tipo;


    public MyInstanceIDListenerService() {
        super(TAG);
    }




    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"Service started");
        userId = intent.getExtras().getString("user_id");
        tipo = intent.getExtras().getString("tipo");
        Log.d(TAG,"ID = " + userId +"; Tipo = " + tipo);
        InstanceID instanceID = InstanceID.getInstance(this);
        String token = null;
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://www.unishare.it/tutored/getdevice.php";
        if (token != null && userId != null) {
            final String finalToken = token;
            Log.d(TAG, "Url maybe ok");
            StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public boolean onResponse(String response) {
                            // response
                            Log.d("Response GCM Reg", response);

                            return false;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.getMessage());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_utente", userId);
                    params.put("regid", finalToken);
                    params.put("tipo", tipo);


                    return params;
                }
            };
            queue.add(jsObjRequest);
            Toast.makeText(getApplicationContext(), "Richiesta Aggiunta", Toast.LENGTH_SHORT);
        }

    }




}
