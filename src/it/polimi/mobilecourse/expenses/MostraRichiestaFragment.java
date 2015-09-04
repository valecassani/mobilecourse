package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Matteo on 20/08/2015.
 */

//singola richiesta per tutor
public class MostraRichiestaFragment extends Fragment {

    private RequestQueue queue;
    private HomeTutor activity;
    private View view;
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


    //Bitmap bitmap;

    private Animator mCurrentAnimator;

    private int mShortAnimationDuration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.mostra_richiesta_fragment, container, false);
        sc=(ScrollView)view.findViewById(R.id.container);
        queue= Volley.newRequestQueue(view.getContext());

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Dati richiesta");


        Bundle bundle=this.getArguments();
        idr=bundle.getString("idr");
        setGraphics();

        showRequest();





        return view;
    }

    private void setGraphics(){

        mBuilder=new NotificationCompat.Builder(activity).
                setSmallIcon(R.drawable.gmc_img).setContentTitle("Tutored").setContentText("Immagine scaricata");
        mBuilder.setAutoCancel(true);

        nome=(TextView)view.findViewById(R.id.nameRichiesta);
        titolo=(TextView)view.findViewById(R.id.titleRichiesta);
        testo=(TextView)view.findViewById(R.id.testoRichiesta);
        facolta=(TextView)view.findViewById(R.id.facRichiesta);
        uni=(TextView)view.findViewById(R.id.uniRichiesta);
        data=(TextView)view.findViewById(R.id.dataR);

        accetta=(Button)view.findViewById(R.id.buttonAccettaR);
        download=(Button)view.findViewById(R.id.buttonDownload);

        progress=(ProgressBar)view.findViewById(R.id.progressBarRichiesta);


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

       nome.setText("Richiesta di " + nomeR + " " + cognomeR.substring(0, 1) + ".");
        titolo.setText(titoloR);
        testo.setText(testoR);
        data.setText("Entro il " + Functions.convertiData(data_entroR.substring(0, 10)));
        if(uniR.compareTo("0")!=0){
            uni.setText("Università:"+uniR);
        }
        if(facR.compareTo("0")!=0){
            facolta.setText("Facoltà:" + facR);
        }
        if(urlR.compareTo("0")!=0){
             downloadImage();
        }
        else {

            download.setVisibility(View.GONE);

        }


        progress(false);


    }

    private void downloadImage(){



        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress(true);

                downloadBitmap db=new downloadBitmap();
               db.execute("http://www.unishare.it/tutored/" + urlR);

            }
        });

        /*Picasso.with(activity.getApplicationContext()).load("http://www.unishare.it/tutored/" + urlR
        ).into(fotor);

        fotor.setVisibility(View.VISIBLE);
        Target t=new Target(){
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                fotor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zoomImageFromThumb(fotor, bitmap);


                    }
                });

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }


        };
        Picasso.with(activity.getApplicationContext()).load("http://www.unishare.it/tutored/" + urlR
            ).into(t);






        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);*/


    }

    /*private void zoomImageFromThumb(final View thumbView, Bitmap bitmap) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.

        sc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final TouchImageView expandedImageView = (TouchImageView) view.findViewById(
                R.id.expanded_image);
        expandedImageView.setImageBitmap(bitmap);




        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);view.
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);


        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                    sc.setOnTouchListener(null);
                }


                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        sc.setOnTouchListener(null);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                        sc.setOnTouchListener(null);

                    }
                });
                set.start();
                mCurrentAnimator = set;


            }
        });
    }*/



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (HomeTutor) activity;
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
                File storagePath = Environment.getExternalStorageDirectory();
                OutputStream output = new FileOutputStream (storagePath + "/imageRichiesta.jpg");
                try {
                    byte[] buffer = new byte[2048];
                    int bytesRead = 0;
                    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                        output.write(buffer, 0, bytesRead);
                    }
                } finally {
                    output.close();

                    filep=addImageToGallery(storagePath.toString()+ "/imageRichiesta.jpg",activity);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }





            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){

            if(success) {


                NotificationManager nm=(NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);

                //Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(filep).build();




                Intent intent = new Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                PendingIntent contentIntent= PendingIntent.getActivity(activity, 0, intent, 0);
                mBuilder.setContentIntent(contentIntent);
                nm.notify(0,mBuilder.build());


                progress(false);
               Toast.makeText(activity,"Immagine scaricata correttamente",Toast.LENGTH_LONG).show();




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




        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return filePath;
    }

}





