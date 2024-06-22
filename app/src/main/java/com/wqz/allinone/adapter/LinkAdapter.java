package com.wqz.allinone.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wqz.allinone.R;
import com.wqz.allinone.entity.Link;

import java.util.List;

public class LinkAdapter extends ArrayAdapter<Link> {
    private final int resourceLayout;
    private final Context mContext;

    // Constructor
    public LinkAdapter(@NonNull Context context,
                       @NonNull List<Link> objects) {
        super(context, R.layout.item_link, objects);
        this.resourceLayout = R.layout.item_link;
        this.mContext = context;
    }

    // Overriding method to create view
    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the layout
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(mContext).inflate(resourceLayout, null);

        // Get the data item for this position
        Link link = getItem(position);

        // Find views
        TextView textViewLinkName = view.findViewById(R.id.tv_title);
        TextView textViewNumber = view.findViewById(R.id.tv_url);

        // Bind data
        if (link != null) {
            textViewLinkName.setText(link.getTitle());
            textViewNumber.setText(link.getUrl());
        }

        return view;
    }
}