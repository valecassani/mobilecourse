package it.polimi.mobilecourse.expenses;

/**
 * Created by valeriocassani on 15/04/15.
 */
public class SearchTutorItem {
    private String nome;
    private String cognome;
    private String id;

    public SearchTutorItem(String nome, String cognome, String id) {
        this.nome = nome;
        this.cognome = cognome;
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getId() {return id;}


}
