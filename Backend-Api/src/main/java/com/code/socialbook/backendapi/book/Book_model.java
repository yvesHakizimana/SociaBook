package com.code.socialbook.backendapi.book;

import com.code.socialbook.backendapi.common.BaseEntity;
import com.code.socialbook.backendapi.feedback.Feedback_Model;
import com.code.socialbook.backendapi.history.BookTransactionHistory_Model;
import com.code.socialbook.backendapi.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Book_model extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback_Model> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory_Model> histories;

    @Transient
    public double getRate(){
        if(feedbacks == null || feedbacks.isEmpty()){
            return 0.0;
        }
        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback_Model::getNote)
                .average()
                .orElse(0.0);
        return Math.round(rate * 10.0) / 10.0;
    }

}
