package com.code.socialbook.backendapi.history;

import com.code.socialbook.backendapi.book.Book_model;
import com.code.socialbook.backendapi.common.BaseEntity;
import com.code.socialbook.backendapi.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_transaction_histories")
public class BookTransactionHistory_Model extends BaseEntity {

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
    @Column(name = "user_id")
    private String userId;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book_model book;
    private boolean returned;
    private boolean returnApproved;

}
