package com.example.ftn_android_10.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.ftn_android_10.db.model.Filmovi;
import com.example.ftn_android_10.db.model.PogledanFilm;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "baza.db";

    private static final int DATABASE_VERSION = 1;

    private Dao<Filmovi, Integer> mFilmoviDao = null;
    private Dao<PogledanFilm, Integer> poglenFilmDao = null;


    public DatabaseHelper(Context context) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable( connectionSource, Filmovi.class );
            TableUtils.createTable( connectionSource, PogledanFilm.class );

        } catch (SQLException e) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {


        try {
            TableUtils.dropTable( connectionSource, Filmovi.class, true );
            TableUtils.dropTable( connectionSource, PogledanFilm.class, true );

        } catch (SQLException e) {
            throw new RuntimeException( e );
        }
    }

    public Dao<Filmovi, Integer> getFilmoviDao() throws SQLException {
        if (mFilmoviDao == null) {
            mFilmoviDao = getDao( Filmovi.class );
        }

        return mFilmoviDao;
    }

    public Dao<PogledanFilm, Integer> getPogledanFilmDao() throws SQLException {
        if (poglenFilmDao == null) {
            poglenFilmDao = getDao( PogledanFilm.class );
        }

        return poglenFilmDao;
    }

    @Override
    public void close() {
        mFilmoviDao = null;
        poglenFilmDao = null;

        super.close();
    }
}
