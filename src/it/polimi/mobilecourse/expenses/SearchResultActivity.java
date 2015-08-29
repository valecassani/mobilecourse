package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
 * Created by valeriocassani on 28/04/15.
 */
public class SearchResultActivity extends AppCompatActivity {

    public static Activity activity;
    private final String TAG = "SearchResultActivity";
    private Toolbar toolbar;
    private ListView mListView;
    private ArrayList<SearchTutorItem> itemsTutor;
    private RequestQueue queue;
    private Context context;
    private SearchTutorAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.search_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(25);
        getSupportActionBar().setHomeButtonEnabled(true);
        itemsTutor = new ArrayList<SearchTutorItem>();
        context = getBaseContext();

        mListView = (ListView) findViewById(R.id.search_list_item);
        queue= Volley.newRequestQueue(getApplicationContext());
        showResults(getIntent().getExtras().getString("query"));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "Item Clicked " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, SearchTutorDetails.class);
                Bundle bundle = new Bundle();
                SearchTutorItem item = (SearchTutorItem) adapter.getItem(position);
                bundle.putString("idTutor", item.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });







    }

    private void showResults(String url) {




        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            if (response.length() == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SearchResultActivity.this);

                                builder.setMessage("Nessun Risultato").setTitle("Risultati ricerca");

                                AlertDialog dialog = builder.create();
                                if (dialog != null)
                                    Log.i(TAG,"Dialog created");
                                builder.setNeutralButton("Chiudi", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            dialog.wait(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        dialog.dismiss();

                                        finish();
                                    }
                                });

                                dialog.show();



                            }
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                Log.d(TAG, obj.toString());
                                SearchTutorItem item = new SearchTutorItem(obj.getString("nome"),obj.getString("cognome"),obj.getString("id"));
                                itemsTutor.add(item);



                            }

                            adapter = new SearchTutorAdapter(getApplicationContext(), itemsTutor);
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
}