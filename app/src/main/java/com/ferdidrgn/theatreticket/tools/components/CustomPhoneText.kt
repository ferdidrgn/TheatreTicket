package com.ferdidrgn.theatreticket.tools.components

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.tools.enums.PhoneNumberLengthsByCountry
import com.ferdidrgn.theatreticket.tools.helpers.MaskWatcher
import com.ferdidrgn.theatreticket.tools.show
import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText

class CustomPhoneEditText : ConstraintLayout {

    lateinit var editText: TextInputEditText
    lateinit var imgView: ImageView
    var textSizeSuccess: ((Boolean) -> Unit)? = null
    var onContactClick: (() -> Unit)? = null

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
        inflate(context, R.layout.custom_phone_number, this)
        setEditTextMask(PhoneNumberLengthsByCountry.Turkish)
        val layoutAttribute =
            context.obtainStyledAttributes(attributeSet, R.styleable.CustomPhone)
        if (layoutAttribute.getBoolean(
                R.styleable.CustomPhone_cst_phone_icon_visibility,
                false
            )
        ) {
            imgView.show()
        }

        imgView.setOnClickListener {
            onContactClick?.invoke()
        }
    }

    private fun setTextView(text: String?) {
        if (this::editText.isInitialized)
            editText.setText(text)
    }

    fun setEditTextMask(phoneNumberEnum: PhoneNumberLengthsByCountry) {
        editText = findViewById(R.id.etCustomPhoneNumber)
        imgView = findViewById(R.id.imgContact)
        editText.addTextChangedListener(
            MaskWatcher(
                phoneNumberEnum.mask
            )
        )
        editText.filters += InputFilter.LengthFilter(phoneNumberEnum.length)
        editText.addTextChangedListener {
            if (it?.length == phoneNumberEnum.length) {
                textSizeSuccess?.invoke(true)
            } else {
                textSizeSuccess?.invoke(false)
            }
        }
    }
}