package com.ditagis.hcm.tanhoa.cskh.tracuu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.ditagis.hcm.tanhoa.cskh.adapter.TitleValueTraCuuTienNuocAdapter;
import com.ditagis.hcm.tanhoa.cskh.async.ThongTinKHAPIAsycn;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.DApplication;
import com.ditagis.hcm.tanhoa.cskh.utities.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class TraCuuTienNuocActivity extends AppCompatActivity implements View.OnClickListener {
    private DatePicker mDatePicker;
    private TextView mTxtMonthYear;
    private TextView mtxtValidation;
    private ListView mListView;
    private DApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu_tien_nuoc);
        mApplication = (DApplication) getApplication();
        mTxtMonthYear = findViewById(R.id.txt_tracuutiennuoc_MonthYear);
        Calendar calendar = Calendar.getInstance();
        mTxtMonthYear.setText(String.format(getString(R.string.format_monthyear), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
        mtxtValidation = findViewById(R.id.txt_tracuutiennuoc_validation);
        mListView = findViewById(R.id.lstView_tracuutiennuoc);
        (Objects.requireNonNull(getSupportActionBar())).setDisplayHomeAsUpEnabled(true);
        (Objects.requireNonNull(getSupportActionBar())).setDisplayShowHomeEnabled(true);
        findViewById(R.id.layout_tracuutiennuoc_select_time).setOnClickListener(this);
    }

    @SuppressLint("ValidFragment")

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void selectTime() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogLayout = inflater.inflate(R.layout.dialog_datepicker, null);
        this.mDatePicker = dialogLayout.findViewById(R.id.datepicker);
        this.mDatePicker.setMaxDate(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 0, 1);
        this.mDatePicker.setMinDate(calendar.getTimeInMillis());
        int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
        if (daySpinnerId != 0) {
            View daySpinner = dialogLayout.findViewById(daySpinnerId);
            if (daySpinner != null) {
                daySpinner.setVisibility(View.GONE);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setCancelable(true);
        builder.setView(dialogLayout);
        builder.setTitle("Chọn thời gian")
                .setNegativeButton("Thoát", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Chọn", (dialogInterface, i) -> {
                    mTxtMonthYear.setText(String.format(getString(R.string.format_monthyear),
                            mDatePicker.getMonth() + 1, mDatePicker.getYear()));
                    dialogInterface.dismiss();

                    getInfoDongHoKhachHang(String.format(getString(R.string.format_number_two),
                            mDatePicker.getMonth() + 1), String.format(getString(R.string.format_number),
                            mDatePicker.getYear()));
                });

        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    /**
     * @input kỳ, năm, danh bạ
     * Hiển thị thông tin lên listview
     */
    private void getInfoDongHoKhachHang(String month, String year) {
        ThongTinKHAPIAsycn asycn = new ThongTinKHAPIAsycn(this, output -> {
            if (output == null) {
                handleFindDongHoKhachHangFail();
                return;
            }

            TitleValueTraCuuTienNuocAdapter adapter = new TitleValueTraCuuTienNuocAdapter(
                    TraCuuTienNuocActivity.this, new ArrayList<TitleValueTraCuuTienNuocAdapter.Item>());

            mListView.setAdapter(adapter);
            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.cscu),
                    String.format(getString(R.string.format_number_m3), output.getChiSoCu())));
            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.csmoi),
                    String.format(getString(R.string.format_number_m3), output.getChiSoMoi())));
            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.tieuthu),
                    String.format(getString(R.string.format_number_m3), output.getTieuThu())));
            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.tiennuoc),
                    String.format(getString(R.string.format_number_money), Utils.getInstance().getNumberFormat().format(output.getThanhTien()))));
            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.thueVAT),
                    String.format(getString(R.string.format_number_money), Utils.getInstance().getNumberFormat().format(output.getThueVAT()))));
            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.phiBVMT),
                    String.format(getString(R.string.format_number_money), Utils.getInstance().getNumberFormat().format(output.getBVMT()))));
            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.tongtien),
                    String.format(getString(R.string.format_number_money), Utils.getInstance().getNumberFormat().format(output.getTongTien()))));
            adapter.notifyDataSetChanged();

            mListView.setVisibility(View.VISIBLE);
            mtxtValidation.setVisibility(View.GONE);
        });
        asycn.execute( mApplication.getUserDangNhap().getUserName(),year, month);
//        FindDongHoKhachHangAsycn asycn = new FindDongHoKhachHangAsycn(this, output -> {
//            if (output == null) {
//                handleFindDongHoKhachHangFail();
//                return;
//            }
//
//            TitleValueTraCuuTienNuocAdapter adapter = new TitleValueTraCuuTienNuocAdapter(
//                    TraCuuTienNuocActivity.this, new ArrayList<TitleValueTraCuuTienNuocAdapter.Item>());
//
//            mListView.setAdapter(adapter);
//            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.cscu),
//                    String.format(getString(R.string.format_number_m3), output.getCscu())));
//            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.csmoi),
//                    String.format(getString(R.string.format_number_m3), output.getCsmoi())));
//            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.tieuthu),
//                    String.format(getString(R.string.format_number_m3), output.getTieuthumoi())));
//            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.tiennuoc),
//                    String.format(getString(R.string.format_number_money), Utils.getInstance().getNumberFormat().format(output.getTienNuoc()))));
//            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.thueVAT),
//                    String.format(getString(R.string.format_number_money), Utils.getInstance().getNumberFormat().format(output.getThueVAT()))));
//            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.phiBVMT),
//                    String.format(getString(R.string.format_number_money), Utils.getInstance().getNumberFormat().format(output.getPhiBVMT()))));
//            adapter.add(new TitleValueTraCuuTienNuocAdapter.Item(getString(R.string.tongtien),
//                    String.format(getString(R.string.format_number_money), Utils.getInstance().getNumberFormat().format(output.getTongTien()))));
//            adapter.notifyDataSetChanged();
//
//            mListView.setVisibility(View.VISIBLE);
//            mtxtValidation.setVisibility(View.GONE);
//        });
//        asycn.execute(year, month, Preference.getInstance().loadPreference(getString(R.string.preference_username)));


    }

    private void handleFindDongHoKhachHangFail() {
        mListView.setVisibility(View.GONE);
        mtxtValidation.setVisibility(View.VISIBLE);
        mtxtValidation.setText(R.string.validate_tracuutiennuoc_invalidtime);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_tracuutiennuoc_select_time:
                selectTime();
                break;
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
