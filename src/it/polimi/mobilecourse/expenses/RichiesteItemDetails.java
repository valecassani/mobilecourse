package it.polimi.mobilecourse.expenses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by valeriocassani on 28/05/15.
 */
public class RichiesteItemDetails extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_req_frag);
        toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                return true;



        }
        return super.onOptionsItemSelected(item);
    }




}
