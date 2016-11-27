package com.example.ningwang.momento;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import android.util.Log;

public class SignupActivity extends AppCompatActivity{

    private EditText username;
    private EditText email;
    private EditText password1;
    private EditText password2;
    private Button buttonSignup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = (EditText)findViewById(R.id.username);
        email = (EditText)findViewById(R.id.email);
        password1 = (EditText)findViewById(R.id.password1);
        password2 = (EditText)findViewById(R.id.password2);
        buttonSignup = (Button)findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public static boolean isValidEmail(String enteredEmail){
        String EMAIL_REGIX = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(EMAIL_REGIX);
        Matcher matcher = pattern.matcher(enteredEmail);
        return ((!enteredEmail.isEmpty()) && (enteredEmail!=null) && (matcher.matches()));
    }

    private boolean checkvalid(String usernamestr, String emailstr, String password1str, String password2str) {
        if (TextUtils.isEmpty(usernamestr)) {
            Toast pass = Toast.makeText(SignupActivity.this, "Please enter your username.", Toast.LENGTH_SHORT);
            pass.show();
            return false;
        }

        if (TextUtils.isEmpty(emailstr)) {
            Toast pass = Toast.makeText(SignupActivity.this, "Please enter your email address.", Toast.LENGTH_SHORT);
            pass.show();
            return false;
        }

        if (!isValidEmail(emailstr)) {
            Toast pass = Toast.makeText(SignupActivity.this, "Your email address is not valid.", Toast.LENGTH_SHORT);
            pass.show();
            return false;
        }

        if (TextUtils.isEmpty(password1str)) {
            Toast pass = Toast.makeText(SignupActivity.this, "Please enter your password.", Toast.LENGTH_SHORT);
            pass.show();
            return false;
        }

        if (TextUtils.isEmpty(password2str)) {
            Toast pass = Toast.makeText(SignupActivity.this, "Please confirm your password.", Toast.LENGTH_SHORT);
            pass.show();
            return false;
        }

        if (password1str.length() < 6) {
            Toast pass = Toast.makeText(SignupActivity.this, "Password must be 6 characters or more.", Toast.LENGTH_SHORT);
            pass.show();
            return false;
        }

        if (!password1str.equals(password2str)) {
            Toast pass = Toast.makeText(SignupActivity.this, "Passwords don't match.", Toast.LENGTH_SHORT);
            pass.show();
            return false;
        }
        return true;
    }

    public void setUsername(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
    }

    private void register() {
        final String usernamestr = username.getText().toString();
        String emailstr = email.getText().toString().trim();
        String password1str = password1.getText().toString().trim();
        String password2str = password2.getText().toString().trim();

        if (!checkvalid(usernamestr, emailstr, password1str, password2str)) {
            return;
        }
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(emailstr, password1str)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            setUsername(usernamestr);
                            Toast.makeText(SignupActivity.this, "Signed up successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(SignupActivity.this, "Could not sign up, please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public void onSignUpClick(View v) {
        if(v == buttonSignup) {
            register();
        }
    }
}
