package com.ferdidrgn.theatreticket.ui.termsAndConditions

import android.os.Bundle
import android.text.Html
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityTermsAndConditionsBinding
import com.ferdidrgn.theatreticket.tools.builderADS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class TermsAndConditionsActivity :
    BaseActivity<TermsViewModel, ActivityTermsAndConditionsBinding>() {

    override fun getVM(): Lazy<TermsViewModel> = viewModels()

    override fun getDataBinding(): ActivityTermsAndConditionsBinding =
        ActivityTermsAndConditionsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        builderADS(this, binding.adView)
        binding.customToolbar.backIconOnBackPress(this)

        fetchWebsiteHtml()
    }

    private fun fetchWebsiteHtml() {
        //MOCK DATA
        val url = "http://test-web.donatbi.com:5001/user-agreement"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val html = viewModel.getHtmlFromUrl(url)
                val formattedHtml = Html.fromHtml(html)
                // Handle the HTML content here (e.g., display it in a TextView)
                runOnUiThread {
                    binding.tvTermsAndCondition.text = formattedHtml
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle any errors that occur during the request
            }
        }
    }
}