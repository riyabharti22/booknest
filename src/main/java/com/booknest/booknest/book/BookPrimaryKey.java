package com.booknest.booknest.book;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyClass
public class BookPrimaryKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKeyColumn(name = "book_id", type = PrimaryKeyType.PARTITIONED)
    private String bookId;
}