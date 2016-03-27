package com.pt2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class ProfilDokter extends AppCompatActivity {
    private TextView nama, email, noHp, alamat,panggilan;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_dokter);
        nama = (TextView) findViewById(R.id.nama);
        email = (TextView) findViewById(R.id.email);
        noHp = (TextView) findViewById(R.id.kontak);
        alamat = (TextView) findViewById(R.id.alamat);
        panggilan = (TextView) findViewById(R.id.panggilan);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String dokter = user.get("id");
        nama.setText(user.get("first_name")+ " " +user.get("last_name"));
        email.setText(user.get("email"));
        noHp.setText(user.get("kontak"));
        alamat.setText(user.get("alamat"));
        panggilan.setText("DR. "+ user.get("first_name"));

    }
}
