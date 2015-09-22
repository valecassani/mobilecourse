package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Valerio on 13/08/2015.
 */
public class PrenotazioneItemDetailsFragment extends Fragment {

    private static final String TAG = "PrenotazioneItemDet";
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleTimeFormat;
    private TextView sceltaData;
    private TextView sceltaOra;

    private Context context;
    private RequestQueue queue;
    private Button sceltaOraButton;
    private TextView editTextCellulare;
    private SessionManager sessionManager;
    private TextView mTextViewMateria;
    private Button sceltaDataButton;
    private Button buttonAggiorna;
    private NumberPicker durataPicker;
    private ProgressBar progressDialog;
    private Button sceltaDurataButton;

    private String idStudente;
    private String idTutor;
    private String note;
    private String confermato;
    private String tutor;
    private String studente;

    private String date;
    private String time;
    private String idPrenotazione;
    private String materia;
    private String durata;
    private Button sendButton;
    private TextView textDurata;
    private int prezzo;
    private String prezzoOrario;
    private String cellulare;

    private PrenotazioniDettagliActivity activity;


    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dettagli_prenot_frag, container, false);
        setRetainInstance(true);


        activity = (PrenotazioniDettagliActivity)getActivity();





        context = getActivity().getApplicationContext();
        queue = Volley.newRequestQueue(context);



        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        simpleTimeFormat = new SimpleDateFormat("HH-mm-ss", Locale.US);

        initializeButtons();



        if (confermato.equals("1")) {
            sendButton.setText("Prenotazione Confermata");

            buttonAggiorna.setVisibility(View.INVISIBLE);



        } else {
            editTextCellulare.setVisibility(View.INVISIBLE);
            sceltaDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar newCalendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            sceltaData.setText(Functions.convertiData(simpleDateFormat.format(newDate.getTime())));
                            date = simpleDateFormat.format(newDate.getTime());


                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                    datePickerDialog.show();

                }
            });
            sceltaOraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confermaPrenotazione();


                }
            });
            buttonAggiorna.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aggiornaPrenotazione();
                }
            });
            sceltaDurataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPicker();
                }
            });


        }

        SessionManager sessionManager = new SessionManager(context);
        if (sessionManager.getUserDetails().get("tipo").equals("0")) {
            TextView mUserText = (TextView) view.findViewById(R.id.utente_selezionato);
            mUserText.setText(" " + tutor);
        } else {
            TextView mUserText = (TextView) view.findViewById(R.id.utente_selezionato);
            mUserText.setText(" " + studente);

        }




        TextView materiaText = (TextView) view.findViewById(R.id.materia_prenotazione);
        materiaText.setText(materia);

        textDurata = (TextView) view.findViewById(R.id.durata_text);
        if (!durata.equals("")) {
            prezzo = Integer.parseInt(prezzoOrario) * Integer.parseInt(durata);
            textDurata.setText("Prezzo totale: " + prezzo + " Euro");
        } else {
            textDurata.setText(" ");
        }





        return view;
    }

    public void initializeButtons() {
        sceltaData = (TextView) view.findViewById(R.id.data_scelta);
        sceltaData.setText(Functions.convertiData(date));

        sceltaDataButton = (Button) view.findViewById(R.id.button_data);





        sessionManager = new SessionManager(context);
        System.out.println(sessionManager.getUserDetails().get("id"));



        sceltaOra = (TextView) view.findViewById(R.id.ora_scelta);
        sceltaOra.setText(time);

        sceltaOraButton = (Button) view.findViewById(R.id.ora_scelta_button);



        sendButton = (Button) view.findViewById(R.id.button_conferma_prenotazione);


        sceltaDurataButton = (Button) view.findViewById(R.id.button_durata);


        editTextCellulare = (TextView) view.findViewById(R.id.cellulare_prenotaz);
        editTextCellulare.setText("Cellulare Tutor: " + cellulare);

        buttonAggiorna = (Button) view.findViewById(R.id.button_aggiorna_prenotazione);




    }

    private void aggiornaPrenotazione() {

        String url = "http://www.unishare.it/tutored/update_prenotazione.php";

        System.out.println(url);


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
                params.put("materia", materia);
                params.put("note", note);
                params.put("id_prenotazione", idPrenotazione);



                params.put("confermato", "0");


                return params;
            }
        };
        queue.add(jsObjRequest);


        getActivity().finish();




    }




    private void confermaPrenotazione() {



        String url = "http://www.unishare.it/tutored/update_prenotazione.php";

        System.out.println(url);


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
                params.put("id_prenotazione", idPrenotazione);



                params.put("confermato", "1");


                return params;
            }
        };
        queue.add(jsObjRequest);

        getActivity().finish();

    }




    public void showPicker()
    {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("N° ore della ripetizione");
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

    private void progress(final boolean show){
        final int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        progressDialog.setVisibility(show ? View.VISIBLE : View.GONE);
        progressDialog.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progressDialog.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });



    }

    public void populateData(JSONObject obj) throws JSONException {


        date=obj.getString("data");
        time=obj.getString("ora_inizio");
        materia = obj.getString("nome_materia");
        cellulare = obj.getString("cellulare");
        idStudente = obj.getString("id_studente");
        durata = obj.getString("durata");
        idTutor = obj.getString("id_tutor");
        confermato = obj.getString("confermato");
        idPrenotazione = obj.getString("id");
        tutor = obj.getString("nome_tutor") + " " + obj.getString("cognome_tutor");
        studente = obj.getString("nome_studente") + " " + obj.getString("cognome_studente");
        prezzoOrario = obj.getString("prezzo_orario");




        note = obj.getString("note");

    }


}




