package com.booknest.booknest.author;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table(value = "author_by_id")
public class Author {

    @PrimaryKey
    private String id;

    @Column("author_name")
    private String name;

    @Column("personal_name")
    private String personalName;

    @Column("bio")
    private String bio;
}