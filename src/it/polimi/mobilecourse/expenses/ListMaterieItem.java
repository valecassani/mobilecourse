package it.polimi.mobilecourse.expenses;

/**
 * Created by Matteo on 24/08/2015.
 */
public class ListMaterieItem {

    private String id;
    private String nome;
    private String prezzo;



    public ListMaterieItem(String id,String nome, String prezzo) {
        this.id=id;
        this.nome = nome;
        this.prezzo=prezzo;


    }


    public String getNome() {
        return nome;
    }

    public String getPrezzo() {
        return prezzo;
    }


    public String getId() {
        return id;
    }


}
