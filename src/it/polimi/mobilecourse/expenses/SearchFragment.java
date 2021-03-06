package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by valeriocassani on 15/04/15.
 */
public class SearchFragment extends Fragment {
    private final String TAG = "Search Fragment";
    private ArrayList<SearchTutorItem> items = new ArrayList<>();
    private HomeStudent activity;
    private RequestQueue queue;
    private Context context;
    private ListView mListView;
    private TextView nores;
    private EditText searchSubject;
    private ButtonFloat searchButton;
    private ProgressBar progress;
    private CheckBox checkBox;

    private LocationManager locationManager;
    private String provider;
    private double lat;
    private double lng;
    private View view;
    private TextView testa;

    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private boolean canGetLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);


        activity.getTitleToolbar().setText("CERCA UN TUTOR");
        activity.getTitleToolbar().setTextSize(18);

        queue= Volley.newRequestQueue(view.getContext());
        context = view.getContext();



        searchSubject = (EditText)view.findViewById(R.id.search_tutor);
        nores=(TextView)view.findViewById(R.id.noresultR);
        testa=(TextView)view.findViewById(R.id.testa);

        searchButton = (ButtonFloat)view.findViewById(R.id.search_tutor_button);
        searchButton.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mListView=(ListView)view.findViewById(R.id.result_tutor_search);
        progress=(ProgressBar)view.findViewById(R.id.progressBarRicercaTutor);

        checkBox = (CheckBox) view.findViewById(R.id.checkbox_citta);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showResults(searchSubject.getText().toString());
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchSubject.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

            }
        });

        // Define the criteria how to select the locatioin provider -> use
        // default
        //Criteria criteria = new Criteria();
        //provider = locationManager.getBestProvider(criteria, false);
        //Location location = locationManager.getLastKnownLocation(provider);







        return view;
    }

    private void showResults(final String searchTerm) {

        progress(true);
        String url;

        SessionManager sessionManager = new SessionManager(context);


        if (checkBox.isChecked()) {
            url = "http://www.unishare.it/tutored/search_tutor.php?search=" + searchTerm + "&id_user=" + sessionManager.getUserDetails().get("id");
        } else {
            url = "http://www.unishare.it/tutored/search_tutor.php?search=" + searchTerm ;
        }
        Log.d(TAG,url);


        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            if (response.length() == 0) {

                                nores.setVisibility(View.VISIBLE);
                            }
                            items.clear();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                Log.d(TAG, obj.toString());
                                SearchTutorItem item = new SearchTutorItem(obj.getString("nome"),obj.getString("cognome"),
                                        obj.getString("id"),obj.getString("uni"),obj.getString("media"),
                                        obj.getString("url"),obj.getString("idfb"));
                                items.add(item);



                            }

                            Collections.sort(items, new Comparator<SearchTutorItem>() {
                                @Override
                                public int compare(SearchTutorItem lhs, SearchTutorItem rhs) {

                                    return (int) rhs.getMedia() - (int) lhs.getMedia();
                                }
                            });

                            SearchTutorAdapter adapter = new SearchTutorAdapter(activity.getApplicationContext(), items);
                            mListView.setAdapter(adapter);

                            if (!searchTerm.equals("")) {

                                SpannableStringBuilder builder = new SpannableStringBuilder();
                                builder.append("Risultati della ricerca per '");
                                SpannableString wordtoSpan = new SpannableString(searchTerm);

                                wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primaryColor)), 0, searchTerm.length(),
                                        0);
                                builder.append(wordtoSpan);
                                builder.append("'");

                                testa.setText(builder, TextView.BufferType.SPANNABLE);
                            }


                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Object o = mListView.getItemAtPosition(position);
                                    System.out.println(o);
                                    SearchTutorItem click = ((SearchTutorItem) o);

                                    FragmentManager fragmentManager = getFragmentManager();

                                    Fragment fragment = new SearchTutorDetails();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("idt", click.getId());
                                    fragment.setArguments(bundle);
                                    System.out.println("Bundle" + bundle);
                                    fragmentManager.beginTransaction().replace(R.id.student_fragment, fragment).addToBackStack(null).commit();

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
        //locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    public void onPause() {
        super.onPause();
        //locationManager.removeUpdates(this);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity =  (HomeStudent)activity;
    }
}





