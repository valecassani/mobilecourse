package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Matteo on 21/12/2014.
 */
public class RequestFtp extends AsyncTask<Void,ArrayList<Entity>,ArrayList<Entity>> {

    private String url;
    private Fragment fragment;
    private HelpActivity act;

    public RequestFtp setParameters(HelpActivity activity, String url, Fragment fragment) {
        this.url = url;
        this.act=(HelpActivity)activity;
        this.fragment = fragment;
        return this;
    }
    /** The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute() */
    protected ArrayList<Entity> doInBackground(Void... params) {
        return Utilities.queryDatabase(act, "http://www.unishare.it/tutored/" + url);
    }
    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    protected void onPostExecute(ArrayList<Entity> result) {
        if(result == null) {
            Toast.makeText(act.getApplicationContext(),"Errore",5).show();
            return;
        }
        act.handleResult(result,fragment);

    }
}