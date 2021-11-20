
package com.raywenderlich.android.borrow.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity
data class BorrowedItemModel(

  @Id
  var id: Long = 0,
  val itemName: String,
  val borrowerName: String,
  val borrowDate: String
)