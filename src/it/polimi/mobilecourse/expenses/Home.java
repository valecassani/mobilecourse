package it.polimi.mobilecourse.expenses;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.*;
import android.widget.*;

import android.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        if (savedInstanceState == null) {
         MidFragmentHome mf=new MidFragmentHome();
          getFragmentManager().beginTransaction().add(R.id.layoutreplace,new TopFragmentHome()).commit();
            getFragmentManager().beginTransaction().add(R.id.secondreplace,mf).commit();












        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }






}
