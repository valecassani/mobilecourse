package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Matteo on 04/09/2015.
 */
public class PrenotazioniDettagliActivity extends AppCompatActivity {

    private static String TAG = "PrenotazioniDett";

    private Context context;
    private ProgressBar progress;
    private AppCompatActivity activity;
    private String indirizzo;
    private TabHost th;
    private float realdist;
    private TextView distance;
    private MapFragment mapFragment;
    private PrenotazioneItemDetailsFragment pid = new PrenotazioneItemDetailsFragment();


    private RequestQueue queue;

    private String idPrenotazione;
    private String distanza;


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

        setContentView(R.layout.prenotazione_dettagli_activity);


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
        getSupportActionBar().setTitle("DETTAGLI PRENOTAZIONE");

        queue = Volley.newRequestQueue(context);

        Bundle data = getIntent().getExtras();
        idPrenotazione = data.getString("id");

        populateData();

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);





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
                            indirizzo = obj.getString("indirizzo");
                            distanza = obj.getString("distanza");
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                                        @Override
                                                        public void onMapReady(GoogleMap map) {

                                                            //i due numeri sono lat e long ricavabili dal metodo getLocationFromAddress che trovi sotto


                                                            //calcolo distanza in km. puoi servirmi per la ricerca




                                                            LatLng ltlg = getLocationFromAddress(indirizzo);
                                                            System.out.println("Latitudine:" + ltlg.latitude);
                                                            System.out.println("Longitudine:" + ltlg.longitude);

                                                            Marker casaTutor = map.addMarker(new MarkerOptions().position(ltlg)
                                                                    .title("Nome Tutor"));



                                                            TextView distanza = (TextView)findViewById(R.id.distanza_text);

                                                            distanza.setText("Distanza: " + distanza);





                                                            // Move the camera instantly to hamburg with a zoom of 15.
                                                            map.moveCamera(CameraUpdateFactory.newLatLng(ltlg));


                                                            // Zoom in, animating the camera.
                                                            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                                                        }
                                                    }

                            );




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

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    private void calcolaDistanza() {


        Location loc1 = new Location("A");
        LatLng firstpoint = getLocationFromAddress(null);
        loc1.setLatitude(firstpoint.latitude);
        loc1.setLongitude(firstpoint.longitude);
        System.out.println(firstpoint.latitude);
        System.out.println(firstpoint.longitude);

        Location loc2 = new Location("B");

        LatLng secondpoint = getLocationFromAddress(null);
        loc2.setLatitude(secondpoint.latitude);
        loc2.setLongitude(secondpoint.longitude);

        System.out.println(secondpoint.latitude);
        System.out.println(secondpoint.longitude);

        realdist = loc1.distanceTo(loc2);


        distance.setText(String.format("%.1f", realdist / 1000) + " Km");


    }

    @Override
    public void onResume() {
        super.onResume();
        populateData();

    }

    @Override
    public void onRestart() {
        super.onRestart();
        populateData();
    }

}
