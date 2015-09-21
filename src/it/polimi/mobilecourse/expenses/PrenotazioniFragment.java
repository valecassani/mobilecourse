package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonFloat;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Matteo on 18/11/2014.
 */
public class PrenotazioniFragment extends Fragment {

    private final String TAG = "Prenotazioni";
    private RequestQueue queue;
    private HomeTutor activityT;
    private HomeStudent activityS;
    private ArrayList<PrenotazioniItem> items;
    private Context context;
    private RecyclerView mRecyclerView;
    private PrenotazioniCardAdapter mAdapter;
    private final LinearLayoutManager mLayoutManager;
    //private ListView mListView;
    private String studentId;
    private String tutorId;
    private SwipeRefreshLayout mSwipeRefresh;
    private FloatingActionButton fab;
    private PrenotazioniAdapter adapter;
    private ProgressDialog progressDialog;
    private String tipo;

    public PrenotazioniFragment() {
        items = new ArrayList<PrenotazioniItem>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.ripet_fragment, container, false);

        setRetainInstance(true);

        if (getArguments().getString("student_id")!= null) {
            studentId=getArguments().getString("student_id");
            tipo = "0";
            activityS.getTitleToolbar().setText("PRENOTAZIONI");
            activityS.getTitleToolbar().setTextSize(18);



        } else {
            if (getArguments().getString("tutor_id")!=null) {
                tutorId=getArguments().getString("tutor_id");
                activityT.getTitleToolbar().setText("PRENOTAZIONI");
                activityT.getTitleToolbar().setTextSize(18);
            }
        }



        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_prenotazioni);
        mSwipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showElements();
            }
        });
        context = view.getContext();
        queue = Volley.newRequestQueue(context);

        //mListView = (ListView) view.findViewById(R.id.list_ripetizioni);


        //fab = (FloatingActionButton) view.findViewById(R.id.buttonFloat);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview_prenotazioni);
        /*
        fab.attachToRecyclerView(mRecyclerView);
        if (tutorId != null)
            fab.setVisibility(View.INVISIBLE);
        Log.i(TAG, "Button Created");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                Bundle bundle = new Bundle();
                bundle.putString("student_id", studentId);
                fragment = new SearchFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().addToBackStack("back").replace(R.id.student_fragment, fragment).commit();


            }
        });

        */









        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Attendere...");
        progressDialog.setCancelable(false);


        //registerForContextMenu(mListView);
        registerForContextMenu(mRecyclerView);
        showElements();

        return view;


    }








    private void showElements() {
        String url;

        if (studentId != null) {
            url = "http://www.unishare.it/tutored/search_prenotazione.php?id_studente=" + studentId;
        } else {
            url = "http://www.unishare.it/tutored/search_prenotazione.php?id_tutor=" + tutorId;
        }

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        items.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Log.d(TAG, response.toString());
                                PrenotazioniItem item;
                                if (obj.getString("confermato").equals("1")) {
                                     item = new PrenotazioniItem(obj.getString("id"),obj.getString("materia"),obj.getString("data"),
                                            obj.getString("cellulare"), obj.getString("num_ore"), true);
                                } else {
                                     item = new PrenotazioniItem(obj.getString("id"),obj.getString("materia"),obj.getString("data"),
                                            obj.getString("cellulare"),obj.getString("num_ore"), false);

                                }
                                item.setStudentNome(obj.getString("student_nome"));
                                item.setStudentCognome(obj.getString("student_cognome"));
                                item.setTutorCognome(obj.getString("tutor_cognome"));
                                item.setTutorNome(obj.getString("tutor_nome"));
                                item.setTutorUrl(obj.getString("tutor_url"));
                                item.setTutorIdfb(obj.getString("tutor_idfb"));
                                item.setStudentIdfb(obj.getString("student_idfb"));
                                item.setStudentUrl(obj.getString("student_url"));
                                item.setOraInizio(obj.getString("orainizio"));
                                item.setIdTutor(obj.getString("id_tutor"));
                                item.setIdStudente(obj.getString("id_studente"));
                                items.add(item);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            SessionManager sm = new SessionManager(context);

                            mAdapter = new PrenotazioniCardAdapter(items,sm.getUserDetails().get("tipo"));
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setHasFixedSize(true);
                            //adapter = new PrenotazioniAdapter(context,R.id.list_ripetizioni,items);
                            //mListView.setAdapter(adapter);
                            mSwipeRefresh.setRefreshing(false);

                        }

                        return false;

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                mSwipeRefresh.setRefreshing(false);

            }
        });

        queue.add(jsonObjReq);



    }


    private void startItemDetails(int position) {
        Intent intent = new Intent(context, PrenotazioniDettagliActivity.class);
        Bundle bundle = new Bundle();
        PrenotazioniItem item =  adapter.getItem(position);
        bundle.putString("id", item.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AlertDialog dialog;
        if (item.getTitle() == "Modifica") {
            mAdapter.startItemDetails(info.position);
        } else {
            if (item.getTitle() == "Elimina") {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Sei sicuro di voler eliminare?")
                        .setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                progressDialog.show();
                                mAdapter.removeItem(info.position);
                                Toast.makeText(context, "Elemento eliminato", Toast.LENGTH_SHORT).show();


                            }
                        })
                        .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });
                dialog = builder.create();
                dialog.show();

            } else {
                return false;
            }
        }
        return true;
    }

    private void removeItem(int position) {

        PrenotazioniItem item = adapter.getItem(position);
        Log.d(TAG,"ID to be deleted " + item.getId());
        String delete_url = "http://www.unishare.it/tutored/delete_prenotazione.php?id="
                + item.getId();
        RequestQueue queue1 = Volley.newRequestQueue(context);
        JsonObjectRequest delete_request = new JsonObjectRequest(delete_url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public boolean onResponse(JSONObject response) {

                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        progressDialog.dismiss();
                        Toast.makeText(context,
                                "Deleted Successfully",
                                Toast.LENGTH_SHORT).show();
                        showElements();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(context,
                                "failed to delete", Toast.LENGTH_SHORT)
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return false;
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Errore di connessione",Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(delete_request);






    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activityT = (HomeTutor) activity;


            activityT.getTitleToolbar().setText("PRENOTAZIONI");
            activityT.getTitleToolbar().setTextSize(18);

            return;

        } catch (ClassCastException e) {

        }
        this.activityS = (HomeStudent) activity;
        activityS.getTitleToolbar().setText("PRENOTAZIONI");
        activityS.getTitleToolbar().setTextSize(18);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {



            activityT.getTitleToolbar().setText("PRENOTAZIONI");
            activityT.getTitleToolbar().setTextSize(18);

            return;

        } catch (NullPointerException e) {

        }

        activityS.getTitleToolbar().setText("PRENOTAZIONI");
        activityS.getTitleToolbar().setTextSize(18);

    }


}