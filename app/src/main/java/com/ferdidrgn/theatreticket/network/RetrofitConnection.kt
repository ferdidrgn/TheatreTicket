package com.ferdidrgn.theatreticket.network

import com.ferdidrgn.theatreticket.enums.Environment
import com.ferdidrgn.theatreticket.tools.*
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.log
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import java.net.URISyntaxException

class HeaderInterceptor : Interceptor {

    @Volatile
    private var host = "https://google.com".toHttpUrlOrNull()

    private fun setHostBaseUrl() {
        when (ClientPreferences.inst.baseUrl) {
            Environment.test.url -> {
                host = Environment.test.url.toHttpUrlOrNull()
            }

            Environment.preprod.url -> {
                host = Environment.preprod.url.toHttpUrlOrNull()
            }

            Environment.prod.url -> {
                host = Environment.prod.url.toHttpUrlOrNull()
            }
        }
    }


    override fun intercept(chain: Interceptor.Chain): Response {
        setHostBaseUrl()
        var token: String = if (ClientPreferences.inst.token.isNullOrEmpty().not()) {
            String.format("Bearer %s", ClientPreferences.inst.token)
        }else{
            String.format("Bearer %s", ClientPreferences.inst.guestToken)
        }
        log(token)

        var request = chain.request()

        if (host != null) {
            var newUrl: HttpUrl? = null
            try {
                newUrl = request.url.newBuilder()
                    .scheme(host!!.scheme)
                    .host(host!!.toUrl().toURI().host)
                    .build()

                request = request.newBuilder()
                    .addHeader(CONNECTION, ClientPreferences.inst.connection ?: "")
                    .url(newUrl)
                    .build()


            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }

        return chain.proceed(request)
    }
}