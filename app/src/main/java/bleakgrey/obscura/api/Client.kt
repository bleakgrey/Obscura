package bleakgrey.obscura.api

import com.google.gson.annotations.SerializedName

data class Client (

    @SerializedName("client_id") val id: String,
    @SerializedName("client_secret") val secret: String,
    var domain: String? = ""

) {

    val empty: Boolean
        get() {
            return id.isEmpty() || secret.isEmpty()
        }

}