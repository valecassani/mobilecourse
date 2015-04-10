package it.polimi.mobilecourse.expenses;

import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;

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
        if(context!=null) {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo infos[] = conMgr.getAllNetworkInfo();
            for (NetworkInfo info : infos) {
                if (info.getState() == State.CONNECTED)
                    return true;
            }
        }
        return false;
    }
}
