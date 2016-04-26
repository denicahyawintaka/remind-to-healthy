package com.pt2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class ProfilDokter extends AppCompatActivity {
    private TextView nama, username,email, noHp, alamat,status,created;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_dokter);
        nama = (TextView) findViewById(R.id.namaLengkap);
        username = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.email);
        noHp = (TextView) findViewById(R.id.kontak);
        alamat = (TextView) findViewById(R.id.alamat);
        status = (TextView) findViewById(R.id.status);
        created = (TextView) findViewById(R.id.created);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String dokter = user.get("id");
        nama.setText(user.get("first_name")+ " " +user.get("last_name"));
        username.setText(user.get("username"));
        email.setText(user.get("email"));
        noHp.setText(user.get("kontak"));
        alamat.setText(user.get("alamat"));
        status.setText(user.get("status"));
        created.setText(user.get("created_at"));

    }
}
