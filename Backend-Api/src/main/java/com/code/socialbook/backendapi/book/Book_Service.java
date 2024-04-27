package com.code.socialbook.backendapi.book;

import com.code.socialbook.backendapi.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.awt.print.Book;

@Service
@RequiredArgsConstructor
public class Book_Service {

    private final BookMapper bookMapper;
    private final Book_Repository bookRepository;

    public Integer save(BookRequest_Dto request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        var newBook = bookMapper.toBook(request);
        newBook.setOwner(user);
        bookRepository.save(newBook);
        return newBook.getId();
    }
}
