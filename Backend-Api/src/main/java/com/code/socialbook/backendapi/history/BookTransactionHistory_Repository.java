package com.code.socialbook.backendapi.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookTransactionHistory_Repository extends JpaRepository<BookTransactionHistory_Model, Integer> {
    @Query("""
         SELECT history
         FROM BookTransactionHistory_Model history
         WHERE history.user.id = :userId
        """)
    Page<BookTransactionHistory_Model> findAllBorrowedBooks(Pageable pageable, Integer userId);


    @Query("""
         SELECT history
         FROM BookTransactionHistory_Model history
         WHERE history.book.owner.id = :userId
""")
    Page<BookTransactionHistory_Model> findAllReturnedBooks(Pageable pageable, Integer userId);

}
