package com.ditagis.hcm.tanhoa.cskh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ditagis.hcm.tanhoa.cskh.cskh.R;
import com.ditagis.hcm.tanhoa.cskh.entities.Constant;
import com.ditagis.hcm.tanhoa.cskh.entities.DApplication;
import com.ditagis.hcm.tanhoa.cskh.utities.ImageFile;
import com.esri.arcgisruntime.data.CodedValue;
import com.esri.arcgisruntime.data.CodedValueDomain;
import com.esri.arcgisruntime.data.Domain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NhapThongTinSuCoActivity extends AppCompatActivity {
    @BindView(R.id.etxtFullName_add_feature)
    EditText etxtFullName;
    @BindView(R.id.etxtPhoneNumber_add_feature)
    EditText etxtPhoneNumber;
    @BindView(R.id.etxtEmail_add_feature)
    EditText etxtEmail;
    @BindView(R.id.etxtAddress_add_feature)
    EditText etxtAddress;
    @BindView(R.id.etxtNote_add_feature)
    EditText etxtNote;
    @BindView(R.id.img_add_feature)
    ImageView mImage;
    @BindView(R.id.spin_hinh_thuc_phat_hien_add_feature)
    Spinner spinHinhThucPhatHien;
    private DApplication mApplication;
    private Uri mUri;
    private List<CodedValue> mCodeValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_thong_tin_su_co);
        mApplication = (DApplication) getApplication();
        ButterKnife.bind(this);
        (Objects.requireNonNull(getSupportActionBar())).setDisplayHomeAsUpEnabled(true);
        (Objects.requireNonNull(getSupportActionBar())).setDisplayShowHomeEnabled(true);
        etxtAddress.setText(mApplication.getDiemSuCo.getVitri());
        if (mApplication.getKhachHang() != null) {
            if (mApplication.getKhachHang().getTenKH() != null)
                etxtFullName.setText(mApplication.getKhachHang().getTenKH());
            if (mApplication.getKhachHang().getSdt() != null)
                etxtPhoneNumber.setText(mApplication.getKhachHang().getSdt());
        }
//        etxtFullName.setText(mApplication.getUserDangNhap.getDisplayName());
        //for camera
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

//        initViews();
    }

    private void initViews() {
        Domain domain = mApplication.getFeatureLayer().getFeatureTable().
                getField(Constant.FIELD_SUCO.HINH_THUC_PHAT_HIEN).getDomain();
        mCodeValues = ((CodedValueDomain) domain).getCodedValues();
        if (mCodeValues != null) {
            List<String> codes = new ArrayList<>();
            for (CodedValue codedValue : mCodeValues)
                codes.add(codedValue.getName());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, codes);
            spinHinhThucPhatHien.setAdapter(adapter);
            spinHinhThucPhatHien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (Constant.HINH_THUC_PHAT_HIEN_BE_NGAM.toLowerCase().equals(adapter.getItem(i).toLowerCase())
                            && !mApplication.getUserDangNhap().getRole().toLowerCase().startsWith(Constant.ROLE_PGN)) {
                        Toast.makeText(NhapThongTinSuCoActivity.this, "Bạn không có quyền chọn hình thức phát hiện Bể ngầm!", Toast.LENGTH_LONG).show();
                        if (adapter.getCount() > 1)
                            spinHinhThucPhatHien.setSelection(1);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    private boolean isNotEmpty() {

        return !etxtFullName.getText().toString().trim().isEmpty() &&
                !etxtPhoneNumber.getText().toString().trim().isEmpty() &&
                !etxtAddress.getText().toString().trim().isEmpty();
    }

    private void handlingEmpty() {
        Toast.makeText(this, "Thiếu thông tin!!!", Toast.LENGTH_SHORT).show();
    }

    public void capture() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());

        File photo = ImageFile.getFile(this);
//        this.mUri= FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider", photo);
        mUri = Uri.fromFile(photo);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
//        this.mUri = Uri.fromFile(photo);
        try {
            this.startActivityForResult(cameraIntent, Constant.REQUEST_CODE_ADD_FEATURE_ATTACHMENT);
        } catch (Exception e) {
            Log.e("Lỗi chụp ảnh", e.toString());
        }
    }

    private void baoSuCo() {
        mApplication.getDiemSuCo.setNguoiCapNhat(etxtFullName.getText().toString().trim());
        mApplication.getDiemSuCo.setSdt(etxtPhoneNumber.getText().toString().trim());
        mApplication.getDiemSuCo.setVitri(etxtAddress.getText().toString().trim());
        mApplication.getDiemSuCo.setGhiChu(etxtNote.getText().toString().trim());
        mApplication.getDiemSuCo.setEmail(etxtEmail.getText().toString().trim());
//        for (CodedValue codedValue : mCodeValues) {
//            if (codedValue.getName().equals(spinHinhThucPhatHien.getSelectedItem().toString()))
//                mApplication.getDiemSuCo.setHinhThucPhatHien(Short.parseShort(codedValue.getCode().toString()));
//        }
        finish();
    }

    public void onClickTextView(View view) {
        switch (view.getId()) {
            case R.id.txt_add_feature_add_feature:
                if (isNotEmpty()) {
                    if (etxtEmail.getText().toString().trim().isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(NhapThongTinSuCoActivity.this.getString(R.string.email_empty_title))
                                .setMessage(NhapThongTinSuCoActivity.this.getString(R.string.email_empty_message))
                                .setCancelable(false)
                                .setPositiveButton(NhapThongTinSuCoActivity.this.getString(R.string.btnOK), (dialog, i) -> {
                                    dialog.dismiss();
                                    baoSuCo();
                                }).setNegativeButton(NhapThongTinSuCoActivity.this.getString(R.string.btn_nhap_email), (dialog, i) -> dialog.dismiss());
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        baoSuCo();
                    }
                } else {
                    handlingEmpty();
                }
                break;
        }
    }

    public void onClickButton(View view) {
        switch (view.getId()) {
            case R.id.btnAttachemnt_add_feature:
                capture();
                break;
        }
    }

    @Nullable
    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            assert in != null;
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            assert in != null;
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " + b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.REQUEST_CODE_ADD_FEATURE_ATTACHMENT:
                if (resultCode == RESULT_OK) {
//                this.mUri= data.getData();
                    if (this.mUri != null) {
//                    Uri selectedImage = this.mUri;
//                    getContentResolver().notifyChange(selectedImage, null);
                        Bitmap bitmap = getBitmap(mUri.getPath());
                        try {
                            if (bitmap != null) {
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90);
                                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                byte[] image = outputStream.toByteArray();
                                Toast.makeText(this, "Đã lưu ảnh", Toast.LENGTH_SHORT).show();
                                mImage.setImageBitmap(rotatedBitmap);
//                            mMapViewHandler.addFeature(image);
                                mApplication.getDiemSuCo.setImage(image);
                            }
                        } catch (Exception ignored) {
                        }
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Hủy chụp ảnh", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(this, "Lỗi khi chụp ảnh", Toast.LENGTH_SHORT);
                }
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                goHome();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        goHome();
    }

    private void goHome() {
        mApplication.getDiemSuCo.setPoint(null);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
