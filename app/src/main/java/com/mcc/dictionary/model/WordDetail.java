package com.mcc.dictionary.model;

import java.io.Serializable;


public class WordDetail implements Serializable{
    private String word,meaning,type,synonym,antonym,example;
    private int id, postId;

    public WordDetail(String word) {
        this.word = word;

    }

    public WordDetail(int id,String word, String meaning, String type, String synonym, String antonym, String example) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.type = type;
        this.synonym = synonym;
        this.antonym = antonym;
        this.example = example;
    }

    public WordDetail(String word, String meaning, String type, String synonym, String antonym, String example) {
        this.word = word;
        this.meaning = meaning;
        this.type=type;
        this.synonym = synonym;
        this.antonym = antonym;
        this.example = example;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public String getAntonym() {
        return antonym;
    }

    public void setAntonym(String antonym) {
        this.antonym = antonym;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}