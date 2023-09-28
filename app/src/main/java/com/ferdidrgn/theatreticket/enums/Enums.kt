package com.ferdidrgn.theatreticket.enums


enum class Environment(val url: String) {
    test("api_base1"),
    preprod("api_base2"),
    prod("api_base3")
}
enum class Languages(val language: String) {
    TURKISH("tr_TUR"),
    English("en_US")
}

enum class ContextLanguages(val language: String) {
    TURKISH("tr"),
    English("en")
}