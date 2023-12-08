package org.example.testcontainersdemo.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
class Book(
  var title: String,
  var summary: String? = null,
  var isbn: String,
  @ManyToOne var author: Author,
  @ManyToOne var genre: Genre?,
) {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0
  @CreationTimestamp val createdAt: Timestamp = Timestamp(0)
  @UpdateTimestamp val updatedAt: Timestamp = Timestamp(0)
}
