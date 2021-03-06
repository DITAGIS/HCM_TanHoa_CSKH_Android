package com.ditagis.hcm.tanhoa.cskh.async;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.Constant;
import com.ditagis.hcm.tanhoa.cskh.entities.DApplication;
import com.ditagis.hcm.tanhoa.cskh.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginByAPIAsycn extends AsyncTask<String, Void, Void> {
    private ProgressDialog mDialog;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private LoginByAPIAsycn.AsyncResponse mDelegate;
    private DApplication mApplication;


    public interface AsyncResponse {
        void processFinish();
    }

    public LoginByAPIAsycn(Context context, LoginByAPIAsycn.AsyncResponse delegate) {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Void doInBackground(String... params) {
        String userName = params[0];
        String pin = params[1];
//        String passEncoded = (new EncodeMD5()).encode(pin + "_DITAGIS");
        // Do some validation here
        String urlParameters = String.format("Username=%s&Password=%s", userName, pin);
        String urlWithParam = String.format("%s?%s", Constant.URL_API.LOGIN, urlParameters);
        try {
//            + "&apiKey=" + API_KEY
            URL url = new URL(urlWithParam);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                conn.setRequestMethod("GET");
                conn.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    break;
                }
                bufferedReader.close();
                String token = stringBuilder.toString().replace("\"", "");

                getProfile();

//                if (checkAccess(token)) {
                User user = new User();
                mApplication.setUserDangNhap(user);
                mApplication.getUserDangNhap().setToken(token);
                mApplication.getUserDangNhap().setUserName(userName);
                conn.disconnect();
                return null;
//                } else {
//                    conn.disconnect();
//                    return null;
//                }
            } catch (Exception e1) {
                Log.e("Lỗi login", e1.toString());
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);

        } finally {
            return null;
        }

    }


    @Override
    protected void onPostExecute(Void aVoid) {
//        if (user != null) {
        mDialog.dismiss();
        this.mDelegate.processFinish();
//        }
    }


    private void getProfile() {

//        String API_URL = "http://sawagis.vn/tanhoa1/api/Account/Profile";
        try {
            URL url = new URL(Constant.URL_API.PROFILE);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", mApplication.getUserDangNhap().getToken());
                conn.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    pajsonRouteeJSon(line);

                    break;
                }

            } catch (Exception e) {
                Log.e("lỗi lấy profile", e.toString());
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            Log.e("lỗi lấy profile", e.toString());
        } finally {
        }
    }

    private Boolean checkAccess(String token) {
        boolean isAccess = false;
        try {
            URL url = new URL(Constant.URL_API.IS_ACCESS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", token);
                conn.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = bufferedReader.readLine();
                if (line.equals("true"))
                    isAccess = true;

            } catch (Exception e) {
                Log.e("error", e.toString());
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        } finally {
            return isAccess;
        }
    }

    private String pajsonRouteeJSon(String data) throws JSONException {
        if (data == null)
            return "";
        String displayName = "";
        String myData = "{ \"account\": [".concat(data).concat("]}");
        JSONObject jsonData = new JSONObject(myData);
        JSONArray jsonRoutes = jsonData.getJSONArray("account");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            mApplication.getUserDangNhap().setDisplayName(jsonRoute.getString(Constant.FIELD_ACCOUNT.DISPLAY_NAME));
            mApplication.getUserDangNhap().setRole(jsonRoute.getString(Constant.FIELD_ACCOUNT.ROLE));
            mApplication.getUserDangNhap().setGroupRole(jsonRoute.getString(Constant.FIELD_ACCOUNT.GROUP_ROLE));
        }
        return displayName;

    }
}
