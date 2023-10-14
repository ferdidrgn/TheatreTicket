package com.ferdidrgn.theatreticket.ui.showAddActivity

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.ActivityShowAddBinding

class ShowAddActivity : BaseActivity<ShowAddViewModel, ActivityShowAddBinding>() {
    override fun getVM(): Lazy<ShowAddViewModel> = viewModels()

    override fun getDataBinding(): ActivityShowAddBinding =
        ActivityShowAddBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        viewModel.addShowPopUp.observe(this@ShowAddActivity) {
            searchTicketPopUp(this@ShowAddActivity)
        }

        viewModel.errorMessage.observe(this@ShowAddActivity) { errorMessage ->
            messagePopUp(this@ShowAddActivity, errorMessage!!, false)
        }
        viewModel.successMessage.observe(this) { successMessage ->
            messagePopUp(this@ShowAddActivity, successMessage!!, true)
        }
    }

    private fun searchTicketPopUp(context: Context) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            setDesc(context.getString(R.string.are_you_serious))
            setOnPositiveClick {
                viewModel.addShow()
                dismiss()
            }
            setOnNegativeClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }

    private fun messagePopUp(context: Context, message: String, isSuccess: Boolean) {
        val pupUp = BasePopUp(isSuccess = isSuccess)
        pupUp.apply {
            setPositiveText(context.getString(R.string.done))
            setDesc(message)
            setOnPositiveClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }
}