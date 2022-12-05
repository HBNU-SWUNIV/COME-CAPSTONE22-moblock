package com.example.hyperledgervirtualmoneyproject.ui.home.CoinListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hyperledgervirtualmoneyproject.R;

import java.util.ArrayList;

public class CoinLIstAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<CoinData> sample;

    public CoinLIstAdapter(Context context, ArrayList<CoinData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CoinData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_view_coinlist, null);

        TextView movieName = (TextView)view.findViewById(R.id.coinListVIew_CoinName);
        TextView grade = (TextView)view.findViewById(R.id.coinListVIew_CoinValue);

        movieName.setText(sample.get(position).getCoinName());
        grade.setText(sample.get(position).getCoinValue());

        return view;
    }
}