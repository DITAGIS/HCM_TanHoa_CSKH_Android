package com.ditagis.hcm.tanhoa.cskh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.GridView;

import com.ditagis.hcm.tanhoa.cskh.adapter.TraCuuAdapter;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entity.Constant;
import com.ditagis.hcm.tanhoa.cskh.entity.DApplication;
import com.ditagis.hcm.tanhoa.cskh.tracuu.TraCuuGiaNuocActivity;
import com.ditagis.hcm.tanhoa.cskh.tracuu.TraCuuSuCoActivity;
import com.ditagis.hcm.tanhoa.cskh.tracuu.TraCuuThongBaoActivity;
import com.ditagis.hcm.tanhoa.cskh.tracuu.TraCuuTienNuocActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TraCuuActivity extends AppCompatActivity {
    @BindView(R.id.grid_tracuu)
    GridView mGridView;
    private DApplication mApplication;
    private TraCuuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu);
        mApplication = (DApplication) getApplication();
        ButterKnife.bind(this);
        (Objects.requireNonNull(getSupportActionBar())).setDisplayHomeAsUpEnabled(true);
        (Objects.requireNonNull(getSupportActionBar())).setDisplayShowHomeEnabled(true);
        List<TraCuuAdapter.Item> items = new ArrayList<>();
        items.add(new TraCuuAdapter.Item(Constant.TitleTraCuu.TIEN_NUOC, R.drawable.bill));
        items.add(new TraCuuAdapter.Item(Constant.TitleTraCuu.GIA_NUOC, R.drawable.budget));
//        items.add(new TraCuuAdapter.Item(Constant.TitleTraCuu.SU_CO, R.drawable.ic_su_co));
        items.add(new TraCuuAdapter.Item(Constant.TitleTraCuu.THONG_BAO, R.drawable.notification));
        mAdapter = new TraCuuAdapter(this, items);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener((adapterView, view, i, l) -> {
            TraCuuAdapter.Item item = (TraCuuAdapter.Item) adapterView.getItemAtPosition(i);
            switch (item.getTitle()) {
                case Constant.TitleTraCuu.TIEN_NUOC:
                    Intent intent = new Intent(TraCuuActivity.this, TraCuuTienNuocActivity.class);
                    startActivity(intent);
                    break;
                case Constant.TitleTraCuu.GIA_NUOC:
                    mApplication.setUrlBrowser(mApplication.getConstant.URL_GIA_NUOC);
                    Intent intentGiaNuoc = new Intent(TraCuuActivity.this, TraCuuGiaNuocActivity.class);
                    startActivity(intentGiaNuoc);
                    break;
                case Constant.TitleTraCuu.SU_CO:
                    Intent intentSuCo = new Intent(TraCuuActivity.this, TraCuuSuCoActivity.class);
                    startActivity(intentSuCo);
                    break;
                case Constant.TitleTraCuu.THONG_BAO:
                    Intent intentThongBao = new Intent(TraCuuActivity.this, TraCuuThongBaoActivity.class);
                    startActivity(intentThongBao);
                    break;
            }
        });
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
