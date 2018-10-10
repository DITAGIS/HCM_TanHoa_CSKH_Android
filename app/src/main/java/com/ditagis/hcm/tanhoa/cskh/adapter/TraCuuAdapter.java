package com.ditagis.hcm.tanhoa.cskh.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;

import java.util.List;

/**
 * Created by ThanLe on 04/10/2017.
 */

public class TraCuuAdapter extends ArrayAdapter<TraCuuAdapter.Item> {
    private Context mContext;
    private List<Item> items;

    public TraCuuAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.mContext = context;
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_grid_tracuu, null);
        }
        Item item = items.get(position);

        TextView txtTop = convertView.findViewById(R.id.txt_item_grid_tracuu_title);
        txtTop.setText(item.getTitle());

        ImageView imageView = convertView.findViewById(R.id.img_item_grid_tracuu);
        imageView.setImageDrawable(mContext.getDrawable(item.getDrawable()));
        return convertView;
    }

    public static class Item {
        private int drawable;
        private String title;

        public Item(String title, int drawable) {
            this.drawable = drawable;
            this.title = title;
        }

        public int getDrawable() {
            return drawable;
        }

        public String getTitle() {
            return title;
        }
    }
}
