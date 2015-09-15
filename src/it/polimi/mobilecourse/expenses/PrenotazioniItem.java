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
    private String id;
    private boolean confermato;
    private String durata;

    private String tutorIdfb;
    private String tutorUrl;

    private String tutorNome;
    private String tutorCognome;
    private String studentNome;
    private String studentCognome;

    public String getStudentUrl() {
        return studentUrl;
    }

    public void setStudentUrl(String studentUrl) {
        this.studentUrl = studentUrl;
    }

    public String getStudentIdfb() {
        return studentIdfb;
    }

    public void setStudentIdfb(String studentIdfb) {
        this.studentIdfb = studentIdfb;
    }

    private String studentUrl;
    private String studentIdfb;

    public String getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(String oraInizio) {
        this.oraInizio = oraInizio;
    }

    public String getTutorIdfb() {
        return tutorIdfb;
    }

    public void setTutorIdfb(String tutorFbImage) {
        this.tutorIdfb = tutorFbImage;
    }

    public String getTutorUrl() {
        return tutorUrl;
    }

    public void setTutorUrl(String tutorImageUrl) {
        this.tutorUrl = tutorImageUrl;
    }

    public String getTutorNome() {
        return tutorNome;
    }

    public void setTutorNome(String tutorNome) {
        this.tutorNome = tutorNome;
    }

    public String getTutorCognome() {
        return tutorCognome;
    }

    public void setTutorCognome(String tutorCognome) {
        this.tutorCognome = tutorCognome;
    }

    public String getStudentNome() {
        return studentNome;
    }

    public void setStudentNome(String studentNome) {
        this.studentNome = studentNome;
    }

    public String getStudentCognome() {
        return studentCognome;
    }

    public void setStudentCognome(String studentCognome) {
        this.studentCognome = studentCognome;
    }

    private String oraInizio;

    public PrenotazioniItem(){}


    public PrenotazioniItem(String id, String materia, String data, String cellulare, String durata, boolean confermato) {
        this.materia = materia;
        this.data = data;
        this.cellulare=cellulare;
        this.id = id;
        this.durata = durata;
        this.confermato = confermato;

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

    public String getId() {return id;}

    public boolean isConfermato() {return confermato;}

    public String getDurata() { return durata;
    }
}
