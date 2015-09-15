package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by Matteo on 26/07/2015.
 */
public class UpdateImageFragment extends Fragment {

    private UpdateImage activity;
    private ProgressDialog progressDialog;
    private String nameFile;
    private String upLoadServerUri = null;
    private int serverResponseCode;
    private String path;


    View view;
    String id;
    String tipo;
    Button browse;
    Button upl;
    Button photoButton;
    TextView url;
    ProgressDialog dialog = null;
    String selectedPath;
    int ok;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.update_image_fragment, container, false);
        upLoadServerUri = "http://www.unishare.it/tutored/upload_to_server.php";
        Bundle get=getArguments();
        id=get.getString("id");
        tipo=get.getString("tipo");

        field();

        browse.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                openGallery(1);

            }

        });

            upl.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            progressDialog = ProgressDialog.show(activity, "", "Uploading file...", true);
                                        }
                                    });


                                    /*
                                    new Thread(new Runnable() {
                                        public void run() {

                                            ok=uploadFile(selectedPath);
                                            if(ok==0){
                                                progressDialog.dismiss();
                                            }


                                        }
                                    }).start();
                                    */
                                    Log.i("CIAO","ciao");


                                    UploadFile upl=new UploadFile();
                                    upl.execute(selectedPath);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Confermare l'immagine?").setPositiveButton("Si", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();


                }
            });

            photoButton = (Button) view.findViewById(R.id.photoButton);
            photoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                }
            });




        return view;
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (UpdateImage) activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }



    private void field(){



        url=(TextView) view.findViewById(R.id.urlFoto);
        browse=(Button) view.findViewById(R.id.browseB);
        photoButton=(Button) view.findViewById(R.id.photoButton);
        upl=(Button) view.findViewById(R.id.uplB);



    }






    public void openGallery(int req_code) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

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

        System.out.println(photo.getName());
        nameFile=photo.getName();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        startActivityForResult(intent, 2);
    }

    public int uploadFile(String sourceFileUri) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });


        String fileName = sourceFileUri;
        if(fileName!=null) {
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

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                    }
                });

                Log.e("uploadFile", "Source File not exist :"
                        + selectedPath);


                return 0;

            } else {
                try {
                    nameFile = sourceFile.getName();
                    insertDb();

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


                    }

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                    Toast.makeText(getActivity().getApplicationContext(), "Immagine caricata correttamente.", Toast.LENGTH_LONG).show();

                    getActivity().finish();

                } catch (MalformedURLException ex) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                        }
                    });
                    ex.printStackTrace();


                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                        }
                    });
                    e.printStackTrace();


                    Log.e("Upload Exception", "Exception : "
                            + e.getMessage(), e);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                    }
                });
                return serverResponseCode;

            } // End else block
        }
        else{

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(), "Nessuna immagine selezionata", Toast.LENGTH_LONG).show();

                }
            });


            return 0;
        }

    }

    private class UploadFile extends AsyncTask<String,Void,Integer> {


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
                    insertDb();


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
                    progressDialog.dismiss();
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "Immagine caricata correttamente!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    Intent intent = null;
                    if (tipo.equals("0")) {
                         intent = new Intent(getActivity(),HomeStudent.class);


                    } else {
                        intent = new Intent(getActivity(),HomeTutor.class);

                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id",id);
                    intent.putExtras(bundle);
                    startActivity(intent);



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

    private void insertDb(){

        String url="update_image.php?type_user=".concat(tipo).concat("&id=").concat(id).concat("&url=images/").concat(nameFile.replace(" ","%20"));
        new RequestFtp().setParameters(activity, url, "setDataStudent", UpdateImageFragment.this).execute();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == activity.RESULT_OK) {

            if (requestCode == 1)

            {
                Uri selectedImageUri = data.getData();
                selectedPath = getRealPathFromURI(selectedImageUri);
                url.setText(selectedPath);


                System.out.println("selectedPath1 : " + selectedPath);


            }

            if(requestCode==2) {
                System.out.println("Selected path from photo: " + path);
                File imgFile = new  File(selectedPath);
                url.setText(selectedPath);


                if(imgFile.exists()) {
                    System.out.println("File exists");

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                }

            }


        }
    }



}
