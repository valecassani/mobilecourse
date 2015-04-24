package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Valerio on 17/04/2015.
 */
public class NuovaRichiestaActivity extends ActionBarActivity implements View.OnClickListener{

    private RequestQueue queue;
    private Context context;
    private EditText mTesto;
    private static EditText mDataEntro;
    private Button sendButton;
    private String testo;
    private Date data;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private String idStudente;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private java.sql.Date dataToSend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_req_frag);


        context = getApplicationContext();
        queue= Volley.newRequestQueue(context);
        idStudente = getIntent().getExtras().getString("student_id");
        mTesto = (EditText)findViewById(R.id.testoRichiesta);
        mDataEntro = (EditText)findViewById(R.id.dataEntro);
        sendButton = (Button)findViewById(R.id.buttonSendRich);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateDialog();


        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                manageInput();
            }
        });

        getSupportActionBar().setTitle("Nuova richiesta");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        return;
    }

    private void manageInput() {
        testo = mTesto.getText().toString();
        try {
            data = new SimpleDateFormat("dd-MM-yyyy").parse(mDataEntro.getText().toString());
            dataToSend = new java.sql.Date(data.getTime());
            Log.i("Nuova Richiesta", "Parse data ok  " + data.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String url = "http://www.unishare.it/tutored/add_richiesta.php";



        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url,
                              new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                // response
                Log.d("Response", response);
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
                    params.put("testo", testo);
                    params.put("data", dataToSend.toString());
                    params.put("id_studente", idStudente);
                    params.put("foto","prova foto");

                    return params;
                }
            };
        queue.add(jsObjRequest);
        Toast.makeText(context,"Richiesta Aggiunta",Toast.LENGTH_SHORT);
        returnToFragmentRichieste();


    }

    private void setDateDialog(){
        mDataEntro.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDataEntro.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                returnToFragmentRichieste();
                return true;
                


        }
        return super.onOptionsItemSelected(item);
    }

    private void returnToFragmentRichieste(){
        Intent intent = new Intent(this,HomeStudent.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id",idStudente );
        bundle.putString("Mail","");
        bundle.putInt("position", 2);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        if(v == mDataEntro) {
            datePickerDialog.show();
        }
    }
}
