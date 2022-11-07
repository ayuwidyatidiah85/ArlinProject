package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class activity_Home extends AppCompatActivity{

    // inisialisasi komponen xml
    private TextView textview_namauser, textview_username ;
    private TextView textview_daya1, textview_daya2, textview_daya3, textview_daya4, textview_totaldaya;
    private TextView textview_nama1, textview_nama2, textview_nama3, textview_nama4 ;
    private Switch switch1, switch2, switch3, switch4 ;
    private Button button_monitor ;
    public String dataNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // intent ambil data yg dibutuhkan dari activity lain : username, location
        Intent intent = getIntent();
        String userNow = intent.getStringExtra("userNow");
        String lokasiNow = intent.getStringExtra("lokasiNow") ;

        // deklarasi intent yang dipakai
        Intent monitorBeban = new Intent(getApplicationContext(), activity_MonitorBeban.class);
        Intent monitor = new Intent(getApplicationContext(), activity_Monitor1.class);
        //Intent monitor2 = new Intent(getApplicationContext(), activity_Monitor2.class);
        Intent akun = new Intent(getApplicationContext(), activity_Akun.class) ;

        // realtime baca data root : user
        DatabaseReference loc = FirebaseDatabase.getInstance().getReference("location") ;
        Query checkLoc = loc.orderByChild("nama").equalTo(lokasiNow) ;
        checkLoc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long harga = snapshot.child(lokasiNow).child("harga").getValue(Long.class);
                float hargaNow = (float) harga ;
                Long ppj = snapshot.child(lokasiNow).child("PPJ").getValue(Long.class);
                float ppjNow = (float) ppj ;
                Long ppn = snapshot.child(lokasiNow).child("PPN").getValue(Long.class);
                float ppnNow = (float) ppn ;

                // Intent utk acitivity MonitorBeban
                monitorBeban.putExtra("hargaNow", hargaNow) ;

                // Intent utk acitivity Monitor
                monitor.putExtra("hargaNow", hargaNow) ;
                monitor.putExtra("ppjNow", ppjNow) ;
                monitor.putExtra("ppnNow", ppnNow) ;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        }) ;

        // Realtime baca data root : user
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("users").child(userNow) ;
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // baca komponen daya, nama, dan switch beban 1-4 (textview)
                textview_namauser = findViewById(R.id.textview_namauser) ;
                textview_username = findViewById(R.id.textview_username) ;

                textview_daya1 = findViewById(R.id.textview_daya1) ;
                textview_daya2 = findViewById(R.id.textview_daya2) ;
                textview_daya3 = findViewById(R.id.textview_daya3) ;
                textview_daya4 = findViewById(R.id.textview_daya4) ;
                textview_totaldaya = findViewById(R.id.textview_totaldaya) ;

                textview_nama1 = findViewById(R.id.textview_nama1) ;
                textview_nama2 = findViewById(R.id.textview_nama2) ;
                textview_nama3 = findViewById(R.id.textview_nama3) ;
                textview_nama4 = findViewById(R.id.textview_nama4) ;

                switch1 = findViewById(R.id.switch1) ;
                switch2 = findViewById(R.id.switch2) ;
                switch3 = findViewById(R.id.switch3) ;
                switch4 = findViewById(R.id.switch4) ;

                button_monitor = findViewById((R.id.button_monitor)) ;

                // read dan get data adaorang
                Boolean adaorang = snapshot.child("adaorang").getValue(Boolean.class) ;
                if (adaorang == true) {
                    // data dimasukkan sebagai "Penggunaan"
                    dataNow = "data1" ;
                } else if (adaorang == false) {
                    // data dimasukkan sebagai "Pemborosan"
                    dataNow = "data2" ;
                }

                // read dan show namauser dan username
                String read_namauser = snapshot.child("nama").getValue(String.class) ;
                String read_username = snapshot.child("username").getValue(String.class) ;
                textview_namauser.setText("Halo " + read_namauser);
                textview_username.setText("@" + read_username);

                // read dan show daya beban 1-4
                Long read_daya1 = snapshot.child(dataNow).child("beban1").child("daya").getValue(Long.class) ;
                Long read_daya2 = snapshot.child(dataNow).child("beban2").child("daya").getValue(Long.class) ;
                Long read_daya3 = snapshot.child(dataNow).child("beban3").child("daya").getValue(Long.class) ;
                Long read_daya4 = snapshot.child(dataNow).child("beban4").child("daya").getValue(Long.class) ;
                Long read_totaldaya = read_daya1 + read_daya2 + read_daya3 + read_daya4 ; // sum daya 1-4
                textview_daya1.setText(read_daya1.toString());
                textview_daya2.setText(read_daya2.toString());
                textview_daya3.setText(read_daya3.toString());
                textview_daya4.setText(read_daya4.toString());
                textview_totaldaya.setText(read_totaldaya.toString());

                // read dan show nama beban 1-4
                String read_nama1 = snapshot.child(dataNow).child("beban1").child("nama").getValue(String.class) ;
                String read_nama2 = snapshot.child(dataNow).child("beban2").child("nama").getValue(String.class) ;
                String read_nama3 = snapshot.child(dataNow).child("beban3").child("nama").getValue(String.class) ;
                String read_nama4 = snapshot.child(dataNow).child("beban4").child("nama").getValue(String.class) ;
                textview_nama1.setText(read_nama1);
                textview_nama2.setText(read_nama2);
                textview_nama3.setText(read_nama3);
                textview_nama4.setText(read_nama4);

                textview_namauser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String golonganNow = snapshot.child("golongan").getValue(String.class) ;
                        akun.putExtra("userNow", userNow);
                        akun.putExtra("lokasiNow", lokasiNow) ;
                        akun.putExtra("golonganNow", golonganNow) ;
                        startActivity(akun);
                    }
                });

                textview_nama1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String bebanNow = "beban1" ;
                        //Intent monitorBeban = new Intent(getApplicationContext(), activity_MonitorBeban.class);
                        monitorBeban.putExtra("bebanNow",bebanNow) ;
                        monitorBeban.putExtra("userNow", userNow);
                        monitorBeban.putExtra("dataNow", dataNow) ;
                        startActivity(monitorBeban);
                    }
                });

                // read dan show state beban 1-4
                Boolean state1 = snapshot.child(dataNow).child("beban1").child("switch").getValue(Boolean.class) ;
                Boolean state2 = snapshot.child(dataNow).child("beban2").child("switch").getValue(Boolean.class) ;
                Boolean state3 = snapshot.child(dataNow).child("beban3").child("switch").getValue(Boolean.class) ;
                Boolean state4 = snapshot.child(dataNow).child("beban4").child("switch").getValue(Boolean.class) ;
                switch1.setChecked(state1);
                switch2.setChecked(state2);
                switch3.setChecked(state3);
                switch4.setChecked(state4);

                // activate when user on / off switch
                switch1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (switch1.isChecked()) {
                            // jika switch di on
                            Boolean state_ = Boolean.TRUE;
                            data.child(dataNow).child("beban1").child("switch").setValue(state_);
                            switch1.setChecked((true));
                        } else {
                            // jika switch off
                            Boolean state_ = Boolean.FALSE;
                            data.child(dataNow).child("beban1").child("switch").setValue(state_);
                            switch1.setChecked((false));
                        }
                    }
                });
                // switch 2
                switch2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (switch2.isChecked()) {
                            // jika switch di on
                            Boolean state_ = Boolean.TRUE;
                            data.child(dataNow).child("beban2").child("switch").setValue(state_);
                            switch2.setChecked((true));
                        } else {
                            // jika switch off
                            Boolean state_ = Boolean.FALSE;
                            data.child(dataNow).child("beban2").child("switch").setValue(state_);
                            switch2.setChecked((false));
                        }
                    }
                });
                // switch 3
                switch3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (switch3.isChecked()) {
                            // jika switch di on
                            Boolean state_ = Boolean.TRUE;
                            data.child(dataNow).child("beban3").child("switch").setValue(state_);
                            switch3.setChecked((true));
                        } else {
                            // jika switch off
                            Boolean state_ = Boolean.FALSE;
                            data.child(dataNow).child("beban3").child("switch").setValue(state_);
                            switch3.setChecked((false));
                        }
                    }
                });
                // switch 4
                switch4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (switch4.isChecked()) {
                            // jika switch di on
                            Boolean state_ = Boolean.TRUE;
                            data.child(dataNow).child("beban4").child("switch").setValue(state_);
                            switch4.setChecked((true));
                        } else {
                            // jika switch off
                            Boolean state_ = Boolean.FALSE;
                            data.child(dataNow).child("beban4").child("switch").setValue(state_);
                            switch4.setChecked((false));
                        }
                    }
                });



                button_monitor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String bebanNow = "beban1" ;
                        //Intent monitor = new Intent(getApplicationContext(), activity_Monitor.class);
                        monitor.putExtra("userNow", userNow) ;
                        monitor.putExtra("dataNow", dataNow) ;
                        monitor.putExtra("lokasiNow", lokasiNow) ;
                        startActivity(monitor);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;

    }


}

