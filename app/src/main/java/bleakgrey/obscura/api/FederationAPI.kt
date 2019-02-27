package bleakgrey.obscura.api

import android.util.Log
import bleakgrey.obscura.network.TokenInjectInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

interface FederationAPI {

    companion object {
        const val ENDPOINT_AUTHORIZE = "/oauth/authorize"

        // This header is only used to know which requests require access token
        // It will should never be passed to the server
        const val HEADER_TOKEN_INJECTION = "X-HeyMateInjectAccessToken4Me"
        private const val HEADER_TOKEN_REQUIRED = "$HEADER_TOKEN_INJECTION: Pls"

        fun create(domain: String, token: String = "lolwhattoken"): FederationAPI {
            val tokenInjector = TokenInjectInterceptor(token)
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(tokenInjector)
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(domain)
                .client(client)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FederationAPI::class.java)
        }
    }

    @FormUrlEncoded
    @POST("oauth/token")
    fun fetchOAuthToken(
        @Header("domain") domain: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String = "authorization_code"
    ): Deferred<OauthToken>

    @FormUrlEncoded
    @POST("api/v1/apps")
    fun registerClient(
        @Header("domain") domain: String,
        @Field("client_name") clientName: String,
        @Field("redirect_uris") redirectUris: String,
        @Field("scopes") scopes: String,
        @Field("website") website: String
    ): Deferred<Client>

    @Headers(HEADER_TOKEN_REQUIRED)
    @GET("api/v1/accounts/verify_credentials")
    fun getSelfProfile(): Deferred<Profile>

}