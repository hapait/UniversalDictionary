package com.mcc.dictionary.utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mcc.dictionary.data.constants.AppConstants;
import com.mcc.dictionary.activity.WordDetailActivity;
import com.mcc.dictionary.model.WordDetail;

public class ActivityUtils {

    private static ActivityUtils sActivityUtils = null;

    public static ActivityUtils getInstance() {
        if (sActivityUtils == null) {
            sActivityUtils = new ActivityUtils();
        }
        return sActivityUtils;
    }
    public void invokeActivity(Activity activity, Class<?> tClass, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public static void invokeWordDetails(Activity activity, WordDetail wordDetail){
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.KEY_WORD, wordDetail);
        Intent intent = new Intent(activity, WordDetailActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static void invokeUpdateWordList(Activity activity, WordDetail wordDetail){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.KEY_WORD_LIST, wordDetail);
        intent.putExtras(bundle);
        activity.setResult(activity.RESULT_OK,intent);
        activity.finish();
    }

}
