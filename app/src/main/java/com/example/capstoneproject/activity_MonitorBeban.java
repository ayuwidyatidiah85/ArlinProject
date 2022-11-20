package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class activity_MonitorBeban extends AppCompatActivity {

    private TextView textview_nama, textview_daya, textview_jam, textview_biaya, textview_harga, textview_kwh ;
    private Switch switch_beban ;
    private ImageView btn_back, btn_reset;
    float read_daya_fixed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitorbeban);

        textview_nama = findViewById(R.id.textview_nama) ;
        textview_daya = findViewById(R.id.textview_daya) ;
        textview_jam = findViewById(R.id.textview_jam) ;
        textview_biaya = findViewById(R.id.textview_biaya) ;
        textview_harga = findViewById(R.id.textview_harga) ;
        textview_kwh = findViewById(R.id.textview_kwh) ;
        switch_beban = findViewById((R.id.switch_beban)) ;
        btn_back = findViewById(R.id.btn_back) ;
        btn_reset = findViewById(R.id.btn_reset) ;

        // intent ambil data yg dibutuhkan dari activity lain : userNow, bebanNow
        Intent monitorBeban = getIntent();
        String bebanNow = monitorBeban.getStringExtra("bebanNow");
        String userNow = monitorBeban.getStringExtra("userNow");
        String dataNow = monitorBeban.getStringExtra("dataNow");
        String lokasiNow = monitorBeban.getStringExtra("lokasiNow");
        String golonganNow = monitorBeban.getStringExtra("golonganNow");
        float hargaNow = monitorBeban.getFloatExtra("hargaNow", 0L);

        // realtime baca data root : user
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("users").child(userNow) ;
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String read_nama = snapshot.child(bebanNow).child("nama").getValue(String.class) ;
                textview_nama.setText(read_nama);
                float read_daya = snapshot.child(bebanNow).child(dataNow).child("daya").getValue(float.class) ;
                textview_daya.setText(String.valueOf(read_daya) + " Watt");

                // Hitung Jam Aktif beban
                Long read_jam = snapshot.child(bebanNow).child(dataNow).child("time").getValue(Long.class) ;
                float time = countJam(read_jam) ;

                // read kWh
                textview_kwh.setText(readkWh(dataNow, bebanNow,snapshot));

                // read tarif dan Hitung biaya beban
                textview_biaya.setText(hitungBiaya(data, snapshot, dataNow, bebanNow, hargaNow));
                textview_harga.setText(toRupiah(hargaNow));
                //textview_harga.setText(String.valueOf(hargaNow));

                // switch
                Boolean state = snapshot.child(bebanNow).child("switch").getValue(Boolean.class) ;
                switch_beban.setChecked(state);
                switch_beban.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (switch_beban.isChecked()) {
                            // jika switch di on
                            Boolean state_ = Boolean.TRUE;
                            data.child(bebanNow).child("switch").setValue(state_);
                            switch_beban.setChecked((true));
                        } else {
                            // jika switch off
                            Boolean state_ = Boolean.FALSE;
                            data.child(bebanNow).child("switch").setValue(state_);
                            switch_beban.setChecked((false));
                        }
                    }
                });

                // button back
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent home_bar = new Intent(getApplicationContext(), activity_Home.class);
                        home_bar.putExtra("userNow",userNow) ;
                        home_bar.putExtra("lokasiNow",lokasiNow) ;
                        home_bar.putExtra("golonganNow",golonganNow) ;
                        startActivity(home_bar);
                        overridePendingTransition(0,0);
                    }
                });

                // button reset data
                btn_reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        data.child(bebanNow).child(dataNow).child("time").setValue(0) ;
                        data.child(bebanNow).child(dataNow).child("daya").setValue(0) ;
                        data.child(bebanNow).child(dataNow).child("biaya").setValue(0) ;
                        Toast.makeText(activity_MonitorBeban.this, "Data has been reset", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;

    }

    // Fungsi readkWh
    public String readkWh(String dataNow, String beban , DataSnapshot snapshot) {
        float read_daya = snapshot.child(beban).child(dataNow).child("daya").getValue(float.class) ;
        Long read_jam = snapshot.child(beban).child(dataNow).child("time").getValue(Long.class) ;
        float time = read_jam / (float)3600 ;

        // error handling saat daya = 0 dan biaya = 0
        if (read_daya != 0) {
            read_daya_fixed = read_daya ;
        }

        float kWh = read_daya_fixed / (float)1000 * time;
        DecimalFormat df = new DecimalFormat("#.######");
        String text = df.format(kWh) + " kWh";
        return text;
    }

    // Hitung Jam Aktif beban
    public float countJam(Long read_jam) {
        float time = read_jam/ (float)3600;
        Long jam = Long.valueOf(0);
        Long menit = Long.valueOf(0);
        Long detik = Long.valueOf(0);

        if (read_jam >= 3600) {
            jam = read_jam / 60 ;
            read_jam = read_jam % 60 ;
        } if (read_jam >= 60) {
            menit = read_jam / 60 ;
            read_jam = read_jam % 60 ;
        } if (read_jam > 0) {
            detik = read_jam;
        }

        textview_jam.setText(jam.toString() + " Jam " + menit.toString() + " menit " + detik.toString() + " detik");
        return time ;
    }

    //Hitung biaya beban
    public String hitungBiaya (DatabaseReference data, DataSnapshot snapshot, String dataNow, String beban, float harga) {
        float read_daya = snapshot.child(beban).child(dataNow).child("daya").getValue(float.class) ;
        Long read_jam = snapshot.child(beban).child(dataNow).child("time").getValue(Long.class) ;
        float time = countJam(read_jam) ;

        // error handling saat daya = 0 dan biaya = 0
        if (read_daya != 0) {
            read_daya_fixed = read_daya ;
        }

        float biaya = read_daya_fixed / (float)1000 * time * harga;
        String biaya_text = toRupiah(biaya) ;
        return biaya_text ;
    }

    // Buat constructor toRupiah
    public String toRupiah(float input) {
        // Constructor Rupiah
        Locale ina = new Locale("id", "ID") ;
        Currency rupiah = Currency.getInstance(ina);
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(ina) ;
        String rupiah_text = rupiahFormat.format(input) ;
        return rupiah_text ;
    }


}