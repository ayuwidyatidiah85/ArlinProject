package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class activity_Login extends AppCompatActivity {

    // deklarasi xml component
    private Button btn_login ;
    private TextInputLayout textinput_username, textinput_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = (Button)findViewById(R.id.btn_login) ;
        textinput_username = (TextInputLayout) findViewById(R.id.textinput_username) ;
        textinput_password = (TextInputLayout) findViewById(R.id.textinput_password) ;
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validate login info
                if(!validateUsername() | !validatePassword()) {
                    return ;
                } else {
                    isUser();
                }
            }
        });
    }

    private Boolean validateUsername() {
        String val = textinput_username.getEditText().getText().toString();
        //String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            textinput_username.setError("Field cannot be empty");
            return false;
        } else {
            textinput_username.setError(null);
            textinput_username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = textinput_password.getEditText().getText().toString();

        if (val.isEmpty()) {
            textinput_password.setError("Field cannot be empty");
            return false;
        } else {
            textinput_password.setError(null);
            textinput_password.setErrorEnabled(false);
            return true;
        }
    }

    private void isUser() {
        String userEnteredUsername = textinput_username.getEditText().getText().toString(); //.trim();
        String userEnteredPassword = textinput_password.getEditText().getText().toString();//.trim();

        DatabaseReference login = FirebaseDatabase.getInstance().getReference("users") ;

        Query checkUser = login.orderByChild("username").equalTo(userEnteredUsername) ;


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    textinput_username.setError(null);
                    textinput_username.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)) {
                        textinput_username.setError(null);
                        textinput_username.setErrorEnabled(false);

                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String lokasiFromDB = snapshot.child(userEnteredUsername).child("location").getValue(String.class);
                        String golonganFromDB = snapshot.child(userEnteredUsername).child("golongan").getValue(String.class);

                        Intent home = new Intent(getApplicationContext(), activity_Home.class);
                        home.putExtra("userNow",usernameFromDB) ;
                        home.putExtra("lokasiNow",lokasiFromDB) ;
                        home.putExtra("golonganNow",golonganFromDB) ;

                        startActivity(home);
                    } else {
                        textinput_password.setError("Wrong Password");
                        textinput_password.requestFocus();
                    }
                } else {
                    textinput_username.setError("No such user existed");
                    textinput_username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Call signup Screen
    public void callSignUpScreen(View view) { }


}