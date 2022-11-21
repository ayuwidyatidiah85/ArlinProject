package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class activity_Monitor1 extends AppCompatActivity {

    // Bottom Navigation
    private BottomNavigationView bottomNavigationView ;

    // component xml
    private TextView textview_totaldaya, textview_biaya2;
    private TextView textview_nama1, textview_nama2, textview_nama3, textview_nama4 ;
    private TextView textview_kWh1, textview_kWh2, textview_kWh3, textview_kWh4, textview_totalkWh ;
    private TextView textview_lokasi, textview_golongan ;
    private TextView textview_harga, textview_ppn, textview_ppj, textview_ppjrupiah, textview_biaya ;
    float read_daya_fixed = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor1);

        // intent ambil data yg dibutuhkan dari activity lain : usename
        Intent monitor1 = getIntent();
        String userNow = monitor1.getStringExtra("userNow");
        String dataNow = monitor1.getStringExtra("dataNow") ;
        String lokasiNow = monitor1.getStringExtra("lokasiNow") ;
        String golonganNow = monitor1.getStringExtra("golonganNow") ;
        float hargaNow = monitor1.getFloatExtra("hargaNow", 0L);
        float ppnNow = monitor1.getFloatExtra("ppnNow", 0L);
        float ppjNow = monitor1.getFloatExtra("ppjNow", 0L);

        // realtime baca data root : user
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("users").child(userNow) ;
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                textview_totaldaya = findViewById(R.id.textview_totaldaya) ;
                textview_biaya2 = findViewById(R.id.textview_biaya2) ;
                textview_kWh1 = findViewById(R.id.textview_kwh1) ;
                textview_kWh2 = findViewById(R.id.textview_kwh2) ;
                textview_kWh3 = findViewById(R.id.textview_kwh3) ;
                textview_kWh4 = findViewById(R.id.textview_kwh4) ;
                textview_totalkWh = findViewById(R.id.textview_totalkWh) ;

                textview_nama1 = findViewById(R.id.textview_nama1) ;
                textview_nama2 = findViewById(R.id.textview_nama2) ;
                textview_nama3 = findViewById(R.id.textview_nama3) ;
                textview_nama4 = findViewById(R.id.textview_nama4) ;

                textview_lokasi = findViewById(R.id.textview_lokasi) ;
                textview_golongan = findViewById(R.id.textview_golongan);
                textview_harga = findViewById(R.id.textview_harga) ;
                textview_ppn = findViewById(R.id.textview_ppn) ;
                textview_ppj = findViewById(R.id.textview_ppj) ;
                textview_ppjrupiah = findViewById(R.id.textview_ppjrupiah) ;
                textview_biaya = findViewById(R.id.textview_biaya) ;

                textview_nama1.setText(showText(dataNow,"beban1","nama",snapshot));
                textview_nama2.setText(showText(dataNow,"beban2","nama",snapshot));
                textview_nama3.setText(showText(dataNow,"beban3","nama",snapshot));
                textview_nama4.setText(showText(dataNow,"beban4","nama",snapshot));

                textview_kWh1.setText(showkWh(dataNow,"beban1",snapshot));
                textview_kWh2.setText(showkWh(dataNow,"beban2",snapshot));
                textview_kWh3.setText(showkWh(dataNow,"beban3",snapshot));
                textview_kWh4.setText(showkWh(dataNow,"beban4",snapshot));
                float total_kWh = showTotalkWh(dataNow, snapshot) ;
                textview_totalkWh.setText(toDecimalkWh(total_kWh, "#.#####") + " kWh");
                textview_totaldaya.setText(toDecimalkWh(total_kWh, "#.##") + " kWh");

                textview_lokasi.setText(lokasiNow);
                textview_golongan.setText(golonganNow);
                textview_harga.setText(toRupiah(hargaNow));
                textview_ppn.setText(toRupiah(ppnNow));
                textview_ppj.setText(String.valueOf(ppjNow) + "%");

                // Hitung total biaya
                //float biaya_bersih = total_kWh * hargaNow ;
                float biaya_bersih = showTotalbiaya(dataNow, snapshot) ;
                float ppjRupiah = ppjNow * biaya_bersih ;
                float biaya_kotor = biaya_bersih + ppjRupiah ;
                textview_ppjrupiah.setText(toRupiah(ppjRupiah));
                textview_biaya.setText(toRupiah(biaya_kotor));
                textview_biaya2.setText(toRupiah(biaya_kotor));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;

        // navigation
        bottomNavigationView = findViewById(R.id.navbar) ;
        bottomNavigationView.setSelectedItemId(R.id.navigation_Monitor1);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_Home:
                        // intent
                        Intent home_bar = new Intent(getApplicationContext(), activity_Home.class);
                        home_bar.putExtra("userNow",userNow) ;
                        home_bar.putExtra("lokasiNow",lokasiNow) ;
                        home_bar.putExtra("golonganNow",golonganNow) ;
                        startActivity(home_bar);
                        overridePendingTransition(0,0);
                        return true ;
                    case R.id.navigation_Monitor1:
                        return true ;
                    case R.id.navigation_Monitor2:
                        // intent
                        String dataNow = "data2" ;
                        Intent monitor2_bar = new Intent(getApplicationContext(), activity_Monitor2.class);
                        monitor2_bar.putExtra("hargaNow", hargaNow) ;
                        monitor2_bar.putExtra("ppjNow", ppjNow) ;
                        monitor2_bar.putExtra("ppnNow", ppnNow) ;
                        monitor2_bar.putExtra("userNow", userNow) ;
                        monitor2_bar.putExtra("dataNow", dataNow) ;
                        monitor2_bar.putExtra("lokasiNow", lokasiNow) ;
                        monitor2_bar.putExtra("golonganNow", golonganNow) ;
                        startActivity(monitor2_bar);
                        overridePendingTransition(0,0);
                        return true ;

                }
                return false;
            }
        });
    }

    public String showText(String dataNow, String beban , String prop, DataSnapshot snapshot) {
        String text = snapshot.child(beban).child(prop).getValue(String.class) ;
        return text;
    }

    public String showkWh(String dataNow, String beban , DataSnapshot snapshot) {
        float read_daya = snapshot.child(beban).child(dataNow).child("daya").getValue(float.class) ;
        Long read_jam = snapshot.child(beban).child(dataNow).child("time").getValue(Long.class) ;
        float time = read_jam/ (float)3600;

        // error handling saat daya = 0 dan biaya = 0
        if (read_daya != 0) {
            read_daya_fixed = read_daya ;
        }

        float kWh = read_daya_fixed/ (float)1000 * time;
        String text = toDecimalkWh(kWh, "#.#####")+ " kWh";
        return text;
    }

    public Float hitungkWh(String dataNow, String beban , DataSnapshot snapshot) {
        float read_daya = snapshot.child(beban).child(dataNow).child("daya").getValue(float.class) ;
        Long read_jam = snapshot.child(beban).child(dataNow).child("time").getValue(Long.class) ;
        float time = read_jam/ (float)3600;

        // error handling saat daya = 0 dan biaya = 0
        if (read_daya != 0) {
            read_daya_fixed = read_daya ;
        }

        float kWh = read_daya_fixed / (float)1000 * time;
        return kWh;
    }
    public float showTotalkWh(String dataNow, DataSnapshot snapshot) {
        float read_kWh1 = hitungkWh(dataNow,"beban1", snapshot) ;
        float read_kWh2 = hitungkWh(dataNow,"beban2", snapshot) ;
        float read_kWh3 = hitungkWh(dataNow,"beban3", snapshot) ;
        float read_kWh4 = hitungkWh(dataNow,"beban4", snapshot) ;
        float kWh = read_kWh1 + read_kWh2 + read_kWh3 + read_kWh4 ;
        return kWh;
    }

    public float showTotalbiaya(String dataNow, DataSnapshot snapshot) {
        float read_biaya1 = snapshot.child("beban1").child(dataNow).child("biaya").getValue(float.class) ;
        float read_biaya2 = snapshot.child("beban2").child(dataNow).child("biaya").getValue(float.class) ;
        float read_biaya3 = snapshot.child("beban3").child(dataNow).child("biaya").getValue(float.class) ;
        float read_biaya4 = snapshot.child("beban4").child(dataNow).child("biaya").getValue(float.class) ;
        float kWh = read_biaya1 + read_biaya2 + read_biaya3 + read_biaya4 ;
        return kWh;
    }

    public String toDecimalkWh(float kWh, String koma) {
        // koma = "#.###"
        DecimalFormat df = new DecimalFormat(koma);
        String text = df.format(kWh);
        return text;
    }

    public String toRupiah(float input) {
        // Constructor Rupiah
        Locale ina = new Locale("id", "ID") ;
        Currency rupiah = Currency.getInstance(ina);
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(ina) ;
        String rupiah_text = rupiahFormat.format(input) ;
        return rupiah_text ;
    }

}