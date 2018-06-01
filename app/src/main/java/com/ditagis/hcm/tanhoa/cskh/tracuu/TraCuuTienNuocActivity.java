package com.ditagis.hcm.tanhoa.cskh.tracuu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ditagis.hcm.tanhoa.cskh.adapter.TitleValueTraCuuTienNuocAdapter;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;

import java.util.ArrayList;
import java.util.Calendar;

public class TraCuuTienNuocActivity extends AppCompatActivity implements View.OnClickListener {
    private DatePicker mDatePicker;
    private TextView mTxtMonthYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu_tien_nuoc);
        mTxtMonthYear = findViewById(R.id.txt_tracuutiennuoc_MonthYear);
        Calendar calendar = Calendar.getInstance();
        mTxtMonthYear.setText(String.format(getString(R.string.format_monthyear), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
        ListView lstView = findViewById(R.id.lstView_tracuutiennuoc);
        TitleValueTraCuuTienNuocAdapter adapter = new TitleValueTraCuuTienNuocAdapter(
                this, new ArrayList<TitleValueTraCuuTienNuocAdapter.Item>());

        lstView.setAdapter(adapter);
        adapter.add(new TitleValueTraCuuTienNuocAdapter.Item("Chỉ số cũ", "300"));
        adapter.add(new TitleValueTraCuuTienNuocAdapter.Item("Chỉ số mới", "310"));
        adapter.add(new TitleValueTraCuuTienNuocAdapter.Item("Tiêu thụ", "10"));
        adapter.add(new TitleValueTraCuuTienNuocAdapter.Item("Tiền nước", "55.000đ"));
        adapter.add(new TitleValueTraCuuTienNuocAdapter.Item("Thuế VAT", "2.500đ"));
        adapter.add(new TitleValueTraCuuTienNuocAdapter.Item("Phí BVMT", "5.500đ"));
        adapter.add(new TitleValueTraCuuTienNuocAdapter.Item("Tổng cộng", "66.000đ"));
        adapter.notifyDataSetChanged();

        ((RelativeLayout) findViewById(R.id.layout_tracuutiennuoc_select_time)).setOnClickListener(this);
    }

    @SuppressLint("ValidFragment")

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void selectTime() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogLayout = inflater.inflate(R.layout.dialog_datepicker, null);
        this.mDatePicker = dialogLayout.findViewById(R.id.datepicker);
        this.mDatePicker.setMaxDate(System.currentTimeMillis());
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
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ;
                    }
                }).setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTxtMonthYear.setText(String.format("%02d", mDatePicker.getMonth() + 1) +
                        "/" + mDatePicker.getYear());
                dialogInterface.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
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
}
