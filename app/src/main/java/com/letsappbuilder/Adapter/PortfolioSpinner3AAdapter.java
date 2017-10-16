package com.letsappbuilder.Adapter;

/**
 * Created by Savaliya Imfotech on 03-01-2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.letsappbuilder.R;

public class PortfolioSpinner3AAdapter extends BaseAdapter {
    Context context;
    int[] imageList;
    LayoutInflater inflter;

    public PortfolioSpinner3AAdapter(Context applicationContext, int[] fontlist) {
        this.context = applicationContext;
        imageList = fontlist;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return imageList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_portfolio_3a_item, null);
        ImageView names = (ImageView) view.findViewById(R.id.img_portfolio_spinner);
        names.setImageResource(imageList[i]);
        return view;
    }
}
