package com.pt2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class menuDokter extends AppCompatActivity implements View.OnClickListener {

    private ImageButton profil, inputKonsultasi, lihatpasien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dokter);
        profil = (ImageButton) findViewById(R.id.profil);
        inputKonsultasi = (ImageButton) findViewById(R.id.inputKonsultasi);
        lihatpasien = (ImageButton) findViewById(R.id.lihatPasien);
        inputKonsultasi.setOnClickListener(this);
        profil.setOnClickListener(this);
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
}
