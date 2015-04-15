package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.swipe.SwipeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Valerio on 13/04/2015.
 */
public class PrenotazioniItem {


    private String title;
    private String data;
    private String materia;
    private String cellulare;

    public PrenotazioniItem(){}


    public PrenotazioniItem(String materia, String data, String cellulare) {
        this.materia = materia;
        this.data = data;
        this.cellulare=cellulare;

    }

    public String getTitle() {
        return title;
    }

    public String getData() {
        return data;
    }

    public String getMateria() {
        return materia;
    }

    public String getCellulare() {
        return cellulare;
    }
}
