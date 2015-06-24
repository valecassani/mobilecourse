package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo on 23/12/2014.
 */
public class RegStudentFBFragment extends Fragment {

    private View view;
    private ProgressBar progressView;
    private Spinner uniSpinner;
    private Spinner citySpinner;
    private Button submit;
    private RegistrationStudent activity;
    private manageSpinner ms=null;
    private int identifierUni;
    private String itemC;
    private String idCity;
    private String idUni;
    private String itemUni;
    private ArrayAdapter<String> adapterUni;
    private ArrayAdapter<String> adapterCity;

    private String nameS;
    private String surnameS;
    private String cellS;
    private String mailS;
    private String passS;
    private String passDue;

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // please enter your sender id
    String SENDER_ID = "420313149585";

    static final String TAG = "GCMDemo";
    GoogleCloudMessaging gcm;

    TextView mDisplay;
    EditText ed;
    Context context;
    String regid;
    Integer tipo;
    Integer id_utente;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.regfbstudent_fragment, container, false);




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


        this.activity = (RegistrationStudent) activity;
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

        adapterUni = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapterUni.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapterCity = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        uniSpinner=(Spinner) view.findViewById(R.id.spinnerUni);
        citySpinner= (Spinner) view.findViewById(R.id.spinnerCitta);
        submit=(Button) view.findViewById(R.id.regS);

    }

    private void setSpinner(){
        ms= new manageSpinner();
        ms.execute((Void) null);
        manageUSpinner();
        manageCSpinner();

    }

    private void manageUSpinner(){




        uniSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemUni = parent.getItemAtPosition(position).toString();
                identifierUni = (parent.getSelectedItemPosition()) + 1;
                idUni = String.valueOf(identifierUni);

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
                idCity=String.valueOf((parent.getSelectedItemPosition())+1);



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
                .concat("&").concat("id_uni=").concat(idUni).concat("&").concat("id_citta=").concat(idCity);
        //new RequestFtp().setParameters(activity, url, "regStudente", RegStudentFBFragment.this).execute();
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                "http://www.unishare.it/tutored/"+url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            System.out.println("response received");
                            Log.d("RegFBStudent", "Registrazione avvenuta con successo");
                            Toast.makeText(getActivity().getApplicationContext(),"Registrazione completata",Toast.LENGTH_SHORT);
                            id_utente = obj.getInt("newid");
                            startGCM(id_utente, 0);
                            Intent myintent = new Intent(view.getContext(),HomeStudent.class);
                            startActivity(myintent);
                            getActivity().finish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RegFBStudent", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });
        queue.add(jsonObjReq);




    }





    private void controlField(){


        if(isCellValid(cellS)==true ){





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



    public void arrayU(ArrayList<ObjDb> result){

        int i=0;

        while(i<result.size()) {
            ObjDb res = result.get(i);
            String str=res.get("nome");
            adapterUni.add(str);
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




    public class manageSpinner extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void ...params){



            new RequestFtp().setParameters(activity, "univer.php", "spinnerUniFB", RegStudentFBFragment.this).execute();

            new RequestFtp().setParameters(activity, "cities.php", "spinnerCityFB", RegStudentFBFragment.this).execute();





            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){

            if(success) {



                uniSpinner.setAdapter(adapterUni);
                uniSpinner.setPrompt("Seleziona tra le seguenti università la tua:");

                citySpinner.setAdapter(adapterCity);
                citySpinner.setPrompt("Seleziona tra le seguenti città la tua:");



            }
        }




    }

    public void startGCM(int id_utente, int tipo) {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this.getActivity());
            regid = getRegistrationId(context);

            if (!regid.isEmpty()) {
                //parametri: id_utente,tipo utente
                new RegisterBackground().execute(String.valueOf(id_utente), String.valueOf(tipo));
            }

        }
        //

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this.getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {

        return this.getActivity().getSharedPreferences(GCMMainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    class RegisterBackground extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;
                System.out.println("regid" + regid);
                Log.d("111", msg);
                int id = Integer.parseInt(arg0[0]);
                int tipo = Integer.parseInt(arg0[1]);
                sendRegistrationIdToBackend(id, tipo);

                // Persist the regID - no need to register again.
                storeRegistrationId(context, regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }


        private void sendRegistrationIdToBackend(int id_utente, int tipo) {
            // Your implementation here.


            String url = "http://www.unishare.it/tutored/getdevice.php";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("regid", regid));
            params.add(new BasicNameValuePair("id_utente", Integer.toString(id_utente)));
            params.add(new BasicNameValuePair("tipo", Integer.toString(tipo)));


            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

        private void storeRegistrationId(Context context, String regId) {
            final SharedPreferences prefs = getGCMPreferences(context);
            int appVersion = getAppVersion(context);
            Log.i(TAG, "Saving regId on app version " + appVersion);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PROPERTY_REG_ID, regId);
            editor.putInt(PROPERTY_APP_VERSION, appVersion);
            editor.commit();
        }


    }


}
