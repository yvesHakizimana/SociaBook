package com.code.socialbook.backendapi.book;

import org.springframework.data.jpa.domain.Specification;

// Specification is used to make advanced queries basing on exactly what you want from the database.
public class BookSpecification {
    public static Specification<Book_model> withOwnerId(Integer ownerId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }
}
