package it.polimi.mobilecourse.expenses;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * Created by Matteo on 21/12/2014.
 */
public class RequestFtp extends AsyncTask<Void, ArrayList<ObjDb>, ArrayList<ObjDb>> {

    private String url;
    private Fragment fragment;
    private String op;
    private HelpActivity act;
    private HelpABActivity act_ab;

    public RequestFtp setParameters(HelpActivity activity, String url, String op, Fragment fragment) {
        this.url = url;
        this.op = op;
        this.act = activity;
        this.fragment = fragment;
        return this;
    }

    public RequestFtp setParameters(HelpABActivity activity, String url, String op, Fragment fragment) {
        this.url = url;
        this.op = op;
        this.act_ab = activity;
        this.fragment = fragment;
        return this;
    }


    /**
     * The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute()
     */
    protected ArrayList<ObjDb> doInBackground(Void... params) {
        if (act != null) {
            return Functions.query(act, "http://www.unishare.it/tutored/" + url);

        } else
            return Functions.query(act_ab, "http://www.unishare.it/tutored/" + url);
    }

    /**
     * The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground()
     */
    protected void onPostExecute(ArrayList<ObjDb> result) {
        if (act != null) {
            if (result == null) {
                Toast.makeText(act.getApplicationContext(), R.string.error_connection, Toast.LENGTH_SHORT).show();
                return;
            }
            act.handleResult(result, op, fragment);


        } else if (result == null) {
            Toast.makeText(act.getApplicationContext(), R.string.error_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        act_ab.handleResult(result, op, fragment);


    }


}