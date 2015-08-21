package it.polimi.mobilecourse.expenses;

/**
 * Created by Matteo on 16/08/2015.
 */
public class ListTutorItem {

    private String nome;
    private String cognome;
    private String id;



    private String uni;
    private String media;



    private String url;


    private String idfb;

    public ListTutorItem(String nome, String cognome, String id,String uni,String media,String url,String idfb) {
        this.nome = nome;
        this.cognome = cognome;
        this.id = id;
        this.uni=uni;
        this.media=media;
        this.url=url;
        this.idfb=idfb;
    }


    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getId() {return id;}

    public String getUni() {
        return uni;
    }


    public float getMedia() {



        return Float.parseFloat(media);
    }

    public String getUrl() {
        return url;
    }

    public String getIdfb(){
        return idfb;
    }








}



