package com.uptune;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;

public class SingUp extends AppCompatActivity {

    Button callLogIn, callSingUp;
    ImageView img;
    TextView loginText, loginTitle;
    TextInputLayout username, password, phone, mail, name;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        callLogIn = findViewById(R.id.btn_login);
        callSingUp = findViewById(R.id.btn_sing_up);
        img = findViewById(R.id.logo);
        loginTitle = findViewById(R.id.LoginTitle);
        loginText = findViewById(R.id.LoginText);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phoneNumber);
        mail = findViewById(R.id.email);
        name = findViewById(R.id.fullName);

        callLogIn.setOnClickListener(v -> {
            Pair[] pairs = new Pair[7];
            Intent intent = new Intent(SingUp.this, Login.class);
            pairs[0] = new Pair<View, String>(img, "logo_img");
            pairs[1] = new Pair<View, String>(loginTitle, "loadTitle");
            pairs[2] = new Pair<View, String>(loginText, "loadText");
            pairs[3] = new Pair<View, String>(username, "loadUsername");
            pairs[4] = new Pair<View, String>(password, "loadPw");
            pairs[5] = new Pair<View, String>(callSingUp, "loadLogin");
            pairs[6] = new Pair<View, String>(callLogIn, "loadSing");
            ActivityOptions opt = ActivityOptions.makeSceneTransitionAnimation(SingUp.this, pairs);
            startActivity(intent, opt.toBundle());
        });
        callSingUp.setOnClickListener(v -> {
            if (!validateName() | !validateUsername() | !validateMail() | !validatePass() | !validateUsername()) {
                return;
            }
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("user");
            UserHelper helper = new UserHelper(name, username, mail, phone, password);
            reference.child(username.getEditText().getText().toString()).setValue(helper);
            Intent intent = new Intent(SingUp.this, Account.class);
            startActivity(intent);
        });
    }


    private Boolean validateName() {
        String val = name.getEditText().getText().toString();
        if (val.isEmpty()) {
            name.setError("Name cannot be empty");
            return false;
        }
        name.setError(null);
        name.setErrorEnabled(false);
        return true;
    }


    private Boolean validateMail() {
        String val = mail.getEditText().getText().toString();
        if (val.isEmpty()) {
            mail.setError("E-Mail cannot be empty");
            return false;
        }
        mail.setError(null);
        mail.setErrorEnabled(false);
        return true;
    }

    private Boolean validatePass() {
        String passVal = "^" +
                //"(?=.*[0-9])" +
                "(?=.*[a-zA-Z])" +
                //"(?=.*[@#$%^&+=!])" +
                "(?=\\S+$)" +
                ".{4,}$";
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Password cannot be empty");
            return false;
        }
        password.setError(null);
        password.setErrorEnabled(false);
        return true;
    }

    private Boolean validateUsername() {
        String space = "\\A\\w{4,20}\\z";
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Username cannot be empty");
            return false;
        }
        if (val.length() >= 20) {
            username.setError("Username too long");
            return false;
        }
        if (!val.matches(space)) {
            username.setError("White Space not allowed");
            return false;
        }
        username.setError(null);
        username.setErrorEnabled(false);
        return true;
    }
}