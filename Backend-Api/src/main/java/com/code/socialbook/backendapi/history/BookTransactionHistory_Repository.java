package com.code.socialbook.backendapi.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

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

    @Query("""
            SELECT
            (COUNT(*) > 0) AS isBorrowed
            FROM BookTransactionHistory_Model historyModel
            WHERE historyModel.user.id = :userId
            AND historyModel.book.id = :bookid
            AND historyModel.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

    @Query("""
        SELECT transaction
        FROM BookTransactionHistory_Model transaction
        WHERE transaction.user.id = :userId
        AND transaction.book.id = :bookid
        AND transaction.returned = false
        AND transaction.returnApproved = false
        """)
    Optional<BookTransactionHistory_Model> findByBookIdAndUserId(Integer bookId, Integer userId);

    @Query("""
        SELECT transaction
        FROM BookTransactionHistory_Model transaction
        WHERE transaction.book.owner.id = :ownerId
        AND transaction.book.id = :bookid
        AND transaction.returned = true
        AND transaction.returnApproved = false
        """)
    Optional<BookTransactionHistory_Model> findByBookIdAndOwnerId(Integer bookId, Integer ownerId);
}
