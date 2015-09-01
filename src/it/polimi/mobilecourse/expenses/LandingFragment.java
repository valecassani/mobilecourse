package it.polimi.mobilecourse.expenses;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessTokenTracker;
import com.facebook.LoggingBehavior;

import com.facebook.login.LoginManager;
import com.google.android.gms.fitness.result.SessionStopResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LandingFragment extends Fragment {

    private View view;
    private LandingActivity activity;
    private String str;


    int id;
    String nome;

    Typeface myCustomFont;

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
        this.activity = (LandingActivity) getActivity();
        context = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.landing_fragment, container, false);
        myCustomFont=Typeface.createFromAsset(activity.getAssets(), "font/Gotham-Book.ttf");

        setFont();
        buttonsActions();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


       // this.activity = (LandingActivity) activity;
    }

    private void setFont(){


        TextView accedi=(TextView)view.findViewById(R.id.textView2);
        //TextView lineuno=(TextView)view.findViewById(R.id.textView27);
        //TextView linedue=(TextView)view.findViewById(R.id.textView28);
        accedi.setTypeface(myCustomFont);
        //lineuno.setTypeface(myCustomFont);
        //linedue.setTypeface(myCustomFont);

    }



    public void buttonsActions() {

        Button butS = (Button) view.findViewById(R.id.buttonStudente);
        butS.setTypeface(myCustomFont);
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(activity, LoginStudente.class);
                startActivity(myintent);

            }
        });

        Button butT = (Button) view.findViewById(R.id.buttonTutor);
        butT.setTypeface(myCustomFont);
        butT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(activity, LoginTutor.class);
                startActivity(myintent);

            }
        });


    }

    private void buttonsSActions() {
        (activity).manageButton();
        Button butS = (Button) view.findViewById(R.id.buttonStudente);
        butS.setTypeface(myCustomFont);
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), HomeStudent.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("Nome", nome);
                mBundle.putString("user_id", Integer.toString(id_utente));
                myintent.putExtras(mBundle);
                startActivity(myintent);

            }
        });

        Button butT = (Button) view.findViewById(R.id.buttonTutor);
        butT.setTypeface(myCustomFont);
        butT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), LoginTutor.class);
                startActivity(myintent);

            }
        });

        try {
            ((manageListener) activity).manageButton();
        }
        catch(ClassCastException cce){

        }


    }

    private void buttonsTActions() {
        (activity).manageButton();

        Button butS = (Button) view.findViewById(R.id.buttonStudente);
        butS.setTypeface(myCustomFont);
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), LoginStudente.class);
                startActivity(myintent);

            }
        });

        Button butT = (Button) view.findViewById(R.id.buttonTutor);
        butT.setTypeface(myCustomFont);
        butT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), HomeTutor.class);
                startActivity(myintent);

            }
        });
        try {
            ((manageListener) activity).manageButton();
        }
        catch(ClassCastException cce){

        }


    }


    public void ftpControl(String url) {

        new RequestFtp().setParameters(this.activity, url, "controlloFB", LandingFragment.this).execute();
    }


    private void setNomeWelcome(String nome) {
        WelcomeFragment wf = activity.getWf();
        wf.setText(nome);
    }


    public void control(JSONObject result) throws JSONException {


        String response = result.getString("Response");
        //System.out.println("Response "+response);
        if (response.compareTo("S") == 0) {

            str = "S";
            id_utente = result.getInt("id_utente");
            nome = result.getString("nome");
            //System.out.println("nome "+nome);
            buttonsSActions();
            activity.startGCM(0,id_utente);
            //tipo 0 studente 1 tutor


        }
        if (response.compareTo("T") == 0) {

            str = "T";

            buttonsTActions();


        }

        if (response.compareTo("ST") == 0) {
            str = "ST";
            String id_utente = result.getString("id_utente");
            nome = result.getString("nome");
            id = Integer.parseInt(id_utente);
            buttonsSTActions();

        }
        if (nome != null && nome.compareTo("") != 0) {
            Log.d(TAG, "Nome arrivato " + nome);
            setNomeWelcome(nome);
        }
        if (response.compareTo("N") == 0) {
            LoginManager.getInstance().logOut();
            buttonsActions();
        }


    }

    private void buttonsSTActions() {
        (activity).manageButton();

        Button butS = (Button) view.findViewById(R.id.buttonStudente);
        butS.setTypeface(myCustomFont);
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), HomeStudent.class);
                startActivity(myintent);

            }
        });

        Button butT = (Button) view.findViewById(R.id.buttonTutor);
        butT.setTypeface(myCustomFont);
        butT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), HomeTutor.class);
                startActivity(myintent);

            }
        });
        try {
            ((manageListener) activity).manageButton();
        }
        catch(ClassCastException cce){

        }





    }

    public interface manageListener{

         void manageButton();
    }
}
