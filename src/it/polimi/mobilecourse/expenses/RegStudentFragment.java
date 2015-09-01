package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.w3c.dom.Text;

import javax.xml.datatype.Duration;
import java.lang.reflect.Array;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo on 23/12/2014.
 */
public class RegStudentFragment extends Fragment {

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


    private String nameS;
    private String surnameS;
    private String cellS;
    private String mailS;
    private String passS;
    private String passDue;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


         view = inflater.inflate(R.layout.regs_frag, container, false);



         settingsReg();
         setSpinner();


        //registrazione
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progress(true);
                getData();


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

        progressView=(ProgressBar)view.findViewById(R.id.progressBarRS);
        accept=(CheckBox)view.findViewById(R.id.checkBox);

        adapterUni = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapterUni.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapterCity = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        adapterFac = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapterFac.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        uniSpinner=(Spinner) view.findViewById(R.id.spinnerUni);
        uniSpinner.setAdapter(adapterUni);
        citySpinner= (Spinner) view.findViewById(R.id.spinnerCitta);
        citySpinner.setAdapter(adapterCity);
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
                idUni=listIdUni.get(identifierUni-1);
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
        EditText name=(EditText) view.findViewById(R.id.nameS);
        EditText surname=(EditText) view.findViewById(R.id.surnameS);
        EditText cell=(EditText) view.findViewById(R.id.cellS);
        EditText mail=(EditText) view.findViewById(R.id.mailS);
        EditText pass=(EditText) view.findViewById(R.id.passS);
        EditText passdue=(EditText) view.findViewById(R.id.pass2S);



        nameS=name.getText().toString();
        surnameS=surname.getText().toString();
        cellS=cell.getText().toString();
        mailS=mail.getText().toString();
        passS=pass.getText().toString();
        passDue=passdue.getText().toString();
        controlUser(mailS);






    }

    private boolean isValidEmail(String target){

        return Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }

    private boolean isPasswordValid(String password){

        return password.length()>4;

    }

    private boolean isCellValid(String cell){
        return Patterns.PHONE.matcher(cell).matches();
    }

    private void completeReg(){

        String url="registration_student.php?username=".concat(mailS).concat("&").concat("password=").concat(passS).concat("&").concat("nome=")
                .concat(nameS).concat("&").concat("cognome=").concat(surnameS).concat("&").concat("cellulare=").concat(cellS)
                .concat("&").concat("id_uni=").concat(idUni).concat("&").concat("id_citta=").concat(idCity).concat("&").concat("id_facolta=").concat(idFac);
        new RequestFtp().setParameters(activity, url, "regStudente", RegStudentFragment.this).execute();
        Toast.makeText(getActivity().getApplicationContext(),"Registrazione completata", Toast.LENGTH_LONG).show();
        Intent myintent = new Intent(view.getContext(), LandingActivity.class);
        startActivity(myintent);



    }

    private void controlUser(String mail){

        String url="control_mail_s.php?mail=".concat(mail);
        new RequestFtp().setParameters(activity, url, "controlS", RegStudentFragment.this).execute();

    }

    public void duplicateMail(ArrayList<ObjDb> result){



        ObjDb res = result.get(0);
        String str=res.get("ID");
        if(str.compareTo("NO")==0){

            controlField();

        }
        else{


            Toast.makeText(getActivity().getApplicationContext(),"Utente già registrato", Toast.LENGTH_LONG).show();
            progress(false);

            }

    }

    private void controlField(){


        if(isValidEmail(mailS)==true && isCellValid(cellS)==true && accept.isChecked()){

            if(passS.compareTo(passDue)==0 && isPasswordValid(passS)==true){




                completeReg();



            }
            else{
                Toast.makeText(getActivity().getApplicationContext(),"Campo password non corretto", Toast.LENGTH_SHORT).show();
                progress(false);
                submit.setVisibility(View.VISIBLE);


            }

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




            new RequestFtp().setParameters(activity, "cities.php", "spinnerCity", RegStudentFragment.this).execute();





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
            new RequestFtp().setParameters(activity, url, "getFacolta", RegStudentFragment.this).execute();

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

            new RequestFtp().setParameters(activity, url, "spinnerUni", RegStudentFragment.this).execute();






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