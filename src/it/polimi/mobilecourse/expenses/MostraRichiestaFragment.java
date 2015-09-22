package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Matteo on 20/08/2015.
 */

//singola richiesta per tutor
public class MostraRichiestaFragment extends Fragment {

    private RequestQueue queue;
    private View view;
    private HomeTutor activityT;
    String idr;

    NotificationCompat.Builder mBuilder;

    //campi
    TextView nome;
    TextView titolo;
    TextView testo;
    TextView facolta;
    TextView uni;
    TextView fotot;
    TextView data;
    ImageButton fotor;
    Button accetta;
    Button download;

    String filep;
    ProgressBar progress;

    ScrollView sc;

    //dati
    String nomeR;
    String cognomeR;
    String titoloR;
    String testoR;
    String urlR;
    String uniR;
    String facR;
    String id_studente;
    String data_entroR;
    String regid;

    String path;

    //Bitmap bitmap;

    private Animator mCurrentAnimator;

    private int mShortAnimationDuration;
    private HomeStudent activityS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.mostra_richiesta_fragment, container, false);
        sc=(ScrollView)view.findViewById(R.id.container);
        queue= Volley.newRequestQueue(view.getContext());
        if (activityT!=null){

            activityT.getTitleToolbar().setText("DETTAGLI DELLA RICHIESTA");
            activityT.getTitleToolbar().setTextSize(18);
        } else {
            activityS.getTitleToolbar().setText("DETTAGLI DELLA RICHIESTA");
            activityS.getTitleToolbar().setTextSize(18);

        }





        Bundle bundle=this.getArguments();
        idr=bundle.getString("idr");
        setGraphics();

        showRequest();





        return view;
    }

    private void setGraphics(){

        mBuilder=new NotificationCompat.Builder(getActivity()).
                setSmallIcon(R.drawable.gmc_img).setContentTitle("Tutored").setContentText("Immagine scaricata");
        mBuilder.setAutoCancel(true);

        nome=(TextView)view.findViewById(R.id.nameRichiesta);
        titolo=(TextView)view.findViewById(R.id.titleRichiesta);
        testo=(TextView)view.findViewById(R.id.testoRichiesta);
        facolta=(TextView)view.findViewById(R.id.facRichiesta);
        uni=(TextView)view.findViewById(R.id.uniRichiesta);
        data=(TextView)view.findViewById(R.id.dataR);

        accetta=(Button)view.findViewById(R.id.buttonAccettaR);
        accetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviaNotificaRichiesta();
            }
        });
        download=(Button)view.findViewById(R.id.buttonDownload);

        progress=(ProgressBar)view.findViewById(R.id.progressBarRichiesta);


    }

    private void inviaNotificaRichiesta() {

        String url;
        String idTutor = null;

        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        if (sessionManager.getUserDetails().get("tipo").equals("1")) {
            idTutor = sessionManager.getUserDetails().get("id");
        }

        url = "http://www.unishare.it/tutored/send_notification_richiesta.php?id_studente=" + id_studente + "&id_tutor=" + idTutor ;
        Log.d("MostraRichiesta", url);
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
        );

        queue.add(jsObjRequest);


        getFragmentManager().popBackStack();
    }

    private void showRequest() {
        progress(true);
        String url;

        url = "http://www.unishare.it/tutored/getDataRichiesta.php?idr=" + idr;


        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                            try {
                                JSONObject obj = response.getJSONObject(0);
                                System.out.println(obj);
                                nomeR=obj.getString("nome");
                                cognomeR=obj.getString("cognome");
                                testoR=obj.getString("testo");
                                titoloR=obj.getString("titolo");
                                id_studente=obj.getString("id_studente");
                                regid=obj.getString("regid");
                                uniR=obj.getString("uni");
                                facR=obj.getString("facolta");
                                urlR=obj.getString("foto");
                                data_entroR=obj.getString("data_entro");
                                setField();




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        return false;

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog

            }
        });

        queue.add(jsonObjReq);



    }

    private void setField(){

        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString wordtoSpan = new SpannableString("Richiesta di ");

        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primaryColor)), 0, "Richiesta di ".length(),
                0);
        builder.append(wordtoSpan);
        builder.append(nomeR);
        builder.append(" ");
        builder.append(cognomeR.substring(0,1));
        builder.append(".");


       nome.setText(builder, TextView.BufferType.SPANNABLE);


        titolo.setText(titoloR);
        testo.setText(testoR);

        SpannableStringBuilder builderData = new SpannableStringBuilder();

        String entro="Entro il ";
        SpannableString wordtoSpandue = new SpannableString(entro);

        wordtoSpandue.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primaryColor)), 0, entro.length(),
                0);
        builderData.append(wordtoSpandue);
        builderData.append(Functions.convertiData(data_entroR.substring(0,10)));

        data.setText(builderData, TextView.BufferType.SPANNABLE);


        if(uniR.compareTo("0")!=0){
            SpannableStringBuilder builderUni = new SpannableStringBuilder();
            SpannableString wordtospantre = new SpannableString("Università:");

            wordtospantre.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primaryColor)), 0, "Università:".length(),
                    0);
            builderUni.append(wordtospantre);
            builderUni.append(uniR);

            uni.setText(builderUni,TextView.BufferType.SPANNABLE);
        }
        if(facR.compareTo("0")!=0){
            SpannableStringBuilder builderFac = new SpannableStringBuilder();

            SpannableString wordtosp = new SpannableString("Facoltà:");

            wordtosp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primaryColor)), 0, "Facoltà:".length(),
                    0);
            builderFac.append(wordtosp);
            builderFac.append(facR);

            facolta.setText(builderFac,TextView.BufferType.SPANNABLE);
        }
        if(urlR.compareTo("0")!=0){
             downloadImage();
        }
        else {

            download.setVisibility(View.GONE);

        }


        progress(false);


    }

    private void downloadImage() {


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress(true);

                downloadBitmap db = new downloadBitmap();
                db.execute("http://www.unishare.it/tutored/" + urlR);

            }
        });


    }




    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }

    private void progress(final boolean show){
        final int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        progress.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });



    }

    public class downloadBitmap extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String ...params){

            String urls=params[0];


            URL url = null;
            try {
                url = new URL(urls);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            InputStream input = null;
            try {
                input = url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                //The sdcard directory e.g. '/sdcard' can be used directly, or
                //more safely abstracted with getExternalStorageDirectory()

                String folder="Tutored";
                File f=new File(Environment.getExternalStorageDirectory(),folder);
                if(!f.exists()){
                    f.mkdirs();
                }

                File storagePath = Environment.getExternalStorageDirectory();
                path=Environment.getExternalStorageDirectory().toString()+"/Tutored";
                OutputStream output = new FileOutputStream (path + "/imageRichiesta.jpg");
                try {
                    byte[] buffer = new byte[2048];
                    int bytesRead = 0;
                    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                        output.write(buffer, 0, bytesRead);
                    }
                } finally {
                    output.close();


                    filep=addImageToGallery(path+ "/imageRichiesta.jpg",getActivity());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }





            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){

            if(success) {


                NotificationManager nm=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                //Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(filep).build();




                //Uri uri=Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/Tutored/imageRichiesta.jpg");
                Intent intent = new Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


                PendingIntent contentIntent= PendingIntent.getActivity(getActivity(), 0, intent, 0);
                mBuilder.setContentIntent(contentIntent);
                Notification note = mBuilder.build();
                note.defaults |= Notification.DEFAULT_VIBRATE;
                note.defaults |= Notification.DEFAULT_SOUND;
                nm.notify(0,note);


                progress(false);
               Toast.makeText(getActivity(),"Immagine scaricata correttamente",Toast.LENGTH_LONG).show();




            }
        }




    }

    public static String addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "imageRichiesta");



        values.put(MediaStore.MediaColumns.DATA, filePath);
        values.put(MediaStore.MediaColumns.TITLE, "imageRichiesta");



            Uri uri=Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

            context.getContentResolver().insert(uri, values);



        return filePath;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.activityT = (HomeTutor) activity;
            return;

        } catch (ClassCastException c) {

        }

        this.activityS = (HomeStudent)activity;


    }



}





