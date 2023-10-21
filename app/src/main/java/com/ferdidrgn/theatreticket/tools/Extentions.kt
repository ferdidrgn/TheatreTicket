package com.ferdidrgn.theatreticket.tools

import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import com.ferdidrgn.theatreticket.tools.helpers.MainSliderHandler
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.hideAndHold() {
    this.visibility = View.INVISIBLE
}

fun View.onClickDelayed(skipDurationMillis: Long = 750, action: () -> Unit) {
    var isEnabled = true
    this.setOnClickListener {
        if (isEnabled) {
            action()
            isEnabled = false
            Handler().postDelayed({ isEnabled = true }, skipDurationMillis)
        }
    }
}

fun Context.setAppLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = Configuration()
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return createConfigurationContext(config)
}

fun ViewPager2.getPositionAndSendHandler2(
    list: List<Any?>?,
    handler: MainSliderHandler,
    sendPosition: (Int) -> Unit
) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                handler.removeChanging()
            } else {
                handler.addChanging()
            }
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            sendPosition.invoke(position)
        }
    })

    handler.setHandler {
        val currentPosition: Int = currentItem
        if (list?.isNotEmpty() == true) {
            if (currentPosition == (list.size - 1)) {
                setCurrentItem(0, true)
                sendPosition.invoke(0)
            } else {
                setCurrentItem(currentPosition + 1, true)
                sendPosition.invoke(currentPosition + 1)
            }
        }
    }
}

suspend fun CoroutineScope.executeBody(block: suspend CoroutineScope.() -> Unit) {
    try {
        block.invoke(this)
    } catch (e: Exception) {
        e.printStackTrace();
        log(e.message ?: "")
    }
}

fun AppCompatActivity.ioScope(block: suspend CoroutineScope.() -> Unit) =
    lifecycleScope.launch { executeBody(block) }

fun AppCompatActivity.mainScope(block: suspend CoroutineScope.() -> Unit) =
    lifecycleScope.launch { executeBody(block) }

fun ViewModel.ioScope(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch { withContext(Dispatchers.IO) { executeBody(block) } }

fun ViewModel.mainScope(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch { executeBody(block) }

fun Fragment.ioScope(block: suspend CoroutineScope.() -> Unit) =
    lifecycleScope.launch { withContext(Dispatchers.IO) { executeBody(block) } }

fun Fragment.mainScope(block: suspend CoroutineScope.() -> Unit) =
    lifecycleScope.launch { executeBody(block) }