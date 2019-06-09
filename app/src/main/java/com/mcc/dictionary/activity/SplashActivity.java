package com.mcc.dictionary.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.mcc.dictionary.R;
import com.mcc.dictionary.data.constants.AppConstants;
import com.mcc.dictionary.utility.ActivityUtils;

public class SplashActivity extends AppCompatActivity {

    // init variables
    private Context mContext;
    private Activity mActivity;


    // init view
    private RelativeLayout splashBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initVariables();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFunctionality();
    }

    private void initVariables() {
        mActivity = SplashActivity.this;
        mContext = getApplicationContext();
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        splashBody = (RelativeLayout) findViewById(R.id.splashBody);
    }

    private void loadFunctionality() {

        splashBody.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.getInstance().invokeActivity(mActivity, MainActivity.class, true);
            }
        }, AppConstants.SPLASH_TIME);
    }
}



