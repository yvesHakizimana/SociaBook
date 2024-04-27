package com.code.socialbook.backendapi.feedback;

import com.code.socialbook.backendapi.book.Book_model;
import org.springframework.stereotype.Service;

@Service
public class FeedbackMapper {
    public Feedback_Model toFeedback(FeedbackRequest request){
        return Feedback_Model.builder()
                .note(request.note())
                .comment(request.comments())
                .book(Book_model.builder()
                        .id(request.bookId())
                        .archived(false)
                        .shareable(false)
                        .build())
                .build();
    }
}
