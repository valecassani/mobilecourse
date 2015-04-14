package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Matteo on 23/12/2014.
 */
public class LoginSFragment extends Fragment {

    private final String TAG = "Login Student";

    private LoginStudente activity;
    private ProgressBar progressView;

    private TextView mail;
    private TextView password;
    private String loginResponse;
    Button access;
    TextView noreg;
    TextView oppure;
    Button reg;
    Fragment fbs;
    Intent intent;
    RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View frags = inflater.inflate(R.layout.logins_frag, container, true);

        interfaceSettings(frags);
        queue = new Volley().newRequestQueue(frags.getContext());
        //login
        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidEmail(mail.getText().toString())==true && isValidPassword(password.getText().toString())==true ) {


                    //hideElements();
                    //progress(true);

                    final ProgressDialog pDialog = new ProgressDialog(frags.getContext());
                    pDialog.setMessage("Loading...");

                    attemptLogin();
                    pDialog.hide();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Campi non validi", Toast.LENGTH_LONG).show();

                }

            }
        });

        intent = new Intent(getActivity(), HomeStudent.class);



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
        // new RequestFtp().setParameters(activity, url, "logStudente", LoginSFragment.this).execute();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                "http://www.unishare.it/tutored/"+url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            Log.d(TAG, response.toString());
                            manageLogin(obj);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });
        queue.add(jsonObjReq);


    }



    private void hideElements(){
        FragmentManager fm = getFragmentManager();
        //fbs=fm.findFragmentById(R.id.loginFB);
        fm.beginTransaction().hide(fbs).commit();

        access.setVisibility(View.GONE);
        noreg.setVisibility(View.GONE);
        reg.setVisibility(View.GONE);
        oppure.setVisibility(View.GONE);

    }

    private void showElements(){
        FragmentManager fm = getFragmentManager();
        //fbs=fm.findFragmentById(R.id.loginFB);
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


    public void manageLogin(JSONObject result) throws JSONException {

        if(result==null){
            progress(false);
            showElements();
        }
        else {



            String str = result.getString("Risposta");
            String userId = null;
            if (str.compareTo("OK")==0) {
                Toast.makeText(getActivity().getApplicationContext(), "Login corretto", Toast.LENGTH_LONG).show();
                userId = result.getString("user_id");
                Bundle bundle = new Bundle();
                bundle.putString("user_id", userId);

                intent.putExtras(bundle);
                startActivity(intent);

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment f = getParentFragment();
        if (f != null && getTargetFragment() != null) {
            android.support.v4.app.FragmentManager fm;
            getFragmentManager().beginTransaction().remove(f);
            f = null;
        }

    }



}