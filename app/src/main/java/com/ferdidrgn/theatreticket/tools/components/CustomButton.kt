package com.ferdidrgn.theatreticket.tools.components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates
import com.ferdidrgn.theatreticket.R

class CustomButton : LinearLayout {

    private var backgroundColorFromAttr by Delegates.notNull<Int>()
    private lateinit var customButton: LinearLayout

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
        inflate(context, R.layout.custom_button, this)
        customButton = findViewById(R.id.btnCustomButton)
        val layoutAttribute = context.obtainStyledAttributes(attributeSet, R.styleable.CustomButton)
        setTextView(layoutAttribute.getString(R.styleable.CustomButton_cst_button_name))
        getDisableBackgroundTint(
            layoutAttribute.getColor(
                R.styleable.CustomButton_cst_backgroundColorFromXml,
                ContextCompat.getColor(context, R.color.light_gray)
            )
        )
        setButtonColor(layoutAttribute.getInt(R.styleable.CustomButton_cst_buttonColor, R.color.main))
    }

    private fun getDisableBackgroundTint(color: Int) {
        backgroundColorFromAttr = color
        customButton.backgroundTintList = ColorStateList.valueOf(backgroundColorFromAttr)
    }

    private fun setTextView(text: String?) {
        val textView = findViewById<TextView>(R.id.tvCustomButtonLabel)
        textView.text = text
    }

    fun setButtonActive() {
        customButton.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(customButton.context, R.color.main_dark))
        isClickable = true
    }

    fun setButtonDisable() {
        customButton.backgroundTintList = ColorStateList.valueOf(backgroundColorFromAttr)
        isClickable = false
    }

    fun setButtonColor(color: Int) {
        customButton.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(customButton.context, color))
    }
}