package it.polimi.mobilecourse.expenses;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.quinny898.library.persistentsearch.SearchBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by valeriocassani on 15/04/15.
 */
public class SearchFragment extends Fragment {
    private final String TAG = "Search Fragment";
    private ArrayList<SearchTutorItem> items = new ArrayList<SearchTutorItem>();
    private RequestQueue queue;
    private Context context;
    private ListView mListView;
    private AppCompatActivity activity;
    private DrawerLayout mDrawerLayout;
    private String query;
    private EditText searchSubject;
    private ButtonFloat searchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        queue= Volley.newRequestQueue(view.getContext());
        context = view.getContext();

        mListView = (ListView)view.findViewById(R.id.search_tutor_list);
        activity = (AppCompatActivity)getActivity();

        searchSubject = (EditText)view.findViewById(R.id.search_tutor);

        searchButton = (ButtonFloat)view.findViewById(R.id.search_tutor_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.unishare.it/tutored/search_by_subject.php?subject=" + searchSubject.getText().toString();
                Intent intent = new Intent(context,SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("query", url);
                intent.putExtras(bundle);
                startActivity(intent);
                //showResults(searchSubject.getText().toString());
                //searchSubject.setText("");
            }
        });

        searchSubject.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    showResults(searchSubject.getText().toString());
                    searchSubject.setText("");
                    return true;
                }
                return false;
            }
        });



        return view;
    }

    private void showResults(String searchTerm) {

        String url = "http://www.unishare.it/tutored/search_by_subject.php?subject=" + searchTerm;


        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }


}
