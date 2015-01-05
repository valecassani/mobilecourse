package it.polimi.mobilecourse.expenses.data;

import java.util.ArrayList;

import it.polimi.mobilecourse.expenses.ObjDb;

/**
 * Created by Valerio on 03/01/2015.
 * classe che fa il parsing JSON dei dati utente a partire dalla query in user_data.php
 *
 * sui setter da aggiungere l'update nel database
 *
 */
public class Student {

    private String username;
    private String name;
    private String surname;
    private String age;
    private String cellulare;
    private String indirizzo;
    private int iduniversità;
    private int idcittia;

    public Student(ArrayList<ObjDb> data){
        for (ObjDb d :data) {
           // if (d.get("username")!=null)
                this.username = d.get("username");
            //if (d.get("eta")!=null)
                this.age = d.get("eta");
           // if (d.get("cellulare")!=null)
                this.cellulare = d.get("cellulare");
            //if (d.get("nome")!=null)
                this.name = d.get("nome");
            //if (d.get("cognome")!=null)
                this.surname = d.get("cognome");

        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCellulare() {
        return cellulare;
    }

    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public int getIduniversità() {
        return iduniversità;
    }

    public void setIduniversità(int iduniversità) {
        this.iduniversità = iduniversità;
    }

    public int getIdcittia() {
        return idcittia;
    }

    public void setIdcittia(int idcittia) {
        this.idcittia = idcittia;
    }
}
