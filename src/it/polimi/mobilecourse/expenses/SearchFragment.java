package it.polimi.mobilecourse.expenses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

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
    private SearchBox search;
    private FragmentActivity activity;
    private DrawerLayout mDrawerLayout;
    private String query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        queue= Volley.newRequestQueue(view.getContext());
        context = view.getContext();

        mListView = (ListView)view.findViewById(R.id.search_tutor_list);
        activity = (AppCompatActivity)getActivity();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        search = (SearchBox) view.findViewById(R.id.searchbox_of_tutors_for_subject);

        ContentResolver contentResolver = view.getContext().getContentResolver();

        String contentUri = "content://" + StudentSuggestionProvider.AUTHORITY + '/' + SearchManager.SUGGEST_URI_PATH_QUERY;
        Uri uri = Uri.parse(contentUri);

        Cursor cursor = contentResolver.query(uri, null, null, new String[]{query}, null);
        if (cursor!=null) {
            cursor.moveToFirst();
        }

        String[] columns = new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1 };
        int[] views = new int[] { R.id.name };


        for(int x = 0; x < columns.length && x < 10; x++){
            SearchResult searchResult = new SearchResult(columns[x],(Drawable) view.getResources().getDrawable(R.drawable.abc_ab_share_pack_mtrl_alpha));
            search.addSearchable(searchResult);
        }
        search.setLogoText("Cerca una materia");
        search.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                //Hamburger has been clicked
                mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }

        });
        Intent intent = getActivity().getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(view.getContext(),
                    StudentSuggestionProvider.AUTHORITY, StudentSuggestionProvider.MODE);

            suggestions.saveRecentQuery(query, null);
        }
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
            }

            @Override
            public void onSearchTermChanged() {
                //clears the listview
                items.clear();
                mListView.setAdapter(null);
            }

            @Override
            public void onSearch(String searchTerm) {

                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(activity.getApplicationContext(),
                        StudentSuggestionProvider.AUTHORITY, StudentSuggestionProvider.MODE);
                suggestions.saveRecentQuery(searchTerm, null);
                Log.i(TAG,"Query saved");
                Toast.makeText(getActivity(), searchTerm + " Searched", Toast.LENGTH_LONG).show();
                items.clear();
                mListView.setAdapter(null);

                showResults(searchTerm);

            }

            @Override
            public void onSearchCleared() {

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
                                SearchTutorItem item = new SearchTutorItem(obj.getString("nome"),obj.getString("cognome"));
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
        if (isAdded() && requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == getActivity().RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class StudentSuggestionProvider extends SearchRecentSuggestionsProvider {
        public final static String AUTHORITY = "it.polimi.mobilecourse.expenses.StudentSuggestionProvider";
        public final static int MODE = DATABASE_MODE_QUERIES;

        public StudentSuggestionProvider() {
            setupSuggestions(AUTHORITY, MODE);
        }
    }
}
