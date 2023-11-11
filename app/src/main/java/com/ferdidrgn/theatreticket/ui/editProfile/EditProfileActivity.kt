package com.ferdidrgn.theatreticket.ui.editProfile

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.ActivityEditProfileBinding
import com.ferdidrgn.theatreticket.tools.builderADS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : BaseActivity<EditProfileViewModel, ActivityEditProfileBinding>() {
    override fun getVM(): Lazy<EditProfileViewModel> = viewModels()

    override fun getDataBinding(): ActivityEditProfileBinding =
        ActivityEditProfileBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        builderADS(this, binding.adView)

        observe()
    }

    private fun observe() {

        viewModel.btnUpdateAccClick.observe(this) {
            if (it == true) updateOrDeleteInformationPopUp(this, true)
        }

        viewModel.btnDeleteAccountClick.observe(this) {
            updateOrDeleteInformationPopUp(this, false)
        }
    }


    private fun updateOrDeleteInformationPopUp(context: Context, isUpdate: Boolean) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            if (isUpdate) {
                setDesc(context.getString(R.string.are_you_serious_update))
                setOnPositiveClick {
                    viewModel.checkRequestFields()
                    dismiss()
                }
            } else {
                setDesc(context.getString(R.string.are_you_serious_delete))
                setOnPositiveClick {
                    viewModel.deleteUser()
                    dismiss()
                }
            }
            setOnNegativeClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }
}