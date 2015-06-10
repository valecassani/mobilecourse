package it.polimi.mobilecourse.expenses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by valeriocassani on 28/05/15.
 */
public class RichiesteItemDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private String id;
    private Intent intent;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        intent = getIntent();
        setContentView(R.layout.request_details);
        toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(25);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
            default:
                super.onOptionsItemSelected(item);





        }
        return false;

    }




}
