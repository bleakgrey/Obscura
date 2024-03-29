package bleakgrey.obscura

import android.app.Application
import android.content.Context
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import android.widget.ImageView
import bleakgrey.obscura.accounts.InstanceManager
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader

class Application : Application() {

    companion object {
        lateinit var ctx: Context
    }

    override fun onCreate() {
        super.onCreate()
        ctx = this

        // Used to load images into MaterialDrawer
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String) {
                Glide.with(imageView.context)
                    .load(uri)
                    .placeholder(placeholder)
                    .override(200, 200)
                    .centerCrop()
                    .into(imageView)
            }

            override fun cancel(imageView: ImageView) {
                Glide.with(imageView.context).clear(imageView)
            }

//            override fun placeholder(ctx: Context, tag: String): Drawable {
//                //define different placeholders for different imageView targets
//                //default tags are accessible via the DrawerImageLoader.Tags
//                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
//                return when (tag) {
//                    DrawerImageLoader.Tags.PROFILE.name -> DrawerUIUtils.getPlaceHolder(ctx)
//                    DrawerImageLoader.Tags.ACCOUNT_HEADER.name -> IconicsDrawable(ctx).iconText(" ")
//                        .backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56)
//                    "customUrlItem" -> IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.colorAccent).sizeDp(56)
//
//                    //we use the default one for
//                    //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()
//                    else -> super.placeholder(ctx, tag)
//                }
//
//            }
        })
    }

}