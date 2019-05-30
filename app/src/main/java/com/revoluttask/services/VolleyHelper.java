package com.revoluttask.services;

import android.annotation.SuppressLint;
import android.content.Context;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.revoluttask.interfaces.OnVolleyResult;




public class VolleyHelper {

    private OnVolleyResult onVolleyResult;
    private Context context;

    private boolean isDialog;
    private String url;
    private String from;

    @SuppressLint("StaticFieldLeak")
    private static final VolleyHelper ourInstance = new VolleyHelper();

    public static VolleyHelper getInstance() {
        return ourInstance;
    }

    private VolleyHelper() {
    }

    private void setConstructor(Context context,  String url, String from) {
        this.context = context;

        this.isDialog = isDialog;
        this.url = url;
        this.from = from;
    }

    /**
     *  Handling Webservices Request
     */
    // For Activity
    public void sendRequest(final Context context,  final String url) {

        setConstructor(context,url,from);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onVolleyResult = (OnVolleyResult) context;
                        onVolleyResult.onResultSuccess(response, from);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onErrorMessage(context,error);
                        onVolleyResult = (OnVolleyResult) context;
                        onVolleyResult.onResultSuccess("", from);
                    }
                }
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(context).addToRequestQueue(stringRequest,null);
    }
    /**
     * Handling Server Errors
     */
    private void onErrorMessage(Context context, VolleyError error) {
        if (error instanceof TimeoutError) {
            // Indicates that the request has time out

        } else if (error instanceof NoConnectionError) {
            // There is no connection
        } else if (error instanceof AuthFailureError) {
            // There was an Authentication Failure while performing the request
        } else if (error instanceof ServerError) {
            // The server responded with a error response

        } else if (error instanceof NetworkError) {
            // Indicates that there was network error while performing the request

        } else if (error instanceof ParseError) {
            // Indicates that the server response could not be parsed

        }
    }

}
