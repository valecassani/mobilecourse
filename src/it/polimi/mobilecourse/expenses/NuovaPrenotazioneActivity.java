package it.polimi.mobilecourse.expenses;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    private Spinner spinnerMaterie;
    private SessionManager sessionManager;
    private ArrayList<ListMaterieItem> items = new ArrayList<ListMaterieItem>();
    private String materia;
    private Button sceltaDurataButton;
    private Adapter materieAdapter;

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

        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        if (toolbar != null) {
            //SpannableString st=new SpannableString("Home");
            //st.setSpan(new TypefaceSpan(this, "Gotham-Light.ttf"),0,st.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            TextView title = (TextView)findViewById(R.id.title);
            title.setText("NUOVA PRENOTAZIONE");
            title.setTextSize(18);
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(25);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.primaryColorDark));




        context = getApplicationContext();
        queue = Volley.newRequestQueue(context);


        getSupportActionBar().setElevation(25);
        Bundle data = getIntent().getExtras();
        idTutor = data.getString("id");
        getMaterieForTutor();

        prezzoOrario = data.getString("prezzo");

        String nomeTutor = data.getString("nome");
        String cognomeTutor = data.getString("cognome");

        spinnerMaterie = (Spinner) findViewById(R.id.spinner_materie);






        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("NUOVA PRENOTAZIONE");

        TextView mTutorSelezionato = (TextView) findViewById(R.id.utente_selezionato);
        mTutorSelezionato.setText(nomeTutor + " " + cognomeTutor);



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
                        newDate.set(year, monthOfYear, dayOfMonth);
                        date = anotherDateFormat.format(newDate.getTime());
                        sceltaData.setText(Functions.convertiDataDialog(simpleDateFormat.format(newDate.getTime())));

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
                        String h;
                        String m;
                        h = Functions.addZeroesToNum(selectedHour);
                        m = Functions.addZeroesToNum(selectedMinute);
                        time = h + ":" + m;
                        sceltaOra.setText(h + ":" + m);
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


            }
        });


        spinnerMaterie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListMaterieItem item = items.get(position);
                prezzoOrario = item.getPrezzo();
                materiaId = item.getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    private void getMaterieForTutor() {


        String url = "http://www.unishare.it/tutored/getMaterie.php?idtutor=".concat(idTutor);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            if (response.length() == 0) {


                            }
                            items.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                ListMaterieItem item = new ListMaterieItem(obj.getString("idm"), obj.getString("nome"), obj.getString("prezzo"));

                                items.add(item);


                            }


                            materieAdapter = new ListMaterieAdapterNoDelete(getApplicationContext(), items);

                            spinnerMaterie.setAdapter((SpinnerAdapter) materieAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        return false;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());


            }
        });

        queue.add(jsonObjReq);

    }

    private void inviaPrenotazione() {

        try {
            if (date != null && time != null && durata != null) {
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

                        params.put("id_tutor", idTutor);

                        params.put("ora", time);
                        Log.d(TAG, time);

                        params.put("durata", durata);
                        params.put("materia", materiaId);

                        params.put("note", "");
                        Log.d(TAG, "");



                        params.put("confermato", "0");
                        Log.d(TAG, "0");



                        return params;
                    }
                };
                queue.add(jsObjRequest);
                Toast.makeText(context, "Prenotazione Aggiunta", Toast.LENGTH_SHORT).show();
                finish();



            } else {
                Toast.makeText(context, "Mancano dei dati", Toast.LENGTH_SHORT).show();


            }



        } catch (NullPointerException e) {
            Toast.makeText(context, "Mancano dei dati", Toast.LENGTH_SHORT).show();
        }


    }

    private void calcolaPrezzo() {

    }

    public void showPicker()
    {

        final Dialog d = new Dialog(NuovaPrenotazioneActivity.this);
        d.setTitle("Durata Ripetizione");
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
                durata = Integer.toString(np.getValue());
                System.out.println(durata);
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
