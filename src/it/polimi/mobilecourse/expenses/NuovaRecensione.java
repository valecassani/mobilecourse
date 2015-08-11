package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Matteo on 04/08/2015.
 */
public class NuovaRecensione extends HelpActivity {

    private NuovaRecensioneFragment nrf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuova_recensione);

        nrf= new NuovaRecensioneFragment();
        //arriva un bundle dall'activity chiamante con  idstudente,idtutor,nome tutor
        //Bundle bundle = getIntent().getExtras();


        Bundle forFrag=new Bundle();
        forFrag.putString("idstudente","47");
        forFrag.putString("idtutor","3");
        forFrag.putString("nome","Matteo");

        nrf.setArguments(forFrag);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragreplace, nrf);
        fragmentTransaction.commit();




    }





    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){




    }


}
