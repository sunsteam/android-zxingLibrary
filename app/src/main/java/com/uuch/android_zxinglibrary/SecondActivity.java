package com.uuch.android_zxinglibrary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 定制化显示扫描界面
 */
public class SecondActivity extends BaseActivity {

    public boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int visibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(visibility);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_second);


        initView();

        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        captureFragment.setAnalyzeCallback(new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(CodeUtils.RESULT_STRING, result);
                setResult(RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onAnalyzeFailed() {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(CodeUtils.RESULT_STRING, "");
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });


    }

    private void initView() {
        TextView caption = (TextView) findViewById(R.id.title_caption);
        caption.setTextColor(Color.WHITE);
        caption.setText("扫一扫");

        TextView close = (TextView) findViewById(R.id.title_tv_left);
        close.setTextColor(Color.WHITE);
        close.setText("关闭");

        View layout = findViewById(R.id.title_layout);
        layout.setBackgroundColor(Color.parseColor("#cc333333"));

        findViewById(R.id.camera_light).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = !isOpen;
                CodeUtils.isLightEnable(isOpen);
            }
        });
    }
}
