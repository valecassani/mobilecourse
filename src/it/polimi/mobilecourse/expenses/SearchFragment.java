package it.polimi.mobilecourse.expenses;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonFloat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by valeriocassani on 15/04/15.
 */
public class SearchFragment extends Fragment implements LocationListener{
    private final String TAG = "Search Fragment";
    private ArrayList<SearchTutorItem> items = new ArrayList<>();
    private RequestQueue queue;
    private Context context;
    private ListView mListView;
    private EditText searchSubject;
    private ButtonFloat searchButton;
    private LocationManager locationManager;
    private String provider;
    private double lat;
    private double lng;

    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private boolean canGetLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        queue= Volley.newRequestQueue(view.getContext());
        context = view.getContext();



        searchSubject = (EditText)view.findViewById(R.id.search_tutor);

        searchButton = (ButtonFloat)view.findViewById(R.id.search_tutor_button);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, false);
                Location location = getLocation();
                String url;
                if (location != null) {
                    Log.d(TAG,"Location is not null");
                    Log.d(TAG,"Latitude : " + location.getLatitude() + "; Longitude : " + location.getLongitude());
                    url = "http://www.unishare.it/tutored/search_by_subject.php?subject=" + searchSubject.getText().toString()+ "&lat="
                            + location.getLatitude() + "&long=" + location.getLongitude() ;

                } else {
                    url = "http://www.unishare.it/tutored/search_by_subject.php?subject=" + searchSubject.getText().toString();
                    Log.d(TAG, "Location is null");
                }

                Intent intent = new Intent(context,SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("query", url);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);







        return view;
    }

    private void showResults(String searchTerm) {

        String url = "http://www.unishare.it/tutored/search_by_subject.php?subject=" + searchTerm + "&lat=" + lat + "&long=" + lng;


        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            if (response.length() == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                builder.setMessage("Nessun Risultato").setTitle("Risultati ricerca");

                                AlertDialog dialog = builder.create();
                                builder.setPositiveButton("Chiudi", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                Log.d(TAG, obj.toString());
                                SearchTutorItem item = new SearchTutorItem(obj.getString("nome"),obj.getString("cognome"),null);
                                items.add(item);



                            }

                            SearchTutorAdapter adapter = new SearchTutorAdapter(context, items);
                            mListView.setAdapter(adapter);





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



    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat =  location.getLatitude();
        lng = location.getLongitude();
        Log.d(TAG,"Latitude " + lat +", Longitude " + lng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getActivity(), "Enabled new provider " + provider,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLocation() {
        Location location = null;

        //TODO aggiungere se esiste indirizzo da database e ottenere coordinate
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1,
                            1, this);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {

                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                1,
                                1, this);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }
}





