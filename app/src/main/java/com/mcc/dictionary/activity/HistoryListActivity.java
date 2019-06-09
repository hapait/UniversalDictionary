package com.mcc.dictionary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdView;
import com.mcc.dictionary.R;
import com.mcc.dictionary.adapter.HistoryAdapter;
import com.mcc.dictionary.data.sqlite.HistoryDbController;
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

public class HistoryListActivity extends BaseActivity  implements TextToSpeech.OnInitListener{

    private Context mContext;
    private Activity mActivity;
    private RecyclerView recycleWordList;
    private ArrayList<WordDetail> historywordList;
    private HistoryAdapter historyAdapter;
    private HistoryDbController dbController;
    private TextToSpeech tts;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariable();
        initView();
        loadFavoriteData();
        initToolbar();
        enableBackButton();
        initListener();

        setToolbarTitle(getString(R.string.history_list));
        // analytics event trigger
        AnalyticsUtils.getAnalyticsUtils(mContext).trackEvent("History page");

    }

    private void initView() {
        setContentView(R.layout.activity_history_list);
        recycleWordList = (RecyclerView) findViewById(R.id.recycleView_history);
        recycleWordList.setHasFixedSize(true);
        recycleWordList.setLayoutManager(new LinearLayoutManager(this));
        recycleWordList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        historyAdapter = new HistoryAdapter(this, historywordList);
        recycleWordList.setAdapter(historyAdapter);
        ((DragScrollBar)findViewById(R.id.dragScrollBar_history))
                .setIndicator(new AlphabetIndicator(this), true);
        // init loader
        initLoader();

    }

    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = HistoryListActivity.this;
        dbController = new HistoryDbController(mActivity);
        historywordList = new ArrayList<>();
        tts = new TextToSpeech(mActivity, this);
    }

    private void initListener() {
        historyAdapter.setItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemListener(View viewItem, int position) {
                switch (viewItem.getId()){
                    case R.id.textView_history:
                        sendDataToDetail(position);
                        break;
                    case R.id.icon_voice_history:
                        speakOut(position);
                        break;
                    default:
                        sendDataToDetail(position);
                        break;
                }

            }
        });

    }

    private void loadFavoriteData(){
        if (!historywordList.isEmpty()){
            historywordList.clear();
        }
        historywordList.addAll(dbController.getAllData());
        Collections.sort(historywordList, (WordDetail s1, WordDetail s2) -> {
            return s1.getWord().compareToIgnoreCase(s2.getWord());
        });
        if (historywordList.isEmpty()){
            showEmptyView();
        }
        else {
            hideLoader();
        }

    }


    private void sendDataToDetail(int position) {
        WordDetail wordDetail=new WordDetail(historywordList.get(position).getWord(),
                historywordList.get(position).getMeaning(),historywordList.get(position).getType(),
                historywordList.get(position).getSynonym(),historywordList.get(position).getAntonym(),
                historywordList.get(position).getExample());
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

        String text =historywordList.get(position).getWord();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    protected void onResume() {
        super.onResume();

        //load banner ad
        AdUtils.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adView));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
