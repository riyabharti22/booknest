package com.booknest.booknest.book;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Data
@Table(value = "book_by_id")
public class Book {

    @PrimaryKey
    private BookPrimaryKey key;

    @Column("book_name")
    private String name;

    @Column("description")
    private String description;

    @Column("author_names")
    private List<String> authorNames;

    @Column("author_id")
    private List<String> authorIds;

    @Column("cover_ids")
    private List<String> coverIds;

    // safer than LocalDate for OpenLibrary data
    @Column("publish_date")
    private String publishDate;

    // -----------------------------
    // SAFE COVER URL (no crash)
    // -----------------------------
    public String getCoverUrl() {
        if (coverIds != null && !coverIds.isEmpty() && coverIds.get(0) != null) {
            return "https://covers.openlibrary.org/b/id/" + coverIds.get(0) + "-L.jpg";
        }
        return "/images/no-cover.png";
    }

    // -----------------------------
    // SAFE AUTHOR DISPLAY
    // -----------------------------
    public String getPrimaryAuthor() {
        if (authorNames != null && !authorNames.isEmpty()) {
            return authorNames.get(0);
        }
        return "Unknown Author";
    }
}