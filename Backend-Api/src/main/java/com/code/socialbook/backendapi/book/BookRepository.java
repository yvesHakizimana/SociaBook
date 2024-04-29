package com.code.socialbook.backendapi.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book_model, Integer>, JpaSpecificationExecutor<Book_model> {
    @Query("""
     SELECT book
     FROM Book_model book
     WHERE book.archived = false
     AND book.shareable = true
     AND book.owner.id != :userId
""")
    Page<Book_model> findAllDisplayableBooks(Pageable pageable, Integer userId);
}
