package com.ditagis.hcm.tanhoa.cskh.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.ditagis.hcm.tanhoa.cskh.entities.Constant;
import com.ditagis.hcm.tanhoa.cskh.entities.DApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class GenerateIDSuCoAsycn extends AsyncTask<Void, Void, String> {
    private ProgressDialog mDialog;
    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;
    private AsyncResponse mDelegate;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    GenerateIDSuCoAsycn(Activity activity, AsyncResponse delegate) {
        this.mActivity = activity;
        this.mDelegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        this.mDialog = new ProgressDialog(this.mActivity.getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
//        this.mDialog.setMessage(mActivity.getString(R.string.message_preparing));
//        this.mDialog.setCancelable(false);
//        this.mDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        //Tránh gặp lỗi networkOnMainThread nên phải dùng asyncTask
        String id = "";
        try {
            URL url = new URL(Constant.URL_API.GENERATE_ID_SUCO);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", ((DApplication) mActivity.getApplication()).getUserDangNhap().getToken());
                conn.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = bufferedReader.readLine();
                id = line.replace("\"", "");
            } catch (Exception e) {
                Log.e("error", e.toString());
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            publishProgress();
            Log.e("Lỗi lấy IDSuCo", e.toString());
        }
        return id;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);


    }

    @Override
    protected void onPostExecute(String value) {
//        if (khachHang != null) {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        this.mDelegate.processFinish(value);
//        }
    }


}
