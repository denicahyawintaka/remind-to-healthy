package com.pt2;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class lihatKonsultasi extends AppCompatActivity {
    private static final String TAG = lihatKonsultasi.class.getSimpleName();
    private TextView username;
    private TextView namaPenyakitTxt,tanggalKonsul,idKonsultasi;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private   LinearLayout linearLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_konsultasi);
        username = (TextView) findViewById(R.id.username);
        namaPenyakitTxt = (TextView) findViewById(R.id.namaPenyakit);
        tanggalKonsul = (TextView) findViewById(R.id.tanggalKonsultasi);
        idKonsultasi = (TextView) findViewById(R.id.idKonsultasi);
        linearLayout = (LinearLayout) findViewById(R.id.dataKonsultasi);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> konsultasi = db.getKonsultasi();
        String pasien = konsultasi.get("id_konsultasi");
        checkKonsultasi(pasien);
    }


    private void checkKonsultasi(final String pasien) {
        // Tag used to cancel the request
        String tag_string_req = "req_lihatKonsultasi";
        System.out.println(pasien);
        pDialog.setMessage("TUNGGU BEBERAPA SAAT...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_lihatKosultasi, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Konsultasi Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in

                        // Now store the user in SQLite
                        JSONObject konsultasi = jObj.getJSONObject("konsultasi");
                        String idDokter = konsultasi.getString("namaDokter");
                        String namaPenyakit = konsultasi.getString("namaPenyakit");
                        JSONArray saranList = konsultasi.getJSONArray("saranDokter");
                        String tanggal = konsultasi.getString("tanggal");
                        String idKon= konsultasi.getString("idKonsultasi");

                        /*
                        JSONObject obat = jObj.getJSONObject("obat");
                        String idObat = obat.getString("idObat");
                        String idKonsultasi = obat.getString("idKonsultasi");
                        String namaObat = obat.getString("namaObat");
                        String frekuensi = obat.getString("frekuensi");
                        String interval = obat.getString("interval");
                        db.addObat(idObat,idKonsultasi,namaObat,frekuensi,interval);
                        */

                        username.setText("DR. "+idDokter);
                        namaPenyakitTxt.setText(namaPenyakit);
                        tanggalKonsul.setText(tanggal);
                        idKonsultasi.setText("Nomor Konsultasi : "+idKon);

                        for(int i=0;i<saranList.length();i++){
                            createEditSaran(saranList.getString(i));
                        }



                            /*Intent intent = new Intent(lihatKonsultasi.this,
                                    LoginActivity.class);
                            startActivity(intent);
                            finish();*/
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("pasien", pasien);

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    private void createEditSaran(String saranDokter){
        TextView txtView = new TextView(lihatKonsultasi.this);
        txtView.setPadding(20, 20, 20, 20);
        txtView.setTextColor(Color.rgb(34, 34, 34));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 20);
        txtView.setLayoutParams(layoutParams);
        txtView.setBackgroundColor(Color.WHITE);
        txtView.setText(saranDokter);
        linearLayout.addView(txtView);

       // edtList.add(edtView);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}