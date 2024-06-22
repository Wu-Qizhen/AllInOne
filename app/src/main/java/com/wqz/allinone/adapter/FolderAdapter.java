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
import com.wqz.allinone.database.BookmarkDBHelper;
import com.wqz.allinone.entity.Folder;

import java.util.List;

public class FolderAdapter extends ArrayAdapter<Folder> {
    private final int resourceLayout;
    private final Context mContext;

    // Constructor
    public FolderAdapter(@NonNull Context context,
                         @NonNull List<Folder> objects) {
        super(context, R.layout.item_folder, objects);
        this.resourceLayout = R.layout.item_folder;
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
        Folder folder = getItem(position);

        // Find views
        TextView textViewLinkFolderName = view.findViewById(R.id.tv_name);
        TextView textViewNumber = view.findViewById(R.id.tv_number);

        // Bind data
        if (folder != null) {
            textViewLinkFolderName.setText(folder.getName());
            try (BookmarkDBHelper bookmarkDBHelper = new BookmarkDBHelper(mContext)) {
                textViewNumber.setText(String.format("%s 个书签", bookmarkDBHelper.getLinkCount(folder.getId())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }
}