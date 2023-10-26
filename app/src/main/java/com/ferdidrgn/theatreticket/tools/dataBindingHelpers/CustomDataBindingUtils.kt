package com.ferdidrgn.theatreticket.tools.dataBindingHelpers

import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import com.ferdidrgn.theatreticket.tools.components.CustomEditText
import com.ferdidrgn.theatreticket.tools.components.CustomPhoneEditText
import com.ferdidrgn.theatreticket.tools.components.CustomToolbar
import com.ferdidrgn.theatreticket.tools.show

object CustomDataBindingUtils {

    @InverseBindingMethods(
        InverseBindingMethod(
            type = CustomEditText::class, attribute = "bind:custom_edit_text", event =
            "bind:textAttrChanged", method = "bind:getText"
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

}