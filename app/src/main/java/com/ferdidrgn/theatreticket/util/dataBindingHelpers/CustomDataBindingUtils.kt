package com.ferdidrgn.theatreticket.util.dataBindingHelpers

import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.components.*
import com.ferdidrgn.theatreticket.util.show

object CustomDataBindingUtils {

    @InverseBindingMethods(
        InverseBindingMethod(
            type = CustomEditText::class, attribute = "bind:custom_edit_text", event =
            "bind:textAttrChanged", method = "bind:getText"
        ),
        InverseBindingMethod(
            type = CustomNumberEditText::class,
            attribute = "bind:cst_number_changeable_edit_text",
            event =
            "bind:textAttrChanged",
            method = "bind:getText"
        ),
        InverseBindingMethod(
            type =
            CustomToolbar::class, attribute = "bind:custom_toolbar_changeable_text", event =
            "bind:textAttrChanged", method = "bind:getToolBarText"
        ),
        InverseBindingMethod(
            type =
            CustomPhoneEditText::class, attribute = "bind:cst_phone_text", event =
            "bind:textAttrChanged", method = "bind:getText"
        ),
        InverseBindingMethod(
            type =
            CustomDatePicker::class, attribute = "bind:cst_picker_changeable_text", event =
            "bind:textAttrChanged", method = "bind:getText"
        ),
        InverseBindingMethod(
            type = CustomPhoneCodeSpinner::class, attribute = "bind:item_value", event =
            "android:selectedItemAttrChanged", method = "bind:itemValue"
        ),
    )
    class CustomEditTextBinder {
        companion object {

            @BindingAdapter("app:textAttrChanged")
            @JvmStatic
            fun setListener(editText: CustomEditText, listener: InverseBindingListener?) {
                if (listener != null) {
                    editText.editText.doAfterTextChanged { listener.onChange() }
                }
            }

            @InverseBindingAdapter(attribute = "custom_edit_text", event = "app:textAttrChanged")
            @JvmStatic
            fun getCustomEditText(nMe: CustomEditText): String {
                return nMe.editText.text.toString()
            }

            @BindingAdapter("custom_edit_text")
            @JvmStatic
            fun setCustomEditText(editText: CustomEditText, text: String?) {
                text?.let {
                    if (it != editText.editText.text.toString()) {
                        editText.editText.setText(it)
                    }
                }
            }
        }
    }

    class CustomToolBarTextBinder {
        companion object {
            @BindingAdapter("app:textAttrChanged")
            @JvmStatic
            fun setListener(toolBar: CustomToolbar, listener: InverseBindingListener?) {
                if (listener != null) {
                    toolBar.tvTitle.show()
                    toolBar.tvTitle.doAfterTextChanged {
                        listener.onChange()
                    }
                }
            }

            @InverseBindingAdapter(
                attribute = "custom_toolbar_changeable_text",
                event = "app:textAttrChanged"
            )
            @JvmStatic
            fun getCustomToolBarText(toolBar: CustomToolbar): String {
                toolBar.tvTitle.show()
                return toolBar.tvTitle.text.toString()
            }

            @BindingAdapter("custom_toolbar_changeable_text")
            @JvmStatic
            fun setCustomToolBarText(toolBar: CustomToolbar, text: String?) {
                text?.let { customText ->
                    if (customText != toolBar.tvTitle.text.toString()) {
                        toolBar.tvTitle.show()
                        toolBar.tvTitle.text = customText
                    }
                }
            }
        }
    }

    class CustomPhoneEditTextBinder {
        companion object {
            @JvmStatic
            @BindingAdapter("android:textAttrChanged")
            fun setListener(editText: CustomPhoneEditText, listener: InverseBindingListener?) {
                if (listener != null) {
                    editText.editText.doAfterTextChanged {
                        listener.onChange()
                    }
                }
            }

            @JvmStatic
            @InverseBindingAdapter(attribute = "cst_phone_text", event = "android:textAttrChanged")
            fun getText(nMe: CustomPhoneEditText): String {
                return nMe.editText.text.toString()
            }

            @JvmStatic
            @BindingAdapter("cst_phone_text")
            fun setText(editText: CustomPhoneEditText, text: String?) {
                text?.let {
                    if (it != editText.editText.text.toString()) {
                        editText.editText.setText(it)
                    }
                }
            }
        }
    }


