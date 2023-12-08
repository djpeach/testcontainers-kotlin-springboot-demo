package org.example.testcontainersdemo.repositories

import org.example.testcontainersdemo.entities.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long> {
  fun findBookByIsbn(isbn: String): Book?
}
