package org.example.testcontainersdemo.repositories

import org.example.testcontainersdemo.entities.BookInstance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookInstanceRepository : JpaRepository<BookInstance, Long> {
  fun findBookInstancesByBookId(bookId: Long): List<BookInstance>?
}
