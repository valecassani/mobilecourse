package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Matteo on 23/12/2014.
 */
public class RegistrationStudent extends HelpABActivity {


    private RegStudentFBFragment rsfbf;
    private RegStudentFragment rsf;

    TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_student);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        if (toolbar != null) {
            //SpannableString st=new SpannableString("Home");
            //st.setSpan(new TypefaceSpan(this, "Gotham-Light.ttf"),0,st.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            title = (TextView)findViewById(R.id.title);
            title.setText("REGISTRAZIONE STUDENTE");
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

        if(op=="getFacolta"){

            RegStudentFragment reg=(RegStudentFragment) fragment;
            reg.arrayF(result);
        }



        if(op=="spinnerCity"){

            RegStudentFragment reg=(RegStudentFragment) fragment;
            reg.arrayC(result);
        }

        if(op=="getFacoltaFB"){

            RegStudentFBFragment reg=(RegStudentFBFragment) fragment;
            reg.arrayF(result);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }

    }

    public TextView getTitleToolbar(){
        return title;
    }


}