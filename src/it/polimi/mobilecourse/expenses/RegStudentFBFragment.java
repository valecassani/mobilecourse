package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Matteo on 23/12/2014.
 */
public class RegStudentFBFragment extends Fragment {

    private View view;
    private ProgressBar progressView;
    private Spinner uniSpinner;
    private Spinner citySpinner;
    private Spinner facSpinner;
    private Button submit;
    private RegistrationStudent activity;
    private manageCSpinner mcs=null;
    private manageUSpinner mus=null;

    private manageFSpinner mfs=null;
    private int identifierUni;
    private CheckBox accept;

    private String itemC;
    private String itemF;
    private String idCity;
    private String idUni;
    private String idFac;
    private String itemUni;
    private ArrayAdapter<String> adapterUni;
    private ArrayAdapter<String> adapterCity;
    private ArrayAdapter<String> adapterFac;

    private ArrayList<String> listIdUni;
    private ArrayList<String> listIdFac;

    private String cellS;
    private String facebookId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.regfbstudent_fragment, container, false);


        activity.getTitleToolbar().setText("REGISTRAZIONE STUDENTE");
        activity.getTitleToolbar().setTextSize(18);

        getFacebookId();

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

    private void getFacebookId() {

        facebookId = Profile.getCurrentProfile().getId();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (RegistrationStudent) activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }


    private void settingsReg(){

        Bundle bundle = activity.getIntent().getExtras();
        String nome = bundle.getString("Nome");

        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString wordtoSpan = new SpannableString(nome);

        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primaryColor)), 0, nome.length(),
                0);
        builder.append(wordtoSpan);
        builder.append(",completa il profilo ");


        TextView complete=(TextView) view.findViewById(R.id.complete);


        complete.setText(builder, TextView.BufferType.SPANNABLE);

        progressView=(ProgressBar)view.findViewById(R.id.progressBarRS);
        accept=(CheckBox)view.findViewById(R.id.checkBox);
        adapterFac = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapterFac.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapterUni = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapterUni.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapterCity = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        uniSpinner=(Spinner) view.findViewById(R.id.spinnerUni);
        citySpinner= (Spinner) view.findViewById(R.id.spinnerCitta);
        facSpinner=(Spinner)view.findViewById(R.id.spinnerFac);
        facSpinner.setAdapter(adapterFac);
        submit=(Button) view.findViewById(R.id.regS);

    }

    private void setSpinner(){
        mcs= new manageCSpinner();
        mcs.execute((Void) null);

        manageCSpinner();

    }

    private void manageUSpinner(){




        uniSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemUni = parent.getItemAtPosition(position).toString();
                identifierUni = (parent.getSelectedItemPosition()) + 1;
                idUni = listIdUni.get(identifierUni - 1);
                adapterFac.clear();
                mfs = new manageFSpinner();
                mfs.execute(idUni);
                manageFSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });




    }

    private void manageCSpinner(){




        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemC=parent.getItemAtPosition(position).toString();
                idCity=String.valueOf((parent.getSelectedItemPosition()) + 1);

                adapterUni.clear();
                mus= new manageUSpinner();
                mus.execute(idCity);

                manageUSpinner();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {




            }
        });




    }

    private void manageFSpinner(){


        facSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemF=parent.getItemAtPosition(position).toString();

                int pos=(parent.getSelectedItemPosition())+1;

                idFac=listIdFac.get(pos-1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {




            }
        });







    }

    private void getData(){

        EditText cell=(EditText) view.findViewById(R.id.cellS);





        cellS=cell.getText().toString();








    }



    private boolean isCellValid(String cell){
        return Patterns.PHONE.matcher(cell).matches();
    }

    private void completeReg(){


        Bundle bundle = activity.getIntent().getExtras();

        String nameS = bundle.getString("Nome");
        String surnameS = bundle.getString("Cognome");
        String mailS = bundle.getString("Mail");



        String url="registration_student_fb.php?username=".concat(mailS).concat("&").concat("nome=")
                .concat(nameS).concat("&").concat("cognome=").concat(surnameS).concat("&").concat("cellulare=").concat(cellS)
                .concat("&").concat("id_uni=").concat(idUni).concat("&").concat("id_citta=").concat(idCity).concat("&").concat("id_facolta=").concat(idFac).concat("&id_fb=".concat(facebookId));
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                "http://www.unishare.it/tutored/"+url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            Log.d("RegFBStudent", "Registrazione avvenuta con successo");
                            Toast.makeText(getActivity().getApplicationContext(),"Registrazione completata", Toast.LENGTH_LONG).show();

                            Intent myintent = new Intent(getActivity(),HomeStudent.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("user_id", obj.getString("newid"));

                            myintent.putExtras(bundle);

                            startActivity(myintent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        return false;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RegFBStudent", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });
        queue.add(jsonObjReq);
       // Activity activity = new GCMMainActivity();
       // Toast.makeText(getActivity().getApplicationContext(),"Registrazione completata", Toast.LENGTH_LONG).show();
       // Intent myintent = new Intent(view.getContext(),LandingActivity.class);
       // startActivity(myintent);



    }





    private void controlField(){


        if(isCellValid(cellS)==true && accept.isChecked() ){





                completeReg();





        }
        else{

            Toast.makeText(getActivity().getApplicationContext(),"Campi non validi", Toast.LENGTH_SHORT).show();
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



    public void arrayU(ArrayList<ObjDb> result){

        int i=0;

        listIdUni=new ArrayList<String>();
        while(i<result.size()) {
            ObjDb res = result.get(i);
            String str=res.get("nome");
            adapterUni.add(str);
            String strID=res.get("ID");
            listIdUni.add(strID);
            i++;
        }
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

    public void arrayF(ArrayList<ObjDb> result){

        int i=0;

        listIdFac=new ArrayList<String>();
        while(i<result.size()) {
            ObjDb res = result.get(i);
            String str=res.get("nome");
            adapterFac.add(str);
            String idF=res.get("ID");
            listIdFac.add(idF);
            i++;
        }

    }




    public class manageCSpinner extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void ...params){




            new RequestFtp().setParameters(activity, "cities.php", "spinnerCityFB", RegStudentFBFragment.this).execute();





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

    public class manageFSpinner extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String ...params){

            String id=params[0];

            String url="getFacolta.php?iduni=".concat(id);
            new RequestFtp().setParameters(activity, url, "getFacoltaFB", RegStudentFBFragment.this).execute();

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){

            if(success) {



                facSpinner.setAdapter(adapterFac);
                facSpinner.setPrompt("Seleziona tra le seguenti facoltà la tua:");




            }
        }
    }

    public class manageUSpinner extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String ...params){

            String id=params[0];


            String url="getUniFromCity.php?idcity=".concat(id);

            new RequestFtp().setParameters(activity, url, "spinnerUniFB", RegStudentFBFragment.this).execute();






            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){

            if(success) {



                uniSpinner.setAdapter(adapterUni);
                uniSpinner.setPrompt("Seleziona tra le seguenti università la tua:");




            }
        }




    }

}
