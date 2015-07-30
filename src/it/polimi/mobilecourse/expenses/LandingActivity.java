package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo on 23/12/2014.
 */
public class LandingActivity extends HelpActivity implements LandingFragment.manageListener {

    boolean loadDone;
    boolean logged;
    private String username;
    private String nome;
    private String cognome;
    private LandingFragment lf;
    private WelcomeFragment wf;
    private ProgressBar progressView;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    //private manageButton mb=null;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // please enter your sender id
    String SENDER_ID = "420313149585";

    static final String TAG = "GCMDemo";
    GoogleCloudMessaging gcm;

    TextView mDisplay;
    EditText ed;
    Context context;
    String regid;
    Integer tipo;
    Integer id_utente;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanding();
        loadDone = false;
        logged = false;
        FacebookSdk.sdkInitialize(getApplicationContext());
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };
        callbackManager = CallbackManager.Factory.create();
        updateWithToken(AccessToken.getCurrentAccessToken());


        progress(true);
        showWelcome();
        manageSession(savedInstanceState);
        if (logged == false) {


        }

        //mb= new manageButton();
        //mb.execute((Void) null);
        //manageButton();


    }

    private void manageSession(Bundle savedInstanceState) {


        //è loggato entra qua


    }

    private void updateWithToken(final AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            Toast.makeText(getApplicationContext(), "Login con Facebook avvenuto", Toast.LENGTH_SHORT);



            getEmail(currentAccessToken);


        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Log.i("Landing", "logged false");
                    //se non è loggato in fb,escono bottoni semplici che mandano a pagine di login
                    FragmentManager fragMan = getFragmentManager();
                    FragmentTransaction fragTrans = fragMan.beginTransaction();
                    fragTrans.replace(R.id.fragreplace, lf).commit();
                    progress(false);

                }
            }, 100);

        }
    }



    private void getEmail(AccessToken currentAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        try {
                            Log.v("LoginActivity", response.getJSONObject().getString("email"));
                            username = object.getString("email");
                            Log.v("LandingFragment", username);
                            nome = object.getString("first_name");
                            Log.v("LandingFragment", nome);
                            cognome = object.getString("last_name");
                            Log.v("LandingFragment", cognome);
                            controlFbLogin();


                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Errore di connessione", Toast.LENGTH_LONG).show();
                        }
                    }


                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();


    }

    private void controlFbLogin() {
        String url = "http://www.unishare.it/tutored/exist_user.php?mail=" + username;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONObject obj = response.getJSONObject(0);
                            lf.control(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Landing", response.toString());


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Landing Fragment", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });
        queue.add(jsonObjectRequest);

    }

    private void setLanding() {
        setContentView(R.layout.landing_activity);

        progressView = (ProgressBar) findViewById(R.id.progressBarRS);
        lf = new LandingFragment();
        //hideButton();
        wf = new WelcomeFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }


    @Override
    public void handleResult(ArrayList<ObjDb> result, String op, Fragment fragment) {


        if (result == null) {
            progress(false);
        }

        if (op == "controlloFB") {
            LandingFragment lfr = (LandingFragment) fragment;
            //lfr.control(result);
        }


    }


    public void showWelcome() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.welcomemessage, wf);
        fragmentTransaction.commit();


    }

    public WelcomeFragment getWf() {

        return wf;
    }


    public void progress(final boolean show) {
        final int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);


        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });


    }

    private void showButton() {
        System.out.println("logged true");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragreplace, lf).commit();
    }

    private void hideButton() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(lf);
        fragmentTransaction.commitAllowingStateLoss();

    }

    /*public class manageButton extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void ...params){



            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){

            if(success) {







            }
        }




    }*/

    @Override
    public void manageButton() {
        progress(false);
        showButton();

    }

    @Override
    public void onPause() {
        super.onPause();
        accessTokenTracker.stopTracking();

    }

    public void onResume() {
        super.onResume();
        manageButton();
        accessTokenTracker.startTracking();
    }

    public void startGCM(int id_utente, int tipo) {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (!regid.isEmpty()) {
                //parametri: id_utente,tipo utente
                new RegisterBackground().execute(String.valueOf(id_utente), String.valueOf(tipo));
            }

        }
        //

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {

        return getSharedPreferences(GCMMainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    class RegisterBackground extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;
                System.out.println("regid" + regid);
                Log.d("111", msg);
                int id = Integer.parseInt(arg0[0]);
                int tipo = Integer.parseInt(arg0[1]);
                sendRegistrationIdToBackend(id, tipo);

                // Persist the regID - no need to register again.
                storeRegistrationId(context, regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }


        private void sendRegistrationIdToBackend(int id_utente, int tipo) {
            // Your implementation here.


            String url = "http://www.unishare.it/tutored/getdevice.php";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("regid", regid));
            params.add(new BasicNameValuePair("id_utente", Integer.toString(id_utente)));
            params.add(new BasicNameValuePair("tipo", Integer.toString(tipo)));


            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

        private void storeRegistrationId(Context context, String regId) {
            final SharedPreferences prefs = getGCMPreferences(context);
            int appVersion = getAppVersion(context);
            Log.i(TAG, "Saving regId on app version " + appVersion);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PROPERTY_REG_ID, regId);
            editor.putInt(PROPERTY_APP_VERSION, appVersion);
            editor.commit();
        }


    }
}