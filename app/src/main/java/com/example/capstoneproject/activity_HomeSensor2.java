package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_HomeSensor2 extends AppCompatActivity {

    // Bottom Navigation
    private BottomNavigationView bottomNavigationView ;

    // inisialisasi komponen xml
    private TextView textview_namauser, textview_username , textview_orang;
    private ImageView image_akun ;
    private Button btn_change ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homesensor2);

        // home ambil data yg dibutuhkan dari activity lain : username
        Intent login = getIntent();
        String userNow = login.getStringExtra("userNow");

        // deklarasi intent tujuan yang dipakai
        Intent homesensor = new Intent(getApplicationContext(), activity_HomeSensor2.class);
        homesensor.putExtra("userNow",userNow) ;

        // Realtime baca data root : user
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("users").child(userNow) ;
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // baca komponen daya, nama, dan switch beban 1-4 (textview)
                textview_namauser = findViewById(R.id.textview_namauser) ;
                textview_username = findViewById(R.id.textview_username) ;
                image_akun = findViewById(R.id.image_akun) ;
                textview_orang = findViewById(R.id.textview_orang) ;
                btn_change = findViewById((R.id.btn_change)) ;


                // read dan get data adaorang
                Boolean adaorang = snapshot.child("adaorang").getValue(Boolean.class) ;
                if (adaorang == true) {
                    textview_orang.setText("IN");
                }
                else if (adaorang == false) {
                    textview_orang.setText("OUT");
                }


                // read dan show namauser dan username
                String read_namauser = snapshot.child("nama").getValue(String.class) ;
                String read_username = snapshot.child("username").getValue(String.class) ;
                textview_namauser.setText("Halo " + read_namauser);
                textview_username.setText("@" + read_username);

                // btn_change
                btn_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        finish();
                        startActivity(homesensor);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to quit?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //your quitting code
                        finishAffinity();
                    }

                })
                .setNegativeButton("no", null)
                .show();
    }
}