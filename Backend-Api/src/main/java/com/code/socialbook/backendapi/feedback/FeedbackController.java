package com.code.socialbook.backendapi.feedback;

import com.code.socialbook.backendapi.book.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@Tag(name = "Feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedbackRequest request,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(feedbackService.saveFeedback(request, connectedUser));
    }

    @GetMapping("/book/feedbacks/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbacks(
            @PathVariable("book-id") Integer bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(feedbackService.findAllFeedbacksByBook(bookId, page, size, connectedUser));
    }

}
