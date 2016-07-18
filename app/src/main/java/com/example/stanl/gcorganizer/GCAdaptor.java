package com.example.stanl.gcorganizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GCAdaptor extends BaseAdapter {
    private List<Card> listData;
    private LayoutInflater layoutInflater;

    public GCAdaptor(Context aContext, List<Card> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.dis_card_row_layout, null);
            holder = new ViewHolder();
            holder.cardNumberView = (TextView) convertView.findViewById(R.id.card_number);
            holder.storeNameView = (TextView) convertView.findViewById(R.id.store_name);
            // holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.cardNumberView.setText(listData.get(position).card_number);
        holder.storeNameView.setText("By, " + listData.get(position).store_name);
        //holder.reportedDateView.setText(listData.get(position).getDate());
        return convertView;
    }

    static class ViewHolder {
        TextView cardNumberView;
        TextView storeNameView;
        TextView reportedDateView;
    }
}