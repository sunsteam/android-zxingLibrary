package com.uuch.android_zxinglibrary;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BaseActivity extends AppCompatActivity {

    public void moveBack(View v){
        onBackPressed();
    }
}
