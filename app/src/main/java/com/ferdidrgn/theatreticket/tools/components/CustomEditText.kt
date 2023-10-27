package com.ferdidrgn.theatreticket.tools.components

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.enums.textInputType
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text

class CustomEditText : ConstraintLayout {
    lateinit var editText: TextInputEditText
    lateinit var tlEditText: TextInputLayout

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
        inflate(context, R.layout.custom_edit_text, this)
        editText = findViewById(R.id.etCustomEditText)
        tlEditText = findViewById(R.id.tlEditText)
        val layoutAttribute =
            context.obtainStyledAttributes(attributeSet, R.styleable.CustomEditText)

        visibilityHintText(layoutAttribute.getString(R.styleable.CustomEditText_custom_edit_hint))
        textType(
            layoutAttribute.getString(
                R.styleable.CustomEditText_custom_edit_input
            )
        )
    }

    private fun visibilityHintText(text: String?) {
        tlEditText.hint = text
    }

    private fun textType(text: String?) {
        when (text) {
            "numberDecimal" -> editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            else -> editText.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

}