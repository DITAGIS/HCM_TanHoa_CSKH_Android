package com.ditagis.hcm.tanhoa.cskh.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.Constant;
import com.ditagis.hcm.tanhoa.cskh.entities.DApplication;
import com.ditagis.hcm.tanhoa.cskh.entities.DLayerInfo;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.Attachment;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by ThanLe on 4/16/2018.
 */

public class SingleTapAddFeatureAsync extends AsyncTask<Void, Feature, Void> {
    private ProgressDialog mDialog;
    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;
    private ServiceFeatureTable mServiceFeatureTable;
    @SuppressLint("StaticFieldLeak")
    private AsyncResponse mDelegate;
    private DApplication mApplication;

    public interface AsyncResponse {
        void processFinish(Feature output);
    }

    public SingleTapAddFeatureAsync(Activity activity,
                                    ServiceFeatureTable serviceFeatureTable, AsyncResponse delegate) {
        this.mServiceFeatureTable = serviceFeatureTable;
        this.mActivity = activity;
        this.mApplication = (DApplication) activity.getApplication();
        this.mDialog = new ProgressDialog(activity, android.R.style.Theme_Material_Dialog_Alert);
        this.mDelegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog.setMessage("Đang xử lý...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        final Feature feature;
        try {
            feature = mServiceFeatureTable.createFeature();
            feature.setGeometry(mApplication.getDiemSuCo.getPoint());
            feature.getAttributes().put(Constant.FIELD_SUCO.DIA_CHI, mApplication.getDiemSuCo.getVitri());
            feature.getAttributes().put(Constant.FIELD_SUCO.GHI_CHU, mApplication.getDiemSuCo.getGhiChu());
            feature.getAttributes().put(Constant.FIELD_SUCO.NGUOI_PHAN_ANH, mApplication.getDiemSuCo.getNguoiCapNhat());
            feature.getAttributes().put(Constant.FIELD_SUCO.TGPHAN_ANH, Calendar.getInstance());
            feature.getAttributes().put(Constant.FIELD_SUCO.SDT, mApplication.getDiemSuCo.getSdt());
            feature.getAttributes().put(Constant.FIELD_SUCO.HINH_THUC_PHAT_HIEN, mApplication.getDiemSuCo.getHinhThucPhatHien());
            feature.getAttributes().put(Constant.FIELD_SUCO.DOI_TUONG_PHAT_HIEN, Constant.DOI_TUONG_PHAT_HIEN_KHACH_HANG);
            feature.getAttributes().put(Constant.FIELD_SUCO.EMAIL_NGUOI_PHAN_ANH, mApplication.getDiemSuCo.getEmail());
            for (DLayerInfo dLayerInfo : mApplication.getdLayerInfos())
                if (dLayerInfo.getId().equals(Constant.ID_BASEMAP)) {
                    ServiceFeatureTable serviceFeatureTableHanhChinh = new ServiceFeatureTable(
                            dLayerInfo.getUrl() + Constant.URL_BASEMAP);
                    QueryParameters queryParameters = new QueryParameters();
                    queryParameters.setGeometry(feature.getGeometry());
                    new QueryServiceFeatureTableAsync(mActivity, serviceFeatureTableHanhChinh, output -> {
                        if (output != null) {
                            Object phuong = output.getAttributes().get(Constant.FIELD_HANHCHINH.ID_HANH_CHINH);
                            Object quan = output.getAttributes().get(Constant.FIELD_HANHCHINH.MA_HUYEN);
                            if (quan != null) {
                                feature.getAttributes().put(Constant.FIELD_SUCO.QUAN, quan.toString());
                            }
                            if (phuong != null)
                                feature.getAttributes().put(Constant.FIELD_SUCO.PHUONG, phuong.toString());
                        }
                        addFeatureAsync(feature);
                    }).execute(queryParameters);


                    break;
                }
        } catch (Exception e) {
            publishProgress();
        }
        return null;
    }

    private void addFeatureAsync(final Feature feature) {
        new GenerateIDSuCoAsycn(mActivity, output -> {
            if (output.isEmpty()) {
                publishProgress();
                return;
            }
            feature.getAttributes().put(Constant.FIELD_SUCO.ID_SU_CO, output);
            Short intObj = (short) 0;
            feature.getAttributes().put(Constant.FIELD_SUCO.TRANG_THAI, intObj);

            mServiceFeatureTable.addFeatureAsync(feature).addDoneListener(() -> {
                final ListenableFuture<List<FeatureEditResult>> listListenableEditAsync = mServiceFeatureTable.applyEditsAsync();
                listListenableEditAsync.addDoneListener(() -> {
                    try {
                        List<FeatureEditResult> featureEditResults = listListenableEditAsync.get();
                        if (featureEditResults.size() > 0) {
                            if (mApplication.getDiemSuCo.getImage() != null)
                                addAttachment((ArcGISFeature) feature);
                            else publishProgress(feature);
//                            long objectId = featureEditResults.get(0).getObjectId();
//                            final QueryParameters queryParameters = new QueryParameters();
//                            final String query = String.format(mActivity.getString(R.string.arcgis_query_by_OBJECTID), objectId);
//                            queryParameters.setWhereClause(query);
//                            final ListenableFuture<FeatureQueryResult> featuresAsync = mServiceFeatureTable.queryFeaturesAsync(queryParameters, ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
//                            featuresAsync.addDoneListener(() -> {
//                                try {
//                                    FeatureQueryResult result = featuresAsync.get();
//                                    if (result.iterator().hasNext()) {
//                                        Feature item = result.iterator().next();
//                                        ArcGISFeature arcGISFeature = (ArcGISFeature) item;
//                                        addAttachment(arcGISFeature, feature);
//                                        publishProgress(item);
//                                    } else publishProgress();
//                                } catch (InterruptedException | ExecutionException e) {
//                                    e.printStackTrace();
//                                    publishProgress();
//                                }
//
//                            });
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        publishProgress();
                        e.printStackTrace();
                    }

                });
            });
        }).execute();
    }

    private void addAttachment(ArcGISFeature arcGISFeature) {
        final String attachmentName = mApplication.getApplicationContext().getString(R.string.attachment) + "_" + System.currentTimeMillis() + ".png";
        final ListenableFuture<Attachment> addResult = arcGISFeature.addAttachmentAsync(
                mApplication.getDiemSuCo.getImage(), Bitmap.CompressFormat.PNG.toString(), attachmentName);
        addResult.addDoneListener(() -> {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            try {
                Attachment attachment = addResult.get();
                if (attachment.getSize() > 0) {
                    final ListenableFuture<Void> tableResult = mServiceFeatureTable.updateFeatureAsync(arcGISFeature);
                    tableResult.addDoneListener(() -> {
                        applyEdit(arcGISFeature);
                    });
                } else publishProgress(arcGISFeature);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                publishProgress();
            }
        });
    }

    private void applyEdit(ArcGISFeature feature) {
        final ListenableFuture<List<FeatureEditResult>> updatedServerResult = mServiceFeatureTable.applyEditsAsync();
        updatedServerResult.addDoneListener(() -> {
            List<FeatureEditResult> edits;
            try {
                edits = updatedServerResult.get();
                if (edits.size() > 0) {
                    if (!edits.get(0).hasCompletedWithErrors()) {
                        publishProgress(feature);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                publishProgress();
                e.printStackTrace();
            }

        });
    }

    @Override
    protected void onProgressUpdate(Feature... values) {
        if (values == null) {
            Toast.makeText(mActivity.getApplicationContext(), "Không phản ánh được sự cố. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
            this.mDelegate.processFinish(null);
        } else if (values.length > 0) this.mDelegate.processFinish(values[0]);
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    @Override
    protected void onPostExecute(Void result) {


    }

}