package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Matteo on 23/12/2014.
 */
public class LoginStudente extends ActionBarActivity {

    boolean logged;
    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;


    private FBSFragment fbsFragment;


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
                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);

            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
        }
        catch (NoSuchAlgorithmException e)
        {
        }
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.login_studente);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.fb_student_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email"));;
        FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {



                Profile profile = Profile.getCurrentProfile();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                try {
                                    Log.v("LoginActivity", response.getJSONObject().getString("email"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();

                // Get User Name

                Log.i("LoginFB", profile.getFirstName());

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




    /*
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Session.getActiveSession().onActivityResult(this,requestCode,resultCode,data);

    }

    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){


        if(op=="logStudente"){

            LoginSFragment lsfrag=(LoginSFragment) fragment;
            //lsfrag.manageLogin(result);
        }



    }

    */

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }


}