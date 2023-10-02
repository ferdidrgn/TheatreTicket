package com.ferdidrgn.theatreticket.repository


//NOT: We added for run Dagger - Hilt. This Class don't working another things.
class ProductRepository() {
    suspend fun getHome(string: String? = ""): String {
        return "true"
    }
}