package com.ditagis.hcm.tanhoa.cskh.entities;

import android.Manifest;

import java.text.SimpleDateFormat;

public class Constant {
    public static final String DATE_FORMAT_STRING = "dd/MM/yyyy";
    public static final SimpleDateFormat DATE_FORMAT_YEAR_FIRST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_VIEW = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final int REQUEST_CODE_PERMISSION = 2;
    public static final int REQUEST_LOGIN = 0;
    public static final int SIZE_FEATURE_RENDERER = 23;
    public static final short DOI_TUONG_PHAT_HIEN_KHACH_HANG = 0;
    public static final String ROLE_PGN = "pgn";
    public static final String HINH_THUC_PHAT_HIEN_BE_NGAM = "Bể ngầm";
    public static final String ID_BASEMAP = "BASEMAP";
    public static final String URL_BASEMAP = "/3";
    public static final String[] REQUEST_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String LICENCE = "runtimelite,1000,rud6046938574,none,4N5X0H4AH5JB003AD169";
    public String URL_GIA_NUOC = "http://sawagis.vn/tanhoa/cskh/views/dichvukhachhang/xemgianuoc.html";
    public static final int REQUEST_CODE_ADD_FEATURE = 7;
    public static final int REQUEST_CODE_ADD_FEATURE_ATTACHMENT = 8;

    //    private static final String SERVER = "http://tanhoa.sawagis.vn";
    private static final String SERVER = "http://113.161.88.180:798";
    private static final String SERVER1 = "http://113.161.88.180:1010";
    private static final String SERVER_API = SERVER + "/apiv1/api";

    public final int MAX_SCALE_IMAGE_WITH_LABLES;

    {
        MAX_SCALE_IMAGE_WITH_LABLES = 5;
    }

    public final String ADMIN_AREA_TPHCM;

    {
        ADMIN_AREA_TPHCM = "Hồ Chí Minh";
    }

    public class URLSymbol {
        public static final String CHUA_SUA_CHUA = SERVER + "/images/map/0.png";
        public static final String CHUA_SUA_CHUA_BE_NGAM = SERVER + "/images/map/bengam.png";
        public static final String DANG_SUA_CHUA = SERVER + "/images/map/1.png";
        public static final String HOAN_THANH = SERVER + "/images/map/2.png";
    }

    public class URL_API {
        public static final String CHECK_VERSION = "http://tanhoa.sawagis.vn/apiv1" + "/versioning/CSKH?version=%s";
        public static final String LAYER_INFO = SERVER_API + "/Account/layerinfo";
        public static final String GENERATE_ID_SUCO = SERVER_API + "/QuanLySuCo/GenerateIDSuCo";
        public static final String PROFILE = SERVER_API + "/Account/Profile";
        public static final String LOGIN = SERVER_API + "/Login";
        public static final String IS_ACCESS = SERVER_API + "/Account/IsAccess/m_cskh";
        public static final String THU_TIEN = SERVER_API + "/Login";
        public static final String LAY_CHI_SO = SERVER_API + "/QuanLySanLuong/LayChiSo?danhBa=%s&nam=%s&ky=%s";
        public static final String LAY_NAM_KY = SERVER_API + "/quanlysanluong/selectsanluongnam?danhba=%s";
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
        public static final String QUAN = "Quan";
        public static final String PHUONG = "Phuong";
    }

    public class FIELD_ACCOUNT {
        public static final String ROLE = "Role";
        public static final String GROUP_ROLE = "GroupRole";
        public static final String DISPLAY_NAME = "DisplayName";
    }

    public class FIELD_HANHCHINH {
        public static final String ID_HANH_CHINH = "IDHanhChinh";
        public static final String MA_HUYEN = "MaHuyen";

    }

    public class SYS_CLOUMN {
        public static final String URL = "Url";
        public static final String DEFINITION = "Definition";
        public static final String OUT_FIELDS = "OutFields";
        public static final String LAYER_ID = "LayerID";
        public static final String LAYER_TITLE = "LayerTitle";
        public static final String IS_CREATE = "IsCreate";
        public static final String IS_DELETE = "IsDelete";
        public static final String IS_EDIT = "IsEdit";
        public static final String IS_VIEW = "IsView";
        public static final String UPDATE_FIELDS = "UpdateFields";

    }

    public class TitleTraCuu {
        public static final String TIEN_NUOC = "Tiền nước";
        public static final String GIA_NUOC = "Giá nước";
        public static final String SU_CO = "Sự cố";
        public static final String THONG_BAO = "Thông báo";
    }

    public class TitleTraCuuThongBao {
        public static final String DONG_TIEN_NUOC = "+ Thông báo đóng tiền nước";
        public static final String CUP_NUOC = "+ Thông báo cúp nước";
        public static final String KHAC = "+ Thông báo khác";
    }

    public class QueryString {
        public static final String GET_HOA_DON = "SELECT TOP 1  [Nam],[Ky],[SoHoaDon],[NgayCapNhat],[TienHD]\n" +
                "  FROM [DocSoTH].[dbo].[HoaDon]\n" +
                "\n" +
                "  where hoadonid = ?\n" +
                "  order by nam desc, ky desc, dot desc";
        public static final String CUP_NUOC = "+ Thông báo cúp nước";
        public static final String KHAC = "+ Thông báo khác";
    }

    public class KhachHangColumn {
        public static final String DANH_BO = "DanhBo";
        public static final String HOP_DONG = "HopDong";
        public static final String TEN_KH = "TenKhachHang";
        public static final String DIA_CHI = "DiaChi";
        public static final String SDT = "SoDienThoai";
        public static final String GB = "GB";
        public static final String DM = "DM";
        public static final String CSC = "ChiSoCu";
        public static final String CSM = "ChiSoMoi";
        public static final String TIEU_THU = "TieuThu";
        public static final String BVMT = "BVMT";
        public static final String GTGT = "GTGT";
        public static final String THANH_TIEN = "ThanhTien";
        public static final String TY_LE_SH = "TiLeSH";
        public static final String TY_LE_SX = "TiLeSX";
        public static final String TY_LE_DV = "TiLeDV";
        public static final String TY_LE_HC = "TiLeHC";
    }

    public class HoaDonColumn {
        public static final String NAM = "Nam";
        public static final String KY = "Ky";
        public static final String SO_HOA_DON = "SoHoaDon";
        public static final String NGAY_CAP_NHAT = "NgayCapNhat";
        public static final String TIEN_HD = "TienHD";

    }

    public Constant() {
    }
}
