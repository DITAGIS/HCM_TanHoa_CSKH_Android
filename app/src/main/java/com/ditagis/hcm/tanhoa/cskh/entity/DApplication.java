package com.ditagis.hcm.tanhoa.cskh.entity;

import android.app.Application;
import android.location.Location;

import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.layers.FeatureLayer;


public class DApplication extends Application {
    public Constant getConstant;

    {
        getConstant = new Constant();
    }

    public User getUserDangNhap;
    public DiemSuCo getDiemSuCo;

    {
        getDiemSuCo = new DiemSuCo();
    }

    private String urlBrowser;

    public String getUrlBrowser() {
        return urlBrowser;
    }

    public void setUrlBrowser(String urlBrowser) {
        this.urlBrowser = urlBrowser;
    }

    private ArcGISFeature arcGISFeature;

    public ArcGISFeature getArcGISFeature() {
        return arcGISFeature;
    }

    public void setArcGISFeature(ArcGISFeature arcGISFeature) {
        this.arcGISFeature = arcGISFeature;
    }

    private FeatureLayer featureLayer;

    public FeatureLayer getFeatureLayer() {
        return featureLayer;
    }

    public void setFeatureLayer(FeatureLayer featureLayer) {
        this.featureLayer = featureLayer;
    }

    private Location mLocation;

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }
}
