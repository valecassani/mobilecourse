package it.polimi.mobilecourse.expenses;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;



import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Matteo on 23/12/2014.
 */
public class LoginStudente extends ActionBarActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private String email;
    private String nome;
    private String cognome;






    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try
        {
            //paste Your package name at the first parameter
            PackageInfo info = getPackageManager().getPackageInfo("it.polimi.mobilecourse.expenses",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        }
        catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        setContentView(R.layout.login_studente);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email"));
        FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i("LoginStudente", "Entro nel ciclo per recuperare i dati");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                try {
                                    email = response.getJSONObject().getString("email");
                                    nome = response.getJSONObject().getString("first_name");



                                    cognome = response.getJSONObject().getString("last_name");
                                    controlFbLogin();





                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();

            }


            @Override
            public void onCancel() {
                Log.d("Shreks Fragmnt", "onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("Shreks Fragment", "onError " + e);
            }
        };

        loginButton.registerCallback(callbackManager,mFacebookCallback);

    }




    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

    private void controlFbLogin() {
        String url = "http://www.unishare.it/tutored/exist_user.php?mail=" + email;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONObject obj = response.getJSONObject(0);
                            if (obj.getString("Response").equals("S")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("mail",email);
                                bundle.putString("user_id",obj.getString("id_utente").toString());
                                Intent myintent = new Intent(LoginStudente.this, HomeStudent.class);
                                myintent.putExtras(bundle);
                                startActivity(myintent);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("Tipo", "FB");
                                bundle.putString("Mail", email);
                                bundle.putString("Nome", nome);
                                bundle.putString("Cognome",cognome);
                                Intent myintent = new Intent(LoginStudente.this, RegistrationStudent.class);
                                myintent.putExtras(bundle);
                                startActivity(myintent);

                            }
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



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        invokeFragmentManagerNoteStateNotSaved();

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void invokeFragmentManagerNoteStateNotSaved() {
        /**
         * For post-Honeycomb devices
         */
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        try {
            Class cls = getClass();
            do {
                cls = cls.getSuperclass();
            } while (!"Activity".equals(cls.getSimpleName()));
            Field fragmentMgrField = cls.getDeclaredField("mFragments");
            fragmentMgrField.setAccessible(true);

            Object fragmentMgr = fragmentMgrField.get(this);
            cls = fragmentMgr.getClass();

            Method noteStateNotSavedMethod = cls.getDeclaredMethod("noteStateNotSaved", new Class[] {});
            noteStateNotSavedMethod.invoke(fragmentMgr, new Object[] {});
            Log.d("DLOutState", "Successful call for noteStateNotSaved!!!");
        } catch (Exception ex) {
            Log.e("DLOutState", "Exception on worka FM.noteStateNotSaved", ex);
        }
    }





}