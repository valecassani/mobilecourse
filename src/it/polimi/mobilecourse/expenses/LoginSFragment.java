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

    private LoginStudente activity;
    private ProgressBar progressView;

    private TextView mail;
    private TextView password;
    Button access;
    TextView noreg;
    TextView oppure;
    Button reg;
    Fragment fbs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View frags = inflater.inflate(R.layout.logins_frag, container, true);

        interfaceSettings(frags);
        //login
        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidEmail(mail.getText().toString())==true && isValidPassword(password.getText().toString())==true ) {


                    hideElements();
                    progress(true);

                    attemptLogin();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Campi non validi", Toast.LENGTH_LONG).show();

                }
            }
        });



        //registrazione
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registration(v);
            }
        });

        return frags;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (LoginStudente) activity;
    }



    private void interfaceSettings(View view){
        progressView=(ProgressBar)view.findViewById(R.id.progressBarLS);

        mail=(EditText) view.findViewById(R.id.userS);
        password=(EditText) view.findViewById(R.id.pasS);
        noreg=(TextView) view.findViewById(R.id.noreg);
        oppure=(TextView) view.findViewById(R.id.oppure);

        reg=(Button) view.findViewById(R.id.regS);
        access=(Button) view.findViewById(R.id.butAccess);

    }

    private void attemptLogin(){


            System.out.println(mail.getText().toString());
            System.out.println(password.getText().toString());
            String url = "login_student.php?mail=".concat(mail.getText().toString()).concat("&password=").concat(password.getText().toString());
            new RequestFtp().setParameters(activity, url, "logStudente", LoginSFragment.this).execute();


    }



    private void hideElements(){
        FragmentManager fm = getFragmentManager();
    fbs=fm.findFragmentById(R.id.loginFB);
    fm.beginTransaction().hide(fbs).commit();

    access.setVisibility(View.GONE);
    noreg.setVisibility(View.GONE);
    reg.setVisibility(View.GONE);
    oppure.setVisibility(View.GONE);

}

    private void showElements(){
        FragmentManager fm = getFragmentManager();
        fbs=fm.findFragmentById(R.id.loginFB);
        fm.beginTransaction().show(fbs).commit();

        access.setVisibility(View.VISIBLE);
        noreg.setVisibility(View.VISIBLE);
        reg.setVisibility(View.VISIBLE);
        oppure.setVisibility(View.VISIBLE);

    }

    private boolean isValidEmail(String target){

        return Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }

    private boolean isValidPassword(String psw){
        return psw.length()>0;
    }


    public void manageLogin(ArrayList<ObjDb> result){


        if(result==null){
            progress(false);
            showElements();
        }
        else {
            ObjDb res = result.get(0);
            String str = res.get("Risposta");
            if (str.compareTo("OK")==0) {
                Toast.makeText(getActivity().getApplicationContext(), "Login corretto", Toast.LENGTH_LONG).show();

            } else {
                progress(false);
                Toast.makeText(getActivity().getApplicationContext(), "Username e/o password errate", Toast.LENGTH_LONG).show();
                showElements();


            }
        }

    }

    private void registration(View v){

        Intent myi=new Intent(v.getContext(),RegistrationStudent.class);
        startActivity(myi);

    }

    private void progress(final boolean show){
        final int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });



    }



}