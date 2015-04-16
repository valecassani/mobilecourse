package it.polimi.mobilecourse.expenses;

/**
 * Created by valeriocassani on 15/04/15.
 */
public class SearchTutorItem {
    private String nome;
    private String cognome;

    public SearchTutorItem(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }


    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }


}
