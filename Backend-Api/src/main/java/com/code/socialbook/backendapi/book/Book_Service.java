package com.code.socialbook.backendapi.book;

import com.code.socialbook.backendapi.exceptions.OperationNotPermittedException;
import com.code.socialbook.backendapi.history.BookTransactionHistory_Model;
import com.code.socialbook.backendapi.history.BookTransactionHistory_Repository;
import com.code.socialbook.backendapi.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.code.socialbook.backendapi.book.BookSpecification.withOwnerId;


@Service
@RequiredArgsConstructor
public class Book_Service {

    private final BookMapper bookMapper;
    private final Book_Repository bookRepository;
    private final BookTransactionHistory_Repository transactionHistoryRepository;

    public Integer save(BookRequest_Dto request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        var newBook = bookMapper.toBook(request);
        newBook.setOwner(user);
        bookRepository.save(newBook);
        return newBook.getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with this id: " + bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book_model> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponseList = books
                .stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(bookResponseList,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser){
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book_model> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponseList = books
                .stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(bookResponseList,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory_Model> allBorrowedBooks = transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> borrowedBookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                borrowedBookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory_Model> allReturnedBooks = transactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> borrowedBookResponses = allReturnedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                borrowedBookResponses,
                allReturnedBooks.getNumber(),
                allReturnedBooks.getSize(),
                allReturnedBooks.getTotalElements(),
                allReturnedBooks.getTotalPages(),
                allReturnedBooks.isFirst(),
                allReturnedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        var foundBook = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with this id: " + bookId));
        if(!Objects.equals(foundBook.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You can not update book shareable status.");
        }
        foundBook.setShareable(!foundBook.isShareable());
        bookRepository.save(foundBook);
        return bookId;
    }

}
