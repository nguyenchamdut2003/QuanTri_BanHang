package com.example.quantri_banhang.actitvity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quantri_banhang.R;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {
    private EditText edemail;
    private EditText edpasswd;
    private EditText edrepasswd;
    private TextInputLayout tilemail;
    private TextInputLayout tilpass;
    private TextInputLayout tilrepass;

    int temp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edemail = findViewById(R.id.signup_email);
        edpasswd = findViewById(R.id.signup_password);
        edrepasswd = findViewById(R.id.signup_repass);

        tilemail = findViewById(R.id.til_email_signup);
        tilpass = findViewById(R.id.til_password_signup);
        tilrepass = findViewById(R.id.til_repass_signup);

        findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }
    void validate(){
        String pass = edpasswd.getText().toString();
        String repass = edrepasswd.getText().toString();
        if (edemail.getText().length() == 0){
            tilemail.setError("Don't leave your email blank!");
            temp++;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(edemail.getText().toString()).matches()){
            tilemail.setError("Incorrect email format!");
            temp++;
        }else{
            tilemail.setError("");
        }

        if (edpasswd.getText().length() == 0){
            tilpass.setError("Don't leave your password blank!");
            temp++;
        }else if(edpasswd.getText().length() <= 6){
            tilpass.setError("Password greater than 6 characters!");
            temp++;
        }
        else{
            tilpass.setError("");
        }

        if (edrepasswd.getText().length() == 0){
            tilrepass.setError("Don't leave your Repassword blank!");
            temp++;
        }else if(!(repass.equalsIgnoreCase(pass))){
            tilrepass.setError("Repassword is incorrect!");
            temp++;
        }else{
            tilrepass.setError("");
        }
    }
}