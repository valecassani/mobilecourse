package it.polimi.mobilecourse.expenses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

/**
 * Created by valeriocassani on 28/05/15.
 */
public class RichiesteItemDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private String id;
    private Intent intent;
    private Context context;
    private String titolo;
    private String testo;
    private String urlPhoto;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        intent = getIntent();
        titolo = intent.getExtras().getString("titolo");
        testo = intent.getExtras().getString("testo");
        urlPhoto = intent.getExtras().getString("foto");
        setContentView(R.layout.request_details);
        toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);

        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(25);

        TextView titoloText = (TextView) findViewById(R.id.titoloRichiesta);
        titoloText.setText(titolo);
        TextView testoText = (TextView) findViewById(R.id.testoRichiesta);
        testoText.setText(testo);
        ImageView image = (ImageView) findViewById(R.id.imageRichiesta);
        Picasso.with(getApplicationContext()).load("http://www.unishare.it/"+urlPhoto).into(image);


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
