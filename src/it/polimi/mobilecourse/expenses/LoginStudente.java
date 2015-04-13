package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import java.util.ArrayList;

/**
 * Created by Matteo on 23/12/2014.
 */
public class LoginStudente extends FragmentActivity {

    boolean logged;
    Button skipButton;


    private FBSFragment fbsFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_studente);









    }


    /*
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Session.getActiveSession().onActivityResult(this,requestCode,resultCode,data);

    }

    @Override
    public void handleResult(ArrayList<ObjDb> result,String op,Fragment fragment){


        if(op=="logStudente"){

            LoginSFragment lsfrag=(LoginSFragment) fragment;
            //lsfrag.manageLogin(result);
        }



    }

    */


}