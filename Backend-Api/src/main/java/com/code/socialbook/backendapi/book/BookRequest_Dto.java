package com.code.socialbook.backendapi.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequest_Dto(Integer id,
                              @NotNull(message = "Title cannot be null")
                              @NotEmpty(message =  "Title cannot be empty")
                              String title,
                              @NotNull(message = "Author name cannot be null")
                              @NotEmpty(message =  "Author name cannot be empty")
                              String authorName,
                              @NotNull(message = "ISBN cannot be null")
                              @NotEmpty(message =  "ISBN cannot be empty")
                              String isbn,
                              @NotNull(message = "Synopsis cannot be null")
                              @NotEmpty(message =  "Synopsis cannot be empty")
                              String synopsis,
                              boolean shareable) {
}

