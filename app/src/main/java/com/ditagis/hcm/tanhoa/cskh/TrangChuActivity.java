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

import com.ditagis.hcm.tanhoa.cskh.acynchronize.FindKhachHangAsycn;
import com.ditagis.hcm.tanhoa.cskh.adapter.TitleValueAdapter;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entity.KhachHang;
import com.ditagis.hcm.tanhoa.cskh.utities.Preference;

import java.util.ArrayList;

public class TrangChuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout mLayout;
    private KhachHang mKhachHang;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        mLayout = findViewById(R.id.layout_content_trangchu);
        mLayout.setVisibility(View.INVISIBLE);
        //-----------------------------
        //start default
        //-----------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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

        FindKhachHangAsycn asycn = new FindKhachHangAsycn(this, new FindKhachHangAsycn.AsyncResponse() {
            @Override
            public void processFinish(KhachHang output) {
                if (output == null) {
                    finish();
                    return;
                }
                mKhachHang = output;
                setInfoMainPage();
            }
        });
        asycn.execute(Preference.getInstance().loadPreference(getString(R.string.preference_username)),
                Preference.getInstance().loadPreference(getString(R.string.preference_password)));


    }

    private void setInfoMainPage() {
        String tenKH = mKhachHang.getTenKH();
        ((TextView) findViewById(R.id.txt_content_trangchu_danhBo)).setText(
                String.format(getString(R.string.format_content_trangchu_danhbo), mKhachHang.getDanhBa()));
        ((TextView) findViewById(R.id.txt_content_trangchu_tenKH)).setText(tenKH);
        ((TextView) findViewById(R.id.txt_content_trangchu_sdt)).setText(mKhachHang.getSdt());
        ((TextView) findViewById(R.id.txt_content_trangchu_diachi)).setText(
                String.format(getString(R.string.format_content_trangchu_diachi),
                        mKhachHang.getSo(), mKhachHang.getDuong()));
        ((TextView) findViewById(R.id.txt_content_trangchu_sh)).setText(
                String.format(getString(R.string.format_content_trangchu_sh), mKhachHang.getSh()));
        ((TextView) findViewById(R.id.txt_content_trangchu_sx)).setText(
                String.format(getString(R.string.format_content_trangchu_sx), mKhachHang.getSx()));
        ((TextView) findViewById(R.id.txt_content_trangchu_dv)).setText(
                String.format(getString(R.string.format_content_trangchu_dv), mKhachHang.getDv()));
        ((TextView) findViewById(R.id.txt_content_trangchu_hc)).setText(
                String.format(getString(R.string.format_content_trangchu_hc), mKhachHang.getHc()));
        //nếu tên khách hàng dài hơn 15 ký tự, thì hiển thị dấu ... phía sau để thay thế
        ((TextView) mDrawer.findViewById(R.id.nav_header_tenkh)).setText(tenKH.length() > 15 ?
                (String.format(getString(R.string.format_nav_tenkh_over), tenKH.substring(0, 15))) :
                (String.format(getString(R.string.format_nav_tenkh), tenKH)));
        ((TextView) mDrawer.findViewById(R.id.nav_header_diachi)).setText(
                String.format(getString(R.string.format_content_trangchu_diachi),
                        mKhachHang.getSo(), mKhachHang.getDuong()));
        ListView lstView = findViewById(R.id.lstView_info_main_page);
        TitleValueAdapter adapter = new TitleValueAdapter(TrangChuActivity.this,
                new ArrayList<TitleValueAdapter.Item>());
        lstView.setAdapter(adapter);
        adapter.add(new TitleValueAdapter.Item(getString(R.string.giabieu), String.format(getString(R.string.format_number), mKhachHang.getGb())));
        adapter.add(new TitleValueAdapter.Item(getString(R.string.dinhmuc), String.format(getString(R.string.format_number), mKhachHang.getDm())));
        adapter.add(new TitleValueAdapter.Item(getString(R.string.cscu), "335"));
        adapter.add(new TitleValueAdapter.Item(getString(R.string.csmoi), "365"));
        adapter.add(new TitleValueAdapter.Item(getString(R.string.tieuthu), "30"));
        adapter.add(new TitleValueAdapter.Item(getString(R.string.tongtien), "150.000"));
        adapter.notifyDataSetChanged();

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
