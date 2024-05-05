package com.ferdidrgn.theatreticket.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ferdidrgn.theatreticket.tools.builderADS
import com.ferdidrgn.theatreticket.tools.checkIfTokenDeleted
import com.ferdidrgn.theatreticket.tools.showToast
import com.google.android.gms.ads.AdView

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {

    protected lateinit var viewModel: VM
    protected lateinit var binding: DB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = getDataBinding()
        viewModel = getVM().value
        binding.lifecycleOwner = this
        observeData()
        onCreateFinished(savedInstanceState)
        return binding.root
    }

    abstract fun getVM(): Lazy<VM>

    abstract fun getDataBinding(): DB

    protected abstract fun onCreateFinished(savedInstanceState: Bundle?)

    fun observeData() {
        viewModel.eventShowOrHideProgress.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showProgress() else hideProgress()
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            checkIfTokenDeleted(error)
            showToast(error?.message ?: "", requireContext())
        }
    }

    fun showProgress(viewGroupToAttack: ViewGroup? = null){
        (requireActivity() as BaseActivity <*,*>).showProgress(viewGroupToAttack)
    }

    fun hideProgress(){
        (requireActivity() as BaseActivity <*,*>).hideProgress()
    }

    fun setAds(adView: AdView) {
        builderADS(requireContext(), adView)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    /*fun runRecorder(textView: TextView? = null, result : ((String)->Unit)? = null) {
        hideKeyboard(requireActivity())
        VoiceFragment { textResponse ->
            result?.invoke(textResponse)
            textView?.text = textResponse
        }.show(parentFragmentManager, "")
    }*/
}