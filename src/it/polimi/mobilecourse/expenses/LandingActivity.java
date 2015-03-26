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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

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
public class LandingActivity extends HelpActivity {


    private LandingFragment lf;
    private ProgressBar progressView;
    private manageButton mb=null;

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback=new Session.StatusCallback(){
        @Override
        public void call(final Session session,final SessionState state,final Exception exception){

            List<String> lp=session.getPermissions();
            if(lp.contains("email")){

                onSessionStateChange(session, state, exception);



            }


        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLanding();
        progress(true);
        uiHelper = new UiLifecycleHelper(this,callback);
        uiHelper.onCreate(savedInstanceState);
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        mb= new manageButton();
        mb.execute((Void) null);




    }

    private void setLanding(){
        setContentView(R.layout.landing_activity);

        progressView=(ProgressBar)findViewById(R.id.progressBarRS);
        lf=new LandingFragment();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        uiHelper.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){

        if(result==null){
            progress(false);
            showButton();
        }

        if(op=="controlloFB"){
            LandingFragment lfr=(LandingFragment) fragment;
            lfr.control(result);
        }


    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {

        Session.StatusCallback statusCallback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {

            }
        };

       //session.requestNewReadPermissions(new Session.NewPermissionsRequest(this,Arrays.asList("email")));

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
            System.out.println(session.getPermissions());


            /*Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(final GraphUser user, final Response response) {
                    if (user != null) {

                        id[0] =user.getId();
                        System.out.println("Id:"+ id[0]);
                        //Log.i("LandingActivity", user.getUsername());

                        //String url="exist_user.php?mail=".concat(u);
                        //lf.ftpControl(url);

                    }
                }
            }).executeAsync();*/

            //Session.OpenRequest req=new Session.OpenRequest(this).setPermissions("basic_info","email");
            //new Session.OpenRequest(this).setPermissions(Arrays.asList("basic_info", "email")).setCallback(statusCallback);
            System.out.println(session.getPermissions());

            new Request(session,"me",null, HttpMethod.GET,new Request.Callback(){
                public void onCompleted(Response response){


                    //String u=null;
                    String us=null;
                    try {
                    us=response.getGraphObject().getProperty("email").toString();



                    //  u = jobj.getString("email");
                    }
                    catch(NullPointerException e){
                        //System.out.println(response.toString());
                         System.out.println(response.getError());
                    }
                    String url="exist_user.php?mail=".concat(us);
                    lf.ftpControl(url);
                }

            }).executeAsync();

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
    }

    private void showButton(){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,lf);
        fragmentTransaction.commit();
    }


    private void controlloUtente(){

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







    }

    private void progress(final boolean show){
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

    public class manageButton extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void ...params){


            //controlloUtente();


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){

            if(success) {
                progress(false);
                showButton();






            }
        }




    }



}