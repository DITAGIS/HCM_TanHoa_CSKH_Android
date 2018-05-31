package com.ditagis.hcm.tanhoa.cskh.acynchronize;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ditagis.hcm.tanhoa.cskh.connectDB.KhachHangDB;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entity.KhachHang;

public class FindKhachHangAsycn extends AsyncTask<String, Void, KhachHang> {
    private ProgressDialog mDialog;
    private Context mContext;

    private AsyncResponse mDelegate;

    public interface AsyncResponse {
        void processFinish(KhachHang output);
    }

    public FindKhachHangAsycn(Context context, AsyncResponse delegate) {
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
    protected KhachHang doInBackground(String... params) {
        String danhBo = params[0];
        String pin = params[1];
        try {
            KhachHangDB khachHangDB = new KhachHangDB(mContext);
            KhachHang khachHang = khachHangDB.find(danhBo, pin);
            return khachHang;
        } catch (Exception e) {
            Log.e("Lỗi tìm khách hàng", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(KhachHang khachHang) {
//        if (khachHang != null) {
        mDialog.dismiss();
        this.mDelegate.processFinish(khachHang);
//        }
    }
}
