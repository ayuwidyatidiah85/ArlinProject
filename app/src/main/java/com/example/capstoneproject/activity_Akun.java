package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_Akun extends AppCompatActivity {

    // deklarasi xml component
    private Button btn_save;
    private ImageButton btn_back ;
    private TextInputLayout textinput_namauser, textinput_password ;
    private AutoCompleteTextView auto_lokasi, auto_golongan;
    private TextView textview_namauser, textview_username ;

    String[] item_lokasi = {"Yogyakarta", "Jakarta", "Semarang"} ;
    String[] item_golongan = {"450VA", "900VA", "1300VA"} ;
    ArrayAdapter<String> adapter_lokasi, adapter_golongan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);

        btn_save = findViewById(R.id.btn_save) ;
        btn_back = findViewById(R.id.btn_back) ;
        textview_namauser = findViewById(R.id.textview_namauser) ;
        textview_username = findViewById(R.id.textview_username) ;
        textinput_namauser = findViewById(R.id.textinput_namauser) ;
        textinput_password = findViewById(R.id.textinput_password) ;
        auto_lokasi = findViewById(R.id.auto_lokasi) ;
        auto_golongan = findViewById(R.id.auto_golongan) ;

        Intent akun = getIntent() ;
        String userNow = akun.getStringExtra("userNow") ;
        String lokasiNow = akun.getStringExtra("lokasiNow") ;
        String golonganNow = akun.getStringExtra("golonganNow") ;

        adapter_lokasi = new ArrayAdapter<>(this, R.layout.dropdown_list, item_lokasi) ;
        adapter_golongan = new ArrayAdapter<>(this, R.layout.dropdown_list, item_golongan) ;

        // Realtime baca data root : user
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("users").child(userNow) ;
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String _nama = snapshot.child("nama").getValue(String.class) ;
                String _password = snapshot.child("password").getValue(String.class) ;
                String _lokasi = snapshot.child("location").getValue(String.class) ;
                String _golongan = snapshot.child("golongan").getValue(String.class) ;

                textview_namauser.setText(_nama);
                textview_username.setText("@" + userNow);

                textinput_namauser.getEditText().setText(_nama);
                textinput_password.getEditText().setText(_password);
                auto_lokasi.setText(_lokasi);
                auto_golongan.setText(_golongan);

                String lokasiNow = snapshot.child("location").getValue(String.class) ;
                String golonganNow = snapshot.child("golongan").getValue(String.class) ;

                // Dropdown
                auto_lokasi.setAdapter(adapter_lokasi);
                auto_lokasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = adapterView.getItemAtPosition(i).toString();
                    }
                });
                auto_golongan.setAdapter(adapter_golongan);
                auto_golongan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = adapterView.getItemAtPosition(i).toString();
                    }
                });

                // button update profile
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        data.child("nama").setValue(textinput_namauser.getEditText().getText().toString()) ;
                        data.child("password").setValue(textinput_password.getEditText().getText().toString()) ;
                        data.child("location").setValue(auto_lokasi.getText().toString()) ;
                        data.child("golongan").setValue(auto_golongan.getText().toString()) ;
                        Toast.makeText(activity_Akun.this, "Data has been updated", Toast.LENGTH_LONG).show();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;


    }


}