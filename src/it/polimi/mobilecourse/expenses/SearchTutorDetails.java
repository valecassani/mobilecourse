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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class SearchTutorDetails extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Toolbar toolbar;
    private HomeStudent activity;
    private ProgressBar progressView;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private LocationRequest mLocationRequest;
    private TextView tutorNome;
    private TextView nomat;
    private TextView norec;
    private TextView cittaT;
    private TextView occT;
    private TextView contentOccT;

    private TextView espuniT;
    private TextView contentEspUniT;


    private TextView esptutT;
    private TextView contentEspTut;

    private TextView facT;
    private TextView contentFacT;

    private TextView uniT;
    private TextView contentUniT;

    private TextView lezT;
    private TextView etaT;

    private TextView distText;
    private TextView distance;
    private CheckBox isgratis;
    private CheckBox isdomicilio;
    private CheckBox isgruppo;
    private CheckBox issede;


    private Button newRec;

    private String idfb;
    private String urlFoto;
    private ListView mListMaterie;
    private ListMaterieProfiloTutorAdapter adapter;
    private ListRecensioniAdapter adapterRec;

    private String idTutor;
    private String nome;
    private String cognome;
    private String uni;
    private String fac;
    private String eta;
    private String materiaSelezionata;
    private String occupazione;
    private String espuni;
    private String esptutor;
    private String citta;
    private String gratis;
    private String domicilio;
    private String sede;
    private String gruppo;
    private String indirizzoTutor;
    private String indirizzoStudente;
    private String cittaStudente;
    private String cellulare;
    private ArrayList<ListMaterieItem> items = new ArrayList<>();
    private ArrayList<ListRecensioneItem> itemsRec = new ArrayList<>();

    private Spinner spinnerMaterie;
    private ListView mat_tutor;
    private ListView rec_tutor;

    private String idMateriaSelezionata;
    private Float realdist;
    private CircularImageView im;
    private String prezzoMateriaSelezionata;
    private View view;
    private Context context;
    private TabHost th;


    public static final String TAG = SearchTutorDetails.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_tutor_details, container, false);
        progressView = (ProgressBar) view.findViewById(R.id.progressBarTut);

        activity.getTitleToolbar().setText("PROFILO TUTOR");
        activity.getTitleToolbar().setTextSize(18);


        context = view.getContext();


        th = (TabHost) view.findViewById(R.id.tabHost);
        th.setup();
        TabHost.TabSpec ts = th.newTabSpec("Profilo");
        ts.setContent(R.id.linearLayout);
        ts.setIndicator("Profilo");
        th.addTab(ts);

        ts = th.newTabSpec("Materie");
        ts.setContent(R.id.linearLayout2);
        ts.setIndicator("Materie");

        th.addTab(ts);
        ts = th.newTabSpec("Recensioni");

        ts.setContent(R.id.linearLayout3);
        ts.setIndicator("Recensioni");

        th.addTab(ts);


        //map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
        //        .getMap();

        //map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profilo Tutor");

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
        newRec = (Button) view.findViewById(R.id.buttonNewRec);
        newRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle forFrag = new Bundle();
                SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
                String ids = sessionManager.getUserDetails().get("id");
                forFrag.putString("idstudente", sessionManager.getUserDetails().get("id"));
                forFrag.putString("idtutor", idTutor);


                NuovaRecensioneFragment nrf = new NuovaRecensioneFragment();
                nrf.setArguments(forFrag);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.student_fragment, nrf).addToBackStack(null).commit();
            }
        });


        layout();


        progress(true);
        showTutorDetails();

        getMaterieForTutor();
        getRecForTutor();


        return view;
    }

            private void controlAddress() {

                SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
                String ids = sessionManager.getUserDetails().get("id");

                String url = "http://www.unishare.it/tutored/student_data.php?id=" + ids;


                Log.d(TAG, url);

                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public boolean onResponse(JSONArray response) {
                                try {


                                    JSONObject obj = response.getJSONObject(0);
                                    indirizzoStudente = obj.getString("indirizzo");
                                    cittaStudente = obj.getString("citta");

                                    if (indirizzoStudente.compareTo("") != 0 && indirizzoTutor.compareTo("") != 0) {

                                        calcolaDistanza();

                                    } else {

                                        distText.setVisibility(View.GONE);
                                        distance.setVisibility(View.GONE);

                                    }
                                    progress(false);


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

    private void calcolaDistanza() {


        Location loc1 = new Location("A");
        if (indirizzoStudente.equals("")) {
            return;
        }
        LatLng firstpoint = getLocationFromAddress(indirizzoStudente);
        try {
            loc1.setLatitude(firstpoint.latitude);
            loc1.setLongitude(firstpoint.longitude);
            System.out.println(firstpoint.latitude);
            System.out.println(firstpoint.longitude);

        } catch (NullPointerException e) {

        }


        Location loc2 = new Location("B");
        if (indirizzoTutor.equals("")) {
            return;
        }
        LatLng secondpoint = getLocationFromAddress(indirizzoTutor);


        try {
            loc2.setLatitude(secondpoint.latitude);
            loc2.setLongitude(secondpoint.longitude);

            System.out.println(secondpoint.latitude);
            System.out.println(secondpoint.longitude);

            realdist = loc1.distanceTo(loc2);


            distance.setText(String.format("%.1f", realdist / 1000) + " Km");

        } catch (NullPointerException e) {

        }



    }

    private void layout() {

        im = (CircularImageView) view.findViewById(R.id.foto);

        mat_tutor = (ListView) view.findViewById(R.id.mat_tutor);
        rec_tutor = (ListView) view.findViewById(R.id.rec_tutor);

        nomat = (TextView) view.findViewById(R.id.nomat);
        norec = (TextView) view.findViewById(R.id.norec);
        distText = (TextView) view.findViewById(R.id.distText);
        distance = (TextView) view.findViewById(R.id.distance);

        occT = (TextView) view.findViewById(R.id.tutor_textv);
        contentOccT = (TextView) view.findViewById(R.id.tutor_occupazione);
        espuniT = (TextView) view.findViewById(R.id.textViewespuni);
        contentEspUniT = (TextView) view.findViewById(R.id.tutor_espuni);
        esptutT = (TextView) view.findViewById(R.id.textv2);
        contentEspTut = (TextView) view.findViewById(R.id.tutor_esptutor);
        facT = (TextView) view.findViewById(R.id.textv4);
        contentFacT = (TextView) view.findViewById(R.id.tutor_fac);
        etaT = (TextView) view.findViewById(R.id.tutor_eta);
        cittaT = (TextView) view.findViewById(R.id.tutor_citta);

        uniT = (TextView) view.findViewById(R.id.textv3);
        contentUniT = (TextView) view.findViewById(R.id.tutor_uni);
        lezT = (TextView) view.findViewById(R.id.textViewlez);
        isgruppo = (CheckBox) view.findViewById(R.id.checkBoxgroup);
        isdomicilio = (CheckBox) view.findViewById(R.id.checkBoxdom);
        isgratis = (CheckBox) view.findViewById(R.id.checkBoxgratis);
        issede = (CheckBox) view.findViewById(R.id.checkBoxsede);

    }

    private void getRecForTutor() {


        String url = "http://www.unishare.it/tutored/getRecensioni.php?idutente=".concat(idTutor);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

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
                                ListRecensioneItem item = new ListRecensioneItem(obj.getString("idrec"), obj.getString("idstudente"), obj.getString("nome"), obj.getString("cognome"), Float.parseFloat(obj.getString("puntualita")),
                                        Float.parseFloat(obj.getString("disponibilita")), Float.parseFloat(obj.getString("chiarezza")), Float.parseFloat(obj.getString("voto_finale")), obj.getString("foto"), obj.getString("idfb"));

                                itemsRec.add(item);


                            }


                            adapterRec = new ListRecensioniAdapter(getActivity().getApplicationContext(), itemsRec);

                            rec_tutor.setAdapter(adapterRec);

                            Functions.setListViewHeightBasedOnChildren(rec_tutor);

                            rec_tutor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                    Object o = rec_tutor.getItemAtPosition(position);
                                    System.out.println(o);
                                    ListRecensioneItem click = ((ListRecensioneItem) o);

                                    FragmentManager fragmentManager = getFragmentManager();

                                    Fragment fragment = new MostraRecensioneFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("idrec", click.getIdrec());
                                    fragment.setArguments(bundle);
                                    System.out.println("Bundle" + bundle);
                                    fragmentManager.beginTransaction().replace(R.id.student_fragment, fragment).addToBackStack(null).commit();

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

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

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


                            adapter = new ListMaterieProfiloTutorAdapter(getActivity().getApplicationContext(), items);

                            mat_tutor.setAdapter(adapter);

                            Functions.setListViewHeightBasedOnChildren(mat_tutor);


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

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {


                            JSONObject obj = response.getJSONObject(0);
                            Log.d(TAG, response.toString());
                            nome = obj.getString("nome");
                            Log.i(TAG, "Name set: " + nome);
                            cognome = obj.getString("cognome");
                            Log.i(TAG, "Surname set: " + cognome);
                            idfb = obj.getString("idfb");

                            urlFoto = obj.getString("url");
                            fac = obj.getString("facolta");
                            uni = obj.getString("universita");

                            indirizzoTutor = obj.getString("indirizzo");


                            setPhoto();


                            eta = obj.getString("eta");
                            occupazione = obj.getString("occupazione");
                            espuni = obj.getString("espuni");
                            esptutor = obj.getString("esptutor");
                            citta = obj.getString("citta");
                            gratis = obj.getString("gratis");
                            sede = obj.getString("sede_propria");
                            domicilio = obj.getString("domicilio");
                            gruppo = obj.getString("gruppo");
                            cellulare = obj.getString("cellulare");


                            setLayout();


                            manageButton();

                            controlAddress();


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

    private void setLayout() {

        tutorNome.setText(nome + " " + cognome);

        if (eta.compareTo("") != 0) {

            etaT.setText(eta + " anni");
        } else {
            etaT.setText("Nessuna età definita");
        }
        if (citta.compareTo("") != 0) {

            cittaT.setText(citta);

        } else {
            cittaT.setText("Nessuna città definita");
        }

        if (occupazione.compareTo("") != 0) {

            contentOccT.setText(occupazione);

        } else {
            contentOccT.setText("Nessuna occupazione definita");


        }

        if (espuni.compareTo("") != 0) {

            contentEspUniT.setText(espuni);

        } else {

            contentEspUniT.setText("Nessuna esperienza universitaria definita");


        }

        if (esptutor.compareTo("") != 0) {

            contentEspTut.setText(esptutor);

        } else {
            contentEspTut.setText("Nessuna esperienza da tutor definita");


        }

        if (uni.compareTo("") != 0) {

            contentUniT.setText(uni);

        } else {
            contentUniT.setText("Nessuna università definita");


        }
        if (fac.compareTo("") != 0) {

            contentFacT.setText(fac);

        } else {
            contentFacT.setText("Nessuna facoltà definita");



        }

        if (gruppo.compareTo("0") == 0 && domicilio.compareTo("0") == 0 && gratis.compareTo("0") == 0 && sede.compareTo("0") == 0) {

            lezT.setVisibility(View.GONE);
            isgruppo.setVisibility(View.GONE);
            isdomicilio.setVisibility(View.GONE);

            isgratis.setVisibility(View.GONE);
            issede.setVisibility(View.GONE);


        }
        if (gruppo.compareTo("0") != 0) {


            isgruppo.setChecked(true);
        } else {
            isgruppo.setVisibility(View.GONE);
        }

        if (domicilio.compareTo("0") != 0) {


            isdomicilio.setChecked(true);
        } else {
            isdomicilio.setVisibility(View.GONE);
        }

        if (gratis.compareTo("0") != 0) {


            isgratis.setChecked(true);
        } else {
            isgratis.setVisibility(View.GONE);
        }

        if (sede.compareTo("0") != 0) {


            issede.setChecked(true);
        } else {
            issede.setVisibility(View.GONE);
        }


    }

    private void manageButton() {

        Button button = (Button) view.findViewById(R.id.prenotaz_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NuovaPrenotazioneActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("id", idTutor);
                bundle.putString("nome", nome);
                bundle.putString("cognome", cognome);
                bundle.putString("materia", materiaSelezionata);
                bundle.putString("prezzo", prezzoMateriaSelezionata);
                bundle.putString("materia_id", idMateriaSelezionata);
                bundle.putString("cellulare",cellulare);
                intent.putExtras(bundle);
                startActivity(intent);

            }


        });

    }

    private void setPhoto() {

        if (idfb.compareTo("") != 0) {

            Picasso.with(context.getApplicationContext()).load("https://graph.facebook.com/" + idfb + "/picture"
            ).into(im);
        } else if (urlFoto.compareTo("") != 0) {


            Picasso.with(context.getApplicationContext()).load("http://www.unishare.it/tutored/" + urlFoto
            ).into(im);

        } else {
            im.setImageResource(R.drawable.dummy_profpic);
        }


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

        Geocoder coder = new Geocoder(getActivity());
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
                getActivity().finish();
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

   /*
    @Override
    public void onDestroyView(){
        super.onDestroyView();
       MapFragment f=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
        if(f!=null){

            getFragmentManager().beginTransaction().remove(f).commit();
        }

    }

    */




    private void progress(final boolean show) {
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity =  (HomeStudent)activity;
    }

    public void onResume() {
        super.onResume();
        activity.getTitleToolbar().setText("PROFILO TUTOR");
        activity.getTitleToolbar().setTextSize(18);

    }


}
