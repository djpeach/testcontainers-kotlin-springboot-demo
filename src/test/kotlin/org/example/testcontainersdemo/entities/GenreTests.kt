package org.example.testcontainersdemo.entities

import org.assertj.core.api.Assertions.assertThat
import org.example.testcontainersdemo.repositories.GenreRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.MariaDBContainer

@DataJpaTest
@Sql(scripts = ["/sql/ddl.sql"])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreTests(@Autowired private val genreRepository: GenreRepository) {
  companion object {
    val db = MariaDBContainer("mariadb")

    @BeforeAll
    @JvmStatic
    fun startDBContainer() {
      db.start()
    }

    @AfterAll
    @JvmStatic
    fun stopDBContainer() {
      db.stop()
    }

    @DynamicPropertySource
    @JvmStatic
    fun registerDBContainer(registry: DynamicPropertyRegistry) {
      registry.add("spring.datasource.url", db::getJdbcUrl)
      registry.add("spring.datasource.username", db::getUsername)
      registry.add("spring.datasource.password", db::getPassword)
    }
  }

  @Test
  fun `dbContainer is running`() {
    assertTrue(db.isRunning)
  }

  @Test
  fun `id is generated when a genre is persisted`() {
    val genre = Genre(name = "Poem")
    assertThat(genre.id).isEqualTo(0L)
    genreRepository.save(genre)
    assertThat(genre.id).isNotEqualTo(0L)
  }
}
