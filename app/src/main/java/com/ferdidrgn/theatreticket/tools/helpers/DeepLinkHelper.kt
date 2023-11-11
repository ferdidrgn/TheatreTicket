package com.ferdidrgn.theatreticket.tools.helpers

import android.content.Context
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.tools.SHOW_NOTIFICATION

fun deepLinkHelper(context: Context, uri: String) {
    if (uri.lowercase().contains("theatre_ticket")) {
        uri.let { url ->
            if (url.contains(SHOW_NOTIFICATION)) {
                NavHandler.instance.toShowDetailsIDActivity(context, url.split("/").last())
            }
        }
    }
}