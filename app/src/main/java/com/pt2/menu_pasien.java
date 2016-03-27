package com.pt2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class menu_pasien extends AppCompatActivity implements View.OnClickListener {

    private ImageButton konsumsiObat, CekKonsultasi, RiwayatKesehatan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pasien);
        konsumsiObat = (ImageButton) findViewById(R.id.konsumsi_obat);
        CekKonsultasi = (ImageButton) findViewById(R.id.cek_konsultasi);
        RiwayatKesehatan = (ImageButton) findViewById(R.id.riwayatKesehatan);
        CekKonsultasi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cek_konsultasi :
                startActivity(new Intent(this, lihatKonsultasi.class));
                break;
        }

    }
}
