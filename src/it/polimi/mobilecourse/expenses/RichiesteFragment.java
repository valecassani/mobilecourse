package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
//import com.google.api.services.calendar.model.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by valeriocassani on 15/04/15.
 */
public class RichiesteFragment extends Fragment {

    private final String TAG = "Richieste";
    private RequestQueue queue;
    private ArrayList<RichiestaItem> items;
    //private ListView mListView;
    private RichiesteCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private Context context;
    private Button newRichiestaButton;
    private String idStudente;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button fab;
    private RichiesteAdapter adapter;
    private ProgressDialog progressDialog;
    private HomeStudent activity;
    private ImageView sfondo;

    TextView nor;
    ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.richieste_fragment, container, false);
        sfondo=(ImageView)view.findViewById(R.id.sfondoReq);
        items = new ArrayList<RichiestaItem>();
        queue = Volley.newRequestQueue(view.getContext());
        idStudente = getActivity().getIntent().getExtras().getString("user_id");

        //mListView = (ListView) view.findViewById(R.id.richieste_list);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.richieste_list);
        mLayoutManager = new LinearLayoutManager(getActivity());

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_richieste);
        nor=(TextView)view.findViewById(R.id.norequest);
        progress=(ProgressBar)view.findViewById(R.id.progressRichieste);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showResults();
            }
        });


        context = container.getContext();

        fab = (Button) view.findViewById(R.id.buttonAddRequest);

        Log.i(TAG, "Button Created");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();

                Fragment fragment = new NuovaRichiestaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("student_id", idStudente);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.student_fragment, fragment).addToBackStack(null).commit();


            }
        });


        //azione su selezione normale
        /*
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startItemDetails(position);
            }
        });



        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                return false;
            }
        });

        */

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Attendere...");
        progressDialog.setCancelable(false);




        showResults();

        return view;
    }

    private void showResults() {
        String url;

        progress(true);

        url = "http://www.unishare.it/tutored/get_richieste.php?id_studente="+ idStudente;

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        if (response.length() == 0) {

                            progress(false);
                            nor.setVisibility(View.VISIBLE);





                        }
                        items.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Log.d(TAG, response.toString());
                                RichiestaItem item = new RichiestaItem(obj.getString("id"), obj.getString("testo"),
                                        obj.getString("dataentro"), obj.getString("foto"), obj.getString("titolo"));
                                items.add(item);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        adapter = new RichiesteAdapter(context, items);
                        mAdapter = new RichiesteCardAdapter(items,"0");
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setLayoutManager(mLayoutManager);

                        //mListView.setAdapter(adapter);
                        progress(false);
                        swipeRefreshLayout.setRefreshing(false);


                        return false;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        queue.add(jsonObjReq);




    }




    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AlertDialog dialog;
        if (item.getTitle() == "Modifica richiesta") {
            startItemDetails(info.position);
        } else if (item.getTitle() == "Elimina richiesta") {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Sei sicuro di voler eliminare?")
                    .setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            progressDialog.show();
                            removeItem(info.position);
                            Toast.makeText(context,"Elemento eliminato",Toast.LENGTH_SHORT).show();


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
        return true;
    }

    @Override
    public void onDestroy(){



        super.onDestroy();
        queue.stop();
    }

    private void removeItem(int position) {

        RichiestaItem item = (RichiestaItem) adapter.getItem(position);
        String delete_url = "http://www.unishare.it/tutored/remove_richiesta.php?id="
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

                        showResults();

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

    private void startItemDetails(int position) {
        Bundle bundle = new Bundle();
        RichiestaItem item = (RichiestaItem) adapter.getItem(position);
        FragmentManager fragmentManager = getFragmentManager();

        Fragment fragment = new ModificaRichiestaFragment();
        bundle.putString("idr", item.getId());
        fragment.setArguments(bundle);
        System.out.println("Bundle" + bundle);
        fragmentManager.beginTransaction().replace(R.id.student_fragment,fragment).addToBackStack(null).commit();


    }

    @Override
    public void onResume() {
        super.onResume();
        showResults();
        activity.getTitleToolbar().setText("LE TUE RICHIESTE");
        activity.getTitleToolbar().setTextSize(18);
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
