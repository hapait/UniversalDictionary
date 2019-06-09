package com.mcc.dictionary.utility;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsUtils {

    private FirebaseAnalytics mFirebaseAnalytics;
    private static AnalyticsUtils analyticsUtils;

    public static AnalyticsUtils getAnalyticsUtils(Context context) {
        if(analyticsUtils == null) {
            analyticsUtils = new AnalyticsUtils(context);
        }
        return analyticsUtils;
    }

    private AnalyticsUtils(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void trackEvent(String eventName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventName);
        mFirebaseAnalytics.logEvent("PAGE_VISIT", bundle);
    }

}
