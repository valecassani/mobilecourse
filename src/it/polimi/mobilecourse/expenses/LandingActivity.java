package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Matteo on 23/12/2014.
 */
public class LandingActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.landing_activity);
        buttonsActions();
    }


    private void buttonsActions(){

        Button butS=(Button) findViewById(R.id.buttonStudente);
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), LoginStudente.class);
                startActivity(myintent);

            }
        });

        Button butT=(Button) findViewById(R.id.buttonTutor);
        butT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(v.getContext(), LoginTutor.class);
                startActivity(myintent);

            }
        });


    }
}