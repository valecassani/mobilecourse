package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    private Button imageButton;
    private String testo;
    private Date data;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private String idStudente;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private java.sql.Date dataToSend;
    private Dialog dialog;
    private String selectedPath;
    private String nameFile;
    private ProgressDialog progressDialog;
    String upLoadServerUri = null;

    private int serverResponseCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_req_frag);

        upLoadServerUri = "http://www.unishare.it/tutored/upload_to_server.php";


        context = getApplicationContext();
        queue= Volley.newRequestQueue(context);
        idStudente = getIntent().getExtras().getString("student_id");
        mTesto = (EditText)findViewById(R.id.testoRichiesta);
        mDataEntro = (EditText)findViewById(R.id.dataEntro);
        sendButton = (Button)findViewById(R.id.buttonSendRich);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateDialog();
        imageButton = (Button)findViewById(R.id.buttonImage);
        imageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                openGallery(1);
            } });


        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                manageInput();
                                progressDialog = ProgressDialog.show(NuovaRichiestaActivity.this, "", "Uploading file...", true);

                                new Thread(new Runnable() {
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            public void run() {

                                            }
                                        });

                                        uploadFile(selectedPath);

                                    }
                                }).start();


                                uploadFile(selectedPath);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(NuovaRichiestaActivity.this);
                builder.setMessage("Vuoi inviare?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });

        getSupportActionBar().setTitle("Nuova richiesta");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        return;
    }

    public void openGallery(int req_code) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(galleryIntent, "Select file to upload "), req_code);

    }

    private void manageInput() {
        testo = mTesto.getText().toString();
        try {
            data = new SimpleDateFormat("dd-MM-yyyy").parse(mDataEntro.getText().toString());
            dataToSend = new java.sql.Date(data.getTime());
            Log.i("Nuova Richiesta", "Parse data ok  " + data.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Toast.makeText(context, "Mancano dei dati", Toast.LENGTH_SHORT);
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
                    params.put("foto","tutored/images/"+nameFile);

                    return params;
                }
            };
        queue.add(jsObjRequest);
        Toast.makeText(context, "Richiesta Aggiunta", Toast.LENGTH_SHORT);
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

    public int uploadFile(String sourceFileUri) {
        progressDialog.show();


        String fileName = sourceFileUri;
        System.out.println(selectedPath);

        HttpURLConnection conn = null;
        DataOutputStream dos;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            progressDialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    + selectedPath);


            return 0;

        } else {
            try {
                nameFile = sourceFile.getName();

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);



                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    + " http://www.unishare.it/tutored/images/"
                                    + selectedPath;

                            
                            Toast.makeText(NuovaRichiestaActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                progressDialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(NuovaRichiestaActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                progressDialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(NuovaRichiestaActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload Exception", "Exception : "
                        + e.getMessage(), e);
            }
            progressDialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = managedQuery(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);

    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (requestCode == 1)

            {

                selectedPath = getRealPathFromURI(selectedImageUri);

                System.out.println("selectedPath1 : " + selectedPath);


            }


        }
    }

    //fa partire il thread,l'argomento di execute() è l'immagine da scaricare

    DownloadImage di =new DownloadImage();
    //di.execute("IMAG0583.jpg");



    //thread che scarica l'immagine

    public class DownloadImage extends AsyncTask<String,Void,Boolean> {


        @Override
        protected Boolean doInBackground(String ...params) {

            try {

                //bmp=Functions.downloadImageFromPath("http://www.unishare.it/tutored/images/"+params[0]);

            }
            catch(Exception exception){
                System.out.println("Error "+exception.getMessage());

            }

            return true;

        }


        @Override
        protected void onPostExecute(final Boolean success){

            if(success){

//                setImage();



            }

        }


    }


    //funzione chiamata da onPostExecute e che setta l'immagine.img è un ImageView del file xml dell'activity.
    //devi far girare le funzioni così,se no rompe :(

    /*public void setImage(){

        Image img = new Image();
        img.setImageBitmap(bmp);
        System.out.println("Immagine settata");
    }
    */

//in realtà al thread tu passi il path che ti arriva da db (es. tutored/images/nome.jpg). basta che cambi il path
// che usa la funzione downloadImageFromPath ,mettendo ("http://www.unishare.it/"+params[0])

}
