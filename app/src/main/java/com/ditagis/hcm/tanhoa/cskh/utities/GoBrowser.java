package com.ditagis.hcm.tanhoa.cskh.utities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.ditagis.hcm.tanhoa.cskh.entity.DApplication;

public class GoBrowser {
    public void goURL(Activity activity) {
        String url = ((DApplication) activity.getApplication()).getUrlBrowser();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), "Không thể tra cứu thông báo lúc này.", Toast.LENGTH_LONG).show();
        }
    }
}
