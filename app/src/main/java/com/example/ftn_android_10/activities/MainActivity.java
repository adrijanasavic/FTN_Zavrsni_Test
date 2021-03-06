package com.example.ftn_android_10.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.ftn_android_10.net.MyService;
import com.example.ftn_android_10.net.model1.Search;
import com.example.ftn_android_10.net.model1.SearchResult;
import com.example.ftn_android_10.settings.SettingsActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ftn_android_10.net.MyServiceContract.APIKEY;
import static com.example.ftn_android_10.tools.Tools.KEY;
import static com.example.ftn_android_10.tools.Tools.NOTIF_CHANNEL_ID;

public class MainActivity extends AppCompatActivity implements SearchAdapter.OnItemClickListener {

    private Toolbar toolbar;
    private ArrayList<String> drawerItems;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout drawerPane;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;

    private ImageButton btnSearch;
    private EditText movieName;

    private FilmoviAdapter adapterLista;

    private SharedPreferences prefs;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        setupToolbar();
        fillDataDrawer();
        setupDrawer();

        fillData();

        createNotificationChannel();
        prefs = PreferenceManager.getDefaultSharedPreferences( this );

    }

    private void getMovieByName(String name) {
        Map<String, String> query = new HashMap<>();
        query.put( "apikey", APIKEY );
        query.put( "s", name.trim() );

        Call<SearchResult> call = MyService.apiInterface().getMovieByName( query );
        call.enqueue( new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {

                if (response.code() == 200) {
                    try {
                        SearchResult searches = response.body();

                        ArrayList<Search> search = new ArrayList<>();

                        for (Search e : searches.getSearch()) {

                            if (e.getType().equals( "movie" ) || e.getType().equals( "series" )) {
                                search.add( e );
                            }
                        }

                        layoutManager = new LinearLayoutManager( MainActivity.this );
                        recyclerView.setLayoutManager( layoutManager );

                        adapter = new SearchAdapter( MainActivity.this, search, MainActivity.this );
                        recyclerView.setAdapter( adapter );

                        Toast.makeText( MainActivity.this, "Prikaz filmova.", Toast.LENGTH_SHORT ).show();

                    } catch (NullPointerException e) {
                        Toast.makeText( MainActivity.this, "Ne postoji film sa tim nazivom", Toast.LENGTH_SHORT ).show();
                    }

                } else {

                    Toast.makeText( MainActivity.this, "Greska sa serverom", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Toast.makeText( MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT ).show();

            }
        } );
    }

    public void deleteFilmove() {

        try {

            ArrayList<Filmovi> filmoviZaBrisanje = (ArrayList<Filmovi>) getDataBaseHelper().getFilmoviDao().queryForAll();
            getDataBaseHelper().getFilmoviDao().delete( filmoviZaBrisanje );

            adapterLista.removeAll();
            adapterLista.notifyDataSetChanged();

            String tekstNotifikacije = "Lista mojih filmova je obrisana!!!";

            boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
            boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );

            if (toast) {
                Toast.makeText( MainActivity.this, tekstNotifikacije, Toast.LENGTH_LONG ).show();
            }

            if (notif) {
                NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
                NotificationCompat.Builder builder = new NotificationCompat.Builder( MainActivity.this, NOTIF_CHANNEL_ID );
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

    public void fillData() {
        btnSearch = findViewById( R.id.btn_search );
        movieName = findViewById( R.id.ime_filma );
        recyclerView = findViewById( R.id.rvList );

        btnSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovieByName( movieName.getText().toString().trim() );
            }
        } );
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
                        startActivity( new Intent( MainActivity.this, PregledSvihPogledanihFilmova.class ) );
                        break;

                    case 1:
                        title = "Pretraga filmova";
                        startActivity( new Intent( MainActivity.this, MainActivity.class ) );
                        break;
                    case 2:
                        title = "Podesavanja";
                        Toast.makeText( getBaseContext(), "Prikaz podesavanja", Toast.LENGTH_SHORT );
                        startActivity( new Intent( MainActivity.this, SettingsActivity.class ) );
                        break;
                    case 3:
                        Toast.makeText( getBaseContext(), "Obisi sve filmove", Toast.LENGTH_SHORT );
                        title = "Brisanje filmova";
                        new AlertDialog.Builder( MainActivity.this ).setTitle( "Da li zelite da obrisete celu listu filmova?" )
                                .setPositiveButton( "Da", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteFilmove();

                                    }
                                } ).setNegativeButton( "Odustani", null ).show();
                        setTitle( "Obrisana lista  filmova" );

                        break;
                    case 4:
                        AboutDialog dialog = new AboutDialog( MainActivity.this );
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
        toolbar.setSubtitle( "Pretraga filmova" );
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

    @Override
    public void onItemClick(int position) {

        Search movie = adapter.get( position );

        Intent i = new Intent( this, DetailsActivity.class );
        i.putExtra( KEY, movie.getImdbID() );
        startActivity( i );
    }
}
