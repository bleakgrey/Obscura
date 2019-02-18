package bleakgrey.obscura.api

import com.google.gson.annotations.SerializedName

data class OauthToken(
    @SerializedName("access_token") val data: String
)