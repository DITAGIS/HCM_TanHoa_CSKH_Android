package com.ditagis.hcm.tanhoa.cskh;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ditagis.hcm.tanhoa.cskh.async.LoginByAPIAsycn;
import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entity.DApplication;
import com.ditagis.hcm.tanhoa.cskh.utities.CheckConnectInternet;
import com.ditagis.hcm.tanhoa.cskh.utities.Preference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private TextView mTxtUsername;
    private TextView mTxtPassword;
    private TextView mTxtChangeAccount;
    private boolean isLastLogin;
    private TextView mTxtValidation;

    private DApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mApplication = (DApplication) getApplication();
        btnLogin = (findViewById(R.id.btnLogin));
        mTxtChangeAccount = findViewById(R.id.txt_login_changeAccount);
        btnLogin.setOnClickListener(this);
        mTxtChangeAccount.setOnClickListener(this);

        mTxtUsername = findViewById(R.id.txtUsername);
        mTxtPassword = findViewById(R.id.txtPassword);

        mTxtValidation = findViewById(R.id.txt_login_validation);
        findViewById(R.id.llayout_login_password).setVisibility(View.GONE);
        try {
            ((TextView) findViewById(R.id.txt_login_version)).setText("Phiên bản: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        mTxtUsername.setText("12101860724");
//        mTxtPassword.setText("123456");
        create();
        //12101860724
    }

    private void create() {
        Preference.getInstance().setContext(this);
        String preference_userName = Preference.getInstance().loadPreference(getString(R.string.preference_username));

        //nếu chưa từng đăng nhập thành công trước đó
        //nhập username và password bình thường
        if (preference_userName == null || preference_userName.isEmpty()) {
            findViewById(R.id.layout_login_tool).setVisibility(View.GONE);
            findViewById(R.id.layout_login_username).setVisibility(View.VISIBLE);
            isLastLogin = false;
        }
        //ngược lại
        //chỉ nhập pasword
        else {
            isLastLogin = true;
            mTxtUsername.setText(preference_userName);
//            findViewById(R.id.layout_login_tool).setVisibility(View.VISIBLE);
//            findViewById(R.id.layout_login_username).setVisibility(View.GONE);
        }

    }

    private void login() {
        if (!CheckConnectInternet.isOnline(this)) {
            mTxtValidation.setText(R.string.validate_no_connect);
            mTxtValidation.setVisibility(View.VISIBLE);
            return;
        }
        mTxtValidation.setVisibility(View.GONE);

        String userName = mTxtUsername.getText().toString().trim();
        final String passWord = "123456";// mTxtPassword.getText().toString().trim();
        if (userName.length() == 0 || passWord.length() == 0) {
            handleInfoLoginEmpty();
            return;
        }
        final String finalUserName = userName;
        LoginByAPIAsycn loginAsycn = new LoginByAPIAsycn(this, () -> {
            if (mApplication.getUserDangNhap() != null)
                handleLoginSuccess(finalUserName, passWord);
            else
                handleLoginFail();
        }
        );
        loginAsycn.execute(userName, passWord);
//        if (userName.equals("12101860725") && passWord.equals("123456")) {

//        }
    }

    private void handleInfoLoginEmpty() {
        mTxtValidation.setText(R.string.info_login_empty);
        mTxtValidation.setVisibility(View.VISIBLE);
    }

    private void handleLoginFail() {
        mTxtValidation.setText(R.string.validate_login_fail);
        mTxtValidation.setVisibility(View.VISIBLE);
    }

    private void handleLoginSuccess(String userName, String passWord) {
        mTxtUsername.setText("");
        mTxtPassword.setText("");

        Preference.getInstance().savePreferences(getString(R.string.preference_username), userName);
        Preference.getInstance().savePreferences(getString(R.string.preference_password), passWord);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void changeAccount() {
        mTxtUsername.setText("");
        mTxtPassword.setText("");

        Preference.getInstance().savePreferences(getString(R.string.preference_username), "");
        create();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.txt_login_changeAccount:
                changeAccount();
                break;
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                if (mTxtPassword.getText().toString().trim().length() > 0) {
                    login();
                    return true;
                }
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
