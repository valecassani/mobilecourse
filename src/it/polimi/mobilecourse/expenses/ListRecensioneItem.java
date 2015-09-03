package it.polimi.mobilecourse.expenses;

/**
 * Created by Matteo on 02/09/2015.
 */
public class ListRecensioneItem {

    private String nome;
    private String cognome;
    private String commento;


    private String idstudente;
    private String foto;
    private String idfb;


    private Float puntualita;
    private Float disponibilita;
    private Float chiarezza;



    private Float voto_finale;










    public ListRecensioneItem(String idstudente,String nome, String cognome,Float puntualita,Float disponibilita,Float chiarezza,Float voto_finale,String foto,String idfb) {

        this.idstudente = idstudente;
        this.nome = nome;
        this.foto=foto;
        this.idfb=idfb;
        this.cognome=cognome;
        this.puntualita = puntualita;
        this.disponibilita=disponibilita;
        this.chiarezza=chiarezza;
        this.voto_finale=voto_finale;



    }

    public String getIdstudente() {
        return idstudente;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCommento() {
        return commento;
    }

    public Float getPuntualita() {
        return puntualita;
    }

    public Float getDisponibilita() {
        return disponibilita;
    }

    public Float getChiarezza() {
        return chiarezza;
    }

    public Float getVoto_finale() {
        return voto_finale;
    }

    public String getFoto() {
        return foto;
    }

    public String getIdfb() {
        return idfb;
    }

}
