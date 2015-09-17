package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
 * Created by Valerio on 15/05/2015.
 */
public class HomeTutorFragment extends Fragment {

    private HomeTutor activity;

    private ArrayList<ListRichiesteItem> items = new ArrayList<>();

    ListRichiesteAdapter adapter ;

   // private ListView richieste_list;
    private RecyclerView mRichiesteRecView;
    private String userId;
    private RequestQueue queue;
    private TextView nor;

    final String TAG="Home Tutor";
    private RichiesteHomeTutorCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int no;
    ProgressBar progress;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_tutor_fragment, container, false);

        progress=(ProgressBar)view.findViewById(R.id.progressBarHome);
        queue= Volley.newRequestQueue(view.getContext());
        nor=(TextView)view.findViewById(R.id.norequestT);


        userId=activity.getUserId();
        mRichiesteRecView = (RecyclerView)view.findViewById(R.id.richieste_list);
        mLayoutManager = new LinearLayoutManager(getActivity());


        //richieste_list=(ListView)view.findViewById(R.id.richieste_list);


        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home Tutor");


        progress(true);
        getList();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity =  (HomeTutor)activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }




    public void getList(){


        String url="http://www.unishare.it/tutored/getRichieste.php?idtutor=".concat(userId);

        Log.d(TAG, url);

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            if (response.length() == 0) {



                                nor.setVisibility(View.VISIBLE);

                            }
                            items.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                ListRichiesteItem item = new ListRichiesteItem(obj.getString("idr"),obj.getString("id_studente"),obj.getString("nome"),obj.getString("cognome"),
                                        obj.getString("data_entro"),obj.getString("titolo"),
                                        obj.getString("uni"),obj.getString("url"),obj.getString("idfb"));

                                items.add(item);



                            }

                            SessionManager sm = new SessionManager(getActivity().getApplicationContext());
                            adapter = new ListRichiesteAdapter(activity.getApplicationContext(), items);

                            mAdapter = new RichiesteHomeTutorCardAdapter(items,sm.getUserDetails().get("tipo"));
                            mRichiesteRecView.setAdapter(mAdapter);
                            mRichiesteRecView.setLayoutManager(mLayoutManager);
                            mRichiesteRecView.setHasFixedSize(true);
                            //richieste_list.setAdapter(adapter);
                            //Functions.setListViewHeightBasedOnChildren(richieste_list);
                            /*
                            richieste_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Object o = richieste_list.getItemAtPosition(position);
                                    System.out.println(o);
                                    ListRichiesteItem click = ((ListRichiesteItem) o);

                                    FragmentManager fragmentManager = getFragmentManager();

                                    Fragment fragment = new MostraRichiestaFragment();
                                     Bundle bundle = new Bundle();
                                     bundle.putString("idr", click.getId());
                                     fragment.setArguments(bundle);
                                    System.out.println("Bundle" + bundle);
                                     fragmentManager.beginTransaction().replace(R.id.tutor_fragment,fragment).addToBackStack(null).commit();

                                }
                            });

                            */


                            progress(false);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        return false;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog

            }


        });

        queue.add(jsonObjReq);

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






}
