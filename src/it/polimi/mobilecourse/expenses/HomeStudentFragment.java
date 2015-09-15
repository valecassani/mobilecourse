package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Valerio on 15/05/2015.
 */
public class HomeStudentFragment extends Fragment {

    private HomeStudent activity;

    private ArrayList<ListTutorItem> items = new ArrayList<>();
    ListTutorAdapter adapter ;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private ListView list_tutor;
    private String userId;
    private RequestQueue queue;
    private TextView not;
    private View view;


    ProgressBar progress;
    int no;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        progress=(ProgressBar)view.findViewById(R.id.progressBarHomeS);
        queue= Volley.newRequestQueue(view.getContext());

        userId=activity.getUserId();
        //list_tutor=(ListView)view.findViewById(R.id.tutor_list);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.tutor_list);
        mLayoutManager = new LinearLayoutManager(getActivity());


        progress(true);
        getList();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity =  (HomeStudent)activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }




    public void getList(){



        String url="http://www.unishare.it/tutored/getTutor.php?idstudente=".concat(userId);

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            if (response.length() == 0) {


                                not=(TextView)view.findViewById(R.id.noTutor);
                                not.setVisibility(View.VISIBLE);



                            }
                            items.clear();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                ListTutorItem item = new ListTutorItem(obj.getString("nome"),obj.getString("cognome"),obj.getString("ID"),
                                        obj.getString("uni"),obj.getString("media"),obj.getString("url"),obj.getString("idfb"));
                                items.add(item);



                            }

                            Collections.sort(items, new Comparator<ListTutorItem>() {
                                @Override
                                public int compare(ListTutorItem lhs, ListTutorItem rhs) {

                                    return (int)rhs.getMedia()-(int)lhs.getMedia();
                                }
                            });

                            //adapter = new ListTutorAdapter(activity.getApplicationContext(), items);

                            //list_tutor.setAdapter(adapter);

                            //Functions.setListViewHeightBasedOnChildren(list_tutor);

                            /*
                            list_tutor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Object o = list_tutor.getItemAtPosition(position);
                                    System.out.println(o);
                                    ListTutorItem click = ((ListTutorItem) o);

                                    FragmentManager fragmentManager = getFragmentManager();

                                    Fragment fragment = new SearchTutorDetails();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("idt", click.getId());
                                    fragment.setArguments(bundle);
                                    System.out.println("Bundle" + bundle);
                                    fragmentManager.beginTransaction().replace(R.id.student_fragment, fragment).addToBackStack(null).commit();


                                }
                            });

                            */
                            mAdapter = new ListTutorRecAdapter(items);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setHasFixedSize(true);

                            if(0==0) {

                            }







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
