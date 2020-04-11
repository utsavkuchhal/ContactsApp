package com.utsav.datatableexample;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class TestApplication extends Application {

    public static final String APPLICATION_ID = "227E0C02-EE26-F849-FF8B-F3D734A94F00";
    public static final String API_KEY = "B1BF1C08-7F8E-49B9-A079-FFBC3F250E61";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;

    public static List<Contact> contacts;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }
}
