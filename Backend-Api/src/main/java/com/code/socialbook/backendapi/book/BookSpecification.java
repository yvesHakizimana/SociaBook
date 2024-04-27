package com.code.socialbook.backendapi.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book_model> withOwnerId(Integer ownerId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }
}
