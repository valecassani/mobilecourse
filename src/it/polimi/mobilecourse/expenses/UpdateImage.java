package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * Created by Matteo on 26/07/2015.
 */
public class UpdateImage extends HelpABActivity {

    private UpdateImageFragment uif;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_info);

        uif= new UpdateImageFragment();
        //arriva un bundle dall'activity chiamante con un campo tipo (0/1 -> 0 studente 1 tutor)e un campo id
        Bundle bundle = getIntent().getExtras();
        //System.out.println("Bundle:" + bundle);
        String id=bundle.getString("id"); //id dell'utente
        String tipo=bundle.getString("tipo");

        getSupportActionBar().setElevation(25);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle forFrag=new Bundle();
        forFrag.putString("id",id);
        forFrag.putString("tipo",tipo);

        uif.setArguments(forFrag);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragreplace, uif);
        fragmentTransaction.commit();




    }





    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){



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