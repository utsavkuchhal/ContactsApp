package com.utsav.datatableexample;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class Register extends AppCompatActivity {

    private View mLoginFormView;
    private View mProgressView;
    private TextView tvLoad;

    EditText etName,etMail,pass,etReset;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);


        etMail = findViewById(R.id.et2Mail);
        etName = findViewById(R.id.et2Name);
        pass  =findViewById(R.id.et2Password);
        etReset = findViewById(R.id.et2Renter);
        btnRegister = findViewById(R.id.btn2Register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(etMail.getText().toString().isEmpty() || etName.getText().toString().isEmpty() ||
                         pass.getText().toString().isEmpty() || etReset.getText().toString().isEmpty())
                 {
                     Toast.makeText(Register.this,"Please Enter All fields",Toast.LENGTH_SHORT).show();
                 }else{
                     if(pass.getText().toString().equals(etReset.getText().toString().trim())){

                         String name  = etName.getText().toString().trim();
                         String Email = etMail.getText().toString().trim();
                         String password = pass.getText().toString().trim();

                         BackendlessUser user = new BackendlessUser();
                         user.setEmail(Email);
                         user.setPassword(password);
                         user.setProperty("name",name);

                         showProgress(true);
                         tvLoad.setText("Busy Registering User Please Wait...");

                         Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                             @Override
                             public void handleResponse(BackendlessUser response) {
                                 showProgress(false);
                                 Toast.makeText(Register.this,"User Registered Successfully",Toast.LENGTH_SHORT).show();
                                 Register.this.finish();
                             }

                             @Override
                             public void handleFault(BackendlessFault fault) {
                                 Toast.makeText(Register.this,"Error " + fault.getMessage(),Toast.LENGTH_SHORT).show();
                                 showProgress(false);
                             }
                         });

                     }else{
                         Toast.makeText(Register.this,"Please Make sure password and retype password are same"
                                 ,Toast.LENGTH_SHORT).show();
                     }
                 }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
