package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

/**
 * Created by Matteo on 29/07/2015.
 */
public class UpdatePasswordFragment extends Fragment {

    private View view;
    private UpdatePassword activity;

    ProgressBar progressView;
    EditText oldpsw;
    EditText newpsw;
    EditText pswr;
    Button submit;
    String id;
    String tipo;
    RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.update_password_fragment, container, false);



        queue= Volley.newRequestQueue(view.getContext());
        Bundle get = getArguments();
        id = get.getString("id");
        tipo = get.getString("tipo");

        field();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit.setVisibility(View.GONE);
                progress(true);

                controlField();



            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (UpdatePassword) activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }


    private void field(){


        progressView=(ProgressBar)view.findViewById(R.id.progressBar);

        oldpsw=(EditText) view.findViewById(R.id.oldpsw);
        newpsw=(EditText) view.findViewById(R.id.newpsw);
        pswr=(EditText) view.findViewById(R.id.psw2);

        submit=(Button)view.findViewById(R.id.submit);




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

    public void controlField(){

        if(isPasswordValid(newpsw.getText().toString())){


            if(newpsw.getText().toString().compareTo(pswr.getText().toString())==0){


                String url="update_password.php?type_user=".concat(tipo).concat("&id=").concat(id).concat("&oldpsw=").concat(oldpsw.getText().toString())
                        .concat("&newpsw=").concat(newpsw.getText().toString());

                new RequestFtp().setParameters(activity, url, "updatePsw", UpdatePasswordFragment.this).execute();



            }
            else {
                Toast.makeText(getActivity().getApplicationContext(), "I campi Nuova password e Ripeti Password non coincidono", Toast.LENGTH_LONG).show();
                progress(false);
                submit.setVisibility(View.VISIBLE);
            }


        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "La nuova password inserita non rispetta i vincoli", Toast.LENGTH_LONG).show();
            progress(false);
            submit.setVisibility(View.VISIBLE);
        }







    }


    private boolean isPasswordValid(String password){

        return password.length()>4;

    }


    public void resUpdate(ArrayList<ObjDb> result){

        ObjDb res = result.get(0);
        String str=res.get("res");
        System.out.println(str);
        if(str.compareTo("OK")==0){

            Toast.makeText(getActivity().getApplicationContext(),"Modifica completata", Toast.LENGTH_LONG).show();
            progress(false);
            submit.setVisibility(View.VISIBLE);
            System.out.println("Ok");
            getActivity().finish();




        }
        else{

            Toast.makeText(getActivity().getApplicationContext(),"La vecchia password inserita non e corretta", Toast.LENGTH_LONG).show();
            progress(false);
            submit.setVisibility(View.VISIBLE);

            System.out.println("no");
        }









    }

}
