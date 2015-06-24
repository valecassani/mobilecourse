package it.polimi.mobilecourse.expenses;

import java.sql.Date;

/**
 * Created by valeriocassani on 16/04/15.
 */
public class RichiestaItem {

    private String idStudente;
    private String id;
    private String testo;
    private String data;
    private String foto;
    private String titolo;

    public RichiestaItem(String id, String testo, String data, String foto, String titolo) {
        this.id = id;
        this.foto = foto;
        this.testo = testo;
        this.data = data;
        this.titolo = titolo;
        this.idStudente = idStudente;
    }

    public String getIdStudente() {
        return idStudente;
    }

    public String getId() { return id; }

    public String getTesto() {
        return testo;
    }

    public String getData() {
        return data;
    }

    public String getTitolo() {return titolo;}

    public String getFoto() {
        return foto;
    }
}
