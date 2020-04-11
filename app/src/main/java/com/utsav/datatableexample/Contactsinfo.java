package com.utsav.datatableexample;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class Contactsinfo extends AppCompatActivity {

    private View mLoginFormView;
    private View mProgressView;
    private TextView tvLoad;

    ImageView call,edit,delete,mail;
    Button submit;
    TextView tvChar,tvName;
    EditText etName,etMail,etPhone;

    boolean edi = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactsinfo);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        call = findViewById(R.id.ivcall);
        edit = findViewById(R.id.ivedit);
        delete = findViewById(R.id.ivdelete);
        mail = findViewById(R.id.ivmail);

        submit = findViewById(R.id.btnsubmit);

        tvChar = findViewById(R.id.tv2char);
        tvName = findViewById(R.id.tv4display);

        etMail = findViewById(R.id.et4mail);
        etName = findViewById(R.id.et4name);
        etPhone = findViewById(R.id.et4phone);

        etName.setVisibility(View.GONE);
        etPhone.setVisibility(View.GONE);
        etMail.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);

        final int index = getIntent().getIntExtra("index",0);
        etName.setText(TestApplication.contacts.get(index).getName());
        etMail.setText(TestApplication.contacts.get(index).getEmail());
        etPhone.setText(TestApplication.contacts.get(index).getNumber());

        tvName.setText(TestApplication.contacts.get(index).getName());
        tvChar.setText(TestApplication.contacts.get(index).getName().toUpperCase().charAt(0) + "");

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:"  + TestApplication.contacts.get(index).getNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edi = !edi;

                if(edi){
                etName.setVisibility(View.VISIBLE);
                etPhone.setVisibility(View.VISIBLE);
                etMail.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);}
                else{
                    etName.setVisibility(View.GONE);
                    etPhone.setVisibility(View.GONE);
                    etMail.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Contactsinfo.this);
                dialog.setMessage("Are you sure you want to delete the contacts");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        tvLoad.setText("Deleting...");
                        Backendless.Persistence.of(Contact.class).remove(TestApplication.contacts.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                TestApplication.contacts.remove(index);
                                Toast.makeText(Contactsinfo.this,"Deleted",Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                Contactsinfo.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(Contactsinfo.this,"Error: " + fault.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, TestApplication.contacts.get(index).getEmail());
                startActivity(Intent.createChooser(intent,"Send Mail to:" + TestApplication.contacts.get(index).getName()));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(etMail.getText().toString().isEmpty()|| etName.getText().toString().isEmpty() || etPhone.getText().toString().isEmpty())
            {
                Toast.makeText(Contactsinfo.this,"Please enter all fields",Toast.LENGTH_SHORT).show();
            }else{
                TestApplication.contacts.get(index).setName(etName.getText().toString().trim());
                TestApplication.contacts.get(index).setNumber(etPhone.getText().toString().trim());
                TestApplication.contacts.get(index).setEmail(etMail.getText().toString().trim());

                showProgress(true);
                tvLoad.setText("Updating...");

                Backendless.Persistence.save(TestApplication.contacts.get(index), new AsyncCallback<Contact>() {
                    @Override
                    public void handleResponse(Contact response) {
                        tvChar.setText(TestApplication.contacts.get(index).getName().toUpperCase().charAt(0) + "");
                        tvName.setText(TestApplication.contacts.get(index).getName());

                        Toast.makeText(Contactsinfo.this,"Done",Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(Contactsinfo.this,"Error" + fault.getMessage(),Toast.LENGTH_SHORT).show();
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
