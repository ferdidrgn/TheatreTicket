package com.ferdidrgn.theatreticket.tools.components

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.text.method.KeyListener
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.ferdidrgn.theatreticket.R
import com.google.android.material.textfield.TextInputEditText

class CustomNumberEditText : ConstraintLayout {
    lateinit var editTextView: TextInputEditText

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
        inflate(context, R.layout.custom_number_edit_text, this)
        val layoutAttribute =
            context.obtainStyledAttributes(attributeSet, R.styleable.CustomNumberEditText)

        layoutAttribute.getString(R.styleable.CustomNumberEditText_cst_number_edit_text)
            ?.let { string -> setText(string) }

        editTextView = findViewById(R.id.custom_field_editText)
        editTextView.inputType = InputType.TYPE_CLASS_NUMBER
    }

    fun setText(text: String) {
        editTextView.setText(text)
    }

    internal fun emailType() {
        editTextView.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        editTextView.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(100))
    }

    fun countCharacter(count: Int) {
        editTextView.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(count))
    }

    fun inputTypeNumber() {
        val keyListener: KeyListener =
            DigitsKeyListener.getInstance(context.getString(R.string.numbersKey))
        editTextView.keyListener = keyListener
        editTextView.inputType = InputType.TYPE_CLASS_NUMBER
    }

    fun formatPrice() {
        //editTextView.addTextChangedListener(NumberTextWatcher(editText))
    }

}