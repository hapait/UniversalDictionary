package com.mcc.dictionary.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcc.dictionary.data.constants.AppConstants;
import com.mcc.dictionary.R;
import com.mcc.dictionary.adapter.WordListAdapter;
import com.mcc.dictionary.data.firebase.FirebaseData;
import com.mcc.dictionary.data.sqlite.FavoriteDbController;
import com.mcc.dictionary.data.sqlite.HistoryDbController;
import com.mcc.dictionary.data.sqlite.WordListDbController;
import com.mcc.dictionary.listener.FirebaseDataLoadListener;
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

public class MainActivity extends BaseActivity implements TextToSpeech.OnInitListener{

    // Variables
    private ArrayList<WordDetail> favWordList = null;
    private ArrayList<WordDetail> historyList = null;
    private ArrayList<WordDetail> wordList;
    private ArrayList<WordDetail> filterWordList = null;
    private WordListAdapter wordListAdapter;
    private TextToSpeech tts;

    //database
    private WordListDbController dbControllerWordList;
    private HistoryDbController dbControllerHis;
    private FirebaseData firebaseData;

    // Views
    private Context mContext;
    private Activity mActivity;
    private RecyclerView recycleWordList;
    private LinearLayoutManager lytManagerWord;
    private TextView tvVoice;
    private EditText etSearch;
    private ImageView imgAddWord;
    private LinearLayout layoutFavorite, layoutSpeech,layoutPronuch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariable();
        initView();
        initToolbar();
        initDrawer();
        initFunctionality();
        initListener();

