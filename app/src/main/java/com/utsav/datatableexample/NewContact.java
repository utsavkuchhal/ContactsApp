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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class NewContact extends AppCompatActivity {

    EditText etname, etnumber , etmail;
    Button btncontact;

    private View mLoginFormView;
    private View mProgressView;
    private TextView tvLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etname = findViewById(R.id.et3Name);
        etmail = findViewById(R.id.et3Mail);
        etnumber = findViewById(R.id.et3Phone);

        btncontact = findViewById(R.id.btn3Contact);

        btncontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(etmail.getText().toString().isEmpty() || etname.getText().toString().isEmpty()
                    || etnumber.getText().toString().isEmpty())
                    {
                        Toast.makeText(NewContact.this,"Please Enter all fields",Toast.LENGTH_SHORT).show();
                    }

                    else{
                        String name = etname.getText().toString().trim();
                        String email = etmail.getText().toString().trim();
                        String number = etnumber.getText().toString().trim();

                        Contact contact = new Contact();

                        contact.setName(name);
                        contact.setEmail(email);
                        contact.setNumber(number);
                        contact.setUseremail(TestApplication.user.getEmail());

                        showProgress(true);
                        tvLoad.setText("Creating new Contact...PLease wait...");

                        Backendless.Persistence.save(contact, new AsyncCallback<Contact>() {
                            @Override
                            public void handleResponse(Contact response) {
                                Toast.makeText(NewContact.this,"New  contact saved successfully",Toast.LENGTH_SHORT).show();
                           showProgress(false);
                            etmail.setText("");
                            etname.setText("");
                            etnumber.setText("");
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                            Toast.makeText(NewContact.this,"Error: " + fault.getMessage(),Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            }
                        });
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
