package com.example.ftn_android_10.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ftn_android_10.R;
import com.example.ftn_android_10.adapters.FilmoviAdapter;
import com.example.ftn_android_10.adapters.SearchAdapter;
import com.example.ftn_android_10.db.DatabaseHelper;
import com.example.ftn_android_10.db.model.Filmovi;
import com.example.ftn_android_10.dialog.AboutDialog;
import com.example.ftn_android_10.settings.SettingsActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.ftn_android_10.tools.Tools.NOTIF_CHANNEL_ID;

public class DetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<String> drawerItems;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout drawerPane;

    private FilmoviAdapter adapterLista;

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

    public void deleteFilmove() {

        try {

            ArrayList<Filmovi> filmoviZaBrisanje = (ArrayList<Filmovi>) getDataBaseHelper().getFilmoviDao().queryForAll();
            getDataBaseHelper().getFilmoviDao().delete( filmoviZaBrisanje );

            adapterLista.removeAll();
            adapterLista.notifyDataSetChanged();

            String tekstNotifikacije = "Lista filmova je obrisana";

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
