package com.code.socialbook.backendapi.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String ownerName;
    private byte[] coverImage;
    private double rate;
    private boolean archived;
    private boolean shareable;
}
