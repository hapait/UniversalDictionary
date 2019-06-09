package com.mcc.dictionary.listener;

import com.mcc.dictionary.model.WordDetail;

import java.util.ArrayList;

public interface FirebaseDataLoadListener {
     void finishLoadData(ArrayList<WordDetail> dataList, boolean isSuccessful);
}
