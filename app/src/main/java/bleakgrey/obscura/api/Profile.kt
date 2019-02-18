package bleakgrey.obscura.api

import com.google.gson.annotations.SerializedName

data class Profile(
    val id: String,
    @SerializedName("username") val localUsername: String,
    @SerializedName("acct") val username: String,
    @SerializedName("display_name") val displayName: String,
    val note: String,
    val url: String,
    val avatar: String,
    val header: String,
    val locked: Boolean = false,
    @SerializedName("followers_count") val followersCount: Int = 0,
    @SerializedName("following_count") val followingCount: Int = 0,
    @SerializedName("statuses_count") val statusesCount: Int = 0,
    val bot: Boolean = false,
    val moved: Profile? = null
)