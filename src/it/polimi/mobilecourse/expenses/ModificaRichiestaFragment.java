package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
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
 * Created by Matteo on 27/08/2015.
 */
public class ModificaRichiestaFragment extends Fragment {

    private View view;
    private HomeStudent activity;
    private RequestQueue queue;
    private String idr;


    private ProgressBar progress;

    private EditText testo;
    private EditText titolo;
    private EditText data;
    private ImageButton img;
    private ImageView expimg;
    private Button save;
    private Date date;

    private Button gallery;
    private Button photo;
    private java.sql.Date dataToSend;
    private String nameFile;
    private int serverResponseCode;
    private String upLoadServerUri = null;



    SimpleDateFormat dateFormatter;
    DatePickerDialog datePickerDialog;
    Bitmap bitmap;
    String path;


    boolean exist=false;

    //dati
    String titoloR;
    String testoR;
    String urlR;
    String id_studente;
    String data_entroR;
    String selectedPath;


    private Animator mCurrentAnimator;

    private int mShortAnimationDuration;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.modifica_richiesta_fragment, container, false);
        upLoadServerUri = "http://www.unishare.it/tutored/upload_to_server.php";

        progress=(ProgressBar)view.findViewById(R.id.progressBarMR);

        queue= Volley.newRequestQueue(view.getContext());

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("La tua richiesta");


        Bundle bundle=this.getArguments();
        idr=bundle.getString("idr");

        getDati();






        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (HomeStudent) activity;
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

    private void getDati(){

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

                            testoR=obj.getString("testo");
                            titoloR=obj.getString("titolo");
                            id_studente=obj.getString("id_studente");

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


        testo=(EditText)view.findViewById(R.id.testoRichiestaMR);
        titolo=(EditText)view.findViewById(R.id.titleRichiestaMR);
        data=(EditText)view.findViewById(R.id.dataMR);
        save=(Button)view.findViewById(R.id.buttonSave);
        photo=(Button)view.findViewById(R.id.buttonPhotoMR);
        gallery=(Button)view.findViewById(R.id.buttonImageMR);
        img=(ImageButton)view.findViewById(R.id.fotoRichiestaMR);

        gallery.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                openGallery(1);
            } });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        expimg=(ImageView)view.findViewById(R.id.expanded_imageMR);

        testo.setText(testoR);
        titolo.setText(titoloR);
        data.setText(Functions.convertiData(data_entroR.substring(0,10)));
        setDateDialog();




        Picasso.with(activity.getApplicationContext()).load("http://www.unishare.it/tutored/" + urlR
        ).into(img);

        img.setVisibility(View.VISIBLE);

        Target t=new Target(){
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zoomImageFromThumb(img, bitmap);



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


        progress(false);



        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }


    private void zoomImageFromThumb(final View thumbView, Bitmap bitmap) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.



        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) view.findViewById(
                R.id.expanded_imageMR);
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
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;

                    }
                });
                set.start();
                mCurrentAnimator = set;


            }
        });
    }

    private void setDateDialog(){

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        data.setInputType(InputType.TYPE_NULL);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v == data) {
                    datePickerDialog.show();
                }

            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                System.out.println(dateFormatter.format(newDate.getTime()));
                data.setText(Functions.convertiDataDialog(dateFormatter.format(newDate.getTime())));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    public void openGallery(int req_code) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(galleryIntent, "Select file to upload "), req_code);


    }

    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File folder = new File(Environment.getExternalStorageDirectory() + "/LoadImg");

        if(!folder.exists())
        {
            folder.mkdir();
        }
        final Calendar c = Calendar.getInstance();
        String new_Date= c.get(Calendar.DAY_OF_MONTH)+"-"+((c.get(Calendar.MONTH))+1)   +"-"+c.get(Calendar.YEAR) +" " + c.get(Calendar.HOUR) + "-" + c.get(Calendar.MINUTE)+ "-"+ c.get(Calendar.SECOND);
        selectedPath=String.format(Environment.getExternalStorageDirectory() +"/%s.jpg","Tutored" +new_Date);
        File photo = new File(selectedPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        startActivityForResult(intent, 2);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == activity.RESULT_OK) {

            if (requestCode == 1)

            {
                Uri selectedImageUri = data.getData();
                selectedPath = getRealPathFromURI(selectedImageUri);
                File file = new File(selectedPath);

                if (file.exists()) {
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    img.setImageBitmap(Bitmap.createBitmap(bitmap));
                    exist=true;
                }

                System.out.println("selectedPath1 : " + selectedPath);


            }

            if(requestCode==2) {
                System.out.println("Selected path from photo: " + path);
                File imgFile = new  File(selectedPath);

                if(imgFile.exists()) {
                    System.out.println("File exists");

                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img.setImageBitmap(Bitmap.createBitmap(bitmap));
                    exist=true;
                }

            }


        }
        if(exist==true) {
            mShortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zoomImageFromThumb(img, bitmap);

                }
            });
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }




    private void sendData(){



        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(data.getText().toString());
            dataToSend = new java.sql.Date(date.getTime());
            Log.i("Nuova Richiesta", "Parse data ok  " + data.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Toast.makeText(activity.getApplicationContext(), "Mancano dei dati", Toast.LENGTH_SHORT);
        }

        String url = "http://www.unishare.it/tutored/update_richiesta.php";
        if (testo.getText().toString().compareTo("")==0 || titolo.getText().toString().compareTo("")==0 ) {
            Toast.makeText(activity.getApplicationContext(),"Hai lasciato dei campi vuoti",Toast.LENGTH_LONG);
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
                    params.put("testo", testo.getText().toString());
                    params.put("data", dataToSend.toString());
                    params.put("idr", idr);
                    params.put("titolo",titolo.getText().toString());
                    params.put("foto","images/"+nameFile);

                    return params;
                }
            };
            queue.add(jsObjRequest);
            Toast.makeText(activity.getApplicationContext(), "Richiesta modificata correttamente", Toast.LENGTH_SHORT);
            //activity.finish();

        }


    }


    private class uploadFile extends AsyncTask<String,Void,Integer> {


        @Override
        protected Integer doInBackground(String... params) {


            String sourceFileUri = params[0];


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

                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);


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



                    /*getActivity().runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    + " http://www.unishare.it/tutored/images/"
                                    + selectedPath;



                        }
                    });*/
                    }

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                    //torna indietro
                    FragmentManager fragmentManager = getFragmentManager();

                    Fragment fragment = new RichiesteFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", id_studente);
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.student_fragment, fragment).addToBackStack(null).commit();
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "Richiesta modificata correttamente!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (MalformedURLException ex) {

                    ex.printStackTrace();

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "MalformedURLException",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {

                    e.printStackTrace();

                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "Got Exception : see logcat ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("Upload Exception", "Exception : "
                            + e.getMessage(), e);
                }
                return serverResponseCode;

            } // End else block
        }

        @Override
        protected void onPostExecute(Integer result){




        }




    }
}



