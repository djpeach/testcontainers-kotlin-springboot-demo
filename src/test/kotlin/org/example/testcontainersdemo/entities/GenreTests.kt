package org.example.testcontainersdemo.entities

import java.sql.Timestamp
import kotlin.reflect.KMutableProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.MariaDBContainer

/**
 * This class tests the configuration of the Genre Entity.
 *
 * Certain fields should not be mutable. (ie, id, createdAt, updatedAt) Timestamps should be
 * generated appropriately.
 *
 * @param em The EntityManager instance to be used to persist and fetch entities.
 */
@DataJpaTest
@Sql(scripts = ["/sql/ddl.sql"])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreTests(@Autowired private val em: TestEntityManager) {
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
  fun `dbContainer is running`() {
    assertTrue(db.isRunning)
  }

  @Test
  fun `id, createdAt, updatedAt cannot be modified`() {
    var genre = Genre(name = "Poem")
    genre = em.persist(genre)
    assertFalse(genre::id is KMutableProperty<*>)
    assertFalse(genre::createdAt is KMutableProperty<*>)
    assertFalse(genre::updatedAt is KMutableProperty<*>)
  }

  @Test
  fun `id is generated when a genre is persisted`() {
    val genre = Genre(name = "Poem")
    assertThat(genre.id).isEqualTo(0L)
    val persistedGenre = em.persist(genre)
    assertThat(persistedGenre.id).isNotNull().isBetween(0L, Long.MAX_VALUE)
  }

  @Test
  fun `createdAt is generated when a genre is persisted`() {
    var genre = Genre(name = "Poem")
    genre = em.persist(genre)
    assertThat(genre.createdAt).isNotNull().isNotEqualTo(Timestamp(0)).isInThePast()
  }

  @Test
  fun `updatedAt is generated when a genre is persisted`() {
    var genre = Genre(name = "Poem")
    genre = em.persist(genre)
    assertThat(genre.updatedAt).isNotNull().isNotEqualTo(Timestamp(0)).isInThePast()
  }

  @Test
  fun `createdAt is not updated when a genre is updated`() {
    var genre = Genre(name = "Poem")
    genre = em.persist(genre)
    val originalCreatedAt = genre.createdAt
    genre.name = "Poems"
    genre = em.persistAndFlush(genre)
    assertThat(genre.createdAt).isEqualTo(originalCreatedAt)
  }

  @Test
  fun `updatedAt is updated when a genre is updated`() {
    var genre = Genre(name = "Poem")
    genre = em.persist(genre)
    val originalUpdatedAt = genre.updatedAt
    genre.name = "Poems"
    genre = em.persistAndFlush(genre)
    assertThat(genre.updatedAt).isNotSameAs(originalUpdatedAt).isAfter(originalUpdatedAt)
  }
}
