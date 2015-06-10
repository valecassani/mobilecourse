package it.polimi.mobilecourse.expenses;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.identity.intents.AddressConstants;
import com.quinny898.library.persistentsearch.SearchResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NuovaPrenotazioneActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SimpleDateFormat simpleDateFormat;
    private TextView sceltaData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_prenotazione);
        toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(25);
        Bundle data = getIntent().getExtras();
        String idTutor = data.getString("id");
        Toast.makeText(getApplicationContext(), "Tutor id " + idTutor, Toast.LENGTH_SHORT).show();
        String nomeTutor =data.getString("nome");
        String cognomeTutor = data.getString("cognome");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView mTutorSelezionato = (TextView) findViewById(R.id.tutor_selezionato);
        mTutorSelezionato.setText(nomeTutor + " " + cognomeTutor);

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        sceltaData = (TextView)findViewById(R.id.data_scelta);
        sceltaData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(NuovaPrenotazioneActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        sceltaData.setText(simpleDateFormat.format(newDate.getTime()));

                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        Button sendButton = (Button)findViewById(R.id.button_prenotazione);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", 4);
                Intent intent = new Intent(getApplicationContext(),HomeStudent.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
                SearchResultActivity.activity.finish();

            }
        });






    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();

                return true;



        }
        return super.onOptionsItemSelected(item);
    }




}
