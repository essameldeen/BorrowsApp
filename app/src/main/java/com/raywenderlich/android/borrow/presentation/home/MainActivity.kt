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
package com.raywenderlich.android.borrow.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.borrow.*
import com.raywenderlich.android.borrow.model.BorrowedItemModel
import com.raywenderlich.android.borrow.model.BorrowedItemModel_
import com.raywenderlich.android.borrow.model.db.ObjectBox
import com.raywenderlich.android.borrow.presentation.itemActivity.BorrowedItemActivity
import com.raywenderlich.android.borrow.presentation.home.adapter.BorrowRecyclerViewAdapter
import com.raywenderlich.android.borrow.presentation.home.adapter.DividerItemDecoration
import com.raywenderlich.android.borrow.presentation.home.adapter.RecyclerViewItemClickListener
import io.objectbox.android.AndroidScheduler
import io.objectbox.query.Query
import io.objectbox.reactive.DataSubscription
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private lateinit var borrowRecyclerViewAdapter: BorrowRecyclerViewAdapter
    private val borrowBox = ObjectBox.boxStore.boxFor(BorrowedItemModel::class.java)
    private lateinit var borrowDateSortQuery: Query<BorrowedItemModel>
    private lateinit var subscription: DataSubscription
    private lateinit var borrowNameSortQuery: Query<BorrowedItemModel>

    companion object {
        val INTENT_EXTRA_KEY = "BORROW_ID"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        borrowDateSortQuery = borrowBox.query()
            .order(BorrowedItemModel_.borrowDate)
            .build()

        borrowNameSortQuery = borrowBox.query()
            .order(BorrowedItemModel_.itemName)
            .build()

        subscription = borrowDateSortQuery
            .subscribe()
            .on(AndroidScheduler.mainThread())
            .observer { notes -> borrowRecyclerViewAdapter.setBorrowedItemList(notes) }


        borrowRecyclerViewAdapter =
            BorrowRecyclerViewAdapter(object : RecyclerViewItemClickListener {
                override fun onItemClicked(borrowedItemModel: BorrowedItemModel) {
                    startBorrowItemActivity(borrowedItemModel.id)
                }
            })

        recyclerView.adapter = borrowRecyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this))

        fab.setOnClickListener {
            startBorrowItemActivity()
        }
    }

    private fun startBorrowItemActivity(id: Long? = null) {
        val intent = Intent(this, BorrowedItemActivity::class.java)
        if (id != null) {
            intent.putExtra(INTENT_EXTRA_KEY, id)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.setOnCloseListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.sort_by_name -> {
                subscription.cancel()
                subscription = borrowNameSortQuery.subscribe()
                    .on(AndroidScheduler.mainThread())
                    .observer { notes ->
                        borrowRecyclerViewAdapter.setBorrowedItemList(notes)
                    }
            }
            R.id.sort_by_date_added -> {
                subscription.cancel()
                subscription = borrowDateSortQuery.subscribe()
                    .on(AndroidScheduler.mainThread())
                    .observer { notes ->
                        borrowRecyclerViewAdapter.setBorrowedItemList(notes)
                    }
            }
        }
        item.isChecked = true
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            queryForText(query)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            queryForText(newText)
        }
        return false
    }

    override fun onDestroy() {
        subscription.cancel()
        super.onDestroy()
    }

    private fun queryForText(query: String) {
        val items = borrowBox.query()
            .contains(BorrowedItemModel_.itemName, query)
            .build()
            .find()

        borrowRecyclerViewAdapter.setBorrowedItemList(items)
    }

    override fun onClose(): Boolean {
        return false
    }
}