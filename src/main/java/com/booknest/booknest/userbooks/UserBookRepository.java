package com.booknest.booknest.userbooks;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBookRepository extends CassandraRepository<UserBook, UserBookKey> {
    List<UserBook> findAllByKeyUserId(String userId);
}