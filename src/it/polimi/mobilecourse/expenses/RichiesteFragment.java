package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.Button;
import com.melnykov.fab.FloatingActionButton;

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
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private RichiesteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.richieste_fragment, container, false);
        items = new ArrayList<RichiestaItem>();
        queue = Volley.newRequestQueue(view.getContext());
        idStudente = getActivity().getIntent().getExtras().getString("user_id");
        Log.i(TAG,"user id " + idStudente);

        mListView = (ListView) view.findViewById(R.id.richieste_list);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_richieste);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showResults();
            }
        });


        context = container.getContext();

        fab = (FloatingActionButton) view.findViewById(R.id.fab_richieste);
        fab.attachToListView(mListView);
        Log.i(TAG, "Button Created");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NuovaRichiestaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("student_id", idStudente);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });


        //azione su selezione normale
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,"Item Clicked " + position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,RichiesteItemDetails.class);
                Bundle bundle = new Bundle();
                RichiestaItem item = (RichiestaItem) adapter.getItem(position);
                bundle.putString("idRichiesta", item.getTesto());
                startActivity(intent);
            }


        });

        //azione su selezione lunga

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "Item Loong clicked " + position, Toast.LENGTH_SHORT).show();

                return true;
            }
        });


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
                        items.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Log.d(TAG, response.toString());
                                RichiestaItem item = new RichiestaItem(obj.getString("id"), obj.getString("testo"),
                                        obj.getString("dataentro"), obj.getString("foto"));
                                items.add(item);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        adapter = new RichiesteAdapter(context, items);
                        mListView.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);



                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        queue.add(jsonObjReq);




    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.list_item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            String[] menuItems = getResources().getStringArray(R.array.richieste_menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public void onDestroy(){



        super.onDestroy();
        queue.stop();
    }

}
