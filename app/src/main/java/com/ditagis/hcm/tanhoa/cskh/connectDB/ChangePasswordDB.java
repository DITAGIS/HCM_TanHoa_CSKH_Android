package com.ditagis.hcm.tanhoa.cskh.connectDB;

import android.content.Context;
import android.os.StrictMode;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ChangePasswordDB implements IDB<KhachHang, Boolean, String> {
    private Context mContext;

    public ChangePasswordDB(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Boolean add(KhachHang khachHang) {
        return null;
    }

    @Override
    public Boolean delete(String k) {
        return false;
    }

    @Override
    public Boolean update(KhachHang khachHang) {
        return null;
    }


    @Override
    public KhachHang find(String danhBo, String oldPassword) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        KhachHang khachHang = null;
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_login);
            PreparedStatement mStatement = cnn.prepareStatement(query);
            mStatement.setString(1, danhBo);
            mStatement.setString(2, oldPassword);
            rs = mStatement.executeQuery();

            while (rs.next()) {

                khachHang = new KhachHang();
                khachHang.setDanhBa(danhBo);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (rs != null && !rs.isClosed())
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return khachHang;
    }
    @Override
    public KhachHang find(String danhBo, String oldPassword, String newPassword) {
     return null;
    }

    public KhachHang change(String danhBo, String newPassword) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        KhachHang khachHang = null;
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_changepassword);
            PreparedStatement mStatement = cnn.prepareStatement(query);
            mStatement.setString(1, newPassword);
            mStatement.setString(2, danhBo);
            int update = mStatement.executeUpdate();

            if (update > 0) {

                khachHang = new KhachHang();
                khachHang.setDanhBa(danhBo);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (rs != null && !rs.isClosed())
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return khachHang;
    }
    @Override
    public List<KhachHang> getAll() {
        return null;
    }


}
