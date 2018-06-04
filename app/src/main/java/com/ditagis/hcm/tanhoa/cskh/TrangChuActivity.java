package com.ditagis.hcm.tanhoa.cskh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ditagis.hcm.tanhoa.cskh.acynchronize.FindDongHoKhachHangAsycn;
import com.ditagis.hcm.tanhoa.cskh.acynchronize.FindKhachHangAsycn;
import com.ditagis.hcm.tanhoa.cskh.acynchronize.FindLichSuDSAsycn;
import com.ditagis.hcm.tanhoa.cskh.adapter.TitleValueAdapter;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entity.DongHoKhachHang;
import com.ditagis.hcm.tanhoa.cskh.entity.KhachHang;
import com.ditagis.hcm.tanhoa.cskh.entity.LichSuDocSo;
import com.ditagis.hcm.tanhoa.cskh.utities.Preference;
import com.ditagis.hcm.tanhoa.cskh.utities.Utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TrangChuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout mLayout;
    private KhachHang mKhachHang;
    private DongHoKhachHang mDongHoKH;
    private LichSuDocSo mLichSuDS;
    private DrawerLayout mDrawer;
    private TextView mTxtValidation;
    private TitleValueAdapter mAdapter;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        mLayout = findViewById(R.id.layout_content_trangchu);
        mLayout.setVisibility(View.VISIBLE);

        //-----------------------------
        //start default
        //-----------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTxtValidation = findViewById(R.id.txt_content_trangchu_validation);
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //-----------------------------
        //end default
        //-----------------------------

        //lấy thông tin từ bảng khách hàng
        FindKhachHangAsycn asycn = new FindKhachHangAsycn(this, new FindKhachHangAsycn.AsyncResponse() {
            @Override
            public void processFinish(KhachHang output) {
                if (output == null) {
                    finish();
                    return;
                }
                mKhachHang = output;
                setInfoMainPageByKhachHang();
                //nếu đồng hồ còn hiệu lực, thì lấy thông tin đọc số
                if (mKhachHang.getHieuLuc() > 0) {

                    mTxtValidation.setVisibility(View.GONE);
                    getInfoLichSuDS();
                }
                //ngược lại, hiện thông báo
                else {
                    handleDongHoKhachHangInvalid();
                }
            }
        });
        asycn.execute(Preference.getInstance().loadPreference(getString(R.string.preference_username)),
                Preference.getInstance().loadPreference(getString(R.string.preference_password)));


    }

    /**
     * xử lý trường hợp đồng hồ khách hàng không còn hiệu lực
     */
    private void handleDongHoKhachHangInvalid() {
        mTxtValidation.setText(R.string.validate_hieuluc);
        mTxtValidation.setVisibility(View.VISIBLE);

        mListView.setVisibility(View.GONE);
    }

    /**
     * @input đợt và máy từ lớp khách hàng
     * Lấy thông tin mới nhất gán vào lớp lịch sử đọc số
     */
    private void getInfoLichSuDS() {
        FindLichSuDSAsycn lichSuDSAsycn = new FindLichSuDSAsycn(this, new FindLichSuDSAsycn.AsyncResponse() {
            @Override
            public void processFinish(LichSuDocSo output) {
                if (output != null) {
                    mLichSuDS = output;
                    getInfoDongHoKhachHang();
                } else {
                    handleDongHoKhachHangInvalid();
                }
            }
        });
        lichSuDSAsycn.execute(mKhachHang.getDot(), mKhachHang.getMay());
    }

    /**
     * @input năm, kỳ từ lớp lịch sử đọc số và danh bạ từ lớp khách hàng
     * Lấy thông tin chỉ số, tiêu thụ, tiền nước gán vào lớp đồng hồ khách hàng
     */
    private void getInfoDongHoKhachHang() {
        FindDongHoKhachHangAsycn findDongHoKhachHangAsycn = new FindDongHoKhachHangAsycn(this,
                new FindDongHoKhachHangAsycn.AsyncResponse() {
                    @Override
                    public void processFinish(DongHoKhachHang output) {
                        if (output != null) {
                            mDongHoKH = output;
                            setInfoMainPageByDongHoKhachHang();
                        }
                    }
                });
        findDongHoKhachHangAsycn.execute(String.format(getString(R.string.format_number), mLichSuDS.getNam()),
                String.format(getString(R.string.format_number_two), mLichSuDS.getKy()), mKhachHang.getDanhBa());
    }

    /**
     * set text với những thông tin lấy được từ lớp khách hàng
     */
    private void setInfoMainPageByKhachHang() {
        String tenKH = mKhachHang.getTenKH();
        ((TextView) findViewById(R.id.txt_content_trangchu_danhBo)).setText(
                String.format(getString(R.string.format_content_trangchu_danhbo), mKhachHang.getDanhBa()));
        StringBuilder builder = new StringBuilder(tenKH);
        if (!mKhachHang.getSdt().isEmpty())
            builder.append(String.format(getString(R.string.format_new_line), mKhachHang.getSdt()));
        builder.append(String.format(getString(R.string.format_newline_address), mKhachHang.getSo(),
                mKhachHang.getDuong()));
        ((TextView) findViewById(R.id.txt_content_trangchu_info_invidual)).setText(builder.toString());
        ((TextView) findViewById(R.id.txt_content_trangchu_sh)).setText(
                String.format(getString(R.string.format_content_trangchu_sh), mKhachHang.getSh()));
        ((TextView) findViewById(R.id.txt_content_trangchu_sx)).setText(
                String.format(getString(R.string.format_content_trangchu_sx), mKhachHang.getSx()));
        ((TextView) findViewById(R.id.txt_content_trangchu_dv)).setText(
                String.format(getString(R.string.format_content_trangchu_dv), mKhachHang.getDv()));
        ((TextView) findViewById(R.id.txt_content_trangchu_hc)).setText(
                String.format(getString(R.string.format_content_trangchu_hc), mKhachHang.getHc()));

        //nếu tên khách hàng dài hơn 10 ký tự, thì hiển thị dấu ... phía sau để thay thế
        ((TextView) mDrawer.findViewById(R.id.nav_header_tenkh)).setText(tenKH.length() > 10 ?
                (String.format(getString(R.string.format_nav_tenkh_over), tenKH.substring(0, 10))) :
                (String.format(getString(R.string.format_nav_tenkh), tenKH)));
        ((TextView) mDrawer.findViewById(R.id.nav_header_diachi)).setText(
                String.format(getString(R.string.format_content_trangchu_diachi),
                        mKhachHang.getSo(), mKhachHang.getDuong()));
        mListView = findViewById(R.id.lstView_info_main_page);
        mAdapter = new TitleValueAdapter(TrangChuActivity.this,
                new ArrayList<TitleValueAdapter.Item>());
        mListView.setAdapter(mAdapter);
        mAdapter.add(new TitleValueAdapter.Item(getString(R.string.giabieu), String.format(getString(R.string.format_number), mKhachHang.getGb())));
        mAdapter.add(new TitleValueAdapter.Item(getString(R.string.dinhmuc), String.format(getString(R.string.format_number), mKhachHang.getDm())));
        mAdapter.notifyDataSetChanged();

        mLayout.setVisibility(View.VISIBLE);
    }

    /**
     * set text với những thông tin lấy được từ lớp đồng hồ khách hàng
     */
    private void setInfoMainPageByDongHoKhachHang() {
        mAdapter.add(new TitleValueAdapter.Item(getString(R.string.cscu),
                String.format(getString(R.string.format_number_m3), mDongHoKH.getCscu())));
        mAdapter.add(new TitleValueAdapter.Item(getString(R.string.csmoi),
                String.format(getString(R.string.format_number_m3), mDongHoKH.getCsmoi())));
        mAdapter.add(new TitleValueAdapter.Item(getString(R.string.tieuthu),
                String.format(getString(R.string.format_number_m3), mDongHoKH.getTieuthumoi())));
        mAdapter.add(new TitleValueAdapter.Item(getString(R.string.tongtien),
                String.format(getString(R.string.format_number_money), Utils.getInstance().getNumberFormat().format(mDongHoKH.getTongTien()))));
        mAdapter.notifyDataSetChanged();

        mLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout mDrawer = findViewById(R.id.drawer_layout);
//        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
//            mDrawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trang_chu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            // Handle the camera action
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(TrangChuActivity.this, TraCuuActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_alert) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}