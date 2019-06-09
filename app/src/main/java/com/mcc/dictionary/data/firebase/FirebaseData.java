package com.mcc.dictionary.data.firebase;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcc.dictionary.data.constants.FirebaseConstant;
import com.mcc.dictionary.listener.FirebaseDataLoadListener;
import com.mcc.dictionary.model.WordDetail;

import java.util.ArrayList;

public class FirebaseData {

    private Context context;
    private FirebaseDataLoadListener mListener;

    public FirebaseData(Context context) {
        this.context = context;
    }

    public void loadWordList() {
        FirebaseDatabase mDatabse = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDatabse.getReference(FirebaseConstant.FIREBASE_KEY);
        final ArrayList<WordDetail> wordList = new ArrayList<>();

        // get all data from firebase
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String word = null, antonym = null, type = null, example = null, synonym = null, meaning = null;

                        try {
                            word = snapshot.child(FirebaseConstant.WORD_FIELD).getValue().toString();
                            type = snapshot.child(FirebaseConstant.TYPE_FIELD).getValue().toString();
                            antonym = snapshot.child(FirebaseConstant.ANTONYM_FIELD).getValue().toString();
                            example = snapshot.child(FirebaseConstant.EXAMPLE_FIELD).getValue().toString();
                            synonym = snapshot.child(FirebaseConstant.SYNONYM_FIELD).getValue().toString();
                            meaning = snapshot.child(FirebaseConstant.WORD_MEANING_FIELD).getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        WordDetail detail = new WordDetail(word, meaning, type, synonym, antonym, example);
                        wordList.add(detail);
                    }
                    mListener.finishLoadData(wordList, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mListener.finishLoadData(null, false);
            }
        });
    }

    public void setClickListener(FirebaseDataLoadListener mListener) {
        this.mListener = mListener;
    }

}
