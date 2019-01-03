package com.ditagis.hcm.tanhoa.cskh.acynchronize;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ditagis.hcm.tanhoa.cskh.connectDB.DongHoKhachHangDB;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.DongHoKhachHang;

public class FindDongHoKhachHangAsycn extends AsyncTask<String, Void, DongHoKhachHang> {
    private ProgressDialog mDialog;
    private Context mContext;

    private AsyncResponse mDelegate;

    public interface AsyncResponse {
        void processFinish(DongHoKhachHang output);
    }

    public FindDongHoKhachHangAsycn(Context context, AsyncResponse delegate) {
        this.mContext = context;
        this.mDelegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mDialog = new ProgressDialog(this.mContext, android.R.style.Theme_Material_Dialog_Alert);
        this.mDialog.setMessage(mContext.getString(R.string.find_khachhHang_message));
        this.mDialog.setCancelable(false);
        this.mDialog.show();
    }

    @Override
    protected DongHoKhachHang doInBackground(String... params) {
        String nam = params[0];
        String thang = params[1];
        String danhBo = params[2];
        try {
            DongHoKhachHangDB khachHangDB = new DongHoKhachHangDB(mContext);
            DongHoKhachHang dongHoKH = khachHangDB.find(nam, thang, danhBo);
            return dongHoKH;
        } catch (Exception e) {
            Log.e("Lỗi tìm khách hàng", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(DongHoKhachHang dongHoKhachHang) {
//        if (khachHang != null) {
        mDialog.dismiss();
        this.mDelegate.processFinish(dongHoKhachHang);
//        }
    }
}
