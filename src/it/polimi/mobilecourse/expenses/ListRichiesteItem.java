package it.polimi.mobilecourse.expenses;

/**
 * Created by Matteo on 16/08/2015.
 */

// richieste nella home del tutor
public class ListRichiesteItem {

    private String id_studente;
    private String nome;
    private String cognome;
    private String data_entro;


    private String titolo;
    private String url;
    private String idfb;



    private String id;

    public ListRichiesteItem(String id,String id_studente, String nome, String cognome,String data_entro,String titolo,String uni,String url,String idfb) {
        this.id=id;
        this.id_studente = id_studente;
        this.nome = nome;
        this.cognome=cognome;
        this.data_entro = data_entro;
        this.titolo=titolo;
        this.url=url;
        this.idfb=idfb;


    }


    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }





    public String getTitolo() {
        return titolo;
    }

    public String getData_entro() {
        return data_entro;
    }


    public String getUrl() {
        return url;
    }

    public String getIdfb(){
        return idfb;
    }

    public String getId() {
        return id;
    }






}