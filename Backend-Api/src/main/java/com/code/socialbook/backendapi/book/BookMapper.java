package com.code.socialbook.backendapi.book;

import com.code.socialbook.backendapi.filehandling.FileUtils;
import com.code.socialbook.backendapi.history.BookTransactionHistory_Model;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book_model toBook(BookRequest_Dto request) {

        return Book_model.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book_model bookModel) {
        return BookResponse.builder()
                .id(bookModel.getId())
                .title(bookModel.getTitle())
                .authorName(bookModel.getAuthorName())
                .synopsis(bookModel.getSynopsis())
                .rate(bookModel.getRate())
                .isbn(bookModel.getIsbn())
                .archived(bookModel.isArchived())
                .shareable(bookModel.isShareable())
//                .ownerName(bookModel.getOwner().fullName())
                .coverImage(FileUtils.readFromFileLocation(bookModel.getBookCover()))
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory_Model historyModel) {

        return BorrowedBookResponse.builder()
                .id(historyModel.getBook().getId())
                .title(historyModel.getBook().getTitle())
                .authorName(historyModel.getBook().getAuthorName())
                .isbn(historyModel.getBook().getIsbn())
                .rate(historyModel.getBook().getRate())
                .returned(historyModel.isReturned())
                .returnApproved(historyModel.isReturnApproved())
                .build();
    }
}
