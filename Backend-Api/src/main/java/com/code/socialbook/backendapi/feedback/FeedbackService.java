package com.code.socialbook.backendapi.feedback;

import com.code.socialbook.backendapi.book.Book_Repository;
import com.code.socialbook.backendapi.book.PageResponse;
import com.code.socialbook.backendapi.exceptions.OperationNotPermittedException;
import com.code.socialbook.backendapi.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final Book_Repository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer saveFeedback(FeedbackRequest request, Authentication connectedUser) {
        var foundBook = bookRepository.findById(request.bookId()).orElseThrow(() -> new EntityNotFoundException("No book found with this id:: " + request.bookId()));

        if(foundBook.isArchived() || !foundBook.isShareable()){
            throw new OperationNotPermittedException("You cannot give a feedback to archived or  not shareable books.");
        }

        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(foundBook.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot give a feedback as you are the owner of the book.");
        }

        Feedback_Model feedback = feedbackMapper.toFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        return null;
    }
}
