package com.booknest.booknest.userbooks;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table(value = "userbook_by_userid")
public class UserBook {

    @PrimaryKey
    private UserBookKey key;

    @Column("reading_status")
    private String readingStatus;

    @Column("rating")
    private int rating;

    @Column("started_date")
    private LocalDate startedDate;

    @Column("finished_date")
    private LocalDate finishedDate;

    @Column("book_name")
    private String bookName;

    @Column("cover_url")
    private String coverUrl;
}