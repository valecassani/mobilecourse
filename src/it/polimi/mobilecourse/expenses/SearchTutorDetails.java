package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchTutorDetails extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Toolbar toolbar;
    private ProgressBar progressView;
    private HomeStudent activity;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Location loc1;
    private Location loc2;
    private LocationRequest mLocationRequest;
    private TextView tutorNome;
    private TextView tutorCognome;
    private TextView nomat;
    private TextView norec;

    private Button newRec;

    private String idfb;
    private String urlFoto;
    private ListView mListMaterie;
    private ListMaterieAdapterNoDelete adapter;
    private ListRecensioniAdapter adapterRec;

    private String idTutor;
    private String nome;
    private String cognome;
    private String materiaSelezionata;
    private ArrayList<ListMaterieItem> items = new ArrayList<>();
    private ArrayList<ListRecensioneItem> itemsRec = new ArrayList<>();

    private Spinner spinnerMaterie;
    private ListView mat_tutor;
    private ListView rec_tutor;

    private String idMateriaSelezionata;
    private CircularImageView im;
    private String prezzoMateriaSelezionata;
    private View view;
    private Context context;
    private TabHost th;


    public static final String TAG = SearchTutorDetails.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_tutor_details, container, false);
        progressView=(ProgressBar)view.findViewById(R.id.progressBarTut);

        context = view.getContext();


        th=(TabHost)view.findViewById(R.id.tabHost);
        th.setup();
        TabHost.TabSpec ts=th.newTabSpec("Profilo");
        ts.setContent(R.id.linearLayout);
        ts.setIndicator("Profilo");
        th.addTab(ts);

        ts=th.newTabSpec("Materie");
        ts.setContent(R.id.linearLayout2);
        ts.setIndicator("Materie");

        th.addTab(ts);
        ts=th.newTabSpec("Recensioni");

        ts.setContent(R.id.linearLayout3);
        ts.setIndicator("Recensioni");

        th.addTab(ts);


        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dati tutor");

        idTutor = getArguments().getString("idt");
        /*mGoogleApiClient = new GoogleApiClient.Builder(activity)
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

        );*/

        tutorNome = (TextView) view.findViewById(R.id.tutor_nome);
        newRec=(Button)view.findViewById(R.id.buttonNewRec);
        newRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle forFrag=new Bundle();
                forFrag.putString("idstudente",activity.getUserId());
                forFrag.putString("idtutor",idTutor);


                NuovaRecensioneFragment nrf=new NuovaRecensioneFragment() ;
                nrf.setArguments(forFrag);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.student_fragment, nrf).addToBackStack(null).commit();
            }
        });
        tutorCognome = (TextView) view.findViewById(R.id.tutor_cognome);
        im=(CircularImageView)view.findViewById(R.id.foto);

        mat_tutor=(ListView)view.findViewById(R.id.mat_tutor);
        rec_tutor=(ListView)view.findViewById(R.id.rec_tutor);

        nomat=(TextView)view.findViewById(R.id.nomat);
        norec=(TextView)view.findViewById(R.id.norec);




        progress(true);
        showTutorDetails();
        getMaterieForTutor();
        getRecForTutor();


        return view;
    }

    private void getRecForTutor() {


        String url = "http://www.unishare.it/tutored/getRecensioni.php?idutente=".concat(idTutor);

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            if (response.length() == 0) {

                                norec.setVisibility(View.VISIBLE);


                            }
                            itemsRec.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                ListRecensioneItem item = new ListRecensioneItem(obj.getString("idrec"),obj.getString("idstudente"), obj.getString("nome"),obj.getString("cognome"), Float.parseFloat(obj.getString("puntualita")),
                                        Float.parseFloat(obj.getString("disponibilita")),Float.parseFloat(obj.getString("chiarezza")),Float.parseFloat(obj.getString("voto_finale")),obj.getString("foto"),obj.getString("idfb"));

                                itemsRec.add(item);


                            }



                            adapterRec = new ListRecensioniAdapter(activity.getApplicationContext(), itemsRec);

                            rec_tutor.setAdapter(adapterRec);

                            rec_tutor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                            Object o = rec_tutor.getItemAtPosition(position);
                                            System.out.println(o);
                                            ListRecensioneItem click = ((ListRecensioneItem) o);

                                            FragmentManager fragmentManager = getFragmentManager();

                                            Fragment fragment = new MostraRichiestaFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("idrec", click.getIdrec());
                                            fragment.setArguments(bundle);
                                            System.out.println("Bundle" + bundle);
                                            fragmentManager.beginTransaction().replace(R.id.student_fragment,fragment).addToBackStack(null).commit();

                                        }
                                    });




                            //spinnerMaterie.setAdapter(adapter);
                            /*spinnerMaterie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                               @Override
                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    materiaSelezionata = items.get(position).getNome();
                                    prezzoMateriaSelezionata = items.get(position).getPrezzo();
                                    idMateriaSelezionata = items.get(position).getId();
                                    Log.d(TAG,"Materia selezionata: " + materiaSelezionata);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {


                                }
                            });*/
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

        queue.add(jsonObjReq);


    }

    private void getMaterieForTutor() {


        String url = "http://www.unishare.it/tutored/getMaterie.php?idtutor=".concat(idTutor);

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            if (response.length() == 0) {

                                nomat.setVisibility(View.VISIBLE);


                            }
                            items.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                ListMaterieItem item = new ListMaterieItem(obj.getString("idm"), obj.getString("nome"), obj.getString("prezzo"));

                                items.add(item);


                            }



                            adapter = new ListMaterieAdapterNoDelete(activity.getApplicationContext(), items);

                            mat_tutor.setAdapter(adapter);


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

        queue.add(jsonObjReq);


    }

    private void showTutorDetails() {
        String url = "http://www.unishare.it/tutored/tutor_data.php?id=" + idTutor;


        Log.d(TAG, url);

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());

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
                            Log.i(TAG, "Name set: " + nome);
                            cognome = obj.getString("cognome");
                            Log.i(TAG, "Surname set: " + cognome);
                            idfb = obj.getString("idfb");

                            urlFoto = obj.getString("url");

                            setPhoto();

                            tutorCognome.setText(cognome);

                            manageButton();


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

    private void manageButton() {

        Button button = (Button) view.findViewById(R.id.prenotaz_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, NuovaPrenotazioneActivity.class);
                Bundle bundle = new Bundle();
                if (materiaSelezionata == null) {
                    Toast.makeText(activity.getApplicationContext(), "Seleziona una materia", Toast.LENGTH_SHORT).show();
                } else {
                    bundle.putString("id", idTutor);
                    bundle.putString("nome", nome);
                    bundle.putString("cognome", cognome);
                    bundle.putString("materia", materiaSelezionata);
                    bundle.putString("prezzo", prezzoMateriaSelezionata);
                    bundle.putString("materia_id", idMateriaSelezionata);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

            }
        });
    }

    private void setPhoto(){

        if(idfb.compareTo("")!=0){

            Picasso.with(context.getApplicationContext()).load("https://graph.facebook.com/" +idfb + "/picture"
            ).into(im);
        }
        else if(urlFoto.compareTo("")!=0){


            Picasso.with(context.getApplicationContext()).load("http://www.unishare.it/tutored/" + urlFoto
            ).into(im);

        }
        else{
            im.setImageResource(R.drawable.dummy_profpic);
        }


        progress(false);

    }


    @Override
    public void onPause() {
        super.onPause();
/*        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }*/
    }


    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(activity);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                activity.finish();
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
        } else {
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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
    public void onDestroyView(){
        super.onDestroyView();
        MapFragment f=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
        if(f!=null){

            getFragmentManager().beginTransaction().remove(f).commit();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (HomeStudent) activity;
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

}
