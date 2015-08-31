package it.polimi.mobilecourse.expenses;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RemoteController;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.identity.intents.AddressConstants;
import com.quinny898.library.persistentsearch.SearchResult;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class NuovaPrenotazioneActivity extends AppCompatActivity {

    private static String TAG = "NuovaPrenotazione";

    private Toolbar toolbar;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat anotherDateFormat;
    private TextView sceltaData;
    private TextView sceltaOra;
    private TextView mMateriaText;
    private Context context;
    private RequestQueue queue;
    private Button sceltaOraButton;
    private Button sceltaDataButton;
    private EditText editTextCellulare;
    private SessionManager sessionManager;
    private String materia;
    private Button sceltaDurataButton;

    private String durata;
    private String date;
    private String time;
    private String idTutor;
    private String prezzoOrario;
    private String materiaId;
    private TextView textDurata;
    private int prezzo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_prenotazione);
        context = getApplicationContext();
        queue = Volley.newRequestQueue(context);

        getSupportActionBar().setElevation(25);
        Bundle data = getIntent().getExtras();
        idTutor = data.getString("id");
        materia = data.getString("materia");
        prezzoOrario = data.getString("prezzo");
        materiaId = data.getString("materia_id");
        Log.d(TAG,"prezzo arrivato: " + prezzoOrario);
        Log.d(TAG, "Materia arrivata: " + materia);
        Toast.makeText(getApplicationContext(), "Tutor id " + idTutor, Toast.LENGTH_SHORT).show();
        String nomeTutor = data.getString("nome");
        String cognomeTutor = data.getString("cognome");


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView mTutorSelezionato = (TextView) findViewById(R.id.tutor_selezionato);
        mTutorSelezionato.setText(nomeTutor + " " + cognomeTutor);

        mMateriaText = (TextView) findViewById(R.id.materia_prenotazione);
        mMateriaText.setText(materia);

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        anotherDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        sceltaData = (TextView) findViewById(R.id.data_scelta);
        sceltaDataButton = (Button) findViewById(R.id.button_data);
        sceltaDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(NuovaPrenotazioneActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        date = anotherDateFormat.format(newDate.getTime());
                        sceltaData.setText(simpleDateFormat.format(newDate.getTime()));

                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });

        sessionManager = new SessionManager(getApplicationContext());
        System.out.println(sessionManager.getUserDetails().get("id"));

        textDurata = (TextView)findViewById(R.id.durata_text);

        sceltaOra = (TextView) findViewById(R.id.ora_scelta);
        sceltaOraButton = (Button) findViewById(R.id.ora_scelta_button);
        sceltaOraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(NuovaPrenotazioneActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time = selectedHour + ":" + selectedMinute;
                        sceltaOra.setText(selectedHour + ":" + selectedMinute);
                        //time.setTime(selectedHour);
                    }
                }, hour, minute, true);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        sceltaDurataButton = (Button) findViewById(R.id.button_durata);
        sceltaDurataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker();
            }
        });
        Button sendButton = (Button) findViewById(R.id.button_prenotazione);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviaPrenotazione();
                Bundle bundle = new Bundle();
                bundle.putInt("position", 4);
                Intent intent = new Intent(NuovaPrenotazioneActivity.this, HomeStudent.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
                // SearchResultActivity.activity.finish();

            }
        });

        editTextCellulare = (EditText) findViewById(R.id.cellulare_nuova_prenotaz);


    }

    private void inviaPrenotazione() {
        final String numeroCellulare = editTextCellulare.getText().toString();

        try {
            if (sceltaData != null && !editTextCellulare.getText().toString().equals("")) {


            } else {
                Toast.makeText(context, "Mancano dei dati", Toast.LENGTH_SHORT).show();


            }



        } catch (NullPointerException e) {
            Toast.makeText(context, "Mancano dei dati", Toast.LENGTH_SHORT).show();
        }

        String url = "http://www.unishare.it/tutored/add_prenotazione.php";


        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public boolean onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        return false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("id_studente", sessionManager.getUserDetails().get("id"));
                Log.d(TAG, sessionManager.getUserDetails().get("id"));
                params.put("data", date);
                Log.d(TAG, date);

                params.put("id_tutor", idTutor);
                Log.d(TAG, idTutor);

                params.put("ora", time);
                Log.d(TAG, time);

                params.put("durata", textDurata.getText().toString());
                params.put("materia", materiaId);
                Log.d(TAG, materia);

                params.put("note", "");
                Log.d(TAG, "");

                params.put("cellulare", numeroCellulare);
                Log.d(TAG, numeroCellulare);

                params.put("confermato", "0");
                Log.d(TAG, "0");



                return params;
            }
        };
        queue.add(jsObjRequest);
        Toast.makeText(context, "Prenotazione Aggiunta", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void calcolaPrezzo() {

    }

    public void showPicker()
    {

        final Dialog d = new Dialog(NuovaPrenotazioneActivity.this);
        d.setTitle("Durata Riptezione");
        d.setContentView(R.layout.dialog_number);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(24);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prezzo = Integer.parseInt(prezzoOrario) * np.getValue();
                textDurata.setText("Prezzo totale: " + prezzo + " Euro");
                calcolaPrezzo();
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


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
