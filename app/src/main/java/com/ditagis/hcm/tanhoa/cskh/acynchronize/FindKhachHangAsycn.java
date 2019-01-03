package com.ditagis.hcm.tanhoa.cskh.acynchronize;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.ditagis.hcm.tanhoa.cskh.connectDB.KhachHangDB;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.Constant;
import com.ditagis.hcm.tanhoa.cskh.entities.DApplication;
import com.ditagis.hcm.tanhoa.cskh.entities.DLayerInfo;
import com.ditagis.hcm.tanhoa.cskh.entities.KhachHang;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FindKhachHangAsycn extends AsyncTask<String, Void, KhachHang> {
    private ProgressDialog mDialog;
    private Activity mActivity;
private DApplication mApplication;
    private AsyncResponse mDelegate;

    public interface AsyncResponse {
        void processFinish(KhachHang output);
    }

    public FindKhachHangAsycn(Activity activity, AsyncResponse delegate) {
        this.mActivity = activity;
        this.mApplication = (DApplication) activity.getApplication();
        this.mDelegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mDialog = new ProgressDialog(this.mActivity, android.R.style.Theme_Material_Dialog_Alert);
        this.mDialog.setMessage(mActivity.getString(R.string.find_khachhHang_message));
        this.mDialog.setCancelable(false);
        this.mDialog.show();
    }

    @Override
    protected KhachHang doInBackground(String... params) {
        String danhBo = params[0];
        String pin = params[1];
        try {
            KhachHangDB khachHangDB = new KhachHangDB(mActivity);
            KhachHang khachHang = khachHangDB.find(danhBo, pin);

            getLayerInfoAPI();
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

    private void getLayerInfoAPI() {
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
                pajsonRouteeJSon(builder.toString());
            } catch (Exception e) {
                Log.e("error", e.toString());
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            Log.e("Lỗi lấy LayerInfo", e.toString());
        }
    }

    private void pajsonRouteeJSon(String data) throws JSONException {
        if (data == null)
            return;
        String myData = "{ \"layerInfo\": ".concat(data).concat("}");
        JSONObject jsonData = new JSONObject(myData);
        JSONArray jsonRoutes = jsonData.getJSONArray("layerInfo");
        List<DLayerInfo> layerDTGS = new ArrayList<>();
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            String url = jsonRoute.getString(Constant.SYS_CLOUMN.URL);
            if (url.startsWith("http://113.161.88.180:800/arcgis/rest/services/TanHoa/SuCo/FeatureServer")) {
                url = url.replace("TanHoa/SuCo", "TanHoa/THSuCo");
            } else if (url.startsWith("http://113.161.88.180:800/arcgis/rest/services/TanHoa/TanHoaSuCo/FeatureServer")) {
                url = url.replace("TanHoa/TanHoaSuCo", "TanHoa/THSuCo");

            }
            String definition = jsonRoute.getString(Constant.SYS_CLOUMN.DEFINITION);
            if (definition.contains("null"))
                definition = null;
            String addFields = "";
            try {
                addFields = "";

            } catch (Exception ignored) {

            }
            String outFields = jsonRoute.getString(Constant.SYS_CLOUMN.OUT_FIELDS);
            String id = jsonRoute.getString(Constant.SYS_CLOUMN.LAYER_ID);

            layerDTGS.add(new DLayerInfo(id,
                    jsonRoute.getString(Constant.SYS_CLOUMN.LAYER_TITLE),
                    url,
                    jsonRoute.getBoolean(Constant.SYS_CLOUMN.IS_CREATE),
                    jsonRoute.getBoolean(Constant.SYS_CLOUMN.IS_DELETE),
                    jsonRoute.getBoolean(Constant.SYS_CLOUMN.IS_EDIT),
                    jsonRoute.getBoolean(Constant.SYS_CLOUMN.IS_VIEW),
                    definition,
                    outFields,
                    addFields,
                    jsonRoute.getString(Constant.SYS_CLOUMN.UPDATE_FIELDS)));


        }
        ((DApplication) mActivity.getApplication()).setdLayerInfos(layerDTGS);

    }
}
