package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
 * Created by Matteo on 20/06/2015.
 */
public class RegTutorFBFragment extends Fragment {

    private View view;
    private ProgressBar progressView;
    private Spinner citySpinner;
    private Button submit;
    private RegistrationTutor activity;
    private manageSpinner ms=null;
    private String itemC;
    private String idCity;
    private String idUni;
    private String itemUni;
    private CheckBox accept;

    private ArrayAdapter<String> adapterCity;


    private String cellT;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.regfbtutor_fragment, container, false);




        settingsReg();
        setSpinner();


        //registrazione
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                submit.setVisibility(View.GONE);
                progress(true);
                getData();
                controlField();


            }
        });



        return view;
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (RegistrationTutor) activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }


    private void settingsReg(){

        Bundle bundle = activity.getIntent().getExtras();
        String nome = bundle.getString("Nome");
        TextView complete=(TextView) view.findViewById(R.id.complete);
        complete.setText(nome + ",completa il profilo.");

        progressView=(ProgressBar)view.findViewById(R.id.progressBarRS);
        accept=(CheckBox)view.findViewById(R.id.checkBox);

        adapterCity = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citySpinner= (Spinner) view.findViewById(R.id.spinnerCitta);
        submit=(Button) view.findViewById(R.id.regT);

    }

    private void setSpinner(){
        ms= new manageSpinner();
        ms.execute((Void) null);
        manageCSpinner();

    }


    private void manageCSpinner(){




        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemC=parent.getItemAtPosition(position).toString();
                idCity=String.valueOf((parent.getSelectedItemPosition())+1);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {




            }
        });




    }

    private void getData(){

        EditText cell=(EditText) view.findViewById(R.id.cellS);





        cellT=cell.getText().toString();








    }



    private boolean isCellValid(String cell){
        return Patterns.PHONE.matcher(cell).matches();
    }

    private void completeReg(){


        Bundle bundle = activity.getIntent().getExtras();

        String nameS = bundle.getString("Nome");
        String surnameS = bundle.getString("Cognome");
        String mailS = bundle.getString("Mail");



        String url="registration_tutor_fb.php?username=".concat(mailS).concat("&").concat("nome=")
                .concat(nameS).concat("&").concat("cognome=").concat(surnameS).concat("&").concat("cellulare=").concat(cellT)
                .concat("&").concat("id_citta=").concat(idCity);
        //new RequestFtp().setParameters(activity, url, "regTutor", RegStudentFBFragment.this).execute();
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                "http://www.unishare.it/tutored/"+url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            Log.d("RegFBStudent", "Registrazione avvenuta con successo");
                            Toast.makeText(getActivity().getApplicationContext(), "Registrazione completata", Toast.LENGTH_SHORT);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RegFBTutor", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });
        queue.add(jsonObjReq);

        Toast.makeText(getActivity().getApplicationContext(),"Registrazione completata", Toast.LENGTH_LONG).show();
        Intent myintent = new Intent(view.getContext(),LandingActivity.class);
        startActivity(myintent);



    }




    private void controlField(){


        if(isCellValid(cellT)==true && accept.isChecked()){
            completeReg();
        }
        else{

            Toast.makeText(getActivity().getApplicationContext(),"Cellulare non valido", Toast.LENGTH_SHORT).show();
            progress(false);
            submit.setVisibility(View.VISIBLE);

        }

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




    public void arrayC(ArrayList<ObjDb> result){
        int i=0;

        while(i<result.size()) {
            ObjDb res = result.get(i);
            String str=res.get("nome");
            adapterCity.add(str);
            i++;
        }

    }




    public class manageSpinner extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void ...params){




            new RequestFtp().setParameters(activity, "cities.php", "spinnerCityFB", RegTutorFBFragment.this).execute();





            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){

            if(success) {




                citySpinner.setAdapter(adapterCity);
                citySpinner.setPrompt("Seleziona tra le seguenti città la tua:");



            }
        }




    }
}
