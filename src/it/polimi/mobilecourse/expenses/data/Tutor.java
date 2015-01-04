package it.polimi.mobilecourse.expenses.data;

import java.util.ArrayList;

import it.polimi.mobilecourse.expenses.ObjDb;

/**
 * Created by valeriocassani on 04/01/15.
 */
public class Tutor {
    
    private String username;
    private String name;
    private String surname;
    private String occupazione;
    private String facolta;
    private String age;
    private String cellulare;
    private String indirizzo;
    private int iduniversità;
    private int idcitta;
    private boolean gruppo;
    private boolean gratis;
    private boolean sede_propria;
    private boolean domicilio;

    public Tutor(ArrayList<ObjDb> data){
        for (ObjDb d :data) {
            this.username = d.get("username");
            this.age = d.get("eta");
            //this.cellulare = d.get("cellulare");
            this.name = d.get("nome");
            //this.surname = d.get("cognome");
            this.facolta = d.get("facolta");
            this.indirizzo = d.get("indirizzo");
            this.occupazione = d.get("occupazione");
            
            

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

    public String getOccupazione() {
        return occupazione;
    }

    public void setOccupazione(String occupazione) {
        this.occupazione = occupazione;
    }

    public String getFacolta() {
        return facolta;
    }

    public void setFacolta(String facolta) {
        this.facolta = facolta;
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

    public int getIdcitta() {
        return idcitta;
    }

    public void setIdcitta(int idcitta) {
        this.idcitta = idcitta;
    }

    public boolean isGruppo() {
        return gruppo;
    }

    public void setGruppo(boolean gruppo) {
        this.gruppo = gruppo;
    }

    public boolean isGratis() {
        return gratis;
    }

    public void setGratis(boolean gratis) {
        this.gratis = gratis;
    }

    public boolean isSede_propria() {
        return sede_propria;
    }

    public void setSede_propria(boolean sede_propria) {
        this.sede_propria = sede_propria;
    }

    public boolean isDomicilio() {
        return domicilio;
    }

    public void setDomicilio(boolean domicilio) {
        this.domicilio = domicilio;
    }
}
