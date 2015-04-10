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
import android.support.v4.app.FragmentActivity;
import android.util.Base64;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
public class LandingActivity extends HelpActivity implements LandingFragment.manageListener {


    private LandingFragment lf;
    private WelcomeFragment wf;
    private ProgressBar progressView;
    //private manageButton mb=null;

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback;

    String nome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLanding();
        progress(true);
        showWelcome();
        manageSession(savedInstanceState);
        //mb= new manageButton();
        //mb.execute((Void) null);
        //manageButton();



    }

    private void manageSession(Bundle savedInstanceState){

        //questa funzione controllo se l'utente è già loggato via fb o no



        //è loggato entra qua
        callback=new Session.StatusCallback(){
            @Override
            public void call(final Session session,final SessionState state,final Exception exception){



                onSessionStateChange(session, state, exception);






            }
        };
        uiHelper = new UiLifecycleHelper(this,callback);
        uiHelper.onCreate(savedInstanceState);
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Session session=Session.getActiveSession();
        if(session.getPermissions().isEmpty()){
            System.out.println("logged false");
            //se non è loggato in fb,escono bottoni semplici che mandano a pagine di login
            FragmentManager fragMan = getFragmentManager();
            FragmentTransaction fragTrans=fragMan.beginTransaction();
            fragTrans.replace(R.id.fragreplace,lf).commit();
            progress(false);

        }


    }

    private void setLanding(){
        setContentView(R.layout.landing_activity);

        progressView=(ProgressBar)findViewById(R.id.progressBarRS);
        lf=new LandingFragment();
        //hideButton();
        wf=new WelcomeFragment();
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
        }

        if(op=="controlloFB"){
            LandingFragment lfr=(LandingFragment) fragment;
            lfr.control(result);
        }


    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {



        if(session==null || session.isClosed()){
            Log.i("LandingActivity", "session nulla...");

            /*session=Session.openActiveSessionFromCache(this);
            new Request(session,"me",null, HttpMethod.GET,new Request.Callback(){
                public void onCompleted(Response response){
                    String us=null;
                    try {
                        us=response.getGraphObject().getProperty("email").toString();
                        nome=response.getGraphObject().getProperty("first_name").toString();

                    }
                    catch(NullPointerException e){
                        System.out.println(response.getError());
                    }
                    String url="exist_user.php?mail=".concat(us);
                    lf.ftpControl(url);
                }

            }).executeAsync();*/


        }


        if(session!=null && session.isOpened()){

            Log.i("LandingActivity", "session not null...");



            System.out.println(session.getPermissions());

            new Request(session,"me",null, HttpMethod.GET,new Request.Callback(){
                public void onCompleted(Response response){
                    String us=null;
                    try {
                        us=response.getGraphObject().getProperty("email").toString();
                        nome=response.getGraphObject().getProperty("first_name").toString();

                    }
                    catch(NullPointerException e){
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