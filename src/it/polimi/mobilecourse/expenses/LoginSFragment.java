package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;



/**
 * Created by Matteo on 23/12/2014.
 */
public class LoginSFragment extends Fragment {

    private TextView mail;
    private TextView password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View frags = inflater.inflate(R.layout.logins_frag, container, true);

        mail=(TextView) frags.findViewById(R.id.userS);
        password=(TextView) frags.findViewById(R.id.pasS);

        Button access=(Button) frags.findViewById(R.id.butAccess);
        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });




        Button reg=(Button) frags.findViewById(R.id.regS);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registration(v);
            }
        });

        return frags;
    }


    private void attemptLogin(){

    }


    private void registration(View v){

        Intent myi=new Intent(v.getContext(),RegistrationStudent.class);
        startActivity(myi);

    }

    private void fbaccess(View v){
        Intent myi=new Intent(v.getContext(),LoginFBActivity.class);
        startActivity(myi);
    }

}