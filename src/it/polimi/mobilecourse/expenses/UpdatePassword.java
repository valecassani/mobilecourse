package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Matteo on 29/07/2015.
 */
public class UpdatePassword extends HelpABActivity {

    private UpdatePasswordFragment upf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(25);
        getSupportActionBar().setHomeButtonEnabled(true);

        upf= new UpdatePasswordFragment();
        //arriva un bundle dall'activity chiamante con un campo tipo (0/1 -> 0 studente 1 tutor)e un campo id
        Bundle bundle = getIntent().getExtras();
        //System.out.println("Bundle:" + bundle);
        String id=bundle.getString("id");//id dell'utente
        String tipo=bundle.getString("tipo");

        Bundle forFrag=new Bundle();
        forFrag.putString("id", id);
        forFrag.putString("tipo",tipo);

        upf.setArguments(forFrag);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragreplace, upf);
        fragmentTransaction.commit();


    }





    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment) {

        if(op=="updatePsw"){

            UpdatePasswordFragment reg=(UpdatePasswordFragment) fragment;
            reg.resUpdate(result);
        }


    }

}