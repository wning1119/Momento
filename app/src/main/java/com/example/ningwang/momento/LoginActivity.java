package com.example.ningwang.momento;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Button login;
    private Button signup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText)findViewById(R.id.user);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        signup = (Button)findViewById(R.id.signup);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() !=  null) {
            //go to homepage
        }
    }

    public static boolean isValidEmail(String enteredEmail){
        String EMAIL_REGIX = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(EMAIL_REGIX);
        Matcher matcher = pattern.matcher(enteredEmail);
        return ((!enteredEmail.isEmpty()) && (enteredEmail!=null) && (matcher.matches()));
    }

    private boolean checkvalid(String userstr, String passwordstr) {
        if (TextUtils.isEmpty(userstr)) {
            Toast pass = Toast.makeText(LoginActivity.this, "Please enter your email address.", Toast.LENGTH_SHORT);
            pass.show();
            return false;
        }

        if (!isValidEmail(userstr)) {
            Toast pass = Toast.makeText(LoginActivity.this, "Your email address is not valid.", Toast.LENGTH_SHORT);
            pass.show();
            return false;
        }

        if (TextUtils.isEmpty(passwordstr)) {
            Toast pass = Toast.makeText(LoginActivity.this, "Please enter your password.", Toast.LENGTH_SHORT);
            pass.show();
            return false;
        }

        return true;
    }
    private void userlogin() {
        String userstr = user.getText().toString().trim();
        String passwordstr = password.getText().toString().trim();
        if (!checkvalid(userstr, passwordstr)) {
            return;
        }
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userstr, passwordstr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            //go to homepage
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);

                        }else {
                            Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onButtonClick(View v) {
        if (v == login) {
            userlogin();
        }
        if (v == signup) {
            Intent i = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(i);
        }
    }
}
