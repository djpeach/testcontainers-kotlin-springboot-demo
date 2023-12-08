package org.example.testcontainersdemo.repositories

import org.example.testcontainersdemo.entities.Genre
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GenreRepository : JpaRepository<Genre, Long> {
  fun findGenreByName(name: String): Genre?
}
