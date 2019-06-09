package com.mcc.dictionary.data.sqlite;


public class DbConstants {

    // commons
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    public static final String COLUMN_NAME_NULLABLE = null;
    // favourites
    public static final String FAVOURITE_TABLE_NAME = "favourite";
    public static final String HISTORY_TABLE_NAME = "history";

    //wordlists
    public static final String WORD_LISTS_TABLE_NAME = "wordlist";

    public static final String _ID = "_id";
    public static final String POST_WORD = "post_word";
    public static final String POST_TYPE = "post_type";
    public static final String POST_MEANING = "post_meaning";
    public static final String POST_SYNONYM = "post_synonym";
    public static final String POST_ANTONYM = "post_antonym";
    public static final String POST_EXAMPLE = "post_example";

    //favorite table sql
    public static final String SQL_CREATE_FAVOURITE_ENTRIES =
            "CREATE TABLE " + FAVOURITE_TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    POST_WORD + TEXT_TYPE + COMMA_SEP +
                    POST_MEANING + TEXT_TYPE + COMMA_SEP +
                    POST_TYPE + TEXT_TYPE + COMMA_SEP +
                    POST_SYNONYM + TEXT_TYPE + COMMA_SEP +
                    POST_ANTONYM + TEXT_TYPE + COMMA_SEP +
                    POST_EXAMPLE + TEXT_TYPE + " )";
    public static final String SQL_DELETE_FAVOURITE_ENTRIES =
            "DROP TABLE IF EXISTS " + FAVOURITE_TABLE_NAME;

    //history table sql
    public static final String SQL_CREATE_HISTORY_ENTRIES =
            "CREATE TABLE " + HISTORY_TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    POST_WORD + TEXT_TYPE + COMMA_SEP +
                    POST_MEANING + TEXT_TYPE + COMMA_SEP +
                    POST_TYPE + TEXT_TYPE + COMMA_SEP +
                    POST_SYNONYM + TEXT_TYPE + COMMA_SEP +
                    POST_ANTONYM + TEXT_TYPE + COMMA_SEP +
                    POST_EXAMPLE + TEXT_TYPE + " )";
    public static final String SQL_DELETE_HISTORY_ENTRIES =
            "DROP TABLE IF EXISTS " + HISTORY_TABLE_NAME;


    //Word list table sql
    public static final String SQL_CREATE_WORD_LISTS_ENTRIES =
            "CREATE TABLE " + WORD_LISTS_TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    POST_WORD + TEXT_TYPE + COMMA_SEP +
                    POST_MEANING + TEXT_TYPE + COMMA_SEP +
                    POST_TYPE + TEXT_TYPE + COMMA_SEP +
                    POST_SYNONYM + TEXT_TYPE + COMMA_SEP +
                    POST_ANTONYM + TEXT_TYPE + COMMA_SEP +
                    POST_EXAMPLE + TEXT_TYPE + " )";
    public static final String SQL_DELETE_WORD_LISTS_ENTRIES =
            "DROP TABLE IF EXISTS " + WORD_LISTS_TABLE_NAME;


}
