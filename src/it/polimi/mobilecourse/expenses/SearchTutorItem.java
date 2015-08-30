package it.polimi.mobilecourse.expenses;

/**
 * Created by valeriocassani on 15/04/15.
 */
public class SearchTutorItem {
    private String nome;
    private String cognome;
    private String id;
    private String media;



    private String url;
    private String idfb;
    private String uni;

    public SearchTutorItem(String nome, String cognome, String id,String uni,String media,String url,String idfb) {
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

    public String getUrl() {
        return url;
    }

    public float getMedia() {



        return Float.parseFloat(media);
    }
    public String getIdfb() {
        return idfb;
    }

    public String getUni() {
        return uni;
    }


}
