package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import it.polimi.mobilecourse.expenses.data.Student;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private final String TAG = "Student Data Fragment";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentDataFragment newInstance(String param1, String param2) {
        StudentDataFragment fragment = new StudentDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public StudentDataFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.student_data_fragment, container, false);

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        String url ="http://www.unishare.it/tutored/student_data.php";

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";



        final ProgressDialog pDialog = new ProgressDialog(view.getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        final TextView mUsername = (TextView)view.findViewById(R.id.username);
        final TextView mName = (TextView)view.findViewById(R.id.name);




        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            Log.d(TAG, response.toString());
                            mUsername.setText(obj.get("username").toString());
                            mName.setText(obj.get("nome").toString());


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

        pDialog.hide();

// Adding request to request queue
        queue.add(jsonObjReq);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



    public void displayResults(ArrayList<ObjDb> result) {
        Student student = new Student(result);
        TextView username = (TextView) getView().findViewById(R.id.username);
        username.setText(student.getUsername());
        /*
        TextView name = (TextView) getView().findViewById(R.id.name);
        name.setText(student.getName());
        TextView surname = (TextView) getView().findViewById(R.id.surname);
        surname.setText(student.getSurname());

        */


    }





}