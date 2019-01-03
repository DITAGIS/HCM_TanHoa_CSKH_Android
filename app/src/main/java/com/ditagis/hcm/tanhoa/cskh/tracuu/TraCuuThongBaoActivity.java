package com.ditagis.hcm.tanhoa.cskh.tracuu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.ditagis.hcm.tanhoa.cskh.adapter.TitleValueAdapter;
import com.ditagis.hcm.tanhoa.cskh.connectDB.KhachHangDB;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.Constant;
import com.ditagis.hcm.tanhoa.cskh.entities.DApplication;
import com.ditagis.hcm.tanhoa.cskh.entities.HoaDon;
import com.ditagis.hcm.tanhoa.cskh.utities.GoBrowser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TraCuuThongBaoActivity extends AppCompatActivity {
    @BindView(R.id.list_tracuuthongbao)
    ListView mListView;

    private TitleValueAdapter mAdapter;
    private DApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu_thong_bao);
        ButterKnife.bind(this);
        (Objects.requireNonNull(getSupportActionBar())).setDisplayHomeAsUpEnabled(true);
        (Objects.requireNonNull(getSupportActionBar())).setDisplayShowHomeEnabled(true);
        mApplication = (DApplication) getApplication();
        List<TitleValueAdapter.Item> items = new ArrayList<>();
        items.add(new TitleValueAdapter.Item(Constant.TitleTraCuuThongBao.DONG_TIEN_NUOC, null));
        items.add(new TitleValueAdapter.Item(Constant.TitleTraCuuThongBao.CUP_NUOC, null));
        items.add(new TitleValueAdapter.Item(Constant.TitleTraCuuThongBao.KHAC, null));
        mAdapter = new TitleValueAdapter(this, items);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            TitleValueAdapter.Item item = (TitleValueAdapter.Item) adapterView.getItemAtPosition(i);
            switch (item.getTitle()) {
                case Constant.TitleTraCuuThongBao.DONG_TIEN_NUOC:
                    traCuuDongTienNuoc();
                    break;
                case Constant.TitleTraCuuThongBao.CUP_NUOC:
                    break;
                case Constant.TitleTraCuuThongBao.KHAC:
                    mApplication.setUrlBrowser("http://www.capnuoctanhoa.com.vn/customer");
                    GoBrowser goBrowser = new GoBrowser();
                    goBrowser.goURL(TraCuuThongBaoActivity.this);
                    break;
            }
        });
    }

    private void traCuuDongTienNuoc() {
        KhachHangDB khachHangDB = new KhachHangDB(TraCuuThongBaoActivity.this);
        String ky = String.format(TraCuuThongBaoActivity.this.getString(R.string.format_number_two), mApplication.getDongHoKhachHang().getKy());
        String hoaDonID = mApplication.getDongHoKhachHang().getNam() +
                ky + mApplication.getDongHoKhachHang().getDanhBa();
        HoaDon bill = khachHangDB.getBill(hoaDonID);
        if (bill != null) {
            Toast.makeText(TraCuuThongBaoActivity.this, "Khách hàng đã thanh toán tiền nước kỳ " + ky, Toast.LENGTH_LONG).show();
            //đã thanh toán
        } else {
            Toast.makeText(TraCuuThongBaoActivity.this, "Khách hàng chưa thanh toán tiền nước kỳ " + ky, Toast.LENGTH_LONG).show();
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
