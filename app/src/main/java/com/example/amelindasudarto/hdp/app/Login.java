package com.example.amelindasudarto.hdp.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity {

        Button loginButton, signupLink;
        EditText email, password;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            email = (EditText) findViewById(R.id.inputUsername);
            password = (EditText) findViewById(R.id.inputPass);

            loginButton = (Button) findViewById(R.id.logInBtn);
            signupLink = (Button) findViewById(R.id.pageRegBtn);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
                        Toast.makeText(Login.this, "insert username or password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(Login.this, Login.class);
                        startActivity(login);
                    }

                }
            });

            signupLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent register = new Intent(Login.this, Register.class);
                    startActivity(register);
                    finish();
                }
            });
        }
}
