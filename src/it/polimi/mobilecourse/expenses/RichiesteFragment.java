package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.gc.materialdesign.views.Button;

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
    private ListView mListView;
    private Context context;
    private Button newRichiestaButton;
    private String idStudente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.richieste_fragment, container, false);
        items = new ArrayList<RichiestaItem>();
        queue = Volley.newRequestQueue(view.getContext());

        mListView = (ListView) view.findViewById(R.id.richieste_list);

        context = container.getContext();
        newRichiestaButton = (Button) view.findViewById(R.id.button_add_richiesta);
        newRichiestaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NuovaRichiestaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("student_id", idStudente);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        idStudente = getArguments().getString("student_id");
        showResults();


        return view;
    }

    private void showResults() {
        String url;

        url = "http://www.unishare.it/tutored/get_richieste.php?id_studente="+ idStudente;

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Log.d(TAG, response.toString());
                                RichiestaItem item = new RichiestaItem(obj.getString("id"), obj.getString("testo"),
                                        obj.getString("foto"), obj.getString("foto"));
                                items.add(item);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            RichiesteAdapter adapter = new RichiesteAdapter(context, items);
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

    @Override
    public void onDestroy(){


        queue.stop();
        super.onDestroy();
    }

}
