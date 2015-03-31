package it.polimi.mobilecourse.expenses;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class LandingFragment extends Fragment {

    private View view;
    private LandingActivity activity;
    private String str;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






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


        this.activity = (LandingActivity) activity;
    }

    private void buttonsActions(){

        Button butS=(Button) view.findViewById(R.id.buttonStudente);
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), LoginStudente.class);
                startActivity(myintent);

            }
        });

        Button butT=(Button) view.findViewById(R.id.buttonTutor);
        butT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), LoginTutor.class);
                startActivity(myintent);

            }
        });


    }

    private void buttonsSActions(){

        Button butS=(Button) view.findViewById(R.id.buttonStudente);
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), HomeStudent.class);
                startActivity(myintent);

            }
        });

        Button butT=(Button) view.findViewById(R.id.buttonTutor);
        butT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), LoginTutor.class);
                startActivity(myintent);

            }
        });


    }

    private void buttonsTActions(){

        Button butS=(Button) view.findViewById(R.id.buttonStudente);
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), LoginStudente.class);
                startActivity(myintent);

            }
        });

        Button butT=(Button) view.findViewById(R.id.buttonTutor);
        butT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), HomeStudent.class);
                startActivity(myintent);

            }
        });


    }


    public void ftpControl(String url){

        new RequestFtp().setParameters(activity, url, "controlloFB", LandingFragment.this).execute();
    }


    private String getControl(){
        return str;

    }



    public void control(ArrayList<ObjDb> result){

        ObjDb res = result.get(0);
        String strR=res.get("ID");
        if(strR=="S"){

            str="S";
            Intent myintent = new Intent(view.getContext(), HomeStudent.class);
            startActivity(myintent);

        }
        if(strR=="T"){

            str="T";
            Intent myintent = new Intent(view.getContext(), HomeStudent.class);
            startActivity(myintent);
        }



    }
}
