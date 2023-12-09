package org.example.testcontainersdemo.repositories

import org.assertj.core.api.Assertions.assertThat
import org.example.testcontainersdemo.entities.Genre
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.MariaDBContainer

@DataJpaTest
@Sql(scripts = ["/sql/ddl.sql"])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreRepositoryTests(@Autowired private val genreRepo: GenreRepository) {
  companion object {
    val db = MariaDBContainer("mariadb")

    @JvmStatic
    @BeforeAll
    fun startDBContainer() {
      db.start()
    }

    @JvmStatic
    @AfterAll
    fun stopDBContainer() {
      db.stop()
    }

    @JvmStatic
    @DynamicPropertySource
    fun registerDBContainer(registry: DynamicPropertyRegistry) {
      registry.add("spring.datasource.url", db::getJdbcUrl)
      registry.add("spring.datasource.username", db::getUsername)
      registry.add("spring.datasource.password", db::getPassword)
    }
  }

  @Test
  fun `genre name must be unique`() {
    val genreName = "Poem"
    genreRepo.save(Genre(name = genreName))
    assertThrows<DataIntegrityViolationException> { genreRepo.save(Genre(name = genreName)) }
  }

  @Test
  fun `can lookup genre by name`() {
    val genreName = "Poem"
    val savedGenreId = genreRepo.save(Genre(name = genreName)).id
    val genre = genreRepo.findGenreByName(genreName)
    assertThat(genre).isNotNull()
    assertThat(genre?.id).isEqualTo(savedGenreId)
  }
}
