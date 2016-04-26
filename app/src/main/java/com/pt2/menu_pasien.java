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

public class menu_pasien extends ActionBarActivity implements View.OnClickListener {
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    private SQLiteHandler db;
    private SessionManager session;
    private ImageButton konsumsiObat, CekKonsultasi, RiwayatKesehatan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pasien);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        konsumsiObat = (ImageButton) findViewById(R.id.konsumsi_obat);
        CekKonsultasi = (ImageButton) findViewById(R.id.cek_konsultasi);
        RiwayatKesehatan = (ImageButton) findViewById(R.id.riwayatKesehatan);
        CekKonsultasi.setOnClickListener(this);
        RiwayatKesehatan.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cek_konsultasi :
                startActivity(new Intent(this, lihatKonsultasi.class));
                break;
            case R.id.riwayatKesehatan :
                startActivity(new Intent(this, inbox.class));
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
            Intent intent = new Intent(menu_pasien.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
