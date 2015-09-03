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
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Matteo on 03/09/2015.
 */
public class MostraRecensioneFragment extends Fragment {

    private View view;
    private HomeStudent activity;
    RequestQueue queue;
    private String idrec;
    private ProgressBar progress;

    private String idfb;
    private String foto;
    private String nome;
    private String cognome;

    private String puntualita;
    private String disponibilita;
    private String chiarezza;
    private String voto_finale;
    private String commento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.mostra_recensione_fragment, container, false);
        progress=(ProgressBar)view.findViewById(R.id.progressBarRecensione);
        queue= Volley.newRequestQueue(view.getContext());

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Recensione");


        Bundle bundle=this.getArguments();
        idrec=bundle.getString("idrec");
        getRecData();






        return view;
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (HomeStudent) activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }

    private void getRecData(){

        progress(true);
        String url;

        url = "http://www.unishare.it/tutored/getRecensioneData.php?idrec=" + idrec;


        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            System.out.println(obj);
                            /*nomeR=obj.getString("nome");
                            cognomeR=obj.getString("cognome");
                            testoR=obj.getString("testo");
                            titoloR=obj.getString("titolo");
                            id_studente=obj.getString("id_studente");
                            regid=obj.getString("regid");
                            uniR=obj.getString("uni");
                            facR=obj.getString("facolta");
                            urlR=obj.getString("foto");
                            data_entroR=obj.getString("data_entro");
                            setField();*/




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




}
