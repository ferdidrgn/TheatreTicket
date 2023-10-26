package com.ferdidrgn.theatreticket.tools.components

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ferdidrgn.theatreticket.R

class CustomPhoneCodeSpinner: LinearLayout {

    constructor(context: Context) : super(context){
        initLayout(context,null,null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet){
        initLayout(context,attributeSet,null)
    }

    constructor(context: Context, attributeSet: AttributeSet, style: Int) : super(context, attributeSet, style){
        initLayout(context,attributeSet,style)
    }

    private fun initLayout(context: Context, attributeSet: AttributeSet?, style: Int?) {
        inflate(context, R.layout.custom_phone_code_spinner,this)
    }
}