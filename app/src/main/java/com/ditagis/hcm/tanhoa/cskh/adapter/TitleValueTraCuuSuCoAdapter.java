package com.ditagis.hcm.tanhoa.cskh.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;

import java.util.List;

/**
 * Created by ThanLe on 04/10/2017.
 */

public class TitleValueTraCuuSuCoAdapter extends ArrayAdapter<TitleValueTraCuuSuCoAdapter.Item> {
    private Context mContext;
    private List<Item> items;

    public TitleValueTraCuuSuCoAdapter(Context context, List<Item> items) {
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
            convertView = inflater.inflate(R.layout.item_tracuutiennuoc, null);
        }
        Item item = items.get(position);

        TextView title = convertView.findViewById(R.id.txt_tracuutiennuoc_tieude);
        title.setText(item.getTitle());

        TextView value = convertView.findViewById(R.id.txt_tracuutiennuoc_giatri);
        value.setText(item.getValue());
        return convertView;
    }

    public static class Item {
        private String title;
        private String value;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Item(String title, String value) {
            this.title = title;
            this.value = value;
        }
    }
}
