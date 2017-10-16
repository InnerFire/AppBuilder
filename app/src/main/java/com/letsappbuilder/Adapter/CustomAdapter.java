package com.letsappbuilder.Adapter;

/**
 * Created by Savaliya Imfotech on 03-01-2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.letsappbuilder.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] fontfamilylist;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] fontlist) {
        this.context = applicationContext;
        fontfamilylist = fontlist;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return fontfamilylist.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_item, null);
        TextView names = (TextView) view.findViewById(R.id.txt_custom_spinner);
        names.setTypeface(Typeface.create(fontfamilylist[i], Typeface.NORMAL));
        names.setText("Hello World!");
        return view;
    }
}
