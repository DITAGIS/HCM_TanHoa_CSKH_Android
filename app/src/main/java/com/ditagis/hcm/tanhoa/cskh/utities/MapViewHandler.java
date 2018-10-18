package com.ditagis.hcm.tanhoa.cskh.utities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;

import com.ditagis.hcm.tanhoa.cskh.BaoSuCoActivity;
import com.ditagis.hcm.tanhoa.cskh.async.SingleTapAddFeatureAsync;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;

/**
 * Created by ThanLe on 2/2/2018.
 */

@SuppressLint("Registered")
public class MapViewHandler extends Activity {
    private android.graphics.Point mClickPoint;
    private MapView mMapView;
    private ServiceFeatureTable mServiceFeatureTable;
    private Popup mPopUp;
    private BaoSuCoActivity mMainActivity;


    public MapViewHandler(FeatureLayer featureLayer, MapView mapView,
                          Popup popupInfos, BaoSuCoActivity mainActivity) {
        this.mMapView = mapView;
        this.mServiceFeatureTable = (ServiceFeatureTable) featureLayer.getFeatureTable();
        this.mPopUp = popupInfos;
        this.mMainActivity = mainActivity;
    }

    public void addFeature(Point pointFindLocation) {
        mClickPoint = mMapView.locationToScreen(pointFindLocation);

        SingleTapAddFeatureAsync singleTapAdddFeatureAsync = new SingleTapAddFeatureAsync(mMainActivity,
                mServiceFeatureTable, output -> {
            if (output != null) {
                mMainActivity.handlingAddFeatureSuccess();
            }else mMainActivity.handlingAddFeatureFail();
        });
        singleTapAdddFeatureAsync.execute();
    }


    public double[] onScroll(MotionEvent e2) {
        Point center = mMapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE).getTargetGeometry().getExtent().getCenter();
        Geometry project = GeometryEngine.project(center, SpatialReferences.getWgs84());
        double[] location = {project.getExtent().getCenter().getX(), project.getExtent().getCenter().getY()};
        mClickPoint = new android.graphics.Point((int) e2.getX(), (int) e2.getY());
        return location;
    }


}