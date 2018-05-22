package cskh.tanhoa.com.ditagis.hcm.cskh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class TraCuuActivity extends AppCompatActivity {
    private LinearLayout mLayoutTraCuuTienNuoc, mLayoutTraCuuGiaNuoc, mLayoutTraCuuSuCo, mLayoutTraCuuThongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu);
        Animation animFade1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade1);
        Animation animFade2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade2);
        Animation animFade3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade3);
        Animation animFade4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade4);

        mLayoutTraCuuTienNuoc = findViewById(R.id.layout_tracuu_tiennuoc);
        mLayoutTraCuuGiaNuoc = findViewById(R.id.layout_tracuu_gianuoc);
        mLayoutTraCuuSuCo = findViewById(R.id.layout_tracuu_suco);
        mLayoutTraCuuThongBao = findViewById(R.id.layout_tracuu_thongbao);

        mLayoutTraCuuTienNuoc.startAnimation(animFade1);
        mLayoutTraCuuGiaNuoc.startAnimation(animFade2);
        mLayoutTraCuuSuCo.startAnimation(animFade3);
        mLayoutTraCuuThongBao.startAnimation(animFade4);
    }
}
