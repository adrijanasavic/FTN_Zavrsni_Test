package com.example.ftn_android_10.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ftn_android_10.R;
import com.example.ftn_android_10.adapters.FilmoviAdapter;
import com.example.ftn_android_10.db.DatabaseHelper;
import com.example.ftn_android_10.db.model.Filmovi;
import com.example.ftn_android_10.dialog.AboutDialog;
import com.example.ftn_android_10.net.MyService;
import com.example.ftn_android_10.net.model2.Detail;
import com.example.ftn_android_10.settings.SettingsActivity;
import com.example.ftn_android_10.tools.Tools;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ftn_android_10.tools.Tools.NOTIF_CHANNEL_ID;
import static com.example.ftn_android_10.net.MyServiceContract.APIKEY;

public class DetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<String> drawerItems;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout drawerPane;

    private FilmoviAdapter adapterLista;

    private Detail detail;
    private Filmovi film, films;

    private DatePicker datumPicker;
    private TimePicker vremePicker;
//    private RatingBar ocena;

    public static final String DATE_FORMAT = "HH:mm";

    private SharedPreferences prefs;

    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_details );

        setupToolbar();
        fillDataDrawer();
        setupDrawer();

        createNotificationChannel();
        prefs = PreferenceManager.getDefaultSharedPreferences( this );
    }

    private void getDetail(String imdbKey) {
        HashMap<String, String> queryParams = new HashMap<>();

        queryParams.put( "apikey", APIKEY );
        queryParams.put( "i", imdbKey );


        Call<Detail> call = MyService.apiInterface().getMovieData( queryParams );
        call.enqueue( new Callback<Detail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if (response.code() == 200) {
                    Log.d( "REZ", "200" );

                    detail = response.body();
                    if (detail != null) {

                        fillData();
                    }
                }
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText( DetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    public void fillData() {

        ImageView image = findViewById( R.id.detalji_slika );
        Picasso.with( DetailsActivity.this ).load( detail.getPoster() ).into( image );

        TextView title = findViewById( R.id.detalji_naziv );
        title.setText( detail.getTitle() );

        TextView year = findViewById( R.id.detalji_godina );
        year.setText( "(" + detail.getYear() + ")" );

        TextView glumci = findViewById( R.id.detalji_glumci );
        glumci.setText( detail.getActors() );

        TextView runtime = findViewById( R.id.detalji_runtime );
        runtime.setText( detail.getRuntime() );

        TextView genre = findViewById( R.id.detalji_zanr );
        genre.setText( detail.getGenre() );

        TextView language = findViewById( R.id.detalji_jezik );
        language.setText( detail.getLanguage() );

        TextView plot = findViewById( R.id.detalji_plot );
        plot.setText( detail.getPlot() );

        TextView drzava = findViewById( R.id.detalji_drzava );
        drzava.setText( detail.getCountry() );

        TextView awards = findViewById( R.id.detalji_awards );
        awards.setText( detail.getAwards() );

        TextView rezija = findViewById( R.id.detalji_rezija );
        rezija.setText( detail.getDirector() );

        TextView buzdet = findViewById( R.id.detalji_budzet );
        buzdet.setText( detail.getBoxOffice() );

        RatingBar ratingBar = findViewById( R.id.rating_bar );
        ratingBar.setRating( 1 );

        vremePicker = findViewById( R.id.details_timePicker );
        datumPicker = findViewById( R.id.details_dataPicker );

        film = new Filmovi();
        film.setmNaziv( detail.getTitle() );
        film.setmGodina( "(" + detail.getYear() + ")" );
        film.setmGlumac( detail.getActors() );
        film.setmImage( detail.getPoster() );
        film.setmImdbId( detail.getImdbID() );
        film.setmJezik( detail.getLanguage() );
        film.setmDrzava( detail.getCountry() );
        film.setmAwards( detail.getAwards() );
        film.setmRezija( detail.getDirector() );
        film.setmBudzet( detail.getBoxOffice() );

        film.setmVreme( detail.getRuntime() );
        film.setmZanr( detail.getGenre() );
        film.setmPlot( detail.getPlot() );
        film.setmRating( ratingBar.getRating() );
        film.setmDate( (datumPicker.getDayOfMonth()) + "." + (datumPicker.getMonth()) + "." + datumPicker.getYear() + "." );

        String vreme = vremePicker.getCurrentHour() + ":" + vremePicker.getCurrentMinute() + " h";

        film.setmTime( vreme );

        try {
            getDataBaseHelper().getFilmoviDao().create( film );

            String tekstNotifikacije = film.getmNaziv() + " je uspesno dodat u listu  " + "* Moji filmovi * " + "!";

            boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
            boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );


            if (toast) {
                Toast.makeText( DetailsActivity.this, tekstNotifikacije, Toast.LENGTH_LONG ).show();

            }

            if (notif) {
                NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
                NotificationCompat.Builder builder = new NotificationCompat.Builder( DetailsActivity.this, NOTIF_CHANNEL_ID );
                builder.setSmallIcon( android.R.drawable.ic_menu_delete );
                builder.setContentTitle( "Notifikacija" );
                builder.setContentText( tekstNotifikacije );

                Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher_foreground );

                builder.setLargeIcon( bitmap );
                notificationManager.notify( 1, builder.build() );

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        Intent i = new Intent( DetailsActivity.this, MainActivity.class );
//        startActivity( i );


    }

    @Override
    protected void onPause() {
        super.onPause();
        RatingBar ratingBar = findViewById( R.id.rating_bar );

        int id = getIntent().getExtras().getInt( "id", 0 );
        if (id == 0) {
            film.setmRating( ratingBar.getRating() );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                film.setmTime( getCurrentTime() );

            }
            try {
                getDataBaseHelper().getFilmoviDao().update( film );
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            films.setmRating( ratingBar.getRating() );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                film.setmTime( getCurrentTime() );
            }
            try {
                getDataBaseHelper().getFilmoviDao().update( films );

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillDetails();

    }

    public void fillDetails() {

        setTitle( "Detalji Filma" );

        String imdbKey = getIntent().getStringExtra( Tools.KEY );
        if (imdbKey != null) {
            getDetail( imdbKey );
        } else {
            int id = getIntent().getIntExtra( "id", -1 );
            try {

                films = getDataBaseHelper().getFilmoviDao().queryForId( id );

                TextView title = findViewById( R.id.detalji_naziv );
                title.setText( films.getmNaziv() );

                TextView godina = this.findViewById( R.id.detalji_godina );
                godina.setText( films.getmGodina() );
                TextView trajanje = findViewById( R.id.detalji_runtime );
                trajanje.setText( films.getmVreme() );
                TextView zanr = findViewById( R.id.detalji_zanr );
                zanr.setText( films.getmZanr() );
                TextView jezik = findViewById( R.id.detalji_jezik );
                jezik.setText( films.getmJezik() );
                TextView awards = findViewById( R.id.detalji_awards );
                awards.setText( films.getmAwards() );
                TextView plot = findViewById( R.id.detalji_plot );
                plot.setText( films.getmPlot() );

                RatingBar ratingBar = findViewById( R.id.rating_bar );
                ratingBar.setRating( films.getmRating() );

                vremePicker = findViewById( R.id.details_timePicker );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String vreme = vremePicker.getCurrentHour() + ":" + vremePicker.getCurrentMinute() + " h";
                }
                ImageView image = findViewById( R.id.detalji_slika );

                Picasso.with( DetailsActivity.this ).load( films.getmImage() ).into( image );

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static String getCurrentTime() {

        DateFormat dateFormat = new SimpleDateFormat( DATE_FORMAT );
        Date date = new Date();

        return dateFormat.format( date );
    }

    public void deleteFilmove() {

        try {

            ArrayList<Filmovi> filmoviZaBrisanje = (ArrayList<Filmovi>) getDataBaseHelper().getFilmoviDao().queryForAll();
            getDataBaseHelper().getFilmoviDao().delete( filmoviZaBrisanje );

            adapterLista.removeAll();
            adapterLista.notifyDataSetChanged();

            String tekstNotifikacije = "Lista filmova je obrisana!!!";

            boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
            boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );

            if (toast) {
                Toast.makeText( DetailsActivity.this, tekstNotifikacije, Toast.LENGTH_LONG ).show();
            }

            if (notif) {
                NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
                NotificationCompat.Builder builder = new NotificationCompat.Builder( DetailsActivity.this, NOTIF_CHANNEL_ID );
                builder.setSmallIcon( android.R.drawable.ic_menu_delete );
                builder.setContentTitle( "Notifikacija" );
                builder.setContentText( tekstNotifikacije );

                Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher_foreground );

                builder.setLargeIcon( bitmap );
                notificationManager.notify( 1, builder.build() );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        startActivity( new Intent( this, MainActivity.class ) );

    }

    private void fillDataDrawer() {
        drawerItems = new ArrayList<>();
        drawerItems.add( "Moji filmovi" );
        drawerItems.add( "Pretraga filmova" );
        drawerItems.add( "Podesavanje" );
        drawerItems.add( "Obrisi sve filmove" );
        drawerItems.add( "O aplikaciji" );
    }

    private void setupDrawer() {
        drawerList = findViewById( R.id.left_drawer );
        drawerLayout = findViewById( R.id.drawer_layout );
        drawerPane = findViewById( R.id.drawerPane );
        drawerList.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, drawerItems ) );

        drawerList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String title = "Unknown";
                switch (i) {
                    case 0:
                        title = "Moji filmova";
                        // TODO: startActivity( new Intent( DetailsActivity.this, PregledSvihPogledanihFilmova.class ) );
                        break;

                    case 1:
                        title = "Pretraga filmova";
                        startActivity( new Intent( DetailsActivity.this, MainActivity.class ) );
                        break;
                    case 2:
                        title = "Podesavanja";
                        Toast.makeText( getBaseContext(), "Prikaz podesavanja", Toast.LENGTH_SHORT );
                        startActivity( new Intent( DetailsActivity.this, SettingsActivity.class ) );
                        break;
                    case 3:
                        Toast.makeText( getBaseContext(), "Obisi sve filmove", Toast.LENGTH_SHORT );
                        title = "Brisanje filmova";
                        new AlertDialog.Builder( DetailsActivity.this ).setTitle( "Da li zelite da obrisete celu listu filmova?" )
                                .setPositiveButton( "Da", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteFilmove();

                                    }
                                } ).setNegativeButton( "Odustani", null ).show();
                        setTitle( "Obrisana lista  filmova" );

                        break;
                    case 4:
                        AboutDialog dialog = new AboutDialog( DetailsActivity.this );
                        dialog.show();
                        title = "O aplikaciji";
                        break;

                    default:
                        break;
                }
                setTitle( title );
                drawerLayout.closeDrawer( Gravity.LEFT );
            }
        } );

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle( "" );
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle( "" );
                invalidateOptionsMenu();
            }
        };
    }

    public void setupToolbar() {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.setSubtitle( "Detalji filma" );
        toolbar.setLogo( R.drawable.heart );

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator( R.drawable.drawer );
            actionBar.setHomeButtonEnabled( true );
            actionBar.show();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public DatabaseHelper getDataBaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper( this, DatabaseHelper.class );
        }
        return databaseHelper;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Description of My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel( NOTIF_CHANNEL_ID, name, importance );
            channel.setDescription( description );

            NotificationManager notificationManager = getSystemService( NotificationManager.class );
            notificationManager.createNotificationChannel( channel );
        }
    }
}