        // analytics event trigger
        AnalyticsUtils.getAnalyticsUtils(mContext).trackEvent("Home page");
        // load full screen ad
        AdUtils.getInstance(mContext).loadFullScreenAd(mActivity);
    }



    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = MainActivity.this;
        firebaseData = new FirebaseData(mActivity);
        dbControllerHis = new HistoryDbController(mActivity);
        dbControllerWordList = new WordListDbController(mActivity);
        favWordList = new ArrayList<>();
        historyList = new ArrayList<>();
        wordList = new ArrayList<>();
        filterWordList = new ArrayList<>();
        tts = new TextToSpeech(mActivity, this);

    }


    private void initView() {
        setContentView(R.layout.activity_main);

        recycleWordList = findViewById(R.id.recycleView_word_list);
        recycleWordList.setHasFixedSize(true);
        lytManagerWord = new LinearLayoutManager(mActivity);
        recycleWordList.setLayoutManager(lytManagerWord);
        recycleWordList.addItemDecoration(new DividerItemDecoration(this, lytManagerWord.VERTICAL, 16));
        wordListAdapter = new WordListAdapter(mActivity, wordList);
        recycleWordList.setAdapter(wordListAdapter);
        ((DragScrollBar) findViewById(R.id.dragScrollBar))
                .setIndicator(new AlphabetIndicator(this), true);

        etSearch = findViewById(R.id.searchBox);
        tvVoice = findViewById(R.id.voice_text);
        imgAddWord = findViewById(R.id.imgToolbarAdd);
        layoutFavorite=findViewById(R.id.linear_Favorite);
        layoutSpeech =findViewById(R.id.linear_speech);
        layoutPronuch=findViewById(R.id.linear_pronounce);
    }

    private void initListener() {

        layoutFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.getInstance().invokeActivity(MainActivity.this, FavoriteListActivity.class, false);
            }
        });

        wordListAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View view, int position) {
                loadFilterData();
                switch (view.getId()) {

                    case R.id.imageView_pronounce:
                        speakOut(position);
                        break;
                    default:
                        checkHistoryData(position);
                        break;
                }
            }
        });

        layoutPronuch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etSearch.equals("")) {
                    speakOut(etSearch.getText().toString());
                }
            }
        });

        imgAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addWOrdIntent=new Intent(mActivity,AddWordActivity.class);
                startActivityForResult(addWOrdIntent, AppConstants.REQ_CODE_ADD_WORD);
            }
        });


        layoutSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //wordListAdapter.getFilter().filter(s);
                filterWordList = getFilterData(wordList, s);
                wordListAdapter.setFilter(filterWordList);
                if (filterWordList.isEmpty()) {
                    showEmptyView();
                } else {
                    hideLoader();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //filter wordlist data
    private ArrayList<WordDetail> getFilterData(ArrayList<WordDetail> models, CharSequence searchKey) {
        searchKey = searchKey.toString().toLowerCase();

        final ArrayList<WordDetail> filteredModelList = new ArrayList<>();
        for (WordDetail model : models) {
            final String word = model.getWord().toLowerCase();
            final String meaning = model.getMeaning().toLowerCase();

            if (word.contains(searchKey) || meaning.contains(searchKey)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void loadFilterData() {
        if (!filterWordList.isEmpty()) {
            filterWordList.clear();
        }
        filterWordList.addAll(wordListAdapter.getDataList());
    }


    private void initFunctionality() {
        // load word list data
        loadWordList();
    }

    //Receiving speech input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AppConstants.REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etSearch.setText(result.get(0));
                }
                break;
            }

            case AppConstants.REQ_CODE_ADD_WORD: {
                if (resultCode == RESULT_OK && data!=null) {
                    Bundle extraDetail = data.getExtras();
                    WordDetail wordDetail = (WordDetail) extraDetail.getSerializable(AppConstants.KEY_WORD_LIST);
                    wordList.add(wordDetail);
                    Collections.sort(wordList, (WordDetail s1, WordDetail s2) -> {
                        return s1.getWord().compareToIgnoreCase(s2.getWord());
                    });
                    wordListAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }

    //load dataList from firebase database
    private void loadWordList() {
        initLoader();
        firebaseData.loadWordList();
        firebaseData.setClickListener(new FirebaseDataLoadListener() {
            @Override
            public void finishLoadData(ArrayList<WordDetail> dataList, boolean isSuccessful) {
                if (isSuccessful) {
                    // Load word list data
                    if (!wordList.isEmpty()) {
                        wordList.clear();
                    }
                    wordList.addAll(dataList);
                    wordList.addAll(dbControllerWordList.getAllData());

                    if (wordList.size() > 0) {
                        hideLoader();
                        // Sort word list by word
                        Collections.sort(wordList, (WordDetail s1, WordDetail s2) -> {
                            return s1.getWord().compareToIgnoreCase(s2.getWord());
                        });
                        wordListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(mContext, "Data doesn't loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //check history table is empty or similar word detail is exist
    private void checkHistoryData(int position) {
        String englishWord = filterWordList.get(position).getWord(); //get the selected position data from dataList
        int count = 0;
        historyList = dbControllerHis.getAllData();
        if (!historyList.isEmpty()) {
            for (int i = 0; i < historyList.size(); i++) {
                if (englishWord.equalsIgnoreCase(historyList.get(i).getWord())) {
                    count++;
                }
            }

            if (count <= 0) {
                int insert = insertHistoryData(position);
                if (insert >= 0) {

                } else {
                    Toast.makeText(this, "History don't save", Toast.LENGTH_LONG).show();
                }
            }

        } else {
            int insert = insertHistoryData(position);
            if (insert >= 0) {
            } else {
                Toast.makeText(this, "History don't save", Toast.LENGTH_LONG).show();
            }
        }
        //send detail data to detail acitvity
        sendDataToDetail(position);
    }

    //insert data to history table sqlite
    private int insertHistoryData(int position) {
        String word = filterWordList.get(position).getWord();
        String meaning = filterWordList.get(position).getMeaning();
        String synonym = filterWordList.get(position).getSynonym();
        String type = filterWordList.get(position).getType();
        String antynym = filterWordList.get(position).getAntonym();
        String example = filterWordList.get(position).getExample();
        int insert = dbControllerHis.insertData(word, meaning, type, synonym, antynym, example);
        return insert;
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

    private void sendDataToDetail(int position) {
        WordDetail wordDetail = new WordDetail(filterWordList.get(position).getWord(),
                filterWordList.get(position).getMeaning(), filterWordList.get(position).getType(),
                filterWordList.get(position).getSynonym(), filterWordList.get(position).getAntonym(),
                filterWordList.get(position).getExample());
        ActivityUtils.invokeWordDetails(mActivity, wordDetail);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, AppConstants.REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speakOut(int position) {
        String text = filterWordList.get(position).getWord();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void speakOut(String word) {
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    protected void onResume() {
        super.onResume();
        wordListAdapter.notifyDataSetChanged();
    }
}
