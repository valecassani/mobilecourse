package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

/**
 * Created by Matteo on 23/12/2014.
 */
public class LoginStudente extends FragmentActivity {

    boolean logged;
    Button skipButton;


    private FBSFragment fbsFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_studente);
        skipButton = (Button) findViewById(R.id.skip_button);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),HomeStudent.class);
                startActivity(intent);
            }
        });







        //Button butS = (Button) findViewById(R.id.buttonLogin);
/*        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), HomeStudent.class);
                startActivity(myintent);

            }
        });*/

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Session.getActiveSession().onActivityResult(this,requestCode,resultCode,data);
        //facebookLogin();

    }

    public void facebookLogin(){
        //final Intent myintent = new Intent(getApplicationContext(), DataActivityStudent.class);


        Session.openActiveSession(this, true, new Session.StatusCallback() {

            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    Request.newMeRequest(session, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(final GraphUser user, final Response response) {
                            if (user != null) {
                                logged = true;
                                String name=user.getName();
                                //myintent.putExtra("Username",name);

                            }
                        }
                    }).executeAsync();
                    //startActivity(myintent);


                }
                else
                {
                    logged = false;
                }
            }});
    }


}