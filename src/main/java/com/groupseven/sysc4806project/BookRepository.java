package com.groupseven.sysc4806project;

import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {
    Iterable<Book> findBooksByGenre (String genre);
    Iterable<Book> findBooksByDeleted (boolean deleted);
}
