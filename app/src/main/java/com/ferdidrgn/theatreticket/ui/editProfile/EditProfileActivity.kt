package com.ferdidrgn.theatreticket.ui.editProfile

import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.base.BaseActivity
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
    }
}