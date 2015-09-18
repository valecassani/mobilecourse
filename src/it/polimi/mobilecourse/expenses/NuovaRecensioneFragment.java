package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Queue;

/**
 * Created by Matteo on 04/08/2015.
 */
public class NuovaRecensioneFragment extends Fragment {

    HomeStudent activity;
    private View view;

    //prova commit
    ProgressBar progressView;
    String idstudente;
    String idtutor;
    TextView name;

    RatingBar disp;
    RatingBar puntual;
    RatingBar finale;
    RatingBar chiar;
    EditText commento;
    Button addRec;
    String nome;
    RequestQueue queue;
    TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.nuova_recensione_fragment, container, false);
        progressView=(ProgressBar) view.findViewById(R.id.progressBar);


        queue= Volley.newRequestQueue(view.getContext());

        title=activity.getTitleToolbar();
        title.setText("NUOVA RECENSIONE");
        title.setTextSize(18);

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Nuova Recensione");

        Bundle get=getArguments();
        idstudente=get.getString("idstudente");
        idtutor=get.getString("idtutor");
        getInfoTutor();







        return view;
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







    private void saveMarks(){

        String url="http://www.unishare.it/tutored/nuova_recensione.php?idstudente=".concat(idstudente).concat("&idtutor=").concat(idtutor).concat("&puntual=")
                .concat(String.valueOf(puntual.getRating())).concat("&disp=").concat(String.valueOf(disp.getRating())).
                concat("&chiar=").concat(String.valueOf(chiar.getRating())).concat("&finale=")
                .concat(String.valueOf(finale.getRating())).concat("&commento=").concat(commento.getText().toString().replace(" ", "%20"));

        //new RequestFtp().setParameters(activity, url, "saveMarks", NuovaRecensioneFragment.this).execute();

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {

                            progress(false);
                            addRec.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity().getApplicationContext(), "Recensione inserita", Toast.LENGTH_LONG).show();
                            Bundle forFrag=new Bundle();
                            forFrag.putString("idt",idtutor);


                            SearchTutorDetails nrf=new SearchTutorDetails() ;
                            nrf.setArguments(forFrag);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.student_fragment, nrf).addToBackStack(null).commit();



                        } catch (NullPointerException e) {
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

    private void getInfoTutor(){

        progress(true);

        String url="http://www.unishare.it/tutored/tutor_data.php?id=".concat(idtutor);
        //new RequestFtp().setParameters(activity, url, "saveMarks", NuovaRecensioneFragment.this).execute();

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {

                            JSONObject obj = response.getJSONObject(0);
                            System.out.println(obj);
                            nome=obj.getString("nome");
                            field();


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

    private void field(){

        float ratingstep=0.5f;

        name=(TextView)view.findViewById(R.id.textNome);

        /*Spannable WordtoSpan = new SpannableString(nome);
        WordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primaryColor)), 0, nome.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);*/

        name.setText("Qui puoi valutare "+nome);

        progress(false);

        chiar=(RatingBar)view.findViewById(R.id.chiar);
        chiar.setStepSize(ratingstep);
        puntual=(RatingBar)view.findViewById(R.id.puntual);
        puntual.setStepSize(ratingstep);
        disp=(RatingBar)view.findViewById(R.id.disp);
        disp.setStepSize(ratingstep);
        finale=(RatingBar)view.findViewById(R.id.finale);
        finale.setStepSize(ratingstep);

        commento=(EditText)view.findViewById(R.id.comment);

        addRec=(Button)view.findViewById(R.id.addRec);

        addRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addRec.setVisibility(View.GONE);
                progress(true);
                saveMarks();
            }
        });





    }





}
