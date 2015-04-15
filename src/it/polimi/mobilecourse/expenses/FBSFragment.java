package it.polimi.mobilecourse.expenses;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;


public class FBSFragment extends android.support.v4.app.Fragment {


    private boolean logged;
    private Activity ctx;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);

        }
    };
    private UiLifecycleHelper uiHelper;
    private View view;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.loginfb, container, false);
        ctx=getActivity();
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setOnErrorListener(new LoginButton.OnErrorListener() {

            @Override
            public void onError(FacebookException error) {
                Log.e("FBSFragment", "Error " + error.getMessage());
            }
        });
        authButton.setReadPermissions(Arrays.asList("public_profile","email"));

        authButton.setFragment(this);




        return view;

    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        final Intent myintent = new Intent(getActivity(), HomeStudent.class);
        final Intent closint=new Intent(this.getActivity(),LandingActivity.class);

        System.out.println(session.getPermissions());


        if (session.isOpened()) {
            Log.i("FBSFragment", "Logged in...");
            //prendo mail e controllo
            /*Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(final GraphUser user, final Response response) {
                    //String name = user.getName();
                    //String mail=user.getUsername();
                    //myintent.putExtra("Username", name);
                    //myintent.putExtra("Mail",mail);
                }
            //}).executeAsync();*/
            //startActivity(myintent);

            /*Session.openActiveSession(ctx, true, new Session.StatusCallback() {

                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    if (session.isOpened()) { */


            new Request(session,"me",null, HttpMethod.GET,new Request.Callback(){
                public void onCompleted(Response response){

                    String email=null;
                    String nome=null;
                    try {
                        email=response.getGraphObject().getProperty("email").toString();
                        nome=response.getGraphObject().getProperty("first_name").toString();

                    }
                    catch(NullPointerException e){
                        System.out.println(response.getError());
                    }
                    Bundle bundle=new Bundle();
                    bundle.putString("Mail",email);
                    bundle.putString("Nome",nome);
                    Intent myintent = new Intent(view.getContext(), RegistrationStudent.class);
                    startActivity(myintent);

                }

            }).executeAsync();


               /*         Request.newMeRequest(session, new Request.GraphUserCallback() {
                            @Override
                            public void onCompleted(final GraphUser user, final Response response) {
                                if (user != null) {
                                    logged = true;
                                    Bundle bundle=new Bundle();
                                    bundle.putString("Username",user.getName());
                                    bundle.putString("Mail",user.getUsername());

                                    myintent.putExtras(bundle);
                                    //startActivity(myintent);



                                }
                            }
                        }).executeAsync(); */


            //   }

            // }});


        } else if (session.isClosed()) {
            Log.i("FBSFragment", "Logged out...");
            //rimando LandingActivity
            //startActivity(closint);

        }

/*       Request.newMeRequest(new Session(ctx), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(final GraphUser user, final Response response) {
                String name = user.getName();
                String mail=user.getUsername();
                myintent.putExtra("Username", name);
                myintent.putExtra("Mail",mail);
            }
        }).executeAsync();
        startActivity(myintent);*/

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }


}
