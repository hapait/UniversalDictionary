package com.mcc.dictionary.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mcc.dictionary.data.constants.AppConstants;
import com.mcc.dictionary.R;
import com.mcc.dictionary.data.firebase.FirebaseData;
import com.mcc.dictionary.data.sqlite.WordListDbController;
import com.mcc.dictionary.listener.FirebaseDataLoadListener;
import com.mcc.dictionary.model.WordDetail;
import com.mcc.dictionary.utility.ActivityUtils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import java.util.ArrayList;

public class AddWordActivity extends BaseActivity {

    //view
    private Activity mActivity;
    private Context mContext;
    private EditText word, wordMeaning, antonym, example, synonym;
    private Button btnSave;
    private ArrayList<WordDetail> wordList;
    //for spinner
    private MaterialBetterSpinner wordTypeSpinner;
    //database
    private FirebaseData firebaseData;
    private WordListDbController dbControllerWordList;
    private WordListDbController DbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initToolbar();
        enableBackButton();
        initVariable();
        initListener();
        setToolbarTitle(getString(R.string.add_word_list));
        initFunctionality();
    }

    private void initView() {
        setContentView(R.layout.activity_add_word);
        word = (EditText) findViewById(R.id.type_word);
        wordMeaning = (EditText) findViewById(R.id.word_meaning);
        antonym = (EditText) findViewById(R.id.type_antonym_word);
        synonym = (EditText) findViewById(R.id.word_synonym);
        example = (EditText) findViewById(R.id.type_example_word);
        btnSave = (Button) findViewById(R.id.button_wordlist);
        wordTypeSpinner = (MaterialBetterSpinner) findViewById(R.id.word_type_spinner);
    }

    //add method initvariable
    private void initVariable() {
        DbController = new WordListDbController(this);
        mActivity = AddWordActivity.this;
        mContext = getApplicationContext();
        wordList = new ArrayList<>();
        firebaseData = new FirebaseData(mActivity);
        dbControllerWordList = new WordListDbController(mActivity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddWordActivity.this, android.R.layout.simple_dropdown_item_1line, AppConstants.SPINNER_DATA);
        wordTypeSpinner.setAdapter(adapter);

    }

    private void initFunctionality() {
        loadWordList();
    }

    // add method of insert
    public void initListener() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                if (!word.getText().toString().isEmpty() &&
                        !wordMeaning.getText().toString().isEmpty() &&
                        !wordTypeSpinner.getText().toString().isEmpty() &&
                        !synonym.getText().toString().isEmpty() &&
                        !antonym.getText().toString().isEmpty() &&
                        !example.getText().toString().isEmpty()) {

                    for (int i = 0; i < wordList.size(); i++) {
                        String strWord=word.getText().toString().trim();
                        if (strWord.equalsIgnoreCase(wordList.get(i).getWord())) {
                            count++;
                            Toast.makeText(getApplicationContext(), "Word already in the dictionary", Toast.LENGTH_LONG).show();
                            break;
                        }
                        else {
                            count=0;
                        }


                    }

                    if (count==0){
                        int isInserted = DbController.insertData(word.getText().toString(),
                                wordMeaning.getText().toString(),
                                wordTypeSpinner.getText().toString(),
                                synonym.getText().toString(),
                                antonym.getText().toString(),
                                example.getText().toString());

                        if (isInserted > 0) {
                            Toast.makeText(getApplicationContext(), "Word Added", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Word not Inserted", Toast.LENGTH_LONG).show();
                        WordDetail wordDetail=new WordDetail(word.getText().toString(),
                                wordMeaning.getText().toString(),
                                wordTypeSpinner.getText().toString(),
                                synonym.getText().toString(),
                                antonym.getText().toString(),
                                example.getText().toString());
                        addDataToWordList(wordDetail);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "please fill up all field", Toast.LENGTH_LONG).show();
                }



            }
        });
    }

    //load dataList from firebase database
    private void loadWordList() {
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

                } else {
                    Toast.makeText(mContext, "Data doesn't loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addDataToWordList(WordDetail wordDetail){
        ActivityUtils.invokeUpdateWordList(mActivity,wordDetail);
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
