package com.ditagis.hcm.tanhoa.cskh.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.ditagis.hcm.tanhoa.cskh.entities.Constant;
import com.ditagis.hcm.tanhoa.cskh.entities.DApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PreparingByAPIAsycn extends AsyncTask<Void, Boolean, String> {
    private Activity mActivity;
    private DApplication mApplication;
    private AsyncResponse mDelegate;

    public interface AsyncResponse {

        void processFinish(String output);
    }

    public PreparingByAPIAsycn(Activity activity, AsyncResponse delegate) {
        this.mActivity = activity;
        this.mDelegate = delegate;
        mApplication = (DApplication) activity.getApplication();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(Void... params) {
        try {
            return getLayerInfoAPI();
        } catch (Exception e) {
            Log.e("Lỗi", e.toString());
        }
        return null;
    }


    @Override
    protected void onPostExecute(String value) {
        this.mDelegate.processFinish(value);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getLayerInfoAPI() {
        try {
            String API_URL = Constant.URL_API.LAYER_INFO;

            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", mApplication.getUserDangNhap().getToken());
                conn.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                return pajsonRouteeJSon(builder.toString());
            } catch (Exception e) {
                Log.e("error", e.toString());
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            Log.e("Lỗi lấy LayerInfo", e.toString());
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String pajsonRouteeJSon(String data) throws JSONException {
        if (data == null)
            return "";
        String myData = "{ \"layerInfo\": ".concat(data).concat("}");
        JSONObject jsonData = new JSONObject(myData);
        JSONArray jsonRoutes = jsonData.getJSONArray("layerInfo");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            String id = jsonRoute.getString("LayerID");
            if (id.equals("diemsucoLYR")) {
                String url = jsonRoute.getString("Url");
                mApplication.setURLFeature(url);
                return url;
            }
        }
        return "";

    }

}
