package com.ferdidrgn.theatreticket.ui.showOperations

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.ActivityShowOperationsBinding
import com.ferdidrgn.theatreticket.tools.IMAGE_REQUEST_CODE
import com.ferdidrgn.theatreticket.tools.builderADS
import com.ferdidrgn.theatreticket.tools.showToast
import com.tayfuncesur.curvedbottomsheet.CurvedBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShowOperationsActivity :
    BaseActivity<ShowOperationsViewModel, ActivityShowOperationsBinding>() {
    override fun getVM(): Lazy<ShowOperationsViewModel> = viewModels()

    override fun getDataBinding(): ActivityShowOperationsBinding =
        ActivityShowOperationsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        binding.showOperationsAdapter = ShowOperationsAdapter(viewModel)
        binding.customToolbar.backIconOnBackPress(this)
        builderADS(this, binding.adView)
        builderADS(this, binding.adViewBottomSheet)
        bottomSheetInit()
        observe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            binding.imgShow.setImageURI(data?.data)
            viewModel.addOrUpdateImgUrl.value = data?.data
            viewModel.imageUrl.value = data?.data.toString()
        }
    }


    private fun observe() {
        viewModel.getAllShow()

        viewModel.imagePermission.observe(this) {
            permissionCheck()
        }
        viewModel.btnAddShowClicked.observe(this) {
            viewModel.checkRequestFields(false)
        }
        viewModel.btnCstmDatePickerDateClick.observe(this) {
            binding.cdpDate.setCustomDataPickerClick()
        }
        viewModel.btnCstmDatePickerTimeClick.observe(this) {
            binding.cdpTime.popTimePicker()
        }

        viewModel.updateBottonVisibility.observe(this) {
            if (it == true) updateOrDeleteInformationPopUp(this, true)
        }

        viewModel.deletePopUp.observe(this) {
            updateOrDeleteInformationPopUp(this, false)
        }

        viewModel.updateShowPopUp.observe(this) {
            updateShowPopUp(this)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null)
                errorOrSuccessMessagePopUp(this, errorMessage, false)
        }

        viewModel.successMessage.observe(this) { successMessage ->
            if (successMessage != null)
                errorOrSuccessMessagePopUp(this, successMessage, true)
        }
    }

    private fun bottomSheetInit() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        CurvedBottomSheet(
            (displayMetrics.widthPixels / 6).toFloat(),
            view = binding.bottomSheet
        ).init()
    }

    private fun updateOrDeleteInformationPopUp(context: Context, isUpdate: Boolean) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            if (isUpdate) {
                setDesc(context.getString(R.string.are_you_serious_update))
                setOnPositiveClick {
                    viewModel.bottomSheetVisibility.postValue(true)
                    dismiss()
                }
            } else {
                setDesc(context.getString(R.string.are_you_serious_delete))
                setOnPositiveClick {
                    viewModel.deleteShow()
                    dismiss()
                }
            }
            setOnNegativeClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }

    private fun errorOrSuccessMessagePopUp(context: Context, message: String, isSuccess: Boolean) {
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

    private fun updateShowPopUp(context: Context) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            setDesc(context.getString(R.string.are_you_serious))
            setOnPositiveClick {
                viewModel.checkRequestFields(true)
                dismiss()
            }
            setOnNegativeClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }

    private fun permissionCheck() {
        val readImagePermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                android.Manifest.permission.READ_MEDIA_IMAGES else android.Manifest.permission.READ_EXTERNAL_STORAGE
        requestPermissionLauncher.launch(readImagePermission)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                pickImageFromGallery()
            } else {
                showToast(getString(R.string.permission_denied))
            }
        }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }
}