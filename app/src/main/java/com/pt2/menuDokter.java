package com.pt2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class menuDokter extends ActionBarActivity implements View.OnClickListener {
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    private SQLiteHandler db;
    private SessionManager session;
    private ImageButton profil, inputKonsultasi, lihatpasien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dokter);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());


        profil = (ImageButton) findViewById(R.id.profil);
        inputKonsultasi = (ImageButton) findViewById(R.id.inputKonsultasi);
        lihatpasien = (ImageButton) findViewById(R.id.lihatPasien);
        inputKonsultasi.setOnClickListener(this);
        profil.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.inputKonsultasi :
                startActivity(new Intent(this, inputKonsultasiActivity.class));
                break;
            case R.id.profil :
                startActivity(new Intent(this, ProfilDokter.class));
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            session.setLogin(false);

            db.deleteUsers();

            // Launching the login activity
            Intent intent = new Intent(menuDokter.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
