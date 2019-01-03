package com.ditagis.hcm.tanhoa.cskh.acynchronize;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ditagis.hcm.tanhoa.cskh.connectDB.LichSuDocSoDB;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.LichSuDocSo;

public class FindLichSuDSAsycn extends AsyncTask<String, Void, LichSuDocSo> {
    private ProgressDialog mDialog;
    private Context mContext;

    private AsyncResponse mDelegate;

    public interface AsyncResponse {
        void processFinish(LichSuDocSo output);
    }

    public FindLichSuDSAsycn(Context context, AsyncResponse delegate) {
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
    protected LichSuDocSo doInBackground(String... params) {
        String dot = params[0];
        String may = params[1];
        try {
            LichSuDocSoDB lichSuDocSoDB = new LichSuDocSoDB(mContext);
            LichSuDocSo lichSuDocSo = lichSuDocSoDB.find(dot, may);
            return lichSuDocSo;
        } catch (Exception e) {
            Log.e("Lỗi tìm lịch sử đọc số", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(LichSuDocSo lichSuDocSo) {
//        if (khachHang != null) {
        mDialog.dismiss();
        this.mDelegate.processFinish(lichSuDocSo);
//        }
    }
}
