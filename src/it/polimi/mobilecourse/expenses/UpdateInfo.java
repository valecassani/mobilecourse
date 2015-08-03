package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Matteo on 03/07/2015.
 */
public class UpdateInfo extends HelpActivity {

    private UpdateInfoStudentFragment uisf;
    private UpdateInfoTutorFragment uitf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_info);

        uisf= new UpdateInfoStudentFragment();
        uitf=new UpdateInfoTutorFragment();
        //arriva un bundle dall'activity chiamante con un campo tipo (0/1 -> 0 studente 1 tutor)e un campo id
        Bundle bundle = getIntent().getExtras();
        String id=bundle.getString("id"); //id dell'utente
        String tipo=bundle.getString("tipo");
        Bundle forFrag=new Bundle();
        forFrag.putString("id",id);


        if(tipo.compareTo("0")==0) {

            //studente
            uisf.setArguments(forFrag);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragreplace, uisf);
            fragmentTransaction.commit();
        }
        else{

            //tutor
            uitf.setArguments(forFrag);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragreplace, uitf);
            fragmentTransaction.commit();
        }


    }





    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){


        if(op=="spinnerUni"){

            UpdateInfoStudentFragment reg=(UpdateInfoStudentFragment) fragment;
            reg.arrayU(result);
        }

        if(op=="spinnerUniT"){

            UpdateInfoTutorFragment reg=(UpdateInfoTutorFragment) fragment;
            reg.arrayU(result);
        }

        if(op=="getFacolta"){

            UpdateInfoStudentFragment reg=(UpdateInfoStudentFragment) fragment;
            reg.arrayF(result);
        }

        if(op=="getFacoltaT"){

            UpdateInfoTutorFragment reg=(UpdateInfoTutorFragment) fragment;
            reg.arrayF(result);
        }


        if(op=="spinnerCity"){

            UpdateInfoStudentFragment reg=(UpdateInfoStudentFragment) fragment;
            reg.arrayC(result);
        }

        if(op=="spinnerCityT"){

            UpdateInfoTutorFragment reg=(UpdateInfoTutorFragment) fragment;
            reg.arrayC(result);
        }

        if(op=="setDataStudent"){

            UpdateInfoStudentFragment reg=(UpdateInfoStudentFragment) fragment;
            reg.arrayData(result);
        }


        if(op=="setDataTutor"){

            UpdateInfoTutorFragment reg=(UpdateInfoTutorFragment) fragment;
            reg.arrayData(result);
        }

    }




}



