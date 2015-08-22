package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Matteo on 20/08/2015.
 */

//singola richiesta per tutor
public class MostraRichiestaFragment extends Fragment {

    private RequestQueue queue;
    private HomeTutor activity;
    private View view;
    String idr;

    //campi
    TextView nome;
    TextView titolo;
    TextView testo;
    TextView facolta;
    TextView uni;
    TextView fotot;
    TextView data;
    ImageView fotor;
    Button accetta;
    ProgressBar progress;


    //dati
    String nomeR;
    String cognomeR;
    String titoloR;
    String testoR;
    String urlR;
    String uniR;
    String facR;
    String id_studente;
    String data_entroR;
    String regid;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.mostra_richiesta_fragment, container, false);
        queue= Volley.newRequestQueue(view.getContext());

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        Bundle bundle=this.getArguments();
        idr=bundle.getString("idr");
        setGraphics();

        showRequest();





        return view;
    }

    private void setGraphics(){

        nome=(TextView)view.findViewById(R.id.nameRichiesta);
        titolo=(TextView)view.findViewById(R.id.titleRichiesta);
        testo=(TextView)view.findViewById(R.id.testoRichiesta);
        facolta=(TextView)view.findViewById(R.id.facRichiesta);
        uni=(TextView)view.findViewById(R.id.uniRichiesta);
        data=(TextView)view.findViewById(R.id.dataR);
        fotot=(TextView)view.findViewById(R.id.fotoTesto);
        fotor=(ImageView)view.findViewById(R.id.fotoRichiesta);
        accetta=(Button)view.findViewById(R.id.buttonAccettaR);
        progress=(ProgressBar)view.findViewById(R.id.progressBarRichiesta);


    }

    private void showRequest() {
        progress(true);
        String url;

        url = "http://www.unishare.it/tutored/getDataRichiesta.php?idr=" + idr;


        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                            try {
                                JSONObject obj = response.getJSONObject(0);
                                System.out.println(obj);
                                nomeR=obj.getString("nome");
                                cognomeR=obj.getString("cognome");
                                testoR=obj.getString("testo");
                                titoloR=obj.getString("titolo");
                                id_studente=obj.getString("id_studente");
                                regid=obj.getString("regid");
                                uniR=obj.getString("uni");
                                facR=obj.getString("facolta");
                                urlR=obj.getString("foto");
                                data_entroR=obj.getString("data_entro");
                                setField();




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



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

    private void setField(){

       nome.setText("Richiesta di "+nomeR + " "+cognomeR.substring(0,1)+".");
        titolo.setText(titoloR);
        testo.setText(testoR);
        data.setText("Entro il "+data_entroR.substring(0,10));
        if(uniR.compareTo("0")!=0){
            uni.setText("Università:"+uniR);
        }
        if(facR.compareTo("0")!=0){
            facolta.setText("Facoltà:" + facR);
        }
        if(urlR.compareTo("0")!=0){
             downloadImage();
        }
        else{

            fotot.setVisibility(View.GONE);
        }


        progress(false);


    }

    private void downloadImage(){
        Picasso.with(activity.getApplicationContext()).load("http://www.unishare.it/tutored/" + urlR
        ).into(fotor);

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (HomeTutor) activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

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
