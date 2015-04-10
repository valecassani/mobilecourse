package it.polimi.mobilecourse.expenses;

import android.app.Activity;
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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_student);



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


        if(op=="controlS"){
            RegStudentFragment reg=(RegStudentFragment) fragment;
            reg.duplicateMail(result);
        }

    }


}