package com.code.socialbook.backendapi.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book_Repository extends JpaRepository<Book_model, Integer> {
}
