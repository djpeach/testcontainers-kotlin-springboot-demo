package org.example.testcontainersdemo

import org.example.testcontainersdemo.entities.Author
import org.example.testcontainersdemo.entities.Book
import org.example.testcontainersdemo.entities.Genre
import org.example.testcontainersdemo.repositories.AuthorRepository
import org.example.testcontainersdemo.repositories.BookInstanceRepository
import org.example.testcontainersdemo.repositories.BookRepository
import org.example.testcontainersdemo.repositories.GenreRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun createGenres(genreRepository: GenreRepository) {
  val genres = listOf("Poem", "Biography", "Novel", "Non-Fiction")
  for (genre in genres) {
    val existingGenre = genreRepository.findGenreByName(genre)
    if (existingGenre == null) {
      val newGenre = genreRepository.save(Genre(name = genre))
    }
  }
}

fun createAuthors(authorRepository: AuthorRepository) {
  val authors =
    listOf(
      mapOf("firstName" to "John", "lastName" to "Smith"),
      mapOf("firstName" to "Jane", "lastName" to "Austen"),
      mapOf("firstName" to "Matt")
    )
  for (author in authors) {
    val existingAuthor =
      authorRepository.findAuthorByGivenNameAndSurname(
        givenName = author.getValue("firstName"),
        surname = author.get("lastName")
      )
    if (existingAuthor == null) {
      val newAuthor =
        authorRepository.save(
          Author(givenName = author.getValue("firstName"), surname = author.get("lastName"))
        )
    }
  }
}

fun createBooks(
  bookRepository: BookRepository,
  authorRepository: AuthorRepository,
  genreRepository: GenreRepository
) {
  val books =
    listOf(
      mapOf(
        "title" to "The Great Escape",
        "isbn" to "0000000000001",
        "author" to Pair("John", "Smith"),
        "genre" to "Novel",
      ),
      mapOf(
        "title" to "The Long War",
        "isbn" to "0000000000002",
        "author" to Pair("John", "Smith"),
        "genre" to "Novel",
      ),
      mapOf(
        "title" to "Another Day Perhaps",
        "isbn" to "0000000000003",
        "author" to Pair("Jane", "Austen"),
        "genre" to "Poem",
      ),
      mapOf(
        "title" to "Life of John Smith",
        "isbn" to "0000000000004",
        "author" to Pair("Matt", null),
        "genre" to "Biography",
      )
    )
  for (book in books) {
    val existingBook = bookRepository.findBookByIsbn(book.getValue("isbn") as String)
    if (existingBook == null) {
      val authorNames = book.getValue("author") as Pair<String, String?>
      val author =
        authorRepository.findAuthorByGivenNameAndSurname(authorNames.first, authorNames.second)
      val genre = genreRepository.findGenreByName(book.getValue("genre") as String)
      val newBook =
        bookRepository.save(
          Book(
            title = book.getValue("title") as String,
            isbn = book.getValue("isbn") as String,
            author = author!!,
            genre = genre
          )
        )
    }
  }
}


class ApplicationDemoData(
  private val genreRepository: GenreRepository,
  private val authorRepository: AuthorRepository,
  private val bookRepository: BookRepository,
  private val bookInstaRepository: BookInstanceRepository,
) : CommandLineRunner {
  override fun run(vararg args: String?) {
    createGenres(genreRepository)
    createAuthors(authorRepository)
    createBooks(bookRepository, authorRepository, genreRepository)
  }
}


@SpringBootApplication
class Application

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}
