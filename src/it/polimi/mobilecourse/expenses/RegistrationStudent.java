package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

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

        if(op=="numeroel") {
            RegStudentFragment regs=(RegStudentFragment) fragment;
            regs.numeroel(result);
        }
        if(op=="spinnerUni"){

            RegStudentFragment reg=(RegStudentFragment) fragment;
            reg.arrayU(result);
        }



    }


}