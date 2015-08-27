package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
    private ArrayList<PrenotazioniItem> items;
    private Context context;
    private ListView mListView;
    private String studentId;
    private String tutorId;
    private SwipeRefreshLayout mSwipeRefresh;
    private FloatingActionButton fab;
    private PrenotazioniAdapter adapter;

    public PrenotazioniFragment() {
        items = new ArrayList<PrenotazioniItem>();
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.ripet_fragment, container, false);
        if (getArguments().getString("student_id")!= null) {
            studentId=getArguments().getString("student_id");

        } else {
            if (getArguments().getString("tutor_id")!=null) {
                tutorId=getArguments().getString("tutor_id");
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
        mListView = (ListView) view.findViewById(R.id.list_ripetizioni);
        fab = (FloatingActionButton) view.findViewById(R.id.buttonFloat);
        fab.attachToListView(mListView);
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
                                PrenotazioniItem item = new PrenotazioniItem(obj.getString("id"),obj.getString("materia"),obj.getString("data"),
                                        obj.getString("cellulare"));
                                items.add(item);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter = new PrenotazioniAdapter(context,R.id.list_ripetizioni,items);
                            mListView.setAdapter(adapter);
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
        Intent intent = new Intent(context, PrenotazioneItemDetails.class);
        Bundle bundle = new Bundle();
        PrenotazioniItem item =  adapter.getItem(position);
        bundle.putString("titolo", item.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }


}