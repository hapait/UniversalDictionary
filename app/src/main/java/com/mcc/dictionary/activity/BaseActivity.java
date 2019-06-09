package com.mcc.dictionary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.mcc.dictionary.R;
import com.mcc.dictionary.data.constants.AppConstants;
import com.mcc.dictionary.utility.ActivityUtils;
import com.mcc.dictionary.utility.AdUtils;
import com.mcc.dictionary.utility.AppUtils;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private Activity mActivity;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private LinearLayout loadingView, noDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();

        // uncomment this line to disable ad
        // AdUtils.getInstance(mContext).disableBannerAd();
        // AdUtils.getInstance(mContext).disableInterstitialAd();
    }

    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = BaseActivity.this;
    }

    public void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    public void enableBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
        }
        return mToolbar;
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void initDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_favorite) {
            //show full screen ads
            showAdThenActivity(FavoriteListActivity.class);

        } else if (id == R.id.nav_history) {
            ActivityUtils.getInstance().invokeActivity(mActivity, HistoryListActivity.class, false);


        } else if (id == R.id.nav_addWord) {
            Intent addWOrdIntent=new Intent(mActivity,AddWordActivity.class);
            startActivityForResult(addWOrdIntent, AppConstants.REQ_CODE_ADD_WORD);

        }else if (id == R.id.nav_setting) {
            Intent intent = new Intent(mActivity, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            AppUtils.shareApp(mActivity);

        } else if (id == R.id.nav_rate) {
            AppUtils.rateThisApp(mActivity); // this feature will only work after publish the app
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showAdThenActivity(final Class<?> activity) {
        if (AdUtils.getInstance(mContext).showFullScreenAd()) {
            AdUtils.getInstance(mContext).getInterstitialAd().setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    ActivityUtils.getInstance().invokeActivity(mActivity, activity, false);
                }
            });
        } else {
            ActivityUtils.getInstance().invokeActivity(mActivity, activity, false);
        }
    }

    public void initLoader() {
        loadingView = (LinearLayout) findViewById(R.id.loadingView);
        noDataView = (LinearLayout) findViewById(R.id.noDataView);
    }

    public void showLoader() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }

        if (noDataView != null) {
            noDataView.setVisibility(View.GONE);
        }
    }

    public void hideLoader() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (noDataView != null) {
            noDataView.setVisibility(View.GONE);
        }
    }

    public void showEmptyView() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (noDataView != null) {
            noDataView.setVisibility(View.VISIBLE);
        }
    }


}
