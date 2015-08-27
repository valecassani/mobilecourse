package it.polimi.mobilecourse.expenses;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

    private String idStudente;
    private String idTutor;
    private String note;
    private String confermato;

    private Date data;
    private Date time;
    private String idPrenotazione;
    private Button sendButton;





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
        String displayIdPrenotazione =data.getString("id_prenotazione");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        simpleTimeFormat = new SimpleDateFormat("HH-mm-ss",Locale.US);

        sceltaData = (TextView)findViewById(R.id.data_scelta);

        sceltaData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getBaseContext(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        sceltaData.setText(simpleDateFormat.format(newDate.getTime()));

                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });

        sessionManager = new SessionManager(getApplicationContext());
        System.out.println(sessionManager.getUserDetails().get("id"));


        mTextViewMateria = (TextView)findViewById(R.id.button_materia);
        sceltaOra = (TextView) findViewById(R.id.ora_scelta);
        sceltaOraButton = (Button) findViewById(R.id.ora_scelta_button);
        sceltaOraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(getBaseContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        Calendar newTime = Calendar.getInstance();
                        newTime.set(selectedHour,selectedMinute);
                        sceltaOra.setText(selectedHour + ":" + selectedMinute);


                    }
                }, hour, minute, true);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        sendButton = (Button)findViewById(R.id.button_conferma_prenotazione);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviaPrenotazione();
                Bundle bundle = new Bundle();
                bundle.putInt("position", 4);
                Intent intent = new Intent(getApplicationContext(), HomeStudent.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        editTextCellulare = (EditText)findViewById(R.id.cellulare_nuova_prenotaz);

        populateData();









    }

    private void populateData() {

        String url = "http://www.unishare.it/tutored/prenotazione_by_id.php?id="+idPrenotazione;

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        Log.d(TAG,response.toString());

                        try {

                            JSONObject obj = (JSONObject)response.get(0);
                            sceltaData.setText(obj.getString("data"));
                            sceltaOra.setText(obj.getString("ora_inizio"));
                            mTextViewMateria.setText(obj.getString("materia"));
                            editTextCellulare.setText(obj.getString("cellulare"));
                            idStudente = obj.getString("id_studente");
                            idTutor = obj.getString("id_tutor");
                            confermato = obj.getString("confermato");

                            note = obj.getString("note");
                            if (confermato.equals("1") ) {
                                sendButton.setVisibility(View.INVISIBLE);
                            }





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

    private void inviaPrenotazione() {
        final String numeroCellulare = editTextCellulare.getText().toString();

        try {
            data = new SimpleDateFormat("dd-MM-yyyy").parse(sceltaData.getText().toString());
            time = null;
            Log.i("Nuova Richiesta", "Parse data ok  " + data.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Toast.makeText(context, "Mancano dei dati", Toast.LENGTH_SHORT);
        }

        String url = "http://www.unishare.it/tutored/add_prenotazione.php";
        if (data == null && numeroCellulare == null ) {
            Toast.makeText(context,"Hai lasciato dei campi vuoti",Toast.LENGTH_LONG);
        } else {



            StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public boolean onResponse(String response) {
                            // response
                            Log.d("Response", response);
                            return false;
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.getMessage());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {

                    Map<String, String>  params = new HashMap<String, String>();

                    params.put("id_studente", idStudente );
                    params.put("data", data.toString());
                    params.put("id_tutor",idTutor );
                    //params.put("ora", sceltaOra.toString());
                    params.put("materia","");
                    params.put("note",note);
                    params.put("cellulare",numeroCellulare);

                    params.put("confermato","1");


                    return params;
                }
            };
            queue.add(jsObjRequest);
            Toast.makeText(context, "Prenotazione Confermata", Toast.LENGTH_SHORT);
            Intent intent = new Intent(this,HomeTutor.class);
            startActivity(intent);

            finish();

        }
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




