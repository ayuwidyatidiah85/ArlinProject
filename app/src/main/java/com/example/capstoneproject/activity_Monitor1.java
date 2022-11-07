package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

    private TextView textview_totaldaya ;
    private TextView textview_nama1, textview_nama2, textview_nama3, textview_nama4 ;
    private TextView textview_kWh1, textview_kWh2, textview_kWh3, textview_kWh4, textview_totalkWh ;
    private TextView textview_harga, textview_ppn, textview_ppj, textview_ppjrupiah, textview_biaya ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        // intent ambil data yg dibutuhkan dari activity lain : usename
        Intent monitor = getIntent();
        String userNow = monitor.getStringExtra("userNow");
        String dataNow = monitor.getStringExtra("dataNow") ;
        float hargaNow = monitor.getFloatExtra("hargaNow", 0L);
        float ppnNow = monitor.getFloatExtra("ppnNow", 0L);
        float ppjNow = monitor.getFloatExtra("ppjNow", 0L);

        // realtime baca data root : user
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("users").child(userNow) ;
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                textview_totaldaya = findViewById(R.id.textview_totaldaya) ;
                textview_kWh1 = findViewById(R.id.textview_kwh1) ;
                textview_kWh2 = findViewById(R.id.textview_kwh2) ;
                textview_kWh3 = findViewById(R.id.textview_kwh3) ;
                textview_kWh4 = findViewById(R.id.textview_kwh4) ;
                textview_totalkWh = findViewById(R.id.textview_totalkWh) ;

                textview_nama1 = findViewById(R.id.textview_nama1) ;
                textview_nama2 = findViewById(R.id.textview_nama2) ;
                textview_nama3 = findViewById(R.id.textview_nama3) ;
                textview_nama4 = findViewById(R.id.textview_nama4) ;

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
                textview_totalkWh.setText(toDecimalkWh(total_kWh, "#.####") + " kWh");
                textview_totaldaya.setText(toDecimalkWh(total_kWh, "#.##"));

                textview_harga.setText(toRupiah(hargaNow));
                textview_ppn.setText(toRupiah(ppnNow));
                textview_ppj.setText(String.valueOf(ppjNow) + "%");
                float biaya_bersih = total_kWh * hargaNow ;
                float ppjRupiah = ppjNow * biaya_bersih ;
                float biaya_kotor = biaya_bersih + ppjRupiah ;
                textview_ppjrupiah.setText(toRupiah(ppjRupiah));
                textview_biaya.setText(toRupiah(biaya_kotor));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;
    }

    public String showText(String dataNow, String beban , String prop, DataSnapshot snapshot) {
        String text = snapshot.child(beban).child(prop).getValue(String.class) ;
        return text;
    }

    public String showkWh(String dataNow, String beban , DataSnapshot snapshot) {
        Long read_daya = snapshot.child(beban).child(dataNow).child("daya").getValue(Long.class) ;
        Long read_jam = snapshot.child(beban).child(dataNow).child("time").getValue(Long.class) ;
        float time = read_jam/ (float)3600;
        float kWh = read_daya/ (float)1000 * time;
        String text = toDecimalkWh(kWh, "#.####")+ " kWh";
        return text;
    }

    public Float hitungkWh(String dataNow, String beban , DataSnapshot snapshot) {
        Long read_daya = snapshot.child(beban).child(dataNow).child("daya").getValue(Long.class) ;
        Long read_jam = snapshot.child(beban).child(dataNow).child("time").getValue(Long.class) ;
        float time = read_jam/ (float)3600;
        float kWh = read_daya / (float)1000 * time;
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