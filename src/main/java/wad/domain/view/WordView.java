package wad.domain.view;

import wad.domain.Word;

public class WordView {
    private Word word;
    private String previous_word_url;
    private String next_word_url;
    private String delete_word_url;
    private String edit_word_url;
    
    // Constructor
    public WordView()
    {
        this.word=new Word();
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public String getPrevious_word_url() {
        return previous_word_url;
    }

    public void setPrevious_word_url(String previous_word_url) {
        this.previous_word_url = previous_word_url;
    }

    public String getNext_word_url() {
        return next_word_url;
    }

    public void setNext_word_url(String next_word_url) {
        this.next_word_url = next_word_url;
    }

    public String getDelete_word_url() {
        return delete_word_url;
    }

    public void setDelete_word_url(String delete_word_url) {
        this.delete_word_url = delete_word_url;
    }

    public String getEdit_word_url() {
        return edit_word_url;
    }

    public void setEdit_word_url(String edit_word_url) {
        this.edit_word_url = edit_word_url;
    }
}
