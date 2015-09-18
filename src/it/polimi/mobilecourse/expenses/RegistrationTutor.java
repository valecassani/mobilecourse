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
 * Created by Matteo on 20/06/2015.
 */
public class RegistrationTutor extends HelpABActivity {

    private RegTutorFBFragment rtfbf;
    private RegTutorFragment rtf;

    TextView title;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_tutor);


        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        if (toolbar != null) {
            //SpannableString st=new SpannableString("Home");
            //st.setSpan(new TypefaceSpan(this, "Gotham-Light.ttf"),0,st.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            title = (TextView)findViewById(R.id.title);
            title.setText("REGISTRAZIONE TUTOR");
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
