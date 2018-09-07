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

public class TraCuuSuCoAdapter extends ArrayAdapter<TraCuuSuCoAdapter.Item> {
    private Context mContext;
    private List<Item> items;

    public TraCuuSuCoAdapter(Context context, List<Item> items) {
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
            convertView = inflater.inflate(R.layout.item_ketqua_tracuusuco, null);
        }
        Item item = items.get(position);

        TextView txtTop = convertView.findViewById(R.id.txt_top_tracuusuco);
        if (item.getIdSuCo() == null || item.getIdSuCo().isEmpty())
            txtTop.setVisibility(View.GONE);
        else txtTop.setText(item.getIdSuCo());

        TextView txtRight = convertView.findViewById(R.id.txt_right_tracuusuco);
        txtRight.setText(item.getTrangThai());
        if (item.getTrangThai() == null || item.getTrangThai().isEmpty())
            txtRight.setVisibility(View.GONE);
        else txtTop.setText(item.getIdSuCo());
        TextView txtBottom = convertView.findViewById(R.id.txt_bottom_tracuusuco);
        txtBottom.setText(item.getDiaChi());

        return convertView;
    }

    public static class Item {
        private String idSuCo;
        private String diaChi;
        private String trangThai;
        private double latitude;
        private double longtitude;

        public Item(String idSuCo, String diaChi, String trangThai) {
            this.idSuCo = idSuCo;
            this.diaChi = diaChi;
            this.trangThai = trangThai;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongtitude() {
            return longtitude;
        }

        public void setLongtitude(double longtitude) {
            this.longtitude = longtitude;
        }

        public String getIdSuCo() {
            return idSuCo;
        }

        public String getDiaChi() {
            return diaChi;
        }

        public String getTrangThai() {
            return trangThai;
        }
    }
}
