package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Matteo on 20/06/2015.
 */
public class RegTutorFragment extends Fragment {

    private View view;
    private ProgressBar progressView;
    private Spinner citySpinner;
    private Button submit;
    private CheckBox accept;
    private RegistrationTutor activity;
    private manageSpinner ms=null;
    private int identifierUni;
    private String itemC;
    private String idCity;
    private String itemUni;
    private ArrayAdapter<String> adapterCity;

    private String nameT;
    private String surnameT;
    private String cellT;
    private String mailT;
    private String passT;
    private String passDue;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.regt_frag, container, false);
        progressView = (ProgressBar)view.findViewById(R.id.progressBarRT);


        settingsReg();
        setSpinner();

        submit = (Button) view.findViewById(R.id.regT);


        //registrazione
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                submit.setVisibility(View.GONE);
                progress(true);
                getData();


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

        accept=(CheckBox)view.findViewById(R.id.checkBox);
        progressView=(ProgressBar)view.findViewById(R.id.progressBarRT);


        adapterCity = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citySpinner= (Spinner) view.findViewById(R.id.spinnerCitta);
        submit=(Button) view.findViewById(R.id.regS);

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
        EditText name=(EditText) view.findViewById(R.id.nameT);
        EditText surname=(EditText) view.findViewById(R.id.surnameT);
        EditText cell=(EditText) view.findViewById(R.id.cellT);
        EditText mail=(EditText) view.findViewById(R.id.mailT);
        EditText pass=(EditText) view.findViewById(R.id.passT);
        EditText passdue=(EditText) view.findViewById(R.id.pass2T);



        nameT=name.getText().toString();
        surnameT=surname.getText().toString();
        cellT=cell.getText().toString();
        mailT=mail.getText().toString();
        passT=pass.getText().toString();
        passDue=passdue.getText().toString();
        controlUser(mailT);






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

        String url="registration_tutor.php?username=".concat(mailT).concat("&").concat("password=").concat(passT).concat("&").concat("nome=")
                .concat(nameT).concat("&").concat("cognome=").concat(surnameT).concat("&").concat("cellulare=").concat(cellT)
                .concat("&").concat("id_citta=").concat(idCity);
        new RequestFtp().setParameters(activity, url, "regTutor", RegTutorFragment.this).execute();
        Toast.makeText(getActivity().getApplicationContext(), "Registrazione completata", Toast.LENGTH_LONG).show();
        Intent myintent = new Intent(view.getContext(), LandingActivity.class);
        startActivity(myintent);



    }

    private void controlUser(String mail){

        String url="control_mail_t.php?mail=".concat(mail);
        new RequestFtp().setParameters(activity, url, "controlT", RegTutorFragment.this).execute();

    }

    public void duplicateMail(ArrayList<ObjDb> result){



        ObjDb res = result.get(0);
        String str=res.get("ID");
        if(str.compareTo("NO")==0){

            controlField();

        }
        else{


            Toast.makeText(getActivity().getApplicationContext(),"Utente gia registrato", Toast.LENGTH_LONG).show();
            Intent myintent = new Intent(view.getContext(), LandingActivity.class);
            startActivity(myintent);
        }

    }

    private void controlField(){


        if(isValidEmail(mailT)==true && isCellValid(cellT)==true && accept.isChecked() ){

            if(passT.compareTo(passDue)==0 && isPasswordValid(passT)==true){




                completeReg();



            }
            else{
                Toast.makeText(getActivity().getApplicationContext(),"Campo password non corretto", Toast.LENGTH_SHORT).show();
                progress(false);
                submit.setVisibility(View.VISIBLE);


            }

        }
        else{

            Toast.makeText(getActivity().getApplicationContext(),"Mail o Cellulare non validi", Toast.LENGTH_SHORT).show();
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




            new RequestFtp().setParameters(activity, "cities.php", "spinnerCity", RegTutorFragment.this).execute();





            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){

            if(success) {





                citySpinner.setAdapter(adapterCity);
                citySpinner.setPrompt("Seleziona tra le seguenti citta la tua:");



            }
        }




    }
}
