package bleakgrey.obscura.api

import com.google.gson.annotations.SerializedName
import java.util.*

data class Status(
    var id: String,
    var url: String?, // not present if it's reblog
    val account: Profile,
    @SerializedName("in_reply_to_id") var inReplyToId: String?,
    @SerializedName("in_reply_to_account_id") val inReplyToAccountId: String?,
    val reblog: Status?,
    val content: String,
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("reblogs_count") val reblogsCount: Int,
    @SerializedName("favourites_count") val favouritesCount: Int,
    var reblogged: Boolean = false,
    var favourited: Boolean = false,
    var sensitive: Boolean,
    @SerializedName("spoiler_text") val spoilerText: String,
    var pinned: Boolean?
) {



}