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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Matteo on 24/08/2015.
 */
public class MaterieTutorFragment extends Fragment {

    private RequestQueue queue;
    private RequestQueue queueEl;

    private HomeTutor activity;
    private View view;
    String idt;
    final String TAG="Materie Tutor";


    //campi
    ProgressBar progress;
    private ArrayList<ListMaterieItem> items = new ArrayList<>();

    ListMaterieAdapter adapter ;

    private ListView materie_list;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.materie_tutor_fragment, container, false);
        progress=(ProgressBar)view.findViewById(R.id.progressBarMaterie);
        materie_list=(ListView)view.findViewById(R.id.materie_list);


        queue= Volley.newRequestQueue(view.getContext());
        queueEl= Volley.newRequestQueue(view.getContext());


        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        Bundle bundle=this.getArguments();
        idt=bundle.getString("idt");


        getMaterie();






        return view;
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

    public void getMaterie(){

        progress(true);

        String url="http://www.unishare.it/tutored/getMaterie.php?idtutor=".concat(idt);

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            if (response.length() == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                builder.setMessage("Nessun Risultato").setTitle("Risultati ricerca");

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
                            items.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                ListMaterieItem item = new ListMaterieItem(obj.getString("idm"),obj.getString("nome"),obj.getString("prezzo"));

                                items.add(item);



                            }


                            adapter = new ListMaterieAdapter(activity.getApplicationContext(), items);
                            materie_list.setAdapter(adapter);

                            materie_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Object o = materie_list.getItemAtPosition(position);
                                    System.out.println(o);
                                    final ListMaterieItem click = ((ListMaterieItem) o);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                    builder.setMessage("Vuoi eliminare questa materia?").setTitle("Cancellazione");

                                    builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            eliminaMateria(click.getId());

                                        }
                                    });


                                    builder.setNegativeButton("No", null);

                                    AlertDialog dialog = builder.create();


                                    dialog.show();


                                }
                            });



                            progress(false);



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

        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        progress.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });



    }

    private void eliminaMateria(String idm){


        String url="http://www.unishare.it/tutored/eliminaMateria.php?idm=".concat(idm);

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


                            Toast.makeText(getActivity().getApplicationContext(), "Eliminazione completata", Toast.LENGTH_LONG).show();

                        items.clear();
                            getMaterie();






                        return false;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog

            }


        });

        queueEl.add(jsonObjReq);

        





    }



}
