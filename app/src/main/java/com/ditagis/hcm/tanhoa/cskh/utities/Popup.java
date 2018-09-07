package com.ditagis.hcm.tanhoa.cskh.utities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ditagis.hcm.tanhoa.cskh.BaoSuCoActivity;
import com.ditagis.hcm.tanhoa.cskh.async.FindLocationAsycn;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entity.Constant;
import com.ditagis.hcm.tanhoa.cskh.entity.DAddress;
import com.ditagis.hcm.tanhoa.cskh.entity.DApplication;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.CodedValue;
import com.esri.arcgisruntime.data.CodedValueDomain;
import com.esri.arcgisruntime.data.FeatureType;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


@SuppressLint("Registered")
public class Popup extends AppCompatActivity implements View.OnClickListener {
    private BaoSuCoActivity mMainActivity;
    private ArcGISFeature mSelectedArcGISFeature = null;
    private ServiceFeatureTable mServiceFeatureTable;
    private Callout mCallout;
    private List<String> lstFeatureType;
    private LinearLayout linearLayout;
    private MapView mMapView;
    private Geocoder mGeocoder;
    private String mIDSuCo;
    private Button mBtnLeft;
    private DApplication mApplication;

    public Popup(BaoSuCoActivity mainActivity, MapView mapView, ServiceFeatureTable serviceFeatureTable,
                 Callout callout, Geocoder geocoder) {
        this.mMainActivity = mainActivity;
        this.mApplication = (DApplication) mainActivity.getApplication();
        this.mMapView = mapView;
        this.mServiceFeatureTable = serviceFeatureTable;
        this.mCallout = callout;
        this.mGeocoder = geocoder;


    }


    public Button getmBtnLeft() {
        return mBtnLeft;
    }

    public Callout getCallout() {
        return mCallout;
    }


    private void dimissCallout() {
        if (mCallout != null && mCallout.isShowing()) {
            mCallout.dismiss();
        }
    }

    @SuppressLint("InflateParams")
    public void showPopupFindLocation(Point position, String location) {
        try {
            if (position == null)
                return;
//            clearSelection();
            dimissCallout();

            LayoutInflater inflater = LayoutInflater.from(this.mMainActivity.getApplicationContext());
            linearLayout = (LinearLayout) inflater.inflate(R.layout.layout_timkiemdiachi, null);

            ((TextView) linearLayout.findViewById(R.id.txt_timkiemdiachi)).setText(location);
            linearLayout.findViewById(R.id.imgBtn_timkiemdiachi_themdiemsuco).setOnClickListener(this);
            linearLayout.findViewById(R.id.imgBtn_cancel_timkiemdiachi).setOnClickListener(view -> mCallout.dismiss());

            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // show CallOut
            mCallout.setLocation(position);
            mCallout.setContent(linearLayout);
            this.runOnUiThread(() -> {
                mCallout.refresh();
                mCallout.show();
            });
        } catch (Exception e) {
            Log.e("Popup tìm kiếm", e.toString());
        }

    }

    public void showPopupFindLocation(final Point position) {
        try {
            if (position == null)
                return;

            @SuppressLint("InflateParams") FindLocationAsycn findLocationAsycn = new FindLocationAsycn(mMainActivity, false,
                    mGeocoder, output -> {
                if (output != null && output.size() > 0) {
//                    clearSelection();
                    dimissCallout();
                    DAddress address = output.get(0);
                    String addressLine = address.getLocation();
                    LayoutInflater inflater = LayoutInflater.from(mMainActivity.getApplicationContext());
                    linearLayout = (LinearLayout) inflater.inflate(R.layout.layout_timkiemdiachi, null);
                    ((TextView) linearLayout.findViewById(R.id.txt_timkiemdiachi)).setText(addressLine);
                    linearLayout.findViewById(R.id.imgBtn_timkiemdiachi_themdiemsuco).setOnClickListener(Popup.this);
                    linearLayout.findViewById(R.id.imgBtn_cancel_timkiemdiachi).setOnClickListener(view -> mCallout.dismiss());
                    linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    // show CallOut
                    mCallout.setLocation(position);
                    mCallout.setContent(linearLayout);
                    Popup.this.runOnUiThread(() -> {
                        mCallout.refresh();
                        mCallout.show();
                    });
                }
            });
            Geometry project = GeometryEngine.project(position, SpatialReferences.getWgs84());
            double[] location = {project.getExtent().getCenter().getX(), project.getExtent().getCenter().getY()};
            findLocationAsycn.setmLongtitude(location[0]);
            findLocationAsycn.setmLatitude(location[1]);
            findLocationAsycn.execute();
        } catch (Exception e) {
            Log.e("Popup tìm kiếm", e.toString());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtn_timkiemdiachi_themdiemsuco:
                mMainActivity.onClick(view);
                break;
        }
    }
}
