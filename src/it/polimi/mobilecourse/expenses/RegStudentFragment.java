package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo on 23/12/2014.
 */
public class RegStudentFragment extends Fragment {

    private Spinner uniSpinner;
    private RegistrationStudent activity;
    private View view;
    private List<String> listaUni=new ArrayList<String>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.regs_frag, container, false);


        uniSpinner=(Spinner) view.findViewById(R.id.spinnerUni);
        //new RequestFtp().setParameters(activity,"numuni.php","numeroel",RegStudentFragment.this).execute();
        manageUSpinner();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (RegistrationStudent) activity;
    }


    public void onActivityCreated(Bundle savedInstancestate){
        super.onActivityCreated(savedInstancestate);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,getListaUni());
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        uniSpinner.setAdapter(adapter);
    }

    private void manageUSpinner(){

        new RequestFtp().setParameters(activity,"univer.php","spinnerUni",RegStudentFragment.this).execute();

        uniSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos=position;
                System.out.println("fvvfv"+pos);
                uniSpinner.setSelection(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }
    public void numeroel(ArrayList<ObjDb> result) {
        ObjDb res= result.get(0);
        String str=res.get("num");
        int num=Integer.parseInt(str);
        //numUni=new String[num];
    }


    public void arrayU(ArrayList<ObjDb> result){

        int i=0;
        while(i<result.size()) {
            ObjDb res = result.get(i);
            String str=res.get("nome");
            listaUni.add(str);
            i++;
        }
    }

    public List<String> getListaUni(){

        return listaUni;
    }

}