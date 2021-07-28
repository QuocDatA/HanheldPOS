package com.hanheldpos.utils.binding;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.hanheldpos.R;

public class BindingImageUtils {

    @BindingAdapter("imgCircleSwitch")
    public static void setImgCircleSwitch(ImageView view, Boolean isInput) {
        if (view == null || isInput == null) return;
        int srcOutline = R.drawable.circle_outline;
        int srcSolid = R.drawable.circle_solid;

        if (isInput) {
            view.setImageResource(srcSolid);
        } else {
            view.setImageResource(srcOutline);
        }
    }

    @BindingAdapter("imgSrc")
    public static void setImgSrc(ImageView view, Integer src) {
        if (view == null) return;
        if (src == null) {
            view.setVisibility(View.GONE);
        } else {
            view.setImageResource(src);
        }
    }
}
