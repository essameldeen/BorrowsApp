/*
 * Copyright (c) 2019 Razeware LLC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish, 
 * distribute, sublicense, create a derivative work, and/or sell copies of the 
 * Software in any work that is designed, intended, or marketed for pedagogical or 
 * instructional purposes related to programming, coding, application development, 
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works, 
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.borrow.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.borrow.R
import com.raywenderlich.android.borrow.model.BorrowedItemModel
import kotlinx.android.synthetic.main.recyclerview_row.view.*

class BorrowRecyclerViewAdapter(
  private val recyclerViewItemClickListener: RecyclerViewItemClickListener
): RecyclerView.Adapter<BorrowRecyclerViewAdapter.BorrowViewHolder>() {

  private val borrowedItemList = mutableListOf<BorrowedItemModel>()

  fun setBorrowedItemList(borrowedItemList: List<BorrowedItemModel>) {
    this.borrowedItemList.clear()
    this.borrowedItemList.addAll(borrowedItemList)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BorrowViewHolder {
    return BorrowViewHolder(LayoutInflater.from(parent.context)
      .inflate(R.layout.recyclerview_row, parent, false))
  }

  override fun getItemCount(): Int {
    return borrowedItemList.size
  }

  override fun onBindViewHolder(holder: BorrowViewHolder, position: Int) {
    holder.bind(borrowedItemList[position])
  }

  inner class BorrowViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(borrowedItemModel: BorrowedItemModel) {
      itemView.borrowed_item_textview.text = borrowedItemModel.itemName
      itemView.borrower_name_textview.text = borrowedItemModel.borrowerName
      itemView.borrow_date_textview.text = borrowedItemModel.borrowDate
      itemView.setOnClickListener {
        recyclerViewItemClickListener.onItemClicked(borrowedItemModel)
      }
    }
  }
}