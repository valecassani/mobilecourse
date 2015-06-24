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


            if(op=="spinnerUni"){

                RegTutorFragment reg=(RegTutorFragment) fragment;
                reg.arrayU(result);
            }

            if(op=="spinnerCity"){

                RegTutorFragment reg=(RegTutorFragment) fragment;
                reg.arrayC(result);
            }

            if(op=="spinnerCityFB"){

                RegTutorFragment reg=(RegTutorFragment) fragment;
                reg.arrayC(result);
            }

            if(op=="spinnerUniFB"){

                RegTutorFragment reg=(RegTutorFragment) fragment;
                reg.arrayU(result);
            }


            if(op=="controlS"){
                RegStudentFragment reg=(RegStudentFragment) fragment;
                reg.duplicateMail(result);
            }

        }
}
