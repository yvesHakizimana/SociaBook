package com.code.socialbook.backendapi.book;

import com.code.socialbook.backendapi.exceptions.OperationNotPermittedException;
import com.code.socialbook.backendapi.filehandling.FileStorageService;
import com.code.socialbook.backendapi.history.BookTransactionHistory_Model;
import com.code.socialbook.backendapi.history.BookTransactionHistoryRepository;
import com.code.socialbook.backendapi.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.code.socialbook.backendapi.book.BookSpecification.withOwnerId;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository transactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public Integer save(BookRequest_Dto request, Authentication connectedUser) {
//        User user = ((User) connectedUser.getPrincipal());
        var newBook = bookMapper.toBook(request);
//        newBook.setOwner(user);
        bookRepository.save(newBook);
        return newBook.getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with this id: " + bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
//        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book_model> books = bookRepository.findAllDisplayableBooks(pageable, connectedUser.getName());
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
//        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book_model> books = bookRepository.findAll(withOwnerId(connectedUser.getName()), pageable);
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
//        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory_Model> allBorrowedBooks = transactionHistoryRepository.findAllBorrowedBooks(pageable, connectedUser.getName());
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
//        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory_Model> allReturnedBooks = transactionHistoryRepository.findAllReturnedBooks(pageable, connectedUser.getName());
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
        var foundBook = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with this id: " + bookId));
        if(!Objects.equals(foundBook.getCreatedBy(),  connectedUser.getName())){
            throw new OperationNotPermittedException("You can not update book shareable status.");
        }
        foundBook.setShareable(!foundBook.isShareable());
        bookRepository.save(foundBook);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        var foundBook = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with this id: " + bookId));
        if(!Objects.equals(foundBook.getCreatedBy(), connectedUser.getName())){
            throw new OperationNotPermittedException("You can not update book archived status.");
        }
        foundBook.setArchived(!foundBook.isArchived());
        bookRepository.save(foundBook);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        var foundBook = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with this id: " + bookId));
        if(foundBook.isArchived() || !foundBook.isShareable()){
            throw new OperationNotPermittedException("You can not borrow the book because it is archived or not shareable.");
        }
//        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(foundBook.getCreatedBy(), connectedUser.getName())){
            throw  new OperationNotPermittedException("You can not borrow the book you are the owner of.");
        }
        //Mechanism of borrowing the book.
        final boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowedByUser(bookId, connectedUser.getName());
        if(isAlreadyBorrowed)
            throw  new OperationNotPermittedException("The requested book is already borrowed.");

        var bookTransactionHistory = BookTransactionHistory_Model.builder()
                .userId(connectedUser.getName())
                .book(foundBook)
                .returned(false)
                .returnApproved(false)
                .build();
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        var foundBook = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with this id: " + bookId));
        if(foundBook.isArchived() || !foundBook.isShareable()){
            throw new OperationNotPermittedException("You can not return the book because it is archived or not shareable.");
        }
//        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(foundBook.getCreatedBy(), connectedUser.getName())){
            throw  new OperationNotPermittedException("You can not return the book you are the owner of.");
        }

        BookTransactionHistory_Model bookTransactionHistory = transactionHistoryRepository.findByBookIdAndUserId(bookId, connectedUser.getName()).orElseThrow(() -> new OperationNotPermittedException("You didn't borrow this book."));

        bookTransactionHistory.setReturned(true);
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnedBorrowedBook(Integer bookId, Authentication connectedUser) {
        var foundBook = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with this id: " + bookId));
        if(foundBook.isArchived() || !foundBook.isShareable()){
            throw new OperationNotPermittedException("You can not return/approve the book because it is archived or not shareable.");
        }
//        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(foundBook.getCreatedBy(),connectedUser.getName())){
            throw  new OperationNotPermittedException("You can not approve the return of  the book you are not the owner of.");
        }
        BookTransactionHistory_Model bookTransactionHistory = transactionHistoryRepository.findByBookIdAndOwnerId(bookId, connectedUser.getName()).orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet, so you can't approve its return"));
        bookTransactionHistory.setReturnApproved(true);
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {
        var foundBook = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with this id: " + bookId));
//        User user = ((User) connectedUser.getPrincipal());
        var bookCover = fileStorageService.saveFile(file, connectedUser.getName());
        foundBook.setBookCover(bookCover);
        bookRepository.save(foundBook);
    }
}
