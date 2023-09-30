package com.ferdidrgn.theatreticket.enums


enum class ToMain { Home, TicketBuy, TicketSearch, Settings }
enum class Languages(val language: String) {
    TURKISH("tr_TUR"),
    English("en_US")
}

enum class ContextLanguages(val language: String) {
    TURKISH("tr"),
    English("en")
}