package com.ditagis.hcm.tanhoa.cskh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entity.DApplication;
import com.ditagis.hcm.tanhoa.cskh.tracuu.TraCuuGiaNuocActivity;
import com.ditagis.hcm.tanhoa.cskh.tracuu.TraCuuSuCoActivity;
import com.ditagis.hcm.tanhoa.cskh.tracuu.TraCuuTienNuocActivity;
import com.ditagis.hcm.tanhoa.cskh.utities.GoBrowser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TraCuuActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.llayout_tracuu_gianuoc)
    LinearLayout mLayoutTraCuuGiaNuoc;
    @BindView(R.id.llayout_tracuu_suco)
    LinearLayout mLayoutTraCuuSuCo;
    @BindView(R.id.llayout_tracuu_thongbao)
    LinearLayout mLayoutTraCuuThongBao;
    @BindView(R.id.llayout_tracuu_tiennuoc)
    LinearLayout mLayoutTraCuuTienNuoc;
    private DApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu);
        mApplication = (DApplication) getApplication();
        ButterKnife.bind(this);
        Animation animFade1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade1);
        Animation animFade2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade2);
        Animation animFade3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade3);
        Animation animFade4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade4);


        mLayoutTraCuuTienNuoc.startAnimation(animFade1);
        mLayoutTraCuuGiaNuoc.startAnimation(animFade2);
        mLayoutTraCuuSuCo.startAnimation(animFade3);
        mLayoutTraCuuThongBao.startAnimation(animFade4);

        mLayoutTraCuuTienNuoc.setOnClickListener(this);
        mLayoutTraCuuSuCo.setOnClickListener(this);
        mLayoutTraCuuGiaNuoc.setOnClickListener(this);
        mLayoutTraCuuThongBao.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llayout_tracuu_tiennuoc:
                Intent intent = new Intent(TraCuuActivity.this, TraCuuTienNuocActivity.class);
                startActivity(intent);
                break;
            case R.id.llayout_tracuu_suco:
                Intent intentSuCo = new Intent(TraCuuActivity.this, TraCuuSuCoActivity.class);
                startActivity(intentSuCo);
                break;
            case R.id.llayout_tracuu_gianuoc:
                mApplication.setUrlBrowser(mApplication.getConstant.URL_GIA_NUOC);
                Intent intentGiaNuoc = new Intent(TraCuuActivity.this, TraCuuGiaNuocActivity.class);
                startActivity(intentGiaNuoc);
                break;
            case R.id.llayout_tracuu_thongbao:
                mApplication.setUrlBrowser("http://www.capnuoctanhoa.com.vn/customer");
                GoBrowser goBrowser = new GoBrowser();
                goBrowser.goURL(TraCuuActivity.this);
//                Intent intentBrowser = new Intent(TraCuuActivity.this, BrowserActivity.class);
//                startActivity(intentBrowser);
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
    public void onBackPressed() {
        goHome();
    }

    private void goHome() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
