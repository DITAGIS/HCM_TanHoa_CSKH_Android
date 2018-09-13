package com.ditagis.hcm.tanhoa.cskh;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entity.DApplication;
import com.ditagis.hcm.tanhoa.cskh.utities.DWebViewClient;

public class BrowserActivity extends AppCompatActivity {
    private WebView mmWebview;
    private SwipeRefreshLayout mmSwipe;
    private DApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mApplication = (DApplication) getApplication();
        mmWebview = findViewById(R.id.web_browse);
        mmSwipe = findViewById(R.id.swipe_browse);

        // Tùy biến WebViewClient để điều khiển các sự kiện trên WebView
        mmWebview.setWebViewClient(new DWebViewClient());

        mmSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                goUrlWebView();
                goURLBrowser();
                mmSwipe.setRefreshing(false);
            }
        });
        goURLBrowser();
//        goUrlWebView();
    }

    private void goUrlWebView() {
        String url = mApplication.getUrlBrowser();
        if (url == null || url.isEmpty()) {
            Toast.makeText(this, "Please enter url", Toast.LENGTH_SHORT).show();
            return;
        }
        mmWebview.getSettings().setLoadsImagesAutomatically(true);
        mmWebview.getSettings().setJavaScriptEnabled(true);
        mmWebview.getSettings().setUseWideViewPort(true);
        mmWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mmWebview.loadUrl(url);
    }

    private boolean goURLBrowser() {
        boolean result = false;
        String url = mApplication.getUrlBrowser();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        try {
            startActivity(intent);
            result = true;
        } catch (Exception e) {
        }
        return result;
    }


    @Override
    public void onBackPressed() {

        if (mmWebview.canGoBack()) {
            mmWebview.goBack();
        } else {
            goHome();
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


    private void goHome() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
