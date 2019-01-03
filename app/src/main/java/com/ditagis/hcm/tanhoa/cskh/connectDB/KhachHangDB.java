package com.ditagis.hcm.tanhoa.cskh.connectDB;

import android.content.Context;
import android.os.StrictMode;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.Constant;
import com.ditagis.hcm.tanhoa.cskh.entities.HoaDon;
import com.ditagis.hcm.tanhoa.cskh.entities.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class KhachHangDB implements IDB<KhachHang, Boolean, String> {
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private Context mContext;

    public KhachHangDB(Context mContext) {
        this.mContext = mContext;
    }

    //    private final String SQL_FIND = "SELECT * FROM " + TABLE_NAME + " WHERE ID=?";
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

    public HoaDon getBill(String hoaDonID) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        HoaDon hoaDon = null;
        ResultSet rs = null;
//        String sqlCode_CSC_SanLuong = "SELECT cscu, csmoi, codemoi, tieuthumoi  FROM Docso where docsoid like ? and danhba = ?";
//        String sqlCode_CSC_SanLuong3Ky = "SELECT cscu, csmoi, codemoi, tieuthumoi  FROM Docso where danhba = ? and (ky = ? or ky =? or ky = ?) and nam =? order by ky desc";
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = Constant.QueryString.GET_HOA_DON;
            PreparedStatement mStatement = cnn.prepareStatement(query);

            mStatement.setString(1, hoaDonID);
            rs = mStatement.executeQuery();

            while (rs.next()) {
                String ky = rs.getString(Constant.HoaDonColumn.KY);
                int nam = rs.getInt(Constant.HoaDonColumn.NAM);
                String soHoaDon = rs.getString(Constant.HoaDonColumn.SO_HOA_DON);

                hoaDon = new HoaDon();
                hoaDon.setKy(ky);
                hoaDon.setNam(nam);
                hoaDon.setSoHoaDon(soHoaDon);
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

        return hoaDon;
    }

    @Override
    public KhachHang find(String danhBo, String pin) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        KhachHang khachHang = null;
        ResultSet rs = null;
//        String sqlCode_CSC_SanLuong = "SELECT cscu, csmoi, codemoi, tieuthumoi  FROM Docso where docsoid like ? and danhba = ?";
//        String sqlCode_CSC_SanLuong3Ky = "SELECT cscu, csmoi, codemoi, tieuthumoi  FROM Docso where danhba = ? and (ky = ? or ky =? or ky = ?) and nam =? order by ky desc";
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = mContext.getString(R.string.sql_get_khachhang);
            PreparedStatement mStatement = cnn.prepareStatement(query);
            mStatement.setString(1, danhBo);
            mStatement.setString(2, pin);
            rs = mStatement.executeQuery();

            while (rs.next()) {
                short hieuLuc = rs.getShort(mContext.getString(R.string.sql_coloumn_khachhang_hieuluc));
                String hopDong = rs.getString(mContext.getString(R.string.sql_coloumn_khachhang_hopdong));
                String so = rs.getString(mContext.getString(R.string.sql_coloumn_khachhang_so));
                String duong = rs.getString(mContext.getString(R.string.sql_coloumn_khachhang_duong));
                String sdt = rs.getString(mContext.getString(R.string.sql_coloumn_khachhang_sdt));
                String tenkh = rs.getString(mContext.getString(R.string.sql_coloumn_khachhang_tenkh));
                String hieu = rs.getString(mContext.getString(R.string.sql_coloumn_khachhang_hieu));
                int co = rs.getInt(mContext.getString(R.string.sql_coloumn_khachhang_co));
                String chiThan = rs.getString(mContext.getString(R.string.sql_coloumn_khachhang_chithan));
                int dm = rs.getInt(mContext.getString(R.string.sql_coloumn_khachhang_dm));
                int gb = rs.getInt(mContext.getString(R.string.sql_coloumn_khachhang_gb));
                int sh = rs.getInt(mContext.getString(R.string.sql_coloumn_khachhang_sh));
                int sx = rs.getInt(mContext.getString(R.string.sql_coloumn_khachhang_sx));
                int dv = rs.getInt(mContext.getString(R.string.sql_coloumn_khachhang_dv));
                int hc = rs.getInt(mContext.getString(R.string.sql_coloumn_khachhang_hc));
                int dot = rs.getInt(mContext.getString(R.string.sql_coloumn_khachhang_dot));
                int may = rs.getInt(mContext.getString(R.string.sql_coloumn_khachhang_may));
                String soThan = rs.getString(mContext.getString(R.string.sql_coloumn_khachhang_sothan));
                khachHang = new KhachHang();
                khachHang.setDanhBa(danhBo);
                khachHang.setHieuLuc(hieuLuc);
                khachHang.setHopDong(hopDong);
                khachHang.setSo(so);
                khachHang.setDuong(duong);
                khachHang.setSdt(sdt);
                khachHang.setTenKH(tenkh);
                khachHang.setHieu(hieu);
                khachHang.setCo(co);
                khachHang.setChiThan(chiThan);
                khachHang.setDm(dm);
                khachHang.setGb(gb);
                khachHang.setSh(sh);
                khachHang.setSx(sx);
                khachHang.setDv(dv);
                khachHang.setHc(hc);
                khachHang.setSoThan(soThan);
                khachHang.setDot(String.format(mContext.getString(R.string.format_number_two), dot));
                khachHang.setMay(String.format(mContext.getString(R.string.format_number_two), may));
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
    public KhachHang find(String s, String k1, String k2) {
        return null;
    }

    @Override
    public List<KhachHang> getAll() {
        return null;
    }


}
