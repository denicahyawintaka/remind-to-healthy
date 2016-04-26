package com.pt2;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DeniCahya on 2/24/2016.
 */
public class inputKonsultasiActivity extends Activity {
    private static final String TAG = inputKonsultasiActivity.class.getSimpleName();
    private Spinner spinner, spinnerKategoriPenyakit,namaPenyakit;
    private EditText username,dosisObat;
    private boolean refresh=false;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private Button submit;
    private ImageButton add;
    LinearLayout linearLayout;
    private EditText namaObat, jumlahHari, intervalEdt;
    ArrayList<EditText> edtList = new ArrayList<EditText>();
    Thread timerThread;
    boolean aa = false;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_konsultasi);
        spinnerKategoriPenyakit = (Spinner) findViewById(R.id.kategoriPenyakit);
        username = (EditText) findViewById(R.id.username);
        namaObat = (EditText) findViewById(R.id.namaObat);
        jumlahHari = (EditText) findViewById(R.id.jumlahHari);
        intervalEdt = (EditText) findViewById(R.id.interval);
        dosisObat = (EditText) findViewById(R.id.Dosis);
        namaPenyakit = (Spinner) findViewById(R.id.namaPenyakit);
        radioGroup = (RadioGroup) findViewById(R.id.radioGrup);
        submit = (Button) findViewById(R.id.btnSubmit);
        add = (ImageButton) findViewById(R.id.btnAdd);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        linearLayout = (LinearLayout) findViewById(R.id.linearSaran);
        pDialog.setCancelable(false);
        spinnerKategoriPenyakit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                KategoriPenyakit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        createEditSaran();



        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createEditSaran();
            }

        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String pasien = username.getText().toString();
                HashMap<String, String> user = db.getUserDetails();
                String dokter = user.get("id");
                String obat = namaObat.getText().toString();
                String nama_penyakit = namaPenyakit.getSelectedItem().toString();
                String jumlah = jumlahHari.getText().toString();
                String interval = intervalEdt.getText().toString();
                String dosis = dosisObat.getText().toString();
                int selectId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectId);
                String penggunaan = radioButton.getText().toString();
                ArrayList<String> etText = new ArrayList<String>();
                for(EditText et : edtList){
                    String settext = et.getText().toString();
                    etText.add(settext);
                }
                if (!pasien.isEmpty() && !nama_penyakit.isEmpty() ) {
                    registerUser(pasien, dokter, nama_penyakit, etText, jumlah, interval, obat,dosis,penggunaan);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }
    private void createEditSaran(){
        EditText edtView = new EditText(inputKonsultasiActivity.this);
        edtView.setHint("Masukkan Saran");
        edtView.setPadding(20, 20, 20, 20);
        edtView.setTextColor(Color.rgb(34, 34, 34));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 20);
        edtView.setLayoutParams(layoutParams);
        edtView.setHintTextColor(Color.rgb(153, 153, 153));
        edtView.setBackgroundColor(Color.WHITE);
        linearLayout.addView(edtView);
        edtList.add(edtView);
    }
    private void KategoriPenyakit(){
        String kategori= spinnerKategoriPenyakit.getSelectedItem().toString();
        if(kategori.equalsIgnoreCase("penyakit infeksi")) {
            ArrayAdapter adapter = ArrayAdapter.createFromResource(
                    this, R.array.infeksi, android.R.layout.simple_spinner_item);
            namaPenyakit.setAdapter(adapter);
        }
        else if(kategori.equalsIgnoreCase("penyakit rongga dada")){
            ArrayAdapter adapter = ArrayAdapter.createFromResource(
                    this, R.array.ronggaDada, android.R.layout.simple_spinner_item);
            namaPenyakit.setAdapter(adapter);
        }
    }

    private void registerUser(final String pasien, final String dokter,
                              final String nama_penyakit, final ArrayList<String> saranList, final String jumlah,
                              final String interval,  final String obat, final String dosis, final String penggunaan) {
        String tag_string_req = "req_konsultasi";

        pDialog.setMessage("MENGINPUTKAN ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_KONSULTASI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                Log.d(TAG, "Konsultasi Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        /*
                        String id = jObj.getString("id");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, id, created_at);
*/

                        Toast.makeText(getApplicationContext(), "SUKSES DIINPUTKAN", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                inputKonsultasiActivity.this,
                                DokterActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Konsultasi Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("pasien", pasien);
                params.put("dokter", dokter);
                params.put("namaPenyakit", nama_penyakit);
                int i=0;
                for(String et : saranList){
                    params.put("saran["+(i++)+"]", et);
                }
                params.put("jumlah", jumlah);
                params.put("interval", interval);
                params.put("namaObat", obat);
                params.put("dosis", dosis);
                params.put("penggunaan", penggunaan);

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);


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

