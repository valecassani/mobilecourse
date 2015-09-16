package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Matteo on 03/07/2015.
 */
public class UpdateInfo extends HelpABActivity {

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

        //toolbar da aggiungere
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        if (toolbar != null) {
            //SpannableString st=new SpannableString("Home");
            //st.setSpan(new TypefaceSpan(this, "Gotham-Light.ttf"),0,st.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            TextView title = (TextView)findViewById(R.id.title);
            title.setText("PROFILO");
            title.setTextSize(18);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(25);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.primaryColorDark));



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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();

                return true;


        }
        return super.onOptionsItemSelected(item);
    }





}



