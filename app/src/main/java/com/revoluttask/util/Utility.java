package com.revoluttask.util;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utility {
    private static final Utility ourInstance = new Utility();

    public static Utility getInstance() {
        return ourInstance;
    }

    private Utility() {
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }
}
