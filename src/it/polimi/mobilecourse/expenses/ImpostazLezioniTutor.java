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
 * Created by Matteo on 03/08/2015.
 */
public class ImpostazLezioniTutor extends HelpABActivity {

    private ImpostazLezioniTutorFragment iltf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impostaz_lezioni);

        //toolbar da aggiungere
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        if (toolbar != null) {
            //SpannableString st=new SpannableString("Home");
            //st.setSpan(new TypefaceSpan(this, "Gotham-Light.ttf"),0,st.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            TextView title = (TextView)findViewById(R.id.title);
            title.setText("IMPOSTAZIONI LEZIONI");
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

        iltf= new ImpostazLezioniTutorFragment();
        //arriva un bundle dall'activity chiamante con  id tutor
        Bundle bundle = getIntent().getExtras();
        String id=bundle.getString("id"); //id dell'utente

        Bundle forFrag=new Bundle();
        forFrag.putString("id",id);

        iltf.setArguments(forFrag);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragreplace, iltf);
        fragmentTransaction.commit();




    }





    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){


        if(op=="getData"){

            ImpostazLezioniTutorFragment reg=(ImpostazLezioniTutorFragment) fragment;
            reg.setField(result);

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



}
