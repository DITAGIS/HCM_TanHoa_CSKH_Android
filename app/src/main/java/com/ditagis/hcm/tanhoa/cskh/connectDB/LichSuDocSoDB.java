package com.ditagis.hcm.tanhoa.cskh.connectDB;

import android.content.Context;
import android.os.StrictMode;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.LichSuDocSo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class LichSuDocSoDB implements IDB<LichSuDocSo, Boolean, String> {
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private Context mContext;

    public LichSuDocSoDB(Context mContext) {
        this.mContext = mContext;
    }

    //    private final String SQL_FIND = "SELECT * FROM " + TABLE_NAME + " WHERE ID=?";
    @Override
    public Boolean add(LichSuDocSo khachHang) {
        return null;
    }

    @Override
    public Boolean delete(String k) {
        return false;
    }

    @Override
    public Boolean update(LichSuDocSo khachHang) {
        return null;
    }


    @Override
    public LichSuDocSo find(String dot, String may) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        LichSuDocSo lichSuDocSo = null;
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_get_lichsuds);
            PreparedStatement mStatement = cnn.prepareStatement(query);
            mStatement.setString(1, dot);
            mStatement.setString(2, may);
            rs = mStatement.executeQuery();

            while (rs.next()) {
                int nam = rs.getInt(mContext.getString(R.string.sql_coloumn_lichsuds_nam));
                int ky = rs.getInt(mContext.getString(R.string.sql_coloumn_lichsuds_ky));
                String nhanVienDS = rs.getString(mContext.getString(R.string.sql_coloumn_lichsuds_nhanviends));
                lichSuDocSo = new LichSuDocSo(may);
                lichSuDocSo.setDot(dot);
                lichSuDocSo.setNam(nam);
                lichSuDocSo.setKy(ky);
                lichSuDocSo.setNhanVienDS(nhanVienDS);
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

        return lichSuDocSo;
    }

    @Override
    public LichSuDocSo find(String s, String k1, String k2) {
        return null;
    }

    @Override
    public List<LichSuDocSo> getAll() {
        return null;
    }


}
