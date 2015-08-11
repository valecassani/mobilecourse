package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Matteo on 03/08/2015.
 */
public class ImpostazLezioniTutorFragment extends Fragment {

    ProgressBar progressView;
    CheckBox gratisC;
    CheckBox groupC;
    CheckBox sedeC;
    CheckBox domicilioC;
    Button save;

    HelpActivity activity;
    String id;

    String sede_propria;
    String grup;
    String grat;
    String dom;


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.impostaz_lezioni_fragment, container, false);
        Bundle get=getArguments();
        id=get.getString("id");
        field();
        getData();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save.setVisibility(View.GONE);
                progress(true);
                takeValue();
                saveSettings();

            }
        });

        return view;
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity =  (ImpostazLezioniTutor)activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }

    private void field(){

        progressView=(ProgressBar) view.findViewById(R.id.progressBar);

        gratisC=(CheckBox)view.findViewById(R.id.checkGratis);
        groupC=(CheckBox)view.findViewById(R.id.checkgruppo);
        domicilioC=(CheckBox)view.findViewById(R.id.checkdom);
        sedeC=(CheckBox)view.findViewById(R.id.checksede);

        save=(Button)view.findViewById(R.id.save);



    }

    private void getData(){

        String url="getSettingLesson.php?idutente=".concat(id);
        new RequestFtp().setParameters(activity, url, "getData", ImpostazLezioniTutorFragment.this).execute();

    }

    public void setField(ArrayList<ObjDb> result){

        ObjDb res = result.get(0);
        String gruppo=res.get("gruppo");
        String gratis=res.get("gratis");
        String domicilio=res.get("domicilio");
        String sedepropria=res.get("sede_propria");

        if(gruppo.compareTo("1")==0){
            groupC.setChecked(true);
        }
        if(gratis.compareTo("1")==0){
            gratisC.setChecked(true);
        }
        if(domicilio.compareTo("1")==0){
            domicilioC.setChecked(true);
        }
        if(sedepropria.compareTo("1")==0){
            sedeC.setChecked(true);
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

    private void takeValue(){

        if(sedeC.isChecked()==true){

            sede_propria="1";
        }
        else{
            sede_propria="0";

        }
        if(domicilioC.isChecked()==true){

            dom="1";
        }
        else{
            dom="0";

        }
        if(gratisC.isChecked()==true){

            grat="1";
        }
        else{
            grat="0";

        }
        if(groupC.isChecked()==true){

            grup="1";
        }
        else{
            grup="0";

        }


    }

    private void saveSettings(){


        String url="setting_lesson.php?idutente=".concat(id).concat("&gruppo=").concat(grup).concat("&sede_propria=").concat(sede_propria)
                .concat("&domicilio=").concat(dom).concat("&gratis=").concat(grat);
        new RequestFtp().setParameters(activity, url, "settingLezioni", ImpostazLezioniTutorFragment.this).execute();
        progress(false);
        save.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity().getApplicationContext(), "Modifiche salvate", Toast.LENGTH_LONG).show();
        activity.finish();





    }

}
