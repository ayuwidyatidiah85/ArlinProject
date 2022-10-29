package com.example.capstoneproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // inisialisasi textview daya
    private TextView daya1 , daya2, daya3, daya4;
    // inisialisasi variabel public
    public int read_data1, read_data2, read_data3, read_data4 ;
    // buat reference untuk firebase (koneksi server / host Firebase)
    private Firebase mRef_daya1, mRef_daya2, mRef_daya3, mRef_daya4 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // baca komponen daya1-4 (textview)
        daya1 = (TextView)findViewById(R.id.daya1) ;
        daya2 = (TextView)findViewById(R.id.daya2) ;
        daya3 = (TextView)findViewById(R.id.daya3) ;
        daya4 = (TextView)findViewById(R.id.daya4) ;
        // buka koneksi ke host firebase
        mRef_daya1 = new Firebase("https://capstonea07-default-rtdb.firebaseio.com/daya1") ;
        mRef_daya2 = new Firebase("https://capstonea07-default-rtdb.firebaseio.com/daya2") ;
        mRef_daya3 = new Firebase("https://capstonea07-default-rtdb.firebaseio.com/daya3") ;
        mRef_daya4 = new Firebase("https://capstonea07-default-rtdb.firebaseio.com/daya4") ;

        // proses realtime daya 1
        mRef_daya1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String read_daya1 = dataSnapshot.getValue(String.class) ; // ambil daya1 dr firebase
                daya1.setText(read_daya1); // tampilkan read_daya1 ke textView(daya1)
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        }) ;

        // proses realtime daya 2
        mRef_daya2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String read_daya2 = dataSnapshot.getValue(String.class) ; // ambil daya2 dr firebase
                daya2.setText(read_daya2); // tampilkan read_daya2 ke textView(daya2)
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        }) ;

        // proses realtime daya 3
        mRef_daya3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String read_daya3 = dataSnapshot.getValue(String.class) ; // ambil daya3 dr firebase
                daya3.setText(read_daya3); // tampilkan read_daya3 ke textView(daya3)
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        }) ;

        // proses realtime daya 4
        mRef_daya4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String read_daya4 = dataSnapshot.getValue(String.class) ; // ambil daya4 dr firebase
                daya4.setText(read_daya4); // tampilkan read_daya4 ke textView(daya4)
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        }) ;

        // daya4.setText(read_daya4);
    }
}