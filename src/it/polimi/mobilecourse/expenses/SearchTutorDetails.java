package it.polimi.mobilecourse.expenses;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;



public class SearchTutorDetails extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private Toolbar toolbar;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Location loc1;
    private Location loc2;
    private LocationRequest mLocationRequest;
    private TextView tutorNome;
    private TextView tutorCognome;
    private TextView subjects;
    private String idTutor;
    private String nome;
    private String cognome;

    public static final String TAG = SearchTutorDetails.class.getSimpleName();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_details);


        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(25);
        idTutor = getIntent().getExtras().getString("idTutor");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        MapFragment mapFragment;
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap map) {

                                        //i due numeri sono lat e long ricavabili dal metodo getLocationFromAddress che trovi sotto


                                        //calcolo distanza in km. puoi servirmi per la ricerca
                                        loc1 = new Location("");

                                        loc2 = new Location("");


                                        LatLng ltlg = null;
                                        ltlg = getLocationFromAddress("Via Silone 3,Desio");
                                        System.out.println("Latitudine:" + ltlg.latitude);
                                        System.out.println("Longitudine:" + ltlg.longitude);
                                        loc1.setLatitude(ltlg.latitude);
                                        loc1.setLongitude(ltlg.longitude);


                                        // Move the camera instantly to hamburg with a zoom of 15.
                                        map.moveCamera(CameraUpdateFactory.newLatLng(ltlg));


                                        // Zoom in, animating the camera.
                                        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                                    }
                                }

        );

        tutorNome = (TextView) findViewById(R.id.search_tutor_name);
        tutorCognome = (TextView) findViewById(R.id.search_tutor_surname);

        subjects = (TextView) findViewById(R.id.tutor_details_subjects);



        showTutorDetails();







        }

    private void showTutorDetails() {
        String url = "http://www.unishare.it/tutored/tutor_data.php?id=" + idTutor;
        Log.d(TAG,url);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                            try {


                                JSONObject obj = response.getJSONObject(0);
                                Log.d(TAG, response.toString());
                                nome = obj.getString("nome");
                                tutorNome.setText(nome);
                                Log.i(TAG, "Name set: " + nome );
                                cognome = obj.getString("cognome");
                                Log.i(TAG, "Surname set: " + cognome);
                                tutorCognome.setText(cognome);
                                Button button = (Button)findViewById(R.id.prenotaz_button);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SearchTutorDetails.this, NuovaPrenotazioneActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("id", idTutor);
                                        bundle.putString("nome", nome);
                                        bundle.putString("cognome", cognome);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        Log.d(TAG,"Intent staarted");
                                    }
                                });



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        return false;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());


            }
        });

        queue.add(request);



    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }
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

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }









@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }









    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }
        else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