    class CustomDatePickerBinder {
        companion object {

            @BindingAdapter("app:textAttrChanged")
            @JvmStatic
            fun setListener(customDatePicker: CustomDatePicker, listener: InverseBindingListener?) {
                if (listener != null) {
                    customDatePicker.tvSelectedDate.doAfterTextChanged { listener.onChange() }
                }
            }

            @InverseBindingAdapter(
                attribute = "cst_picker_changeable_text",
                event = "app:textAttrChanged"
            )
            @JvmStatic
            fun getCustomEditText(nMe: CustomDatePicker): String {
                return nMe.tvSelectedDate.text.toString()
            }

            @BindingAdapter("cst_picker_changeable_text")
            @JvmStatic
            fun setCustomEditText(customDatePicker: CustomDatePicker, text: String?) {
                text?.let {
                    if (it != customDatePicker.tvSelectedDate.text.toString()) {
                        customDatePicker.tvSelectedDate.text = it
                    }
                }
            }
        }
    }

    class CustomNumberEditTextBinder {
        companion object {
            @BindingAdapter("app:textAttrChanged")
            @JvmStatic
            fun setListener(editText: CustomNumberEditText, listener: InverseBindingListener?) {
                if (listener != null) {
                    editText.editTextView.doAfterTextChanged { listener.onChange() }
                }
            }

            @InverseBindingAdapter(
                attribute = "cst_number_changeable_edit_text",
                event = "app:textAttrChanged"
            )
            @JvmStatic
            fun getCustomEditText(nMe: CustomNumberEditText): String {
                return nMe.editTextView.text.toString()
            }

            @BindingAdapter("cst_number_changeable_edit_text")
            @JvmStatic
            fun setCustomEditText(editText: CustomNumberEditText, text: String?) {
                text?.let {
                    if (it != editText.editTextView.text.toString()) {
                        editText.editTextView.setText(it)
                    }
                }
            }
        }
    }

    class CustomPhoneCodeSpinnerDropDownBinder {
        companion object {

            @BindingAdapter("setArrayList")
            @JvmStatic
            fun <T> setArrayList(view: CustomPhoneCodeSpinner, list: List<T>) {
                // Adapter
                if (list.isNotEmpty()) {
                    when (list.first()) {
                        is String -> { //CountryPhoneCode
                            view.itemList = list.map { item -> (item as String) }
                            val adapter =
                                ArrayAdapter(view.context, R.layout.item_drop_down, R.id.tvDropDown,
                                    (view.itemList as List<String>).map { item -> item })
                            view.dropDown.setAdapter(adapter)
                        }
                    }
                }
            }

            @BindingAdapter(value = ["android:selectedItemAttrChanged"])
            @JvmStatic
            fun setListener(dropDown: CustomPhoneCodeSpinner, listener: InverseBindingListener?) {
                if (listener != null) {
                    dropDown.dropDown.setOnItemClickListener { parent, _, position, _ ->
                        dropDown.selectedItemID = dropDown.itemList?.get(position)
                        listener.onChange()
                    }
                }
            }

            @InverseBindingAdapter(
                attribute = "item_value",
                event = "android:selectedItemAttrChanged"
            )
            @JvmStatic
            fun getItemValue(view: CustomPhoneCodeSpinner): String {
                return when (view.selectedItemID) {
                    is String -> {
                        (view.selectedItemID as String).toString()
                    }

                    else -> {
                        if (view.selectedItemID == null) "" else view.selectedItemID.toString()
                    }
                }
            }

            @BindingAdapter(value = ["item_value"])
            @JvmStatic
            fun setItemValue(customDropDown: CustomPhoneCodeSpinner, text: String?) {
                val newValue = text ?: customDropDown.dropDown.adapter.getItem(0)
                customDropDown.dropDown.setText(newValue.toString(), false)
            }
        }
    }

}