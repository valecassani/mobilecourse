package it.polimi.mobilecourse.expenses;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Created by valeriocassani on 14/05/15.
 */
public class DownloadImage extends AsyncTask<String,Void,Boolean> {
    Bitmap bmp = null;
    ImageView img = null;

    public DownloadImage(ImageView img) {
        this.img = img;
    }



    @Override
    protected Boolean doInBackground(String ...params) {



        try {

            bmp=Functions.downloadImageFromPath("http://www.unishare.it/tutored/images/"+params[0]);

        }
        catch(Exception exception){
            System.out.println("Error "+exception.getMessage());

        }

        return true;

    }


    @Override
    protected void onPostExecute(final Boolean success){

        if(success){

            setImage();



        }

    }

    public void setImage(){
        img.setImageBitmap(bmp);
        System.out.println("Immagine settata");
    }



}
