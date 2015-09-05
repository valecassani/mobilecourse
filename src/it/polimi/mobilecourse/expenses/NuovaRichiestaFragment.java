package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


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
public class NuovaRichiestaFragment extends Fragment{
    private final String TAG = "Nuova RichiestaActivity";


    private View view;

    private RequestQueue queue;
    private Context context;
    private HomeStudent activity;
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
    private String titolo;
    private String selectedPath;
    private String nameFile;
    private ImageView imgView;
    private ProgressBar progress;
    private String upLoadServerUri = null;
    private Button photoButton;
    private String path;
    private int serverResponseCode;
    private ImageView expanded;
    private Animator mCurrentAnimator;
    private EditText textTitolo;

    private int mShortAnimationDuration;
    private boolean exist=false;
    private Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_req_frag, container, false);

        upLoadServerUri = "http://www.unishare.it/tutored/upload_to_server.php";

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Nuova richiesta");


        context=activity.getApplicationContext();
        queue= Volley.newRequestQueue(view.getContext());

        Bundle bundle=this.getArguments();
        idStudente=bundle.getString("student_id");

        textTitolo = (EditText)view.findViewById(R.id.titoloRichiesta);

        mTesto = (EditText)view.findViewById(R.id.testoRichiesta);
        mDataEntro = (EditText)view.findViewById(R.id.dataEntro);
        progress=(ProgressBar)view.findViewById(R.id.progressBarNewR);
        sendButton = (Button)view.findViewById(R.id.buttonSendRich);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateDialog();
        imageButton = (Button)view.findViewById(R.id.buttonImage);
        imageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                openGallery(1);
            } });
        photoButton = (Button)view.findViewById(R.id.buttonPhoto);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });





        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (exist == true) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //progressDialog = ProgressDialog.show(activity, "", "Uploading file...", true);

                                    progress(true);

                                    new Thread(new Runnable() {
                                        public void run() {
                                            activity.runOnUiThread(new Runnable() {
                                                public void run() {

                                                }
                                            });

                                            uploadFile upl=new uploadFile();
                                            upl.execute(selectedPath);
                                            //uploadFile(selectedPath);

                                        }
                                    }).start();

                                    manageInput();

                                    //uploadFile upl=new uploadFile();
                                    //upl.execute(selectedPath);
                                    //uploadFile(selectedPath);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };


                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Vuoi inviare?").setPositiveButton("Si", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();




                }
                else{

                    Toast.makeText(context,"Aggiungi una immagine",Toast.LENGTH_LONG).show();
                }

            }

        });





        return view;
    }

    public void openGallery(int req_code) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(galleryIntent, "Select file to upload "), req_code);


    }

    private void manageInput() {
        testo = mTesto.getText().toString();
        titolo = textTitolo.getText().toString();

        try {
            data = new SimpleDateFormat("dd-MM-yyyy").parse(mDataEntro.getText().toString());
            dataToSend = new java.sql.Date(data.getTime());
            Log.i("Nuova Richiesta", "Parse data ok  " + data.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Toast.makeText(context, "Mancano dei dati", Toast.LENGTH_SHORT).show();
        }

        String url = "http://www.unishare.it/tutored/add_richiesta.php";
        if (testo == null || titolo == null || selectedPath == null) {
            Toast.makeText(context,"Hai lasciato dei campi vuoti",Toast.LENGTH_LONG).show();
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
                    params.put("testo", testo);
                    params.put("data", dataToSend.toString());
                    params.put("id_studente", idStudente);
                    params.put("titolo",titolo);
                    //Log.i(TAG, nameFile);
                    params.put("foto","images/"+nameFile);

                    return params;
                }
            };
            queue.add(jsObjRequest);
            Toast.makeText(context, "Richiesta Aggiunta", Toast.LENGTH_SHORT);
            //activity.finish();

        }


    }

    private void setDateDialog(){

        mDataEntro.setInputType(InputType.TYPE_NULL);
        mDataEntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v == mDataEntro) {
                    datePickerDialog.show();
                }

            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {

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
                this.getActivity().finish();
                return true;
                


        }
        return super.onOptionsItemSelected(item);
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));
        startActivityForResult(intent, 2);

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
                bundle.putString("user_id", idStudente);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.student_fragment, fragment).addToBackStack(null).commit();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Richiesta aggiunta correttamente!",
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == activity.RESULT_OK) {

            if (requestCode == 1)

            {
                Uri selectedImageUri = data.getData();
                selectedPath = getRealPathFromURI(selectedImageUri);
                File file = new File(selectedPath);

                if (file.exists()) {
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    //imgView = (ImageView) view.findViewById(R.id.anteprima_immagine);
                    //imgView.setImageBitmap(Bitmap.createBitmap(bitmap));

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
                    //imgView = (ImageView) view.findViewById(R.id.anteprima_immagine);
                    //imgView.setImageBitmap(Bitmap.createBitmap(bitmap));

                    exist=true;
                }

            }


        }

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

    private void zoomImageFromThumb(final View thumbView, Bitmap bitmap) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.



        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) view.findViewById(
                R.id.expanded_imageR);
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
                findViewById(R.id.containerR)
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




}
