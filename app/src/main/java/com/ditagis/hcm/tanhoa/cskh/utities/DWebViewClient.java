package com.ditagis.hcm.tanhoa.cskh.utities;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class DWebViewClient extends WebViewClient {


    public DWebViewClient() {
    }


    // Khi bạn click vào link bên trong trình duyệt (Webview)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i("MyLog", "Click on any interlink on webview that time you got url :-" + url);
        return super.shouldOverrideUrlLoading(view, url);
    }


    // Khi trang bắt đầu được tải
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.i("MyLog", "Your current url when webpage loading.." + url);
    }


    // Khi trang tải xong
    @Override
    public void onPageFinished(WebView view, String url) {
        Log.i("MyLog", "Your current url when webpage loading.. finish" + url);

        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

}