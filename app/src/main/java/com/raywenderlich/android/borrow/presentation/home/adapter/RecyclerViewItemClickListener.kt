
package com.raywenderlich.android.borrow.presentation.home.adapter

import com.raywenderlich.android.borrow.model.BorrowedItemModel

interface RecyclerViewItemClickListener {

  fun onItemClicked(borrowedItemModel: BorrowedItemModel)
}