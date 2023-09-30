package com.ferdidrgn.theatreticket.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.databinding.BasePopUpBinding
import com.ferdidrgn.theatreticket.tools.hide
import com.ferdidrgn.theatreticket.tools.show

class BasePopUp(val isError: Boolean = false, val isSuccess: Boolean = false) : DialogFragment() {

    private var _binding: BasePopUpBinding? = null

    private val binding get() = _binding

    private var onPositiveClick: (() -> Unit)? = null
    private var onNegativeClick: (() -> Unit)? = null

    private var title = ""
    private var desc = ""
    private var positiveText = ""
    private var negativeText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme_transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BasePopUpBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isError) setErrorWindow()
        if (isSuccess) setSuccessWindow()
        binding?.tbTitle?.text = title
        binding?.positiveTv?.text = positiveText
        binding?.negativeTv?.text = negativeText
        binding?.tvDesc?.text = desc

        binding?.negativeTv?.setOnClickListener {
            onNegativeClick?.let { it() }
        }

        binding?.positiveTv?.setOnClickListener {
            onPositiveClick?.let { it() }
        }
    }

    fun setOnPositiveClick(listener: () -> Unit) {
        this.onPositiveClick = listener
    }

    fun setOnNegativeClick(onNegativeClick: () -> Unit) {
        this.onNegativeClick = onNegativeClick
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setDesc(desc: String) {
        this.desc = desc
    }

    fun setPositiveText(positiveText: String) {
        this.positiveText = positiveText
    }

    fun setNegativeText(negativeText: String) {
        this.negativeText = negativeText
    }

    private fun setSuccessWindow() {
        binding?.apply {
            negativeTv.hide()
            tbTitle.hide()
            imgSuccess.show()
        }
    }

    private fun setErrorWindow() {
        binding?.apply {
            negativeTv.hide()
            tbTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_err))
            tvDesc.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            container.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_main_forth_10_border_red)
            positiveTv.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_bg_red_radius_10)
        }
    }


}