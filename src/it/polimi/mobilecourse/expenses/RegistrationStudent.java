package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Matteo on 23/12/2014.
 */
public class RegistrationStudent extends HelpActivity {


    private RegStudentFBFragment rsfbf;
    private RegStudentFragment rsf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_student);

        rsfbf= new RegStudentFBFragment();
        rsf=new RegStudentFragment();
        Bundle bundle = getIntent().getExtras();
        System.out.println("Bundle:" + bundle);
        if(bundle.getString("Tipo").compareTo("FB")==0) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragreplace, rsfbf);
            fragmentTransaction.commit();
        }
        else{

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragreplace, rsf);
            fragmentTransaction.commit();
        }


    }





    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){


        if(op=="spinnerUni"){

            RegStudentFragment reg=(RegStudentFragment) fragment;
            reg.arrayU(result);
        }

        if(op=="spinnerCity"){

            RegStudentFragment reg=(RegStudentFragment) fragment;
            reg.arrayC(result);
        }

        if(op=="spinnerCityFB"){

            RegStudentFBFragment reg=(RegStudentFBFragment) fragment;
            reg.arrayC(result);
        }

        if(op=="spinnerUniFB"){

            RegStudentFBFragment reg=(RegStudentFBFragment) fragment;
            reg.arrayU(result);
        }


        if(op=="controlS"){
            RegStudentFragment reg=(RegStudentFragment) fragment;
            reg.duplicateMail(result);
        }

    }


}