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
class BookInstance(
  var imprint: String,
  var status: BookStatus = BookStatus.OUT_OF_CIRCULATION,
  @ManyToOne var book: Book,
) {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0
  @CreationTimestamp val createdAt: Timestamp = Timestamp(0)
  @UpdateTimestamp val updatedAt: Timestamp = Timestamp(0)
}

enum class BookStatus(val value: String) {
  AVAILABLE("AVAILABLE"),
  ON_HOLD("ON_HOLD"),
  ON_LOAN("ON_LOAN"),
  OUT_OF_CIRCULATION("OUT_OF_CIRCULATION"),
}
