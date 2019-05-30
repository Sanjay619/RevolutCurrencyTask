package com.revoluttask.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.revoluttask.R;
import com.revoluttask.model.CurrencyModel;

import java.text.DecimalFormat;
import java.util.List;

public class CurrencyExchangerAdapter extends RecyclerView.Adapter<CurrencyExchangerAdapter.MyViewHolder> {

    private List<CurrencyModel> currencyModelList;
    AdapterResponse adapterResponse;
    Activity activity;


    public CurrencyExchangerAdapter(Activity activity, List<CurrencyModel> currencyModelList) {
        this.currencyModelList = currencyModelList;
        adapterResponse = (AdapterResponse) activity;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CurrencyModel currecny = currencyModelList.get(position);

        holder.etCurrencyRate.setTag(currecny);
        holder.tvCurrency.setText(currecny.getName());
        DecimalFormat f = new DecimalFormat("#.##");

        holder.etCurrencyRate.setTag(currecny);

        holder.etCurrencyRate.setText(f.format(currecny.getAmount()));


        holder.llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapterResponse.OnPayMentChange(currecny);
            }


        });


    }




    @Override
    public int getItemCount() {
        return currencyModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCurrency;
        public EditText etCurrencyRate;
        public LinearLayout llView;


        public MyViewHolder(View view) {
            super(view);

            tvCurrency = view.findViewById(R.id.tvTitle);
            etCurrencyRate = view.findViewById(R.id.etRate);


            llView = view.findViewById(R.id.llView);
        }


    }


    public interface AdapterResponse {
        void OnPayMentChange(CurrencyModel currencyModel);
    }


}
