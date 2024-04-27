package com.code.socialbook.backendapi.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback_Model, Integer> {
    @Query("""
        SELECT feedback
        FROM Feedback_Model feedback
        WHERE feedback.book.id = :bookId
        """)
    Page<Feedback_Model> findAllByBookId(Integer bookId, Pageable pageable);
}
