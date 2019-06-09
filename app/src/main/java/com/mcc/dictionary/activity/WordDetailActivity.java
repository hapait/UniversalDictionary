package com.mcc.dictionary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcc.dictionary.data.constants.AppConstants;
import com.mcc.dictionary.R;
import com.mcc.dictionary.data.sqlite.FavoriteDbController;
import com.mcc.dictionary.model.WordDetail;

import java.util.ArrayList;
import java.util.Locale;

public class WordDetailActivity extends BaseActivity implements TextToSpeech.OnInitListener {

    //view
    private TextView tvWord, tvWordMeaning, tvSynonym, tvAntonym, tvExample, tvType;
    private ImageView voiceIcon,favorite,unFavorite;
    private Activity mActivity;
    //variable
    private TextToSpeech tts;
    private FavoriteDbController dbControllerFav;
    private ArrayList<WordDetail> favWordList;
    private WordDetail wordDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initView();
        initToolbar();
        enableBackButton();
        initVariable();
        initListener();
    }

    private void initVariable() {
        mActivity = WordDetailActivity.this;
        tts = new TextToSpeech(mActivity, this);
        dbControllerFav=new FavoriteDbController(mActivity);
        Bundle extraDetail = getIntent().getExtras();
        wordDetail = (WordDetail) extraDetail.getSerializable(AppConstants.KEY_WORD);
        setWordDetail(wordDetail);
        FavoriteIconSetting(wordDetail.getWord());
    }

    private void initView() {
        setContentView(R.layout.activity_word_detail);
        tvWord = findViewById(R.id.textview_word);
        tvWordMeaning = findViewById(R.id.textview_word_meaning);
        tvType = findViewById(R.id.textview_word_type);
        tvSynonym = findViewById(R.id.textview_synonym);
        tvAntonym = findViewById(R.id.textview_antonym);
        tvExample = findViewById(R.id.textview_example);
        voiceIcon = findViewById(R.id.voice_icon_detail);
        favorite=findViewById(R.id.favorite_icon_detail);
        unFavorite=findViewById(R.id.unfavorite_icon_detail);

    }

    private void initListener() {
        voiceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut(tvWord.getText().toString());
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite.setVisibility(View.GONE);
                unFavorite.setVisibility(View.VISIBLE);
                dbControllerFav.deleteFavoriteItemByWord(tvWord.getText().toString());
            }
        });

        unFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unFavorite.setVisibility(View.GONE);
                favorite.setVisibility(View.VISIBLE);
                checkFavoriteData(tvWord.getText().toString());
            }
        });

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setWordDetail(WordDetail wordDetail) {
        tvWord.setText(wordDetail.getWord());
        tvWordMeaning.setText(wordDetail.getMeaning());
        tvSynonym.setText( wordDetail.getSynonym());
        tvAntonym.setText( wordDetail.getAntonym());
        tvType.setText("---" + wordDetail.getType());
        tvExample.setText(wordDetail.getExample());
    }



    //check favorite table is empty or similar word detail is exist
    private void checkFavoriteData(String word) {
        int count = 0;
        favWordList = dbControllerFav.getAllData(); //get all data from sqlite
        if (!favWordList.isEmpty()) {
            for (int i = 0; i < favWordList.size(); i++) {

                if (word.equalsIgnoreCase(favWordList.get(i).getWord())) {
                    //if data found insert don't work
                    count++;
                    break;
                }
            }

            if (count <= 0) {
                // here data is inserted into database
                int insert = insertFavouriteData();
                if (insert >= 0) {
                    Toast.makeText(getApplicationContext(), "Added to favorite", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Not added to favorite", Toast.LENGTH_LONG).show();
                }

            } else {
                unFavorite.setVisibility(View.GONE);
                favorite.setVisibility(View.VISIBLE);
                //Toast.makeText(this, "Allready in favorite list", Toast.LENGTH_LONG).show();
            }

        } else {
            int insert = insertFavouriteData();
            if (insert >= 0) {
                Toast.makeText(getApplicationContext(), "Added to favorite", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Not added to favorite", Toast.LENGTH_LONG).show();
            }
        }

    }

    //insert data to favorite table sqlite
    private int insertFavouriteData() {
        String word = wordDetail.getWord();
        String meaning = wordDetail.getMeaning();
        String synonym = wordDetail.getSynonym();
        String type = wordDetail.getType();
        String antynym = wordDetail.getAntonym();
        String example = wordDetail.getExample();
        int insert = dbControllerFav.insertData(word, meaning, type, synonym, antynym, example);
        return insert;
    }


    private void FavoriteIconSetting(String word) {
        int count = 0;
        favWordList = dbControllerFav.getAllData(); //get all data from sqlite
        if (!favWordList.isEmpty()) {
            for (int i = 0; i < favWordList.size(); i++) {
                if (word.equalsIgnoreCase(favWordList.get(i).getWord())) {
                    //if data found insert don't work
                    count++;
                    break;
                }
            }

            if (count <= 0) {
                // here data is inserted into database+

            } else {
                unFavorite.setVisibility(View.GONE);
                favorite.setVisibility(View.VISIBLE);
                //Toast.makeText(this, "Allready in favorite list", Toast.LENGTH_LONG).show();
            }

        }
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
            Log.e("TTS", "Initialization Failed!");
        }
    }

    private void speakOut(String word) {
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
