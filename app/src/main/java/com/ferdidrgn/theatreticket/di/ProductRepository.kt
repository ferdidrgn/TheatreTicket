package com.ferdidrgn.theatreticket.di


class ProductRepository() {
    suspend fun getHome(string: String? = ""): String {
        return "true"
    }
}