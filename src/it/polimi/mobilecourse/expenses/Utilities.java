package it.polimi.mobilecourse.expenses;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Utilities {

    static JSONArray getJSONFromURL(String url) {
        try {
            String json = IOUtils.toString(new URL(url));
            return new JSONArray(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static ArrayList<Entity> queryDatabase(Context context, String url) {
        System.out.println(url);
        if(!checkNetworkState(context)) {
            return null;
        }
        ArrayList<Entity> result = null;
        result = Entity.jsonArrayToEntityList(getJSONFromURL(url));
        return result;
    }
    public static boolean checkNetworkState(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infos[] = conMgr.getAllNetworkInfo();
        for (NetworkInfo info : infos) {
            if (info.getState() == State.CONNECTED)
                return true;
        }
        return false;
    }
}
