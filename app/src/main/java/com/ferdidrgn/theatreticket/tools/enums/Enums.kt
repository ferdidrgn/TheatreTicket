package com.ferdidrgn.theatreticket.tools.enums

import android.content.Context
import androidx.annotation.StringRes
import com.ferdidrgn.theatreticket.R


enum class ToMain { Home, TicketBuy, TicketSearch, Settings }

enum class WhichTermsAndPrivacy { TermsAndCondition, PrivacyAndPolicy }

enum class WhichEditProfile { LogIn, Settings }

enum class Response { Empty, ServerError, ThereIs, NotEqual }

enum class ID(val id: String) {
    Ticket(".t"),
    Sell(".s"),
    User(".u"),
    Show(".sh"),
    Stage(".st"),
}

enum class Roles(val role: String) {
    Admin("admin"),
    User("user"),
    Guest("guest")
}

enum class PhoneNumberLengthsByCountry(val length: Int, val mask: String) {
    Arabic(13, "### ### ## ##"),
    English(13, "### ### ## ##"),
    Turkish(13, "### ### ## ##")
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

enum class textInputType(val text: String, val type: Int) {
    Text("text", 0x00000001),
    NumberDecimal("numberdecimal", 0x00002002),
    Phone("phone", 0x00000003),
    DateTime("datetime", 0x00000004),
    Date("date", 0x00000014),
    Time("time", 0x00000024)
}