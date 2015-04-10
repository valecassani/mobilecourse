package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DataActivityStudent extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_activity_student);


    }


    public void onStart(){
        super.onStart();
        Bundle dati=getIntent().getExtras();

        String mail=dati.getString("Mail");

        String name=dati.getString("Username");
        TextView saluto=(TextView)findViewById(R.id.ciaonome);
        saluto.setText("Ciao "+name+"!");




    }


}

