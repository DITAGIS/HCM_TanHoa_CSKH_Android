package com.ditagis.hcm.tanhoa.cskh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
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
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.UniqueValueRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
    private boolean mIsAddingFeature;
    private LocationDisplay mLocationDisplay;
    private boolean mIsFirstLocating = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_su_co);
        ButterKnife.bind(this);
        (Objects.requireNonNull(getSupportActionBar())).setDisplayHomeAsUpEnabled(true);
        (Objects.requireNonNull(getSupportActionBar())).setDisplayShowHomeEnabled(true);
        mImgBtnSearch.setOnClickListener(this);
        mETxtQuery.setOnKeyListener(this);

        mApplication = (DApplication) getApplication();
        requestPermisson();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        if (mApplication.getKhachHang() != null && mApplication.getKhachHang().getSo() != null &&
                mApplication.getKhachHang().getDuong() != null)
            mETxtQuery.setText(mApplication.getKhachHang().getSo() + " " + mApplication.getKhachHang().getDuong()
            );
        startProgressBar();
        mIsAddingFeature = false;
        mFloatBtnLocation.setOnClickListener(this::onClick);
        mSearchAdapter = new TraCuuSuCoAdapter(BaoSuCoActivity.this, new ArrayList<>());
        mLstViewSearch.setAdapter(mSearchAdapter);
        mLstViewSearch.setOnItemClickListener(this);
        mGeocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());

        ArcGISMap arcGISMap = new ArcGISMap(Basemap.Type.OPEN_STREET_MAP, 10.8035455, 106.6182534, 13);
        mMapView.setMap(arcGISMap);
        mMapView.getMap().addDoneLoadingListener(() -> {
            if (mMapView.getMap().getLoadStatus() == LoadStatus.LOADED) {
                mLocationDisplay = mMapView.getLocationDisplay();
                mLocationDisplay.startAsync();
                mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mMapView) {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        return super.onSingleTapConfirmed(e);
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        //center is x, y
                        mGraphicsOverlay.getGraphics().clear();
                        if (mIsAddingFeature) {
                            Point center = mMapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE).getTargetGeometry().getExtent().getCenter();

                            //project is long, lat
//                    Geometry project = GeometryEngine.project(center, SpatialReferences.getWgs84());

                            //geometry is x,y
//                    Geometry geometry = GeometryEngine.project(project, SpatialReferences.getWebMercator());
                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED, 20);
                            Graphic graphic = new Graphic(center, symbol);
                            mGraphicsOverlay.getGraphics().add(graphic);
                            mPopUp.getCallout().setLocation(center);
                            mPointFindLocation = center;
                        }
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
                mFeatureLayer.setDefinitionExpression("1 = 0");
                mMapView.getMap().getOperationalLayers().add(mFeatureLayer);
                mFeatureLayer.addDoneLoadingListener(() -> {
                    if (mFeatureLayer.getLoadStatus() == LoadStatus.LOADED) {
                        setRendererSuCoFeatureLayer(mFeatureLayer);
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

    public void requestPermisson() {
        boolean permissionCheck1 = ContextCompat.checkSelfPermission(this,
                Constant.REQUEST_PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED;
        boolean permissionCheck2 = ContextCompat.checkSelfPermission(this,
                Constant.REQUEST_PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED;
        boolean permissionCheck3 = ContextCompat.checkSelfPermission(this,
                Constant.REQUEST_PERMISSIONS[2]) == PackageManager.PERMISSION_GRANTED;
        boolean permissionCheck4 = ContextCompat.checkSelfPermission(this,
                Constant.REQUEST_PERMISSIONS[3]) == PackageManager.PERMISSION_GRANTED;

        if (!(permissionCheck1 && permissionCheck2 && permissionCheck3 && permissionCheck4)) {

            // If permissions are not already granted, request permission from the user.
            ActivityCompat.requestPermissions(this, Constant.REQUEST_PERMISSIONS, Constant.REQUEST_CODE_PERMISSION);
        }  // Report other unknown failure types to the user - for example, location services may not // be enabled on the device. //                    String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent //                            .getSource().getLocationDataSource().getError().getMessage()); //                    Toast.makeText(QuanLySuCo.this, message, Toast.LENGTH_LONG).show();
        else {
            init();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean isGrant = true;
        for (int i : grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED) {
                isGrant = false;
                break;
            }
        }
        if (isGrant) {
            init();
        } else finish();
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

        mSearchAdapter.clear();
        mSearchAdapter.notifyDataSetChanged();
    }

    public void handlingAddFeatureSuccess() {
        mGraphicsOverlay.getGraphics().clear();
        if (mLocationDisplay.isStarted())
            mLocationDisplay.stop();
        if (mPopUp.getCallout() != null & mPopUp.getCallout().isShowing())
            mPopUp.getCallout().dismiss();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("CTY CP Cấp nước Tân Hòa");

        // set dialog message
        alertDialogBuilder
                .setMessage("Xin cảm ơn quý khách đã báo sự cố!")
                .setCancelable(true)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }
    public void handlingAddFeatureFail() {
        Toast.makeText(this,"Không báo được sự cố. Vui lòng thử lại sau", Toast.LENGTH_LONG).show();

    }

    private void handlingLocation() {
        if (mIsFirstLocating) {
            mIsFirstLocating = false;
            mLocationDisplay.stop();
            mLocationDisplay.startAsync();
            setViewPointCenter(mLocationDisplay.getMapLocation());
            mIsAddingFeature = true;
        } else {
            if (mLocationDisplay.isStarted()) {
                mLocationDisplay.stop();
                if (mPopUp.getCallout() != null && mPopUp.getCallout().isShowing())
                    mPopUp.getCallout().dismiss();
            } else {
                mLocationDisplay.startAsync();
                setViewPointCenter(mLocationDisplay.getMapLocation());
                mIsAddingFeature = true;
            }
        }
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

    private void setRendererSuCoFeatureLayer(FeatureLayer mSuCoTanHoaLayer) {
        UniqueValueRenderer uniqueValueRenderer = new UniqueValueRenderer();
        uniqueValueRenderer.getFieldNames().add(Constant.FIELD_SUCO.TRANG_THAI);
        uniqueValueRenderer.getFieldNames().add(Constant.FIELD_SUCO.HINH_THUC_PHAT_HIEN);


        PictureMarkerSymbol chuaXuLySymbol = new PictureMarkerSymbol(mApplication.getConstant.URL_SYMBOL_CHUA_SUA_CHUA);
        chuaXuLySymbol.setHeight(Constant.SIZE_FEATURE_RENDERER);
        chuaXuLySymbol.setWidth(Constant.SIZE_FEATURE_RENDERER);

        PictureMarkerSymbol dangXuLySymbol = new PictureMarkerSymbol(mApplication.getConstant.URL_SYMBOL_DANG_SUA_CHUA);
        dangXuLySymbol.setHeight(Constant.SIZE_FEATURE_RENDERER);
        dangXuLySymbol.setWidth(Constant.SIZE_FEATURE_RENDERER);

        PictureMarkerSymbol hoanThanhSymBol = new PictureMarkerSymbol(mApplication.getConstant.URL_SYMBOL_HOAN_THANH);
        hoanThanhSymBol.setHeight(Constant.SIZE_FEATURE_RENDERER);
        hoanThanhSymBol.setWidth(Constant.SIZE_FEATURE_RENDERER);

        PictureMarkerSymbol beNgamSymbol = new PictureMarkerSymbol(mApplication.getConstant.URL_SYMBOL_CHUA_SUA_CHUA_BE_NGAM);
        beNgamSymbol.setHeight(Constant.SIZE_FEATURE_RENDERER);
        beNgamSymbol.setWidth(Constant.SIZE_FEATURE_RENDERER);

        uniqueValueRenderer.setDefaultSymbol(chuaXuLySymbol);
        uniqueValueRenderer.setDefaultLabel("Chưa xác định");

        List<Object> chuaXuLyValue = new ArrayList<>();
        chuaXuLyValue.add(0);

        //đang xử lý: begin
        List<Object> dangXuLyValue = new ArrayList<>();
        dangXuLyValue.add(1);
        dangXuLyValue.add(1);
        List<Object> dangXuLyValue1 = new ArrayList<>();
        dangXuLyValue1.add(1);
        dangXuLyValue1.add(2);

        List<Object> dangXuLyValue2 = new ArrayList<>();
        dangXuLyValue2.add(1);
        dangXuLyValue2.add(3);

        List<Object> dangXuLyValue3 = new ArrayList<>();
        dangXuLyValue3.add(1);
        dangXuLyValue3.add(4);

        List<Object> dangXuLyValue4 = new ArrayList<>();
        dangXuLyValue4.add(1);
        dangXuLyValue4.add(5);

        List<Object> dangXuLyValue5 = new ArrayList<>();
        dangXuLyValue5.add(1);
        dangXuLyValue5.add(6);
        //đang xỷ lý: end

        List<Object> beNgamChuaXuLyValue = new ArrayList<>();
        beNgamChuaXuLyValue.add(0);
        beNgamChuaXuLyValue.add(1);

        //hoàn thành: begin
        List<Object> hoanThanhValue = new ArrayList<>();
        hoanThanhValue.add(3);
        hoanThanhValue.add(1);
        List<Object> hoanThanhValue1 = new ArrayList<>();
        hoanThanhValue1.add(3);
        hoanThanhValue1.add(2);

        List<Object> hoanThanhValue2 = new ArrayList<>();
        hoanThanhValue2.add(3);
        hoanThanhValue2.add(3);

        List<Object> hoanThanhValue3 = new ArrayList<>();
        hoanThanhValue3.add(3);
        hoanThanhValue3.add(4);

        List<Object> hoanThanhValue4 = new ArrayList<>();
        hoanThanhValue4.add(3);
        hoanThanhValue4.add(5);

        List<Object> hoanThanhValue5 = new ArrayList<>();
        hoanThanhValue5.add(3);
        hoanThanhValue5.add(6);
        //hoàn thành: end

        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Chưa xử lý", "Chưa xử lý", chuaXuLySymbol, chuaXuLyValue));

        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue1));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue2));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue3));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue4));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Đang xử lý", "Đang xử lý", dangXuLySymbol, dangXuLyValue5));

        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Chưa xử lý bể ngầm", "Chưa xử lý bể ngầm", beNgamSymbol, beNgamChuaXuLyValue));

        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue1));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue2));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue3));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue4));
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue(
                "Hoàn thành", "Hoàn thành", hoanThanhSymBol, hoanThanhValue5));
        mSuCoTanHoaLayer.setRenderer(uniqueValueRenderer);
        mSuCoTanHoaLayer.loadAsync();


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
            case R.id.floatBtnLocatoin_baosuco:
                handlingLocation();
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
        mIsAddingFeature = true;
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
                    handlingAddFeatureSuccess();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                goHome();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        goHome();
    }


    public void goHome() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
