package edu.bluejack20_1.gogames.rawg.utils

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.bluejack20_1.gogames.R

fun Fragment.LoadImage(view: ImageView, url: String) {
    val requestOptions = RequestOptions()
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_background)

    Glide.with(view.context)
        .applyDefaultRequestOptions(requestOptions)
        .load(url)
        .into(view)
}