package com.example.ftn_android_10.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Filmovi.TABLE_NAME_USERS)

public class PogledanFilm {

    public static final String TABLE_NAME_USERS = "pogledani_filmovi";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_NAZIV = "naziv";
    public static final String FIELD_NAME_SLIKA = "poster_path";
    public static final String FIELD_NAME_OPIS = "opis";
    public static final String FIELD_NAME_GODINA = "godina";
    public static final String FIELD_NAME_GLUMCI = "glumci";
    public static final String FIELD_NAME_OCENA = "ocena";
    public static final String FIELD_NAME_TRAJANJE = "trajanje";
    public static final String FIELD_NAME_ZANR = "zanr";
    public static final String FIELD_NAME_JEZIK = "jezik";
    public static final String FIELD_NAME_DRZAVA = "drzava";
    public static final String FIELD_NAME_REZIJA = "rezija";
    public static final String FIELD_NAME_BUDZET = "budzet";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_NAZIV)
    private String naziv;


    @DatabaseField(columnName = FIELD_NAME_SLIKA)
    private String poster_path;
    @DatabaseField(columnName = FIELD_NAME_OPIS)
    private String opis;
    @DatabaseField(columnName = FIELD_NAME_GODINA)
    private String godina;
    @DatabaseField(columnName = FIELD_NAME_GLUMCI)
    private String glumci;

    @DatabaseField(columnName = FIELD_NAME_OCENA)
    private Float ocena;
    @DatabaseField(columnName = FIELD_NAME_TRAJANJE)
    private String trajanje;
    @DatabaseField(columnName = FIELD_NAME_ZANR)
    private String zanr;
    @DatabaseField(columnName = FIELD_NAME_JEZIK)
    private String jezik;
    @DatabaseField(columnName = FIELD_NAME_DRZAVA)
    private String drzava;
    @DatabaseField(columnName = FIELD_NAME_REZIJA)
    private String rezija;
    @DatabaseField(columnName = FIELD_NAME_BUDZET)
    private String budzet;

    public PogledanFilm() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getGodina() {
        return godina;
    }

    public void setGodina(String godina) {
        this.godina = godina;
    }

    public String getGlumci() {
        return glumci;
    }

    public void setGlumci(String glumci) {
        this.glumci = glumci;
    }

    public Float getOcena() {
        return ocena;
    }

    public void setOcena(Float ocena) {
        this.ocena = ocena;
    }

    public String getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }

    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    public String getJezik() {
        return jezik;
    }

    public void setJezik(String jezik) {
        this.jezik = jezik;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getRezija() {
        return rezija;
    }

    public void setRezija(String rezija) {
        this.rezija = rezija;
    }

    public String getBudzet() {
        return budzet;
    }

    public void setBudzet(String budzet) {
        this.budzet = budzet;
    }

    @Override
    public String toString() {
        return naziv;
    }
}
