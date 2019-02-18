package bleakgrey.obscura.network

import bleakgrey.obscura.api.FederationAPI
import okhttp3.Interceptor
import okhttp3.Response

class TokenInjectInterceptor(private val token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if(request.header(FederationAPI.HEADER_TOKEN_INJECTION) == null)
            return chain.proceed(request)

        val builder = request.newBuilder()
        builder.removeHeader(FederationAPI.HEADER_TOKEN_INJECTION)
        builder.addHeader("Authorization", "Bearer $token")

        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }

}