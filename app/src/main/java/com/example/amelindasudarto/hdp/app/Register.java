package com.example.amelindasudarto.hdp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Amelinda Sudarto on 21/11/2016.
 */

public class Register extends AppCompatActivity {

    EditText uname; //== username;
    EditText pswd; // == password;
    EditText retype; // == retype;
    EditText date ; //== date;

    Button signupLink2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        uname = (EditText) findViewById(R.id.editText4);
        pswd = (EditText) findViewById(R.id.editText);
        retype = (EditText) findViewById(R.id.editText2);
        date = (EditText) findViewById(R.id.editText3);
        signupLink2 = (Button) findViewById(R.id.singup);


        signupLink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uname.getText().toString().equals("") || pswd.getText().toString().equals("") || retype.getText().toString().equals("") || date.getText().toString().equals("")) {
                    Toast.makeText(Register.this, "you should insert your information here", Toast.LENGTH_LONG).show();
                }else if(!retype.getText().toString().equals(pswd.getText().toString()) ){
                    Toast.makeText(Register.this, "Doesn't match passwords", Toast.LENGTH_SHORT).show();
                }else {
                    Intent regis = new Intent(Register.this, Konfirmasi.class);
                    startActivity(regis);
                }
            }
        });

    }

}
