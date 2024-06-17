package com.vivelibre.pruebafilter.pojos;

import java.time.LocalDate;

public class BookDate extends Book{
    
    private String date;
    
    public BookDate(int id, String title, int pages, String summary, LocalDate publicationDate, Author author, String date){
        super(id, title, pages, summary, publicationDate, author);
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
