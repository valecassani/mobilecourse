package it.polimi.mobilecourse.expenses;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LandingFragment extends Fragment {

    private View view;
    private LandingActivity activity;
    private String str;


    int id;
    String nome;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        this.activity = (LandingActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.landing_fragment, container, false);
        buttonsActions();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


       // this.activity = (LandingActivity) activity;
    }



    public void buttonsActions() {

        Button butS = (Button) view.findViewById(R.id.buttonStudente);
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(activity, LoginStudente.class);
                startActivity(myintent);
                activity.finish();

            }
        });

        Button butT = (Button) view.findViewById(R.id.buttonTutor);
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
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), HomeStudent.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("Nome", nome);
                mBundle.putString("user_id", Integer.toString(id));
                myintent.putExtras(mBundle);
                startActivity(myintent);

            }
        });

        Button butT = (Button) view.findViewById(R.id.buttonTutor);
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
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), LoginStudente.class);
                startActivity(myintent);

            }
        });

        Button butT = (Button) view.findViewById(R.id.buttonTutor);
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
            String id_utente = result.getString("id_utente");
            nome = result.getString("nome");
            //System.out.println("nome "+nome);
            id = Integer.parseInt(id_utente);
            buttonsSActions();


        }
        if (response.compareTo("T") == 0) {

            str = "T";
            String id_utente = result.getString("id_utente");
            nome = result.getString("nome");
            id = Integer.parseInt(id_utente);
            buttonsTActions();


        }
        if (nome != null && nome.compareTo("") != 0) {
            setNomeWelcome(nome);
        }
        if (response.compareTo("N") == 0) {
            LoginManager.getInstance().logOut();
            buttonsActions();
        }


    }

    public interface manageListener{

         void manageButton();
    }
}
