package org.example.testcontainersdemo.repositories

import org.example.testcontainersdemo.entities.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : JpaRepository<Author, Long> {
  fun findAuthorByGivenNameAndSurname(givenName: String, surname: String? = null): Author?
}
