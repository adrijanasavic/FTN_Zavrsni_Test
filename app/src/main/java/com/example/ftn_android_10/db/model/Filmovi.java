package com.example.ftn_android_10.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Filmovi.TABLE_NAME_USERS)
public class Filmovi {

    public static final String TABLE_NAME_USERS = "filmovi";

    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_NAZIV = "naziv";
    public static final String FIELD_NAME_GLUMAC = "gumac";
    public static final String FIELD_NAME_GODINA = "godina";
    public static final String FIELD_NAME_VREME = "vreme";
    public static final String FIELD_NAME_IMAGE = "image";
    public static final String FIELD_NAME_IMDBID = "imdb_id";
    public static final String FIELD_NAME_ZANR = "zanr";
    public static final String FIELD_NAME_JEZIK = "jezik";
    public static final String FIELD_NAME_AWARDS = "awards";
    public static final String FIELD_NAME_PLOT = "plot";
    public static final String FIELD_NAME_DRZAVA = "drzva";
    public static final String FIELD_NAME_REZIJA = "rezija";
    public static final String FIELD_NAME_BUDZET = "budzet";

    public static final String FIELD_NAME_RATING = "rating";
    public static final String FIELD_NAME_TIME = "time";
    public static final String FIELD_NAME_DATE = "date";


    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_NAZIV)
    private String mNaziv;

    @DatabaseField(columnName = FIELD_NAME_GLUMAC)
    private String mGlumac;

    @DatabaseField(columnName = FIELD_NAME_GODINA)
    private String mGodina;

    @DatabaseField(columnName = FIELD_NAME_VREME)
    private String mVreme;

    @DatabaseField(columnName = FIELD_NAME_IMAGE)
    private String mImage;

    @DatabaseField(columnName = FIELD_NAME_IMDBID)
    private String mImdbId;

    @DatabaseField(columnName = FIELD_NAME_ZANR)
    private String mZanr;

    @DatabaseField(columnName = FIELD_NAME_JEZIK)
    private String mJezik;

    @DatabaseField(columnName = FIELD_NAME_AWARDS)
    private String mAwards;

    @DatabaseField(columnName = FIELD_NAME_PLOT)
    private String mPlot;

    @DatabaseField(columnName = FIELD_NAME_DRZAVA)
    private String mDrzava;

    @DatabaseField(columnName = FIELD_NAME_REZIJA)
    private String mRezija;

    @DatabaseField(columnName = FIELD_NAME_BUDZET)
    private String mBudzet;

    @DatabaseField(columnName = FIELD_NAME_RATING)
    private Float mRating;

    @DatabaseField(columnName = FIELD_NAME_TIME)
    private String mTime;

    @DatabaseField(columnName = FIELD_NAME_DATE)
    private String mDate;

    public Filmovi() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmNaziv() {
        return mNaziv;
    }

    public void setmNaziv(String mNaziv) {
        this.mNaziv = mNaziv;
    }

    public String getmGlumac() {
        return mGlumac;
    }

    public void setmGlumac(String mGlumac) {
        this.mGlumac = mGlumac;
    }

    public String getmGodina() {
        return mGodina;
    }

    public void setmGodina(String mGodina) {
        this.mGodina = mGodina;
    }

    public String getmVreme() {
        return mVreme;
    }

    public void setmVreme(String mVreme) {
        this.mVreme = mVreme;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmImdbId() {
        return mImdbId;
    }

    public void setmImdbId(String mImdbId) {
        this.mImdbId = mImdbId;
    }

    public String getmZanr() {
        return mZanr;
    }

    public void setmZanr(String mZanr) {
        this.mZanr = mZanr;
    }

    public String getmJezik() {
        return mJezik;
    }

    public void setmJezik(String mJezik) {
        this.mJezik = mJezik;
    }

    public String getmAwards() {
        return mAwards;
    }

    public void setmAwards(String mAwards) {
        this.mAwards = mAwards;
    }

    public String getmPlot() {
        return mPlot;
    }

    public void setmPlot(String mPlot) {
        this.mPlot = mPlot;
    }

    public String getmDrzava() {
        return mDrzava;
    }

    public void setmDrzava(String mDrzava) {
        this.mDrzava = mDrzava;
    }

    public String getmRezija() {
        return mRezija;
    }

    public void setmRezija(String mRezija) {
        this.mRezija = mRezija;
    }

    public String getmBudzet() {
        return mBudzet;
    }

    public void setmBudzet(String mBudzet) {
        this.mBudzet = mBudzet;
    }

    public Float getmRating() {
        return mRating;
    }

    public void setmRating(Float mRating) {
        this.mRating = mRating;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}

