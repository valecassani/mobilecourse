package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Matteo on 20/06/2015.
 */
public class RegistrationTutor extends HelpActivity {

    private RegTutorFBFragment rtfbf;
    private RegTutorFragment rtf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_tutor);

        rtfbf= new RegTutorFBFragment();
        rtf=new RegTutorFragment();
        Bundle bundle = getIntent().getExtras();
        System.out.println("Bundle:" + bundle);
        if(bundle.getString("Tipo").compareTo("FB")==0) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragreplace, rtfbf);
            fragmentTransaction.commit();
        }
        else{

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragreplace, rtf);
        fragmentTransaction.commit();
        }


    }





    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){



        if(op=="spinnerCity"){

            RegTutorFragment reg=(RegTutorFragment) fragment;
            reg.arrayC(result);
        }

        if(op=="spinnerCityFB"){

            RegTutorFBFragment reg=(RegTutorFBFragment) fragment;
            reg.arrayC(result);
        }




        if(op=="controlT"){
            RegTutorFragment reg=(RegTutorFragment) fragment;
            reg.duplicateMail(result);
        }

    }
}