package com.ferdidrgn.theatreticket.enums

import android.content.Context
import androidx.annotation.StringRes
import com.ferdidrgn.theatreticket.R


enum class ToMain { Home, TicketBuy, TicketSearch, Settings }

enum class ID(val id: String) {
    Ticket(".t"),
    Sell(".s"),
    Customer(".c")
}

enum class Response(val response: String) {
    Empty("empty"),
    ServerError("serverError"),
    ThereIs("thereIs")
}

enum class Roles(val role: String) {
    Admin("admin"),
    User("user")
}

enum class Languages(val language: String) {
    TURKISH("tr_TUR"),
    English("en_US")
}

enum class ContextLanguages(val language: String) {
    TURKISH("tr"),
    English("en")
}

enum class ToolBarTitles(@field:StringRes @param:StringRes private val mLabel: Int) {
    Home(R.string.title_home),
    TicketBuy(R.string.ticket_buy),
    TicketSearch(R.string.ticket_search),
    Settings(R.string.settings),
    Language(R.string.language),
    ShowAdd(R.string.show_add),
    ShowDelete(R.string.show_delete);

    internal inner class Entry(context: Context) {
        private val mContext: Context
        override fun toString(): String {
            return mContext.getString(mLabel)
        }

        init {
            mContext = context
        }
    }
}