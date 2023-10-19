package com.ferdidrgn.theatreticket.ui.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.databinding.FragmentHomeBinding
import com.ferdidrgn.theatreticket.tools.ioScope
import com.ferdidrgn.theatreticket.tools.mainScope
import com.ferdidrgn.theatreticket.tools.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val snapHelper: SnapHelper = PagerSnapHelper()
    lateinit var job: kotlinx.coroutines.Job

    override fun getVM(): Lazy<HomeViewModel> = viewModels()

    override fun getDataBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.showAdapter = ShowsAdapter(viewModel)
        binding.showSliderAdapter = ShowsSliderHorizontalAdapter(viewModel)

        observe()

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            messagePopUp(requireContext(), errorMessage!!, false)
        }
    }

    private fun observe() {

        viewModel.getAllShow()

        viewModel.show.observe(viewLifecycleOwner) { showList ->
            binding.indicator.count = showList?.size ?: 1
            val linearLayoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            snapHelper.attachToRecyclerView(binding.rvSlider)
            binding.rvSlider.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val position = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if (position != -1)
                        binding.indicator.selection = position
                }
            })
            job = lifecycleScope.launch(Dispatchers.Main, start = CoroutineStart.LAZY) {
                var position = 0
                repeat(1000) {
                    delay(3000)
                    position?.let { it1 -> binding.rvSlider.smoothScrollToPosition(it1) }
                    when (showList?.size) {
                        position?.plus(1) -> position = 0
                        else -> position++
                    }
                    binding.rvSlider.layoutManager = linearLayoutManager
                }
            }
            job.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
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
        pupUp.show(parentFragmentManager, "")
    }
}