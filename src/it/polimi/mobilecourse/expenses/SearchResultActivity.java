package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by valeriocassani on 28/04/15.
 */
public class SearchResultActivity extends Activity {
    private final String TAG = "SearchResultActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            Log.i(TAG,"Query received " );
            Bundle appData = getIntent().getBundleExtra(SearchManager.APP_DATA);
            if (appData != null) {
                String hello = appData.getString("hello");
            }

            showResults(query);
        }


        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG,"intent started");


        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {


    }

    private void showResults(String query) {
        // Query your data set and show results
        // ...
    }
}