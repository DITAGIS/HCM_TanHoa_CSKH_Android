package com.ditagis.hcm.tanhoa.cskh.async;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.Constant;
import com.ditagis.hcm.tanhoa.cskh.entities.DApplication;
import com.ditagis.hcm.tanhoa.cskh.entities.NamKy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ThongTinKHNamKyAPIAsycn extends AsyncTask<String, Void, NamKy> {
    private ProgressDialog mDialog;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private ThongTinKHNamKyAPIAsycn.AsyncResponse mDelegate;
    private DApplication mApplication;


    public interface AsyncResponse {
        void processFinish(NamKy dongHoKhachHang);
    }

    public ThongTinKHNamKyAPIAsycn(Context context, ThongTinKHNamKyAPIAsycn.AsyncResponse delegate) {
        this.mContext = context;
        this.mDelegate = delegate;
        mApplication = (DApplication) context.getApplicationContext();
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.mDialog = new ProgressDialog(this.mContext, android.R.style.Theme_Material_Dialog_Alert);
        this.mDialog.setMessage(mContext.getString(R.string.connect_message));
        this.mDialog.setCancelable(false);
        this.mDialog.show();
    }

    @Override
    protected NamKy doInBackground(String... params) {
        if (params != null && params.length > 0) {
            String danhBa = params[0];
//        String passEncoded = (new EncodeMD5()).encode(pin + "_DITAGIS");
            // Do some validation here
            String urlWithParam = String.format(Constant.URL_API.LAY_NAM_KY, danhBa);

            try {
                URL url = new URL(urlWithParam);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
            }
        }
        return null;
    }


    @Override
    protected void onPostExecute(NamKy result) {
        mDialog.dismiss();
        this.mDelegate.processFinish(result);
    }

    private NamKy pajsonRouteeJSon(String data) throws JSONException {
        if (data == null)
            return null;
        String myData = "{ \"account\": ".concat(data).concat("}");
        JSONObject jsonData = new JSONObject(myData);
        JSONArray jsonRoutes = jsonData.getJSONArray("account");

        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            NamKy namKy = new NamKy(jsonRoute.getInt("Nam"),
                    jsonRoute.getString("Thang"));

            return namKy;
        }
        return null;

    }
}
