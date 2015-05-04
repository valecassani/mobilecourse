package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

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

    public PrenotazioniFragment() {
        items = new ArrayList<PrenotazioniItem>();
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View contView = inflater.inflate(R.layout.ripet_fragment, container, false);
        studentId=getArguments().getString("student_id");
        ((ActionBarActivity)getActivity()).getSupportActionBar().show();


        context = contView.getContext();
        queue = Volley.newRequestQueue(context);
        mListView = (ListView) contView.findViewById(R.id.list_ripetizioni);


        showElements();

        return contView;




    }






    public static Fragment newInstance(){

        PrenotazioniFragment nfg=new PrenotazioniFragment();
        return nfg;

    }

    private void showElements() {
        String url;

        if (studentId != null) {
            url = "http://www.unishare.it/tutored/search_prenotazione.php?id_studente="+studentId;
        } else {
            url = "http://www.unishare.it/tutored/search_prenotazione.php?id_tutor=" + tutorId;
        }

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Log.d(TAG, response.toString());
                                PrenotazioniItem item = new PrenotazioniItem(obj.getString("materia"),obj.getString("data"),
                                        obj.getString("cellulare"));
                                items.add(item);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            PrenotazioniAdapter adapter = new PrenotazioniAdapter(context,R.id.list_ripetizioni,items);
                            mListView.setAdapter(adapter);

                        }



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


}