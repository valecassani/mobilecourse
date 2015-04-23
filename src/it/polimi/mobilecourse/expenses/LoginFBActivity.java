package it.polimi.mobilecourse.expenses;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;


/**
 * Created by Matteo on 02/01/2015.
 */
public class LoginFBActivity extends Activity {

    boolean logged;
    Context ctx;
    TextView nome;
    private LoginButton lb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.loginfb);
        //nome=(TextView)findViewById(R.id.nome);
        ctx=this;
        logged=false;

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //Session.getActiveSession().onActivityResult(this,requestCode,resultCode,data);
        //facebookLogin();

    }




}