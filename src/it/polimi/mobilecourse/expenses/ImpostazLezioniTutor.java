package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Matteo on 03/08/2015.
 */
public class ImpostazLezioniTutor extends HelpActivity {

    private ImpostazLezioniTutorFragment iltf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impostaz_lezioni);

        iltf= new ImpostazLezioniTutorFragment();
        //arriva un bundle dall'activity chiamante con  id tutor
        Bundle bundle = getIntent().getExtras();
        String id=bundle.getString("id"); //id dell'utente

        Bundle forFrag=new Bundle();
        forFrag.putString("id",id);

        iltf.setArguments(forFrag);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragreplace, iltf);
        fragmentTransaction.commit();




    }





    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){


        if(op=="getData"){

            ImpostazLezioniTutorFragment reg=(ImpostazLezioniTutorFragment) fragment;
            reg.setField(result);

        }


    }

}
