package com.raywenderlich.android.borrow.presentation.itemActivity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.raywenderlich.android.borrow.utils.DateHelper
import com.raywenderlich.android.borrow.R
import com.raywenderlich.android.borrow.model.BorrowedItemModel
import com.raywenderlich.android.borrow.model.db.ObjectBox
import com.raywenderlich.android.borrow.presentation.home.MainActivity
import kotlinx.android.synthetic.main.activity_add_edit.*
import java.util.*


class BorrowedItemActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val calendar = Calendar.getInstance()
    private lateinit var datePickerDialog: DatePickerDialog
    private var borrowItemToBeEdited: BorrowedItemModel? = null
    val borrowBox = ObjectBox.boxStore.boxFor(BorrowedItemModel::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)



        val borrowId = intent.extras?.getLong(MainActivity.INTENT_EXTRA_KEY)
        if (borrowId != null) {
            borrowItemToBeEdited = borrowBox.get(borrowId)
            borrowed_item_name_edittext.setText(borrowItemToBeEdited?.itemName)
            borrower_name_edittext.setText(borrowItemToBeEdited?.borrowerName)
            borrow_date_textview.text = borrowItemToBeEdited?.borrowDate
        }

        datePickerDialog = DatePickerDialog(
            this, this, calendar.get(Calendar.YEAR), calendar.get(
                Calendar.MONTH
            ), calendar.get(Calendar.DAY_OF_MONTH)
        )

        date_button.setOnClickListener {
            datePickerDialog.show()
        }

        fab.setOnClickListener {
            if (borrowed_item_name_edittext.text.isNullOrBlank() ||
                borrower_name_edittext.text.isNullOrBlank() ||
                borrow_date_textview.text.isNullOrBlank()
            ) {
                Toast.makeText(
                    this, "Please enter all the required fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val borrowedItem = BorrowedItemModel(
                    itemName = borrowed_item_name_edittext.text.toString(),
                    borrowerName = borrower_name_edittext.text.toString(),
                    borrowDate = borrow_date_textview.text.toString()
                )
                if (borrowItemToBeEdited != null) {
                    borrowedItem.id = borrowItemToBeEdited!!.id
                }
                borrowBox.put(borrowedItem)
                finish()
            }
        }
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        borrow_date_textview.text = DateHelper.formatDate(calendar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.add_edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.delete -> {
                deleteBorrowedItem()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteBorrowedItem() {

        if (borrowItemToBeEdited != null) {
            borrowBox.remove(borrowItemToBeEdited!!)
            Toast.makeText(this,  "Removed note with title " +
                    borrowItemToBeEdited?.itemName, Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
