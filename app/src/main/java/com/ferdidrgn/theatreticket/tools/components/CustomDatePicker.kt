package com.ferdidrgn.theatreticket.tools.components

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.tools.onClickDelayed
import java.text.SimpleDateFormat
import java.util.*

class CustomDatePicker : LinearLayout {

    private lateinit var customDatePicker: LinearLayout
    private lateinit var tvSelectedDate: TextView
    private val calendar = Calendar.getInstance()

    constructor(context: Context) : super(context) {
        initLayout(context, null, null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initLayout(context, attributeSet, null)
    }

    constructor(context: Context, attributeSet: AttributeSet, style: Int) : super(
        context,
        attributeSet,
        style
    ) {
        initLayout(context, attributeSet, style)
    }

    private fun initLayout(context: Context, attributeSet: AttributeSet?, style: Int?) {
        inflate(context, R.layout.custom_date_picker, this)
        customDatePicker = findViewById(R.id.llCustomDatePicker)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)

    }

    fun setCustomDataPickerClick(context: Context) {
        customDatePicker.onClickDelayed {

            val datePickerDialog = DatePickerDialog(
                context, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)
                    tvSelectedDate.text = "Selected Date: $formattedDate"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }
}