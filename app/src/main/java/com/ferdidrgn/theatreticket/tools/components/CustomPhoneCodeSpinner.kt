package com.ferdidrgn.theatreticket.tools.components

import android.content.Context
import android.util.AttributeSet
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ferdidrgn.theatreticket.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class CustomPhoneCodeSpinner : ConstraintLayout {


    private lateinit var etCustomDDText: MaterialAutoCompleteTextView
    lateinit var dropDown: AutoCompleteTextView
    var selectedItemID: Any? = null
    var itemList: List<Any>? = null
    lateinit var imgError: ImageView

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
        inflate(
            context,
            R.layout.custom_phone_code_spinner,
            this
        )
        dropDown = findViewById(R.id.etCustomDDText)
        etCustomDDText = findViewById(R.id.etCustomDDText)

        val layoutAttribute =
            context.obtainStyledAttributes(attributeSet, R.styleable.CustomPhoneCodeSpinner)

        visibilityHintText(layoutAttribute.getString(R.styleable.CustomPhoneCodeSpinner_cst_phone_code_hint_text))

        setListenerForPaddingTop()
    }

    private fun setListenerForPaddingTop() {

    }

    private fun visibilityHintText(text: String?) {
        val editTextView = findViewById<TextView>(R.id.tvHint)
        editTextView.hint = text
    }
}