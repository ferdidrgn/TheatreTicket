package com.ferdidrgn.theatreticket.presentation.ui.stageOperations

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
import com.ferdidrgn.theatreticket.databinding.ActivityStageOperationsBinding
import com.ferdidrgn.theatreticket.tools.builderADS
import com.ferdidrgn.theatreticket.tools.showToast
import com.tayfuncesur.curvedbottomsheet.CurvedBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StageOperationsActivity :
    BaseActivity<StageOperationsViewModel, ActivityStageOperationsBinding>() {

    private val IMAGE_REQUEST_CODE = 1_000
    override fun getVM(): Lazy<StageOperationsViewModel> = viewModels()

    override fun getDataBinding(): ActivityStageOperationsBinding =
        ActivityStageOperationsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        binding.customToolbar.backIconOnBackPress(this)
        binding.stageOperationsAdapter = StageOperationsAdapter(viewModel)
        builderADS(this, binding.adView)
        builderADS(this, binding.adViewBottomSheet)
        bottomSheetInit()
        observe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            binding.imgStage.setImageURI(data?.data)
            viewModel.addOrUpdateImgUrl.value = data?.data
            viewModel.imgUrl.value = data?.data.toString()
        }
    }


    private fun observe() {
        viewModel.getAllStage()

        binding.customToolbar.backIconOnBackPress(this)

        viewModel.imagePermission.observe(this) {
            permissionCheck()
        }
        viewModel.btnAddStageClicked.observe(this) {
            addOrUpdateStagePopUp(this, false)
        }

        viewModel.updateBottonVisibility.observe(this) {
            if (it == true) updateOrDeleteInformationPopUp(this, true)
        }

        viewModel.deletePopUp.observe(this) {
            updateOrDeleteInformationPopUp(this, false)
        }

        viewModel.updateStagePopUp.observe(this) {
            addOrUpdateStagePopUp(this, true)
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
                    viewModel.deleteStage()
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

    private fun addOrUpdateStagePopUp(context: Context, isUpdate: Boolean) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            setDesc(context.getString(R.string.are_you_serious))
            setOnPositiveClick {
                viewModel.checkRequestFields(isUpdate)
                dismiss()
            }
            setOnNegativeClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }
}