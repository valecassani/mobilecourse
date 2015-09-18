package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

/**
 * Created by Matteo on 24/08/2015.
 */
public class AggiungiMateriaFragment extends Fragment{

    private RequestQueue queue;
    private HomeTutor activity;
    private View view;
    String idt;

    //campi
    Button conf;
    ProgressBar progress;
    EditText nome;
    EditText prezzo;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.aggiungi_materia_fragment, container, false);
        queue= Volley.newRequestQueue(view.getContext());

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Aggiungi materia");


        Bundle bundle=this.getArguments();
        idt=bundle.getString("idt");
        activity.getTitleToolbar().setText("AGGIUNGI MATERIA");
        activity.getTitleToolbar().setTextSize(18);



        field();
        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aggiungi();


            }
        });


        return view;
    }


    private void field(){

        progress=(ProgressBar)view.findViewById(R.id.progressBarAgg);
        nome=(EditText)view.findViewById(R.id.nameM);
        prezzo=(EditText)view.findViewById(R.id.prezzoM);
        conf=(Button)view.findViewById(R.id.confermaAdd);




    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity =  (HomeTutor)activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }


    private void aggiungi()
    {

        if(nome.getText().toString().compareTo("")!=0 && prezzo.getText().toString().compareTo("")!=0 )
        {


            conf.setVisibility(View.GONE);
            progress(true);
            String url="http://www.unishare.it/tutored/add_materia.php?idt=".concat(idt).concat("&nome=").concat(nome.getText().toString())
                    .concat("&prezzo=").concat(prezzo.getText().toString());


            final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public boolean onResponse(JSONArray response) {

                                if (response.length() == 0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                    builder.setMessage("Errore").setTitle("Attenzione");

                                    AlertDialog dialog = builder.create();
                                    if (dialog != null)
                                        builder.setNeutralButton("Chiudi", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                try {
                                                    dialog.wait(2000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                dialog.dismiss();

                                                activity.finish();
                                            }
                                        });

                                    dialog.show();

                                }



                            progress(false);
                            Toast.makeText(getActivity().getApplicationContext(), "Materia aggiunta correttamente", Toast.LENGTH_LONG).show();



                            FragmentManager fragmentManager = getFragmentManager();

                            Fragment fragment = new MaterieTutorFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("idt", idt);
                            fragment.setArguments(bundle);
                            fragmentManager.beginTransaction().replace(R.id.tutor_fragment,fragment).addToBackStack(null).commit();




                            return false;
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // hide the progress dialog

                }


            });

            queue.add(jsonObjReq);


                        }
        else{




            Toast.makeText(getActivity().getApplicationContext(), "Dati mancanti", Toast.LENGTH_LONG).show();

        }






    }

    private void progress(final boolean show){
        final int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        progress.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });



    }



}
