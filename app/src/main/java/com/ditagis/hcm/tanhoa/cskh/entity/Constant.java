package com.ditagis.hcm.tanhoa.cskh.entity;

import android.Manifest;

import java.text.SimpleDateFormat;

public class Constant {
    public static final SimpleDateFormat DATE_FORMAT_YEAR_FIRST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_VIEW = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final String DATE_FORMAT_STRING = "dd/MM/yyyy";
    public static final int REQUEST_CODE_PERMISSION = 2;
    public static final int REQUEST_LOGIN = 0;
    public static final int SIZE_FEATURE_RENDERER = 23;
    public static final short DOI_TUONG_PHAT_HIEN_KHACH_HANG = 0;
    public static final String ROLE_PGN = "pgn";
    public static final String HINH_THUC_PHAT_HIEN_BE_NGAM = "Bể ngầm";
    public static final String[] REQUEST_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public final String LICENCE = "runtimelite,1000,rud6046938574,none,4N5X0H4AH5JB003AD169";
    public String URL_FEATURE = "http://113.161.88.180:800/arcgis/rest/services/TanHoa/THSuCo/FeatureServer/0";
    public String URL_GIA_NUOC = "http://sawagis.vn/tanhoa/cskh/views/dichvukhachhang/xemgianuoc.html";
    public static final int REQUEST_CODE_ADD_FEATURE = 7;
    public static final int REQUEST_CODE_ADD_FEATURE_ATTACHMENT = 8;
    public static final short LOAISUCO_ONGNGANH = 1;
    public static final short LOAISUCO_ONGCHINH = 2;
    public static final short LOAISUCO_CHUAPHANLOAI = 3;

    private final String SERVER = "http://sawagis.vn";
    public String URL_SYMBOL_CHUA_SUA_CHUA = SERVER + "/tanhoa/Content/images/map/0.png";
    public String URL_SYMBOL_CHUA_SUA_CHUA_BE_NGAM = "http://tanhoa.sawagis.vn/images/map/bengam.png";
    public String URL_SYMBOL_DANG_SUA_CHUA = SERVER + "/tanhoa/Content/images/map/1.png";
    public String URL_SYMBOL_HOAN_THANH = SERVER + "/tanhoa/Content/images/map/3.png";
    private String SERVER_API = SERVER + "/tanhoa1/api";
    public String API_LOGIN;


    {
        API_LOGIN = SERVER_API + "/Login";
    }

    public String DISPLAY_NAME;


    {
        DISPLAY_NAME = SERVER_API + "/Account/Profile";
    }

    public String GENERATE_ID_SUCO;


    {
        GENERATE_ID_SUCO = SERVER_API + "/QuanLySuCo/GenerateIDSuCo";
    }

    public String LAYER_INFO;


    {
        LAYER_INFO = SERVER_API + "/Account/layerinfo";
    }

    public int MAX_SCALE_IMAGE_WITH_LABLES;

    {
        MAX_SCALE_IMAGE_WITH_LABLES = 5;
    }

    public String ADMIN_AREA_TPHCM;

    {
        ADMIN_AREA_TPHCM = "Hồ Chí Minh";
    }

    public class FIELD_SUCO {
        public static final String ID_SU_CO = "IDSuCo";
        public static final String TRANG_THAI = "TrangThai";
        public static final String GHI_CHU = "GhiChu";
        public static final String SDT = "SDTPhanAnh";
        public static final String NGUOI_PHAN_ANH = "NguoiPhanAnh";
        public static final String EMAIL_NGUOI_PHAN_ANH = "EmailNguoiPhanAnh";
        public static final String NGUYEN_NHAN = "NguyenNhan";
        public static final String TGPHAN_ANH = "TGPhanAnh";
        public static final String DIA_CHI = "DiaChi";
        public static final String DOI_TUONG_PHAT_HIEN = "DoiTuongPhatHien";
        public static final String HINH_THUC_PHAT_HIEN = "HinhThucPhatHien";
    }

    public class FIELD_ACCOUNT {
        public static final String ROLE = "Role";
        public static final String GROUP_ROLE = "GroupRole";
        public static final String DISPLAY_NAME = "DisplayName";
    }


    public Constant() {
    }
}
