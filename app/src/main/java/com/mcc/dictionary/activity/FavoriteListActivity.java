package com.mcc.dictionary.activity;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdView;
import com.mcc.dictionary.R;
import com.mcc.dictionary.adapter.FavouriteAdapter;
import com.mcc.dictionary.data.sqlite.FavoriteDbController;
import com.mcc.dictionary.listener.OnItemClickListener;
import com.mcc.dictionary.model.WordDetail;
import com.mcc.dictionary.utility.ActivityUtils;
import com.mcc.dictionary.utility.AdUtils;
import com.mcc.dictionary.utility.AnalyticsUtils;
import com.mcc.dictionary.utility.DividerItemDecoration;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;
import com.turingtechnologies.materialscrollbar.DragScrollBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class FavoriteListActivity extends BaseActivity implements TextToSpeech.OnInitListener {

    private Context mContext;
    private Activity mActivity;

    private RecyclerView recycleWordList;
    private ArrayList<WordDetail> favWordList;
    private FavouriteAdapter favouriteAdapter;
    private FavoriteDbController dbController;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariable();
        initView();
        loadFavoriteData();
        initToolbar();
        enableBackButton();
        initListener();

        // add toolbar title
        setToolbarTitle(getString(R.string.favorite_list));

        // analytics event trigger
        AnalyticsUtils.getAnalyticsUtils(mContext).trackEvent("Favorite page");

    }

    private void initView() {
        setContentView(R.layout.activity_favorite_list);
        recycleWordList = findViewById(R.id.recycleView_favorite);
        recycleWordList.setHasFixedSize(true);
        recycleWordList.setLayoutManager(new LinearLayoutManager(this));
        recycleWordList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        favouriteAdapter = new FavouriteAdapter(this, favWordList);
        recycleWordList.setAdapter(favouriteAdapter);
        ((DragScrollBar)findViewById(R.id.dragScrollBar_favorite))
                .setIndicator(new AlphabetIndicator(this), true);

        // init loader
        initLoader();

    }

    private void initVariable() {

        mContext = getApplicationContext();
        mActivity = FavoriteListActivity.this;
        dbController = new FavoriteDbController(mActivity);
        favWordList = new ArrayList<>();
        tts = new TextToSpeech(mActivity, this);

    }

    private void loadFavoriteData() {
        if (!favWordList.isEmpty()) {
            favWordList.clear();
        }
        favWordList.addAll(dbController.getAllData());
        Collections.sort(favWordList, (WordDetail s1, WordDetail s2) -> {
            return s1.getWord().compareToIgnoreCase(s2.getWord());
        });
        if (favWordList.isEmpty()){
            showEmptyView();
        }
        else {
            hideLoader();
        }
    }


    private void initListener() {

        favouriteAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View viewItem, int position) {
                switch (viewItem.getId()) {
                    case R.id.icon_favorite_list_checked:
                        dbController.deleteFavoriteItem(favWordList.get(position).getId());
                        favWordList.remove(position);
                        favouriteAdapter.notifyDataSetChanged();
                        loadFavoriteData();
                        break;

                    case R.id.icon_voice_favorite:
                        speakOut(position);
                        break;
                    default:
                        sendDataToDetail(position);
                        break;
                }

            }
        });

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private void sendDataToDetail(int position) {
        WordDetail wordDetail=new WordDetail(favWordList.get(position).getWord(),
                favWordList.get(position).getMeaning(),favWordList.get(position).getType(),
                favWordList.get(position).getSynonym(),favWordList.get(position).getAntonym(),
                favWordList.get(position).getExample());
        ActivityUtils.invokeWordDetails(mActivity,wordDetail);

    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //pronounce.setEnabled(true);
                //speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut(int position) {

        String text =favWordList.get(position).getWord();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //load banner ad
        AdUtils.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adView));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
