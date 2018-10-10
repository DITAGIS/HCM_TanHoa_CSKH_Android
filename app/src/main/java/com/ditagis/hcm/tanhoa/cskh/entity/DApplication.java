package com.ditagis.hcm.tanhoa.cskh.entity;

import android.app.Application;
import android.location.Location;

import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.layers.FeatureLayer;

import java.util.List;


public class DApplication extends Application {
    public Constant getConstant;

    {
        getConstant = new Constant();
    }

    public DiemSuCo getDiemSuCo;

    {
        getDiemSuCo = new DiemSuCo();
    }

    private KhachHang khachHang;

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    private User userDangNhap;

    public User getUserDangNhap() {
        return userDangNhap;
    }

    public void setUserDangNhap(User userDangNhap) {
        this.userDangNhap = userDangNhap;
    }

    private List<DLayerInfo> dLayerInfos;

    public List<DLayerInfo> getdLayerInfos() {
        return dLayerInfos;
    }

    public void setdLayerInfos(List<DLayerInfo> dLayerInfos) {
        this.dLayerInfos = dLayerInfos;
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
