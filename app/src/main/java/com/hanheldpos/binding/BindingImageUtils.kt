package com.hanheldpos.binding

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.hanheldpos.R


@BindingAdapter("imageUrl")
    fun loadImageFromUrl(imageView: ImageView, imageUrl: String? = null) {
        imageView.apply {
            Glide.with(context)
                .load(imageUrl ?: "")
                .placeholder(startLoadingProgress(context))
                .thumbnail(0.5f)
                .error(R.drawable.bg_no_image)
                .centerCrop()
                .into(this)
        }
    }

@BindingAdapter("imgCircleSwitch")
fun setImgCircleSwitch(view: ImageView?, isInput: Boolean?) {
    if (view == null || isInput == null) return
    val srcOutline = R.drawable.circle_outline
    val srcSolid = R.drawable.circle_solid
    if (isInput) {
        view.setImageResource(srcSolid)
    } else {
        view.setImageResource(srcOutline)
    }
}

@BindingAdapter("imgSrc")
fun setImgSrc(view: ImageView?, src: Int?) {
    if (view == null) return
    if (src == null) {
        view.visibility = View.GONE
    } else {
        view.setImageResource(src)
    }
}

private fun startLoadingProgress(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 4f
        centerRadius = 30f
        setColorSchemeColors(ContextCompat.getColor(context, R.color.primary))
        start()
    }
}
