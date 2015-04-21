package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Matteo on 23/12/2014.
 */
public class LandingActivity extends HelpActivity implements LandingFragment.manageListener {

    boolean loadDone;
    private String username;
    private LandingFragment lf;
    private WelcomeFragment wf;
    private ProgressBar progressView;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    //private manageButton mb=null;


    String nome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDone = false;
        FacebookSdk.sdkInitialize(getApplicationContext());
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                while(loadDone = false);
                updateWithToken(newAccessToken);
            }
        };
        callbackManager = CallbackManager.Factory.create();
        loadDone = false;
        setLanding();
        progress(true);

        while (loadDone = false);
        updateWithToken(AccessToken.getCurrentAccessToken());
        showWelcome();
        manageSession(savedInstanceState);
        Log.i("Landing", "logged false");
        //se non è loggato in fb,escono bottoni semplici che mandano a pagine di login
        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        fragTrans.replace(R.id.fragreplace, lf).commit();
        //mb= new manageButton();
        //mb.execute((Void) null);
        //manageButton();




    }

    private void manageSession(Bundle savedInstanceState){





                //è loggato entra qua








    }

    private void updateWithToken(final AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            Toast.makeText(getApplicationContext(), "Login con Facebook avvenuto", Toast.LENGTH_SHORT);
            Intent intent = new Intent(LandingActivity.this, HomeStudent.class);
            Bundle bundle = new Bundle();


            bundle.putString("mail", username);

            Log.i("Landing", "username" + bundle.getString("mail"));
            intent.putExtras(bundle);

            startActivity(intent);



        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                }
            }, 100);
                progress(false);

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
                            loadDone = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();



    }

    private void setLanding(){
        setContentView(R.layout.landing_activity);

        progressView=(ProgressBar)findViewById(R.id.progressBarRS);
        lf=new LandingFragment();
        //hideButton();
        wf=new WelcomeFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }



    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){


        if(result==null){
            progress(false);
        }

        if(op=="controlloFB"){
            LandingFragment lfr=(LandingFragment) fragment;
            lfr.control(result);
        }


    }







    public void showWelcome(){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.welcomemessage, wf);
        fragmentTransaction.commit();


    }

    public WelcomeFragment getWf(){

        return wf;
    }


    /*private void controlloUtente(){

        Session session=Session.getActiveSession();
        if(session==null || session.isClosed()){
            Log.i("LandingActivity", "session nulla...");

            session=Session.openActiveSessionFromCache(this);
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(final GraphUser user, final Response response) {
                    if (user != null) {
                        String url = "exist_user.php?mail=".concat(user.getUsername());
                        lf.ftpControl(url);



                    }
                }
            }).executeAsync();


        }

        if(session!=null && session.isOpened()){

            Log.i("LandingActivity", "session not null...");

            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(final GraphUser user, final Response response) {
                    if (user != null) {
                        String url="exist_user.php?mail=".concat(user.getUsername());
                        lf.ftpControl(url);

                    }
                }
            }).executeAsync();



        }







    }*/

    public void progress(final boolean show){
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

    private void showButton(){
        System.out.println("logged true");
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragreplace,lf).commit();
    }

    /*private void hideButton(){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.hide(lf);
        fragmentTransaction.commitAllowingStateLoss();

    }*/

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

    public void manageButton(){
        progress(false);
        showButton();

    }



}