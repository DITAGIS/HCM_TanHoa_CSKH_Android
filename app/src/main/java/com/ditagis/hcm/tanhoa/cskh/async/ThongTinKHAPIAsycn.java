package com.ditagis.hcm.tanhoa.cskh.async;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.Constant;
import com.ditagis.hcm.tanhoa.cskh.entities.DApplication;
import com.ditagis.hcm.tanhoa.cskh.entities.DongHoKhachHang;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ThongTinKHAPIAsycn extends AsyncTask<String, Void, DongHoKhachHang> {
    private ProgressDialog mDialog;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private ThongTinKHAPIAsycn.AsyncResponse mDelegate;
    private DApplication mApplication;

    private String mNam;
    private String mKy;

    public interface AsyncResponse {
        void processFinish(DongHoKhachHang dongHoKhachHang);
    }

    public ThongTinKHAPIAsycn(Context context, ThongTinKHAPIAsycn.AsyncResponse delegate) {
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
    protected DongHoKhachHang doInBackground(String... params) {
        if (params != null && params.length == 3) {
            String danhBa = params[0];
            mNam = params[1];
            mKy = params[2];
//        String passEncoded = (new EncodeMD5()).encode(pin + "_DITAGIS");
            // Do some validation here
            String urlWithParam = String.format(Constant.URL_API.LAY_CHI_SO, danhBa, mNam, mKy);

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
    protected void onPostExecute(DongHoKhachHang result) {
        mDialog.dismiss();
        this.mDelegate.processFinish(result);
    }

    private DongHoKhachHang pajsonRouteeJSon(String data) throws JSONException {
        if (data == null)
            return null;
        String myData = "{ \"account\": ".concat(data).concat("}");
        JSONObject jsonData = new JSONObject(myData);
        JSONArray jsonRoutes = jsonData.getJSONArray("account");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            DongHoKhachHang dongHoKhachHang = new DongHoKhachHang(
                    jsonRoute.getString(Constant.KhachHangColumn.DANH_BO),
                    jsonRoute.getString(Constant.KhachHangColumn.HOP_DONG),
                    jsonRoute.getString(Constant.KhachHangColumn.TEN_KH),
                    jsonRoute.getString(Constant.KhachHangColumn.DIA_CHI),
                    jsonRoute.getString(Constant.KhachHangColumn.SDT),
                    jsonRoute.getInt(Constant.KhachHangColumn.TY_LE_SH),
                    jsonRoute.getInt(Constant.KhachHangColumn.TY_LE_SX),
                    jsonRoute.getInt(Constant.KhachHangColumn.TY_LE_DV),
                    jsonRoute.getInt(Constant.KhachHangColumn.TY_LE_HC),
                    jsonRoute.getInt(Constant.KhachHangColumn.GB),
                    jsonRoute.getInt(Constant.KhachHangColumn.DM),
                    jsonRoute.getInt(Constant.KhachHangColumn.CSC),
                    jsonRoute.getInt(Constant.KhachHangColumn.CSM),
                    jsonRoute.getInt(Constant.KhachHangColumn.TIEU_THU),
                    jsonRoute.getDouble(Constant.KhachHangColumn.BVMT),
                    jsonRoute.getDouble(Constant.KhachHangColumn.GTGT),
                    jsonRoute.getDouble(Constant.KhachHangColumn.THANH_TIEN),
                    Integer.parseInt(mNam),
                    Integer.parseInt(mKy),
                    0, 0, 0

            );
            return dongHoKhachHang;
        }
        return null;

    }
}
