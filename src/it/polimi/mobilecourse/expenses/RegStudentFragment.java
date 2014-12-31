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
    private Button submit;
    private RegistrationStudent activity;
    private SpinnerUni su;
    private manageSpinner ms=null;
    private int identifierUni;
    private String idUni;
    private String itemUni;
    private ArrayAdapter<String> adapter;
    private String nameS;
    private String surnameS;
    private String cellS;
    private String mailS;
    private String passS;
    private String passDue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


         view = inflater.inflate(R.layout.regs_frag, container, false);
        progressView=(ProgressBar)view.findViewById(R.id.progressBarRS);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        uniSpinner=(Spinner) view.findViewById(R.id.spinnerUni);
        submit=(Button) view.findViewById(R.id.regS);

        ms= new manageSpinner();
        ms.execute((Void) null);
        manageUSpinner();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressBar();
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





    private void manageUSpinner(){




        uniSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemUni=parent.getItemAtPosition(position).toString();
                identifierUni=(parent.getSelectedItemPosition())+1;
                idUni=String.valueOf(identifierUni);

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

        String url="registration.php?username=".concat(mailS).concat("&").concat("password=").concat(passS).concat("&").concat("nome=")
                .concat(nameS).concat("&").concat("cognome=").concat(surnameS).concat("&").concat("cellulare=").concat(cellS)
                .concat("&").concat("id_uni=").concat(idUni);
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
            Intent myintent = new Intent(view.getContext(), LandingActivity.class);
            startActivity(myintent);
            }

    }

    private void controlField(){


        if(isValidEmail(mailS)==true && isCellValid(cellS)==true ){

            if(passS.compareTo(passDue)==0 && isPasswordValid(passS)==true){




                completeReg();



            }
            else{
                Toast.makeText(getActivity().getApplicationContext(),"Campo password non corretto", Toast.LENGTH_SHORT).show();

            }

        }
        else{

            Toast.makeText(getActivity().getApplicationContext(),"Mail o Cellulare non validi", Toast.LENGTH_SHORT).show();
        }

    }



    private void progressBar(){

        final int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        progressView.setVisibility(View.VISIBLE);

        progressView.animate().setDuration(shortAnimTime).alpha(1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progressView.setVisibility(View.GONE);
            }
        });
        try{

            Thread.sleep(2000);

        }
        catch(Exception e){

        }


    }



    public void arrayU(ArrayList<ObjDb> result){

        int i=0;

        while(i<result.size()) {
            ObjDb res = result.get(i);
            String str=res.get("nome");
            adapter.add(str);
            i++;
        }
    }




    public class manageSpinner extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void ...params){



            new RequestFtp().setParameters(activity, "univer.php", "spinnerUni", RegStudentFragment.this).execute();






            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){

            if(success) {



                uniSpinner.setAdapter(adapter);
                uniSpinner.setPrompt("Seleziona tra le seguenti");



            }
        }




    }

}