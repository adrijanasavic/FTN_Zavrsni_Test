package com.example.ftn_android_10.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ftn_android_10.R;
import com.example.ftn_android_10.dialog.AboutDialog;
import com.example.ftn_android_10.settings.SettingsActivity;

import java.util.ArrayList;

public class PregledSvihPogledanihFilmova extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<String> drawerItems;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout drawerPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_pregled_svih_pogledanih_filmova );

        setupToolbar();
        fillDataDrawer();
        setupDrawer();
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
                        startActivity( new Intent( PregledSvihPogledanihFilmova.this, PregledSvihPogledanihFilmova.class ) );
                        break;

                    case 1:
                        title = "Pretraga filmova";
                        startActivity( new Intent( PregledSvihPogledanihFilmova.this, MainActivity.class ) );
                        break;
                    case 2:
                        title = "Podesavanja";
                        Toast.makeText( getBaseContext(), "Prikaz podesavanja", Toast.LENGTH_SHORT );
                        startActivity( new Intent( PregledSvihPogledanihFilmova.this, SettingsActivity.class ) );
                        break;
                    case 3:
                        Toast.makeText( getBaseContext(), "Obisi sve filmove", Toast.LENGTH_SHORT );
                        title = "Brisanje filmova";
                        new AlertDialog.Builder( PregledSvihPogledanihFilmova.this ).setTitle( "Da li zelite da obrisete celu listu filmova?" )
                                .setPositiveButton( "Da", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //TODO: deleteFilmove();

                                    }
                                } ).setNegativeButton( "Odustani", null ).show();
                        setTitle( "Obrisana lista  filmova" );

                        break;
                    case 4:
                        AboutDialog dialog = new AboutDialog( PregledSvihPogledanihFilmova.this );
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
        toolbar.setSubtitle( "Moji filmovi" );
        toolbar.setLogo( R.drawable.heart );

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator( R.drawable.drawer );
            actionBar.setHomeButtonEnabled( true );
            actionBar.show();
        }
    }
}