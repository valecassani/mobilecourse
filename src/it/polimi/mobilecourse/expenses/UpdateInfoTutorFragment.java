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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Matteo on 04/07/2015.
 */
public class UpdateInfoTutorFragment extends Fragment {

    private View view;
    private ProgressBar progressView;
    private Spinner uniSpinner;
    private Spinner citySpinner;
    private Spinner facSpinner;
    private Button submit;
    private UpdateInfo activity;
    private manageCSpinner mcs=null;
    private manageUSpinner mus=null;

    private manageFSpinner mfs=null;
    private int identifierUni;

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


    private String nameT;
    private String surnameT;
    private String cellT;
    private String etaT;
    private String addressT;
    private String occT;
    private String expuniT;
    private String expT;

    EditText name;
    EditText surname;
    EditText cell;
    EditText eta;
    EditText address;
    EditText occ;
    EditText exp_uni;
    EditText exp_tut;
    String id;
    String id_universitaU;
    String id_facoltaU;
    int idcittaU;
    int idcittaData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.update_info_tutor_fragment, container, false);
        Bundle get=getArguments();
        id=get.getString("id");

        field();
        settingsReg();

        setData();


        //salva modifiche
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


        this.activity = (UpdateInfo) activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }


    private void field(){



        name=(EditText) view.findViewById(R.id.nameT);
        surname=(EditText) view.findViewById(R.id.surnameT);
        cell=(EditText) view.findViewById(R.id.cellT);
        occ=(EditText) view.findViewById(R.id.occT);

        exp_tut=(EditText) view.findViewById(R.id.espT);
        exp_uni=(EditText) view.findViewById(R.id.espUniT);
        eta=(EditText)view.findViewById(R.id.etaT);
        address=(EditText)view.findViewById(R.id.addressT);
    }


    private void settingsReg(){

        progressView=(ProgressBar)view.findViewById(R.id.progressBarT);


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
        submit=(Button) view.findViewById(R.id.updateT);

    }

    private void setSpinner(){
        mcs= new manageCSpinner();
        mcs.execute((Void) null);
        mus= new manageUSpinner();
        mus.execute(String.valueOf(idcittaU));
        mfs= new manageFSpinner();
        mfs.execute(String.valueOf(idcittaData));




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

                itemC = parent.getItemAtPosition(position).toString();
                idCity = String.valueOf((parent.getSelectedItemPosition()) + 1);

                adapterUni.clear();
                mus = new manageUSpinner();
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

                itemF = parent.getItemAtPosition(position).toString();

                int pos = (parent.getSelectedItemPosition()) + 1;

                idFac = listIdFac.get(pos - 1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });







    }


    private void setData(){

        String url="getData.php?type=1&id=".concat(id);
        new RequestFtp().setParameters(activity, url, "setDataTutor", UpdateInfoTutorFragment.this).execute();

    }



    private void getData(){




        nameT=name.getText().toString();
        surnameT=surname.getText().toString();
        cellT=cell.getText().toString();
        if(cellT.compareTo("")==0){
            cellT="";
        }
        etaT=eta.getText().toString();
        if(etaT.compareTo("")==0){
            etaT="";
        }
        addressT=address.getText().toString();
        if(addressT.compareTo("")==0){
            addressT="";
        }
        occT=occ.getText().toString();
        if(occT.compareTo("")==0){
            occT="";
        }
        expT=exp_tut.getText().toString();
        if(expT.compareTo("")==0){
            expT="";
        }
        expuniT=exp_uni.getText().toString();
        if(expuniT.compareTo("")==0){
            expuniT="";
        }




    }


    private boolean isCellValid(String cell){
        return Patterns.PHONE.matcher(cell).matches();
    }

    private void saveData(){

        String url="update_info.php?type_user=1&idtutor=".concat(id).concat("&nome=").concat(nameT).concat("&cognome=").concat(surnameT.replace(" ","%20")).
                concat("&cellulare=").concat(cellT).concat("&eta=").concat(etaT).concat("&indirizzo=").concat(addressT.replace(" ","%20")).concat("&id_uni=").
                concat(idUni).concat("&id_citta=").concat(idCity).concat("&id_facolta=").concat(idFac).concat("&esp_uni=").concat(expuniT.replace(" ","%20")).concat("&esp_tutor=")
                .concat(expT.replace(" ","%20")).concat("&occupazione=").concat(occT);
        new RequestFtp().setParameters(activity, url, "updateTutor", UpdateInfoTutorFragment.this).execute();
        //new RequestFtp().setParameters(activity, url_due, "updateTutor2", UpdateInfoTutorFragment.this).execute();

        Toast.makeText(getActivity().getApplicationContext(), "Modifiche salvate", Toast.LENGTH_LONG).show();
       getActivity().finish();



    }



    private void controlField() {


        if (isCellValid(cellT) == true) {


            saveData();

        }
        else{

            Toast.makeText(getActivity().getApplicationContext(), "Cellulare non valido", Toast.LENGTH_SHORT).show();
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

    public void arrayData(ArrayList<ObjDb> result){

        ObjDb res = result.get(0);
        String nameU=res.get("nome");
        name.setText(nameU);
        String surnameU=res.get("cognome");
        surname.setText(surnameU);
        String etaU=res.get("eta");
        if(etaU.compareTo("0")!=0) {
            eta.setText(etaU);
        }
        String cellU=res.get("cellulare");
        cell.setText(cellU);
        String addressU=res.get("indirizzo");
        address.setText(addressU);
        String id_cittaU=res.get("id_citta");
        idcittaU=Integer.parseInt(id_cittaU);


        id_universitaU=res.get("id_universita");
        idcittaData =Integer.parseInt(id_universitaU);

        id_facoltaU=res.get("id_facolta");

        String oc=res.get("occupazione");
        occ.setText(oc);
        String euni=res.get("esp_uni");
        exp_uni.setText(euni);
        String etut=res.get("esp_tutor");
        exp_tut.setText(etut);


        setSpinner();


    }



    public void arrayU(ArrayList<ObjDb> result){

        int i=0;

        adapterUni.clear();

        listIdUni=new ArrayList<String>();
        while(i<result.size()) {
            ObjDb res = result.get(i);
            String str=res.get("nome");
            adapterUni.add(str);
            String strID=res.get("ID");
            listIdUni.add(strID);
            i++;
        }


        manageUSpinner();

        if(id_universitaU.compareTo("0")!=0) {
            int pos = listIdUni.indexOf(id_universitaU);
            uniSpinner.setSelection(pos);
            System.out.println("Set uni " + pos);

        }
        else{
            uniSpinner.setSelection(listIdUni.indexOf("5"));

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

        manageCSpinner();

        if(idcittaU!=0) {
            citySpinner.setSelection(idcittaU - 1);
            System.out.println("Set città " + idcittaU);
        }
        else{
            citySpinner.setSelection(0);

        }


    }

    public void arrayF(ArrayList<ObjDb> result){

        int i=0;

        adapterFac.clear();

        listIdFac=new ArrayList<String>();
        while(i<result.size()) {
            ObjDb res = result.get(i);
            String str=res.get("nome");
            adapterFac.add(str);
            String idF=res.get("ID");
            listIdFac.add(idF);
            i++;
        }
        manageFSpinner();

        if(id_facoltaU.compareTo("0")!=0) {
            int pos = listIdFac.indexOf(id_facoltaU);

            facSpinner.setSelection(pos);
            System.out.println("Set facoltà " + pos);
        }



    }




    public class manageCSpinner extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void ...params){




            new RequestFtp().setParameters(activity, "cities.php", "spinnerCityT", UpdateInfoTutorFragment.this).execute();





            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

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
            new RequestFtp().setParameters(activity, url, "getFacoltaT", UpdateInfoTutorFragment.this).execute();

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {


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

            new RequestFtp().setParameters(activity, url, "spinnerUniT", UpdateInfoTutorFragment.this).execute();






            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){

            if (success) {


                uniSpinner.setAdapter(adapterUni);

                uniSpinner.setPrompt("Seleziona tra le seguenti università la tua:");




            }
        }




    }
}

