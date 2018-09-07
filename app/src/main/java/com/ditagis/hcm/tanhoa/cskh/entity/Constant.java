package com.ditagis.hcm.tanhoa.cskh.entity;

import android.Manifest;

import java.text.SimpleDateFormat;

public class Constant {
    public static final SimpleDateFormat DATE_FORMAT_YEAR_FIRST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_VIEW = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final String DATE_FORMAT_STRING = "dd/MM/yyyy";
    public static final int REQUEST_CODE_PERMISSION = 2;
    public static final String[] REQUEST_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public String URL_FEATURE = "http://113.161.88.180:800/arcgis/rest/services/TanHoa/THSuCo/FeatureServer/0";
    public String URL_GIA_NUOC = "http://sawagis.vn/tanhoa/cskh/views/dichvukhachhang/xemgianuoc.html";

    public static final short LOAISUCO_ONGNGANH = 1;
    public static final short LOAISUCO_ONGCHINH = 2;
    public static final short LOAISUCO_CHUAPHANLOAI = 3;

    public class FIELD_SUCO {
        public static final String TRANG_THAI = "TrangThai";
        public static final String TGPHAN_ANH = "TGPhanAnh";
        public static final String DIA_CHI = "DiaChi";
        public static final String ID_SU_CO = "IDSuCo";

    }

    public Constant() {
    }
}
