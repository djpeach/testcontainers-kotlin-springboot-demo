package org.example.testcontainersdemo.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
class Genre(
  var name: String,
) {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0
  @CreationTimestamp val createdAt: Timestamp = Timestamp(0)
  @UpdateTimestamp val updatedAt: Timestamp = Timestamp(0)
}
