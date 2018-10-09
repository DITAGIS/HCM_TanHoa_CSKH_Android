package com.ditagis.hcm.tanhoa.cskh.tracuu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ditagis.hcm.tanhoa.cskh.adapter.TitleValueTraCuuSuCoAdapter;
import com.ditagis.hcm.tanhoa.cskh.adapter.TraCuuSuCoAdapter;
import com.ditagis.hcm.tanhoa.cskh.async.QueryFeatureAsync;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entity.Constant;
import com.ditagis.hcm.tanhoa.cskh.entity.DApplication;
import com.esri.arcgisruntime.data.CodedValue;
import com.esri.arcgisruntime.data.CodedValueDomain;
import com.esri.arcgisruntime.data.Domain;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TraCuuSuCoActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.btn_tra_cuu_tra_cuu_su_co)
    Button mBtnTraCuu;
    @BindView(R.id.llayout_ket_qua_tra_cuu_su_co)
    LinearLayout mLayoutKetQua;
    @BindView(R.id.etxtAddress_tra_cuu_su_co)
    EditText mETxtDiaChi;
    @BindView(R.id.spinTrangThai_tra_cuu_su_co)
    Spinner mSpinTrangThai;
    @BindView(R.id.txtThoiGian_tra_cuu_su_co)
    TextView mTxtThoiGian;
    @BindView(R.id.lst_tra_cuu_su_co)
    ListView mLstKetQua;
    @BindView(R.id.progressBarLoadingRecite_tra_cuu_su_co)
    ProgressBar mProgressBar;
    @BindView(R.id.llayout_main_tra_cuu_su_co)
    LinearLayout mLayoutMain;
    @BindView(R.id.mapview_tra_cuu_su_co)
    MapView mMapView;
    @BindView(R.id.txt_tra_cuu_su_co_ket_qua)
    TextView mTxtKetQua;

    private FeatureLayer mFeatureLayer;
    private DApplication mApplication;
    private List<CodedValue> mCodeValues;
    private List<Feature> mFeaturesResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu_su_co);
        ButterKnife.bind(this);
        mBtnTraCuu.setOnClickListener(this);
        mTxtThoiGian.setOnClickListener(this);
        mApplication = (DApplication) getApplication();

        init();
    }

    private void init() {
        startProgressBar();

        ArcGISMap arcGISMap = new ArcGISMap(Basemap.Type.OPEN_STREET_MAP, 10.8035455, 106.6182534, 13);
        mMapView.setMap(arcGISMap);
        mMapView.getMap().addDoneLoadingListener(() -> {
            if (mMapView.getMap().getLoadStatus() == LoadStatus.LOADED) {
                mFeatureLayer = new FeatureLayer(new ServiceFeatureTable(mApplication.getConstant.URL_FEATURE));
                mMapView.getMap().getOperationalLayers().add(mFeatureLayer);
                mFeatureLayer.addDoneLoadingListener(() -> {
                    if (mFeatureLayer.getLoadStatus() == LoadStatus.LOADED) {
                        mApplication.setFeatureLayer(mFeatureLayer);
                        stopProgressBar();
                        initSpinTrangThai();
                        initListViewKetQuaTraCuu();
                    }
                });
            }
        });
    }

    private void initSpinTrangThai() {
        Domain domain = mFeatureLayer.getFeatureTable().getField(Constant.FIELD_SUCO.TRANG_THAI).getDomain();
        if (domain != null) {
            mCodeValues = ((CodedValueDomain) domain).getCodedValues();
            if (mCodeValues != null) {
                List<String> codes = new ArrayList<>();
                codes.add("Tất cả");
                for (CodedValue codedValue : mCodeValues)
                    codes.add(codedValue.getName());
                if (codes.size() > 4)
                    codes.remove(4);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(TraCuuSuCoActivity.this, android.R.layout.simple_list_item_1, codes);
                mSpinTrangThai.setAdapter(adapter);
            }
        }
    }

    private void initListViewKetQuaTraCuu() {
        mLstKetQua.setOnItemClickListener((adapterView, view, i, l) -> {
            if (mFeaturesResult != null & mFeaturesResult.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TraCuuSuCoActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_Alert);
                builder.setTitle("Sự cố");

                @SuppressLint("InflateParams") final ConstraintLayout layout = (ConstraintLayout) TraCuuSuCoActivity.this.getLayoutInflater().
                        inflate(R.layout.layout_item_tracuusuco, null);

                List<TitleValueTraCuuSuCoAdapter.Item> items = new ArrayList<>();
                Feature feature = mFeaturesResult.get(i);
                Map<String, Object> attributes = feature.getAttributes();
                for (Field field : feature.getFeatureTable().getFields()) {
                    Object value = attributes.get(field.getName());
                    if (value != null) {
                        TitleValueTraCuuSuCoAdapter.Item item = new TitleValueTraCuuSuCoAdapter.Item(field.getAlias(), value.toString());
                        if (field.getDomain() != null) {
                            List<CodedValue> codedValues = ((CodedValueDomain) field.getDomain()).getCodedValues();
                            for (CodedValue codedValue : codedValues)
                                if (codedValue.getCode().equals(value)) {
                                    item.setValue(codedValue.getName());
                                    break;
                                }
                        } else switch (field.getFieldType()) {
                            case DATE:
                                item.setValue(Constant.DATE_FORMAT_VIEW.format(((Calendar) value).getTime()));
                                break;
                            case OID:
                            case TEXT:
                            case SHORT:
                            case DOUBLE:
                            case INTEGER:
                            case FLOAT:
                                item.setValue(value.toString());
                                break;
                        }
                        items.add(item);
                    }
                }

                TitleValueTraCuuSuCoAdapter adapter = new TitleValueTraCuuSuCoAdapter(TraCuuSuCoActivity.this, items);
                ListView listView = layout.findViewById(R.id.lst_item_tracuusuco);
                listView.setAdapter(adapter);

                builder.setView(layout);
                final AlertDialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });
    }

    private void startProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLayoutMain.setVisibility(View.GONE);
    }

    private void stopProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mLayoutMain.setVisibility(View.VISIBLE);
    }

    private void showDateTimePicker() {
        final View dialogView = View.inflate(this, R.layout.date_time_picker, null);
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(view -> {
            DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
            Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            String displaytime = (String) DateFormat.format((Constant.DATE_FORMAT_STRING), calendar.getTime());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatGmt = Constant.DATE_FORMAT_YEAR_FIRST;
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            mTxtThoiGian.setText(displaytime);
            alertDialog.dismiss();
        });
        alertDialog.setView(dialogView);
        alertDialog.show();

    }

    private void traCuu() {
//        if (!mTxtThoiGian.getText().toString().equals(TraCuuSuCoActivity.this.getString(R.string.txt_chon_thoi_gian_tracuusuco))) {
        startProgressBar();
        mLayoutKetQua.setVisibility(View.GONE);
        short trangThai = -1;
        for (CodedValue codedValue : mCodeValues) {
            if (codedValue.getName().equals(mSpinTrangThai.getSelectedItem().toString())) {
                trangThai = Short.parseShort(codedValue.getCode().toString());
            }
        }
        new QueryFeatureAsync(this, trangThai,
                mETxtDiaChi.getText().toString(),
                mTxtThoiGian.getText().toString(), output -> {
            stopProgressBar();
            if (output != null && output.size() > 0) {
                mFeaturesResult = output;
                mTxtKetQua.setText(TraCuuSuCoActivity.this.getString(R.string.txt_ket_qua_tra_cuu, output.size()));
                handlingTraCuuHoanTat();
            } else
                mTxtKetQua.setText(TraCuuSuCoActivity.this.getString(R.string.txt_ket_qua_tra_cuu, 0));
        }).execute();
//        } else
//            Toast.makeText(TraCuuSuCoActivity.this, "Vui lòng chọn thời gian", Toast.LENGTH_SHORT).show();
    }

    private void handlingTraCuuHoanTat() {
        List<TraCuuSuCoAdapter.Item> items = new ArrayList<>();
        for (Feature feature : mFeaturesResult) {
            Map<String, Object> attributes = feature.getAttributes();
            for (CodedValue codedValue : mCodeValues) {
                if (Short.parseShort(codedValue.getCode().toString()) ==
                        Short.parseShort(attributes.get(Constant.FIELD_SUCO.TRANG_THAI).toString())) {
                    items.add(new TraCuuSuCoAdapter.Item(attributes.get(Constant.FIELD_SUCO.ID_SU_CO).toString(),
                            attributes.get(Constant.FIELD_SUCO.DIA_CHI).toString(), codedValue.getName()));
                }
            }

        }
        TraCuuSuCoAdapter adapter = new TraCuuSuCoAdapter(TraCuuSuCoActivity.this, items);
        mLstKetQua.setAdapter(adapter);
        mLayoutKetQua.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tra_cuu_tra_cuu_su_co:
                traCuu();
                break;
            case R.id.txtThoiGian_tra_cuu_su_co:
                showDateTimePicker();
                break;
        }
    }
}
