package com.uuch.android_zxinglibrary;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;


    private int currentClickId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         /*
         * 打开默认二维码扫描界面
         *
         * 打开系统图片选择界面
         *
         * 定制化显示扫描界面
         *
         * 测试生成二维码图片
         */
        setContentView(R.layout.activity_main);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra(CodeUtils.RESULT_STRING);
                Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
        }

        /*
         * 选择系统图片并解析
         */
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_CAMERA_PERM) {
            Toast.makeText(this, "从设置页面返回...", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 请求Camera权限
     */
    public static final int REQUEST_CAMERA_PERM = 101;

    /**
     * 请求读取SD卡权限
     */
    public static final int REQUEST_SD_PERM = 102;


    /**
     * EsayPermissions接管权限处理逻辑
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    public void cameraTask() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            openScanActivity();
        } else {
            EasyPermissions.requestPermissions(this, "需要请求camera权限",REQUEST_CAMERA_PERM, perms);
        }
    }

    @AfterPermissionGranted(REQUEST_SD_PERM)
    public void pickImageTask() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            startPickImage();
        } else {
            EasyPermissions.requestPermissions(this, "需要请求储存空间权限",REQUEST_SD_PERM, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(this, "执行onPermissionsGranted()...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "执行onPermissionsDenied()...", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "当前App需要申请camera权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null /* click listener */)
                    .setRequestCode(REQUEST_CAMERA_PERM)
                    .build()
                    .show();
        }
    }

    public void decodeScan(View view) {
        currentClickId = view.getId();
        cameraTask();
    }

    public void decodeLocal(View view) {
        pickImageTask();
    }

    public void encode(View view) {
        Intent intent = new Intent(this, ThreeActivity.class);
        startActivity(intent);
    }


    private void openScanActivity() {
        switch (currentClickId) {
            case R.id.button1:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.button3:
                intent = new Intent(this, SecondActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    private void startPickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }
}
