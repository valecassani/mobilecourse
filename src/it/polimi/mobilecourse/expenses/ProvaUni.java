package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Matteo on 21/12/2014.
 */
public class ProvaUni extends HelpActivity {

    //RequestFtp rf=new RequestFtp(this.getClass(),"univer.php",this.getApplicationContext());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lay_frag);

        if (savedInstanceState == null) {



        }



    }

    @Override
    public void handleResult(ArrayList<Entity> result,Fragment fragment){

        UniFragment unifr=(UniFragment) fragment;
        unifr.displayResults(result);



    }
}