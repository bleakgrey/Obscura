package bleakgrey.obscura

import android.content.Context

class Prefs(val ctx: Context) {

    val prefs = ctx.applicationContext.getSharedPreferences(BuildConfig.APPLICATION_ID, 0)

}