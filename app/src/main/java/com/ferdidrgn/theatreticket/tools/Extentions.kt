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
import com.ferdidrgn.theatreticket.forFirebaseQueries.ForFirebaseQueries
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