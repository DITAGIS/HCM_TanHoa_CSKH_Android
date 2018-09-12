package com.ditagis.hcm.tanhoa.cskh.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.ditagis.hcm.tanhoa.cskh.entity.Constant;
import com.ditagis.hcm.tanhoa.cskh.entity.DApplication;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;


/**
 * Created by ThanLe on 4/16/2018.
 */

public class QueryFeatureAsync extends AsyncTask<Void, List<Feature>, Void> {
    @SuppressLint("StaticFieldLeak")
    private AsyncResponse mDelegate;
    private DApplication mApplication;
    private ServiceFeatureTable mServiceFeatureTable;
    private int mTrangThai;
    private String mDiaChi;
    private String mThoiGian;

    public interface AsyncResponse {
        void processFinish(List<Feature> output);
    }

    public QueryFeatureAsync(Activity activity, int trangThai, String diaChi, String thoiGianPhanAnh
            , AsyncResponse delegate) {
        this.mApplication = (DApplication) activity.getApplication();
        this.mServiceFeatureTable = (ServiceFeatureTable) mApplication.getFeatureLayer().getFeatureTable();
        this.mDelegate = delegate;
        this.mTrangThai = trangThai;
        this.mDiaChi = diaChi;
        this.mThoiGian = thoiGianPhanAnh;
        try {
            Date date = Constant.DATE_FORMAT.parse(thoiGianPhanAnh);
            this.mThoiGian = formatTimeToGMT(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private String formatTimeToGMT(Date date) {
        SimpleDateFormat dateFormatGmt = Constant.DATE_FORMAT_YEAR_FIRST;
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(date);
    }

    @Override
    protected Void doInBackground(Void... aVoids) {
        try {

            QueryParameters queryParameters = new QueryParameters();
            @SuppressLint("DefaultLocale") String queryClause = String.format("%s = %d and %s > date '%s' and %s like N'%%%s%%'",
                    Constant.FIELD_SUCO.TRANG_THAI, mTrangThai,
                    Constant.FIELD_SUCO.TGPHAN_ANH, mThoiGian,
                    Constant.FIELD_SUCO.DIA_CHI, mDiaChi);
            queryParameters.setWhereClause(queryClause);

            ListenableFuture<FeatureQueryResult> featureQueryResultListenableFuture = mServiceFeatureTable.queryFeaturesAsync(queryParameters, ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
            featureQueryResultListenableFuture.addDoneListener(() -> {
                try {
                    FeatureQueryResult result = featureQueryResultListenableFuture.get();
                    Iterator<Feature> iterator = result.iterator();
                    Feature item;
                    List<Feature> features = new ArrayList<>();
                    while (iterator.hasNext()) {
                        item = iterator.next();
                        features.add(item);
                    }
                    publishProgress(features);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    publishProgress();
                }
            });
        } catch (
                Exception e)

        {
            publishProgress();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(List<Feature>... values) {
        if (values == null) {
            mDelegate.processFinish(null);
        } else if (values.length > 0) mDelegate.processFinish(values[0]);
    }


    @Override
    protected void onPostExecute(Void result) {


    }

}