package com.code.socialbook.backendapi.book;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{book-id")
    public ResponseEntity<BookResponse> findBO
}
