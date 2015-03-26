package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Matteo on 23/12/2014.
 */
public class LoginTutor extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_tutor);

        Button butS=(Button) findViewById(R.id.buttonLogin);
        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Intent myintent = new Intent(v.getContext(), HomeTutor.class);
               // startActivity(myintent);

            }
        });


    }


}