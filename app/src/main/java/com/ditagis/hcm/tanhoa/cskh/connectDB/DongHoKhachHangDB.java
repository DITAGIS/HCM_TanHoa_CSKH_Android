package com.ditagis.hcm.tanhoa.cskh.connectDB;

import android.content.Context;
import android.os.StrictMode;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entity.DongHoKhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class DongHoKhachHangDB implements IDB<DongHoKhachHang, Boolean, String> {
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private Context mContext;

    public DongHoKhachHangDB(Context mContext) {
        this.mContext = mContext;
    }

    //    private final String SQL_FIND = "SELECT * FROM " + TABLE_NAME + " WHERE ID=?";
    @Override
    public Boolean add(DongHoKhachHang khachHang) {
        return null;
    }

    @Override
    public Boolean delete(String k) {
        return false;
    }

    @Override
    public Boolean update(DongHoKhachHang khachHang) {
        return null;
    }

    @Override
    public DongHoKhachHang find(String s, String k1) {
        return null;
    }


    @Override
    public DongHoKhachHang find(String nam, String thang, String danhBo) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        DongHoKhachHang dongHoKhachHang = null;
        ResultSet rs = null;
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_get_docso);
            PreparedStatement mStatement = cnn.prepareStatement(query);
            mStatement.setString(1, nam + thang + danhBo);
            rs = mStatement.executeQuery();

            while (rs.next()) {
                int cscu = rs.getInt(mContext.getString(R.string.sql_coloumn_docso_cscu));
                int csmoi = rs.getInt(mContext.getString(R.string.sql_coloumn_docso_csmoi));
                int tieuthucu = rs.getInt(mContext.getString(R.string.sql_coloumn_docso_tieuthucu));
                int tieuthumoi = rs.getInt(mContext.getString(R.string.sql_coloumn_docso_tieuthumoi));
                double tiennuoc = rs.getDouble(mContext.getString(R.string.sql_coloumn_docso_tiennuoc));
                int thueVAT = rs.getInt(mContext.getString(R.string.sql_coloumn_docso_thuevat));
                int phiBVMT = rs.getInt(mContext.getString(R.string.sql_coloumn_docso_phibvmt));
                double tongTien = rs.getDouble(mContext.getString(R.string.sql_coloumn_docso_tongtien));
                dongHoKhachHang = new DongHoKhachHang(danhBo);
                dongHoKhachHang.setCscu(cscu);
                dongHoKhachHang.setCsmoi(csmoi);
                dongHoKhachHang.setTieuthucu(tieuthucu);
                dongHoKhachHang.setTieuthumoi(tieuthumoi);
                dongHoKhachHang.setTienNuoc(tiennuoc);
                dongHoKhachHang.setThueVAT(thueVAT);
                dongHoKhachHang.setPhiBVMT(phiBVMT);
                dongHoKhachHang.setTongTien(tongTien);
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

        return dongHoKhachHang;
    }

    @Override
    public List<DongHoKhachHang> getAll() {
        return null;
    }


}
