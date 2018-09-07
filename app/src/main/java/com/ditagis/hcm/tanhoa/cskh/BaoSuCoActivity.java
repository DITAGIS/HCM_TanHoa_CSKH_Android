package com.ditagis.hcm.tanhoa.cskh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.tanhoa.cskh.adapter.TraCuuSuCoAdapter;
import com.ditagis.hcm.tanhoa.cskh.async.FindLocationAsycn;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entity.Constant;
import com.ditagis.hcm.tanhoa.cskh.entity.DAddress;
import com.ditagis.hcm.tanhoa.cskh.entity.DApplication;
import com.ditagis.hcm.tanhoa.cskh.utities.MapViewHandler;
import com.ditagis.hcm.tanhoa.cskh.utities.MySnackBar;
import com.ditagis.hcm.tanhoa.cskh.utities.Popup;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaoSuCoActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener,
        AdapterView.OnItemClickListener {
    @BindView(R.id.etxt_querysearch_baosuco)
    EditText mETxtQuery;
    @BindView(R.id.imgBtnSearch_baosuco)
    ImageButton mImgBtnSearch;
    @BindView(R.id.floatBtnLocatoin_baosuco)
    FloatingActionButton mFloatBtnLocation;
    @BindView(R.id.mapview_bao_su_co)
    MapView mMapView;
    @BindView(R.id.progressBarLoadingRecite_baosuco)
    ProgressBar mProgressBar;
    @BindView(R.id.clayout_main_baosuco)
    ConstraintLayout mCLayoutMain;
    @BindView(R.id.lstview_search_baosuco)
    ListView mLstViewSearch;
    @BindView(R.id.txt_info_baosuco)
    TextView mTxtInfo;

    private FeatureLayer mFeatureLayer;
    private DApplication mApplication;
    private Geocoder mGeocoder;
    private TraCuuSuCoAdapter mSearchAdapter;
    private Point mPointFindLocation;
    private GraphicsOverlay mGraphicsOverlay;
    private Popup mPopUp;
    private MapViewHandler mMapViewHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_su_co);
        ButterKnife.bind(this);

        mImgBtnSearch.setOnClickListener(this);
        mETxtQuery.setOnKeyListener(this);

        mApplication = (DApplication) getApplication();
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        startProgressBar();
        mSearchAdapter = new TraCuuSuCoAdapter(BaoSuCoActivity.this, new ArrayList<>());
        mLstViewSearch.setAdapter(mSearchAdapter);
        mLstViewSearch.setOnItemClickListener(this);
        mGeocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());

        ArcGISMap arcGISMap = new ArcGISMap(Basemap.Type.OPEN_STREET_MAP, 10.8035455, 106.6182534, 13);
        mMapView.setMap(arcGISMap);
        mMapView.getMap().addDoneLoadingListener(() -> {
            if (mMapView.getMap().getLoadStatus() == LoadStatus.LOADED) {
                mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mMapView) {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        return super.onSingleTapConfirmed(e);
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        //center is x, y
                        Point center = mMapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE).getTargetGeometry().getExtent().getCenter();

                        //project is long, lat
//                    Geometry project = GeometryEngine.project(center, SpatialReferences.getWgs84());

                        //geometry is x,y
//                    Geometry geometry = GeometryEngine.project(project, SpatialReferences.getWebMercator());
                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED, 20);
                        Graphic graphic = new Graphic(center, symbol);
                        mGraphicsOverlay.getGraphics().clear();
                        mGraphicsOverlay.getGraphics().add(graphic);
                        mPopUp.getCallout().setLocation(center);
                        mPointFindLocation = center;
                        return super.onScroll(e1, e2, distanceX, distanceY);
                    }

                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        return super.onScale(detector);
                    }
                });
                mGraphicsOverlay = new GraphicsOverlay();
                mMapView.getGraphicsOverlays().add(mGraphicsOverlay);
                setLicense();
                ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(mApplication.getConstant.URL_FEATURE);
                mFeatureLayer = new FeatureLayer(serviceFeatureTable);
                mMapView.getMap().getOperationalLayers().add(mFeatureLayer);
                mFeatureLayer.addDoneLoadingListener(() -> {
                    if (mFeatureLayer.getLoadStatus() == LoadStatus.LOADED) {
                        Toast.makeText(BaoSuCoActivity.this, "Tìm kiếm địa chỉ để báo sự cố, hoặc lấy vị trí hiện tại", Toast.LENGTH_LONG).show();
                        mApplication.setFeatureLayer(mFeatureLayer);
                        stopProgressBar();

                        Callout callout = mMapView.getCallout();
                        mPopUp = new Popup(BaoSuCoActivity.this, mMapView, serviceFeatureTable, callout, mGeocoder);
                        mMapViewHandler = new MapViewHandler(mFeatureLayer, mMapView, mPopUp,
                                BaoSuCoActivity.this);
                    }
                });
            }
        });
    }

    private void setLicense() {
        //way 1
        ArcGISRuntimeEnvironment.setLicense(mApplication.getConstant.LICENCE);
        //way 2
//        UserCredential credential = new UserCredential("thanle95", "Gemini111");
//
//// replace the URL with either the ArcGIS Online URL or your portal URL
//        final Portal portal = new Portal("https://than-le.maps.arcgis.com");
//        portal.setCredential(credential);
//
//// load portal and listen to done loading event
//        portal.loadAsync();
//        portal.addDoneLoadingListener(new Runnable() {
//            @Override
//            public void run() {
//                LicenseInfo licenseInfo = portal.getPortalInfo().getLicenseInfo();
//                // Apply the license at Standard level
//                ArcGISRuntimeEnvironment.setLicense(licenseInfo);
//            }
//        });
    }

    private void setViewPointCenter(final Point position) {
        if (mPopUp == null) {
            MySnackBar.make(mMapView, getString(R.string.message_unloaded_map), true);
        } else {
            final Geometry geometry = GeometryEngine.project(position, SpatialReferences.getWebMercator());
            final ListenableFuture<Boolean> booleanListenableFuture = mMapView.setViewpointCenterAsync(geometry.getExtent().getCenter());
            booleanListenableFuture.addDoneListener(() -> {
                try {
                    if (booleanListenableFuture.get()) {
                        BaoSuCoActivity.this.mPointFindLocation = position;
                    }
                    mPopUp.showPopupFindLocation(position);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            });
        }

    }

    private void setViewPointCenterLongLat(Point position, String location) {
        if (mPopUp == null) {
            MySnackBar.make(mMapView, getString(R.string.message_unloaded_map), true);
        } else {
            Geometry geometry = GeometryEngine.project(position, SpatialReferences.getWgs84());
            Geometry geometry1 = GeometryEngine.project(geometry, SpatialReferences.getWebMercator());
            Point point = geometry1.getExtent().getCenter();

            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED, 20);
            Graphic graphic = new Graphic(point, symbol);
            mGraphicsOverlay.getGraphics().add(graphic);

            mMapView.setViewpointCenterAsync(point, mApplication.getConstant.MAX_SCALE_IMAGE_WITH_LABLES);
            mPopUp.showPopupFindLocation(point, location);
            this.mPointFindLocation = point;
        }

    }

    private void themDiemSuCoNoCapture() {
        FindLocationAsycn findLocationAsycn = new FindLocationAsycn(this, false,
                mGeocoder, output -> {
            if (output != null) {
                String adminArea = output.get(0).getAdminArea();
                if (adminArea.equals(mApplication.getConstant.ADMIN_AREA_TPHCM)) {
                    mApplication.getDiemSuCo.setPoint(mPointFindLocation);
                    mApplication.getDiemSuCo.setVitri(output.get(0).getLocation());
                    Intent intent = new Intent(this, NhapThongTinSuCoActivity.class);
                    startActivityForResult(intent, Constant.REQUEST_CODE_ADD_FEATURE);
//
                } else {
                    Toast.makeText(BaoSuCoActivity.this, R.string.message_not_area_management, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.message_not_area_management, Toast.LENGTH_LONG).show();
            }

        });
        Geometry project = GeometryEngine.project(mPointFindLocation, SpatialReferences.getWgs84());
        double[] location = {project.getExtent().getCenter().getX(), project.getExtent().getCenter().getY()};
        findLocationAsycn.setmLongtitude(location[0]);
        findLocationAsycn.setmLatitude(location[1]);
        findLocationAsycn.execute();
    }

    private void startProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTxtInfo.setVisibility(View.VISIBLE);
        mCLayoutMain.setVisibility(View.GONE);
    }

    private void stopProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mTxtInfo.setVisibility(View.GONE);
        mCLayoutMain.setVisibility(View.VISIBLE);
    }

    private void deleteSearching() {
        mGraphicsOverlay.getGraphics().clear();
        mSearchAdapter.clear();
        mSearchAdapter.notifyDataSetChanged();
    }

    private void search() {
        String query = mETxtQuery.getText().toString();
        if (query.length() > 0) {
            deleteSearching();
            FindLocationAsycn findLocationAsycn = new FindLocationAsycn(BaoSuCoActivity.this,
                    true, mGeocoder, output -> {
                if (output != null) {
                    mSearchAdapter.notifyDataSetChanged();
                    if (output.size() > 0) {
                        for (DAddress address : output) {
                            TraCuuSuCoAdapter.Item item = new TraCuuSuCoAdapter.Item("", address.getLocation(), "");
                            item.setLatitude(address.getLatitude());
                            item.setLongtitude(address.getLongtitude());
                            mSearchAdapter.add(item);
                        }
                        mSearchAdapter.notifyDataSetChanged();
                    }
                }

            });
            findLocationAsycn.execute(query);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnSearch_baosuco:
                search();
                break;
            case R.id.imgBtn_timkiemdiachi_themdiemsuco:
                themDiemSuCoNoCapture();
                break;
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        switch (view.getId()) {
            case R.id.etxt_querysearch_baosuco:
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                    search();
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TraCuuSuCoAdapter.Item item = ((TraCuuSuCoAdapter.Item) adapterView.getItemAtPosition(i));
        deleteSearching();
        setViewPointCenterLongLat(new Point(item.getLongtitude(), item.getLatitude()), item.getDiaChi());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.REQUEST_CODE_ADD_FEATURE:
                if (mApplication.getDiemSuCo.getPoint() != null) {
                    mMapViewHandler.addFeature(mApplication.getDiemSuCo.getPoint());
                    deleteSearching();
                }
                break;
        }
    }
}
