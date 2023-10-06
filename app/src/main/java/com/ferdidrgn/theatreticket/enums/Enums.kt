package com.ferdidrgn.theatreticket.enums


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