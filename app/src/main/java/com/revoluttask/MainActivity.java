package com.revoluttask;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.revoluttask.adapter.CurrencyExchangerAdapter;
import com.revoluttask.interfaces.OnVolleyResult;
import com.revoluttask.model.CurrencyModel;
import com.revoluttask.services.VolleyHelper;
import com.revoluttask.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity implements CurrencyExchangerAdapter.AdapterResponse, OnVolleyResult {


    private RecyclerView recyclerView;
    private TextView tvTitle;
    private  EditText edtRate;
    private CurrencyExchangerAdapter mAdapter;
    private Double d = 0.0;
    private  List<CurrencyModel> list = new ArrayList<>();
    private  String selectedParam = "EUR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initField();

        try {
            setDataInAdapter();
            startUpdatetingValue();

        }catch (Exception e){

        }

        edtRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()) {
                    d = Double.parseDouble(editable.toString().trim());
                    for(int i = 0 ; i < list.size(); i ++){
                        list.get(i).setAmount(d * list.get(i).getAmount());
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }



    private void startUpdatetingValue() {

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                try {

                    while(true) {
                        Thread.sleep(1000);
                        sendRequest();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private  void sendRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(Utility.getInstance().isNetworkAvailable(MainActivity.this) && Utility.getInstance().isInternetAvailable()){
                    String url = "https://revolut.duckdns.org/latest?base=" +selectedParam;
                    VolleyHelper.getInstance().sendRequest(MainActivity.this, url );
                }

            }
        }).start();

    }

    private void initField() {
        recyclerView = findViewById(R.id.recyclerView);
        tvTitle = findViewById(R.id.tvTitle);
        edtRate = findViewById(R.id.etRate);
        edtRate.setText(String.valueOf(d));
    }

    private void setDataInAdapter(){

        mAdapter = new CurrencyExchangerAdapter(this, list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());


        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }


    @Override
    public void OnPayMentChange(CurrencyModel currencyModel) {
        Log.d("OnPayMentChange", currencyModel.getName().trim());
        selectedParam = currencyModel.getName();
        tvTitle.setText(currencyModel.getName());
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        edtRate.setText(decimalFormat.format(currencyModel.getAmount()));


    }

    @Override
    public void onResultSuccess(String response, String from) {
        try {
            updateListValue(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateListValue(String json) throws JSONException {
        JSONObject mainObj = new JSONObject(json);
        tvTitle.setText(mainObj.getString("base"));


        JSONObject rateBaseObj = mainObj.getJSONObject("rates");
        Log.e("100", rateBaseObj.toString());
        Iterator<?> keys = rateBaseObj.keys();
        list.clear();
        while( keys.hasNext() ) {
            String key = (String)keys.next();


            CurrencyModel m = new CurrencyModel();
            m.setName(key);
            if(!edtRate.getText().toString().isEmpty()) {
                Double c = d * rateBaseObj.getDouble(key);
                m.setAmount(c);
            }else{
                m.setAmount(rateBaseObj.getDouble(key));
            }
            list.add(m);


        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });

    }
}
