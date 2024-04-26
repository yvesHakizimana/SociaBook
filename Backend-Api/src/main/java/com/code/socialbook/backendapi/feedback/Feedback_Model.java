package com.code.socialbook.backendapi.feedback;

import com.code.socialbook.backendapi.book.Book_model;
import com.code.socialbook.backendapi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.print.Book;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "feedbacks")

public class Feedback_Model extends BaseEntity {
    private Double note;// Like nbr of stars
    private String comment;
    @ManyToOne()
    @JoinColumn(name = "book_id")
    private Book_model book;

}
