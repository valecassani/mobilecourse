package it.polimi.mobilecourse.expenses;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Functions {

    static JSONArray getJSONURL(String url) {
        try {
            String json = IOUtils.toString(new URL(url));
            return new JSONArray(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static ArrayList<ObjDb> query(Context context, String url) {
        System.out.println(url);
        if(!checkRete(context)) {
            System.out.println("Problema rete");
            return null;
        }
        ArrayList<ObjDb> result = null;
        result = ObjDb.jsonArrayToObjDBList(getJSONURL(url));
        return result;
    }
    public static boolean checkRete(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infos[] = conMgr.getAllNetworkInfo();
        for (NetworkInfo info : infos) {
            if (info.getState() == State.CONNECTED)
                return true;
        }
        return false;
    }
    public static Bitmap downloadImageFromPath(String path){
        InputStream in =null;
        Bitmap bmp=null;
        int responseCode = -1;
        try{
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                //download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
            }
        }
        catch(Exception ex){
            Log.e("Exception", ex.toString());
        }
        return bmp;
    }


    public static String convertiData(String data){
       String result;
        result=data.substring(8,10);
        String mese=data.substring(5,7);
        switch(mese){

            case "01":
                result=result.concat(" Gennaio ");
                break;
            case "02":
                result=result.concat(" Febbraio ");

                break;
            case "03":
                result=result.concat(" Marzo ");

                break;
            case "04":
                result=result.concat(" Aprile ");

                break;
            case "05":
                result=result.concat(" Maggio ");

                break;
            case "06":
                result=result.concat(" Giugno ");

                break;
            case "07":
                result=result.concat(" Luglio ");

                break;
            case "08":
                result=result.concat(" Agosto ");

                break;
            case "09":
                result=result.concat(" Settembre ");

                break;
            case "10":
                result=result.concat(" Ottobre ");

                break;
            case "11":
                result=result.concat(" Novembre ");

                break;
            case "12":
                result=result.concat(" Dicembre ");

                break;




        }

        result=result.concat(data.substring(0,4));




       return  result;
    }

    public static String convertiDataDialog(String data){
        String result;
        result=data.substring(0,2);
        String mese=data.substring(3,5);
        switch(mese){

            case "01":
                result=result.concat(" Gennaio ");
                break;
            case "02":
                result=result.concat(" Febbraio ");

                break;
            case "03":
                result=result.concat(" Marzo ");

                break;
            case "04":
                result=result.concat(" Aprile ");

                break;
            case "05":
                result=result.concat(" Maggio ");

                break;
            case "06":
                result=result.concat(" Giugno ");

                break;
            case "07":
                result=result.concat(" Luglio ");

                break;
            case "08":
                result=result.concat(" Agosto ");

                break;
            case "09":
                result=result.concat(" Settembre ");

                break;
            case "10":
                result=result.concat(" Ottobre ");

                break;
            case "11":
                result=result.concat(" Novembre ");

                break;
            case "12":
                result=result.concat(" Dicembre ");

                break;




        }

        result=result.concat(data.substring(6,10));




        return  result;
    }
}
