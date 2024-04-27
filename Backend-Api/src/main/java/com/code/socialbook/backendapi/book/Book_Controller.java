package com.code.socialbook.backendapi.book;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class Book_Controller {

    private final Book_Service bookService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@RequestBody @Valid BookRequest_Dto request, Authentication connectedUser){
       return ResponseEntity.ok(bookService.save(request, connectedUser));
    }

    @GetMapping("{book-id")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Integer bookId){
        return ResponseEntity.ok(bookService.findById(bookId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser){
        return ResponseEntity.ok(bookService.findAllBooks(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){

      return ResponseEntity.ok(bookService.findAllBooksByOwner(page,size,connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){

        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page,size,connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){

        return ResponseEntity.ok(bookService.findAllReturnedBooks(page,size,connectedUser));
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer>  updateShareableStatus(@PathVariable("book-id") Integer bookId,
                                                          Authentication connectedUser)
    {
        return ResponseEntity.ok(bookService.updateShareableStatus(bookId, connectedUser));
    }




}
