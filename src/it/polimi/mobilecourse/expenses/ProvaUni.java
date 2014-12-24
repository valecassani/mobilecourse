package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Matteo on 21/12/2014.
 */
public class ProvaUni extends HelpActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lay_frag);

        if (savedInstanceState == null) {



        }



    }

    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){

        UniFragment unifr=(UniFragment) fragment;
        unifr.displayResults(result);



    }
}