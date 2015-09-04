package it.polimi.mobilecourse.expenses;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Valerio on 13/08/2015.
 */
public class PrenotazioneItemDetails extends AppCompatActivity {

    private static final String TAG = "PrenotazioneItemDet";
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleTimeFormat;
    private TextView sceltaData;
    private TextView sceltaOra;

    private Context context;
    private RequestQueue queue;
    private Button sceltaOraButton;
    private EditText editTextCellulare;
    private SessionManager sessionManager;
    private TextView mTextViewMateria;
    private Button sceltaDataButton;
    private NumberPicker durataPicker;
    private ProgressBar progressDialog;


    private String idStudente;
    private String idTutor;
    private String note;
    private String confermato;

    private String date;
    private String time;
    private String idPrenotazione;
    private String materia;
    private String durata;
    private Button sendButton;
    private TextView textDurata;
    private int prezzo;
    private String prezzoOrario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prenotaz_item);

        context = getApplicationContext();
        queue = Volley.newRequestQueue(context);

        getSupportActionBar().setElevation(25);
        Bundle data = getIntent().getExtras();
        idPrenotazione = data.getString("id");
        Toast.makeText(getApplicationContext(), "Prenotazione id " + idPrenotazione, Toast.LENGTH_SHORT).show();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        simpleTimeFormat = new SimpleDateFormat("HH-mm-ss", Locale.US);

        sceltaData = (TextView) findViewById(R.id.data_scelta);
        sceltaDataButton = (Button) findViewById(R.id.button_data);

        sceltaDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(PrenotazioneItemDetails.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        sceltaData.setText(simpleDateFormat.format(newDate.getTime()));
                        date = simpleDateFormat.format(newDate.getTime());


                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });

        sessionManager = new SessionManager(getApplicationContext());
        System.out.println(sessionManager.getUserDetails().get("id"));


        mTextViewMateria = (TextView) findViewById(R.id.button_materia);
        sceltaOra = (TextView) findViewById(R.id.ora_scelta);
        sceltaOraButton = (Button) findViewById(R.id.ora_scelta_button);
        sceltaOraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(PrenotazioneItemDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        time = selectedHour + ":" + selectedMinute;
                        sceltaOra.setText(selectedHour + ":" + selectedMinute);


                    }
                }, hour, minute, true);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        sendButton = (Button) findViewById(R.id.button_conferma_prenotazione);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confermaPrenotazione();


            }
        });

        editTextCellulare = (EditText) findViewById(R.id.cellulare_nuova_prenotaz);



        populateData();


    }

    private void populateData() {

        String url = "http://www.unishare.it/tutored/prenotazione_by_id.php?id=" + idPrenotazione;

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {

                            JSONObject obj = (JSONObject) response.get(0);
                            sceltaData.setText(obj.getString("data"));
                            date=obj.getString("data");
                            sceltaOra.setText(obj.getString("ora_inizio"));
                            time=obj.getString("ora_inizio");
                            mTextViewMateria.setText(obj.getString("nome_materia"));
                            materia = obj.getString("materia");
                            editTextCellulare.setText(obj.getString("cellulare"));
                            idStudente = obj.getString("id_studente");
                            idTutor = obj.getString("id_tutor");
                            confermato = obj.getString("confermato");


                            note = obj.getString("note");
                            if (confermato.equals("1")) {
                                sendButton.setVisibility(View.INVISIBLE);
                                sceltaDataButton.setVisibility(View.INVISIBLE);
                                sceltaOraButton.setVisibility(View.INVISIBLE);
                            }

                            progressDialog.setVisibility(View.INVISIBLE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        return false;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog

            }


        });

        queue.add(jsonObjReq);


    }

    private void confermaPrenotazione() {
        final String numeroCellulare = editTextCellulare.getText().toString();

        try {



        } catch (NullPointerException e) {
            Toast.makeText(context, "Mancano dei dati", Toast.LENGTH_SHORT);
        }

        String url = "http://www.unishare.it/tutored/update_prenotazione.php";


        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public boolean onResponse(String response) {
                        // response
                        Log.d("Response Upd Prenot", response);
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

                params.put("id_studente", idStudente);
                params.put("data", date);
                params.put("id_tutor", idTutor);
                params.put("ora", time);
                params.put("durata", "");
                params.put("materia", materia);
                params.put("note", note);
                params.put("cellulare", numeroCellulare);
                params.put("id_prenotazione", idPrenotazione);

                params.put("confermato", "1");


                return params;
            }
        };
        queue.add(jsObjRequest);
        Toast.makeText(context, "Prenotazione Confermata", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeTutor.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", idTutor);
        intent.putExtras(bundle);
        startActivity(intent);

        finish();

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

    public void showPicker()
    {

        final Dialog d = new Dialog(PrenotazioneItemDetails.this);
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


}




