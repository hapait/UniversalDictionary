package com.mcc.dictionary.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mcc.dictionary.model.WordDetail;

import java.util.ArrayList;

public class FavoriteDbController {

    private SQLiteDatabase db;
    private Context mContext;

    public FavoriteDbController(Context context) {
        db = DbHelper.getInstance(context).getWritableDatabase();
        mContext = context;
    }

    public int insertData(String postWord, String postMeaning, String postType, String postSynonym, String postAntonym, String postExample) {

        ContentValues values = new ContentValues();
        values.put(DbConstants.POST_WORD, postWord);
        values.put(DbConstants.POST_MEANING, postMeaning);
        values.put(DbConstants.POST_TYPE, postType);
        values.put(DbConstants.POST_SYNONYM, postSynonym);
        values.put(DbConstants.POST_ANTONYM, postAntonym);
        values.put(DbConstants.POST_EXAMPLE, postExample);

        // Insert the new row, returning the primary key value of the new row
        return (int) db.insert(
                DbConstants.FAVOURITE_TABLE_NAME,
                DbConstants.COLUMN_NAME_NULLABLE,
                values);
    }

    public ArrayList<WordDetail> getAllData() {


        String[] projection = {
                DbConstants._ID,
                DbConstants.POST_WORD,
                DbConstants.POST_MEANING,
                DbConstants.POST_TYPE,
                DbConstants.POST_SYNONYM,
                DbConstants.POST_ANTONYM,
                DbConstants.POST_EXAMPLE,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DbConstants._ID + " DESC";

        Cursor c = db.query(
                DbConstants.FAVOURITE_TABLE_NAME,  // The table name to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return fetchData(c);
    }

    private ArrayList<WordDetail> fetchData(Cursor c) {
        ArrayList<WordDetail> favDataArray = new ArrayList<>();

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    // get  the  data into array,or class variable
                    int itemId = c.getInt(c.getColumnIndexOrThrow(DbConstants._ID));
                    String word = c.getString(c.getColumnIndexOrThrow(DbConstants.POST_WORD));
                    String type = c.getString(c.getColumnIndexOrThrow(DbConstants.POST_TYPE));
                    String meaning = c.getString(c.getColumnIndexOrThrow(DbConstants.POST_MEANING));
                    String synonym = c.getString(c.getColumnIndexOrThrow(DbConstants.POST_SYNONYM));
                    String antonym = c.getString(c.getColumnIndexOrThrow(DbConstants.POST_ANTONYM));
                    String example = c.getString(c.getColumnIndexOrThrow(DbConstants.POST_EXAMPLE));


                    // wrap up data list and return
                    favDataArray.add(new WordDetail(itemId, word, meaning, type, synonym, antonym, example));
                } while (c.moveToNext());
            }
            c.close();
        }
        return favDataArray;
    }

    public void deleteFavoriteItem(int itemId) {
        // Which row to update, based on the ID
        String selection = DbConstants._ID + "=?";
        String[] selectionArgs = {String.valueOf(itemId)};

        db.delete(
                DbConstants.FAVOURITE_TABLE_NAME,
                selection,
                selectionArgs);
    }

    public void deleteFavoriteItemByWord(String itemWord) {
        // Which row to update, based on the ID
        String selection = DbConstants.POST_WORD + "=?";
        String[] selectionArgs = {String.valueOf(itemWord)};

        db.delete(
                DbConstants.FAVOURITE_TABLE_NAME,
                selection,
                selectionArgs);
    }

    public void deleteAllFav() {
        db.delete(
                DbConstants.FAVOURITE_TABLE_NAME,
                null,
                null);
    }

}
