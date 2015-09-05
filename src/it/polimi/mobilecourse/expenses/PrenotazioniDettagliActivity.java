package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;

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
 * Created by Matteo on 04/09/2015.
 */
public class PrenotazioniDettagliActivity extends AppCompatActivity {

    private static String TAG = "PrenotazioniDett";

    private Context context;
    private ProgressBar progress;

    private TabHost th;

    private PrenotazioneItemDetailsFragment pid = new PrenotazioneItemDetailsFragment();


    private RequestQueue queue;

    private String idPrenotazione;


    /*
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.prenotazione_dettagli_fragment, container, false);

        context = view.getContext();


        th = (TabHost) view.findViewById(R.id.tabHostP);
        th.setup();
        TabHost.TabSpec ts = th.newTabSpec("Dettagli");
        ts.setContent(R.id.ll);
        ts.setIndicator("Dettagli");
        th.addTab(ts);

        ts = th.newTabSpec("Mappa");
        ts.setContent(R.id.ll2);
        ts.setIndicator("Mappa");

        th.addTab(ts);


        return view;
    }

    */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.prenotazione_dettagli_fragment);

        context = getApplicationContext();

        th = (TabHost) findViewById(R.id.tabHostP);
        th.setup();
        TabHost.TabSpec ts = th.newTabSpec("Dettagli");
        ts.setContent(R.id.ll);
        ts.setIndicator("Dettagli");
        th.addTab(ts);

        ts = th.newTabSpec("Mappa");
        ts.setContent(R.id.ll2);
        ts.setIndicator("Mappa");

        th.addTab(ts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(context);

        Bundle data = getIntent().getExtras();
        idPrenotazione = data.getString("id");

        populateData();




    }

    private void populateData() {

        String url = "http://www.unishare.it/tutored/prenotazione_by_id.php?id=" + idPrenotazione;

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {

                            JSONObject obj = (JSONObject) response.get(0);

                            pid.populateData(obj);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().addToBackStack("back").replace(R.id.fragreplace, pid).commit();





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        return false;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog

            }


        });

        queue.add(jsonObjReq);


    }



    public void progress(final boolean show){
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();

                return true;


        }
        return super.onOptionsItemSelected(item);
    }





}
