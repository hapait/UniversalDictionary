package com.mcc.dictionary.activity;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.mcc.dictionary.R;


public class SettingsActivity extends BaseActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        // set parent view
        setContentView(R.layout.activity_settings);

        initToolbar();
        enableBackButton();

        // replace linear layout by preference screen
        getFragmentManager().beginTransaction().replace(R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_preference);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
