package com.hanheldpos.model.cart

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.diadiem.pos_components.PTextView
import com.hanheldpos.R
import com.hanheldpos.binding.setBackColor
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.product.BaseProductInCart

object CartPresenter {
    fun showCartAnimation(item: BaseProductInCart, slideView: View, popupView: View, slideInListener: Runnable) {

        slideView.visibility = View.VISIBLE
        val animSlideIn = ObjectAnimator.ofFloat(
            slideView,
            "translationY",
            100f,
            0f
        ).apply {
            duration = 400
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    slideInListener.run()
                }
            })
        }

        val animSlideStand = ObjectAnimator.ofFloat(
            slideView,
            "translationY",
            0f,
            0f
        ).apply {
            duration = 1000
        }

        val animSlideOut = ObjectAnimator.ofFloat(
            slideView,
            "translationY",
            0f,
            100f
        ).apply {
            duration = 400
        }

        AnimatorSet().apply {
            play(animSlideIn).before(animSlideStand)
            play(animSlideStand).before(animSlideOut)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    slideView.visibility = View.GONE
                }
            })
        }.start()

        showPopupWindowWithoutBinging(
            popupView,
            item
        )
    }

    private fun showPopupWindowWithoutBinging(anchor: View, item: BaseProductInCart) {
        PopupWindow(anchor.context).apply {
            isOutsideTouchable = true
            val inflater = LayoutInflater.from(anchor.context)
            contentView = inflater.inflate(R.layout.popup_cart_added, null).apply {
                measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
            }
            setBackgroundDrawable(null)

            val circle: View = contentView.findViewById(R.id.circleCartAnimation);
            setBackColor(circle, item.proOriginal?.color);
            val txt: PTextView = contentView.findViewById(R.id.txtProductAdded);
            txt.text = item.proOriginal?.acronymn;

        }.also { popupWindow ->
            val location = IntArray(2).apply {
                anchor.getLocationOnScreen(this)
            }

            val rootView: FrameLayout = popupWindow.contentView.findViewById(R.id.rootPopup)

            val size = Size(
                popupWindow.contentView.measuredWidth,
                popupWindow.contentView.measuredHeight
            )
            popupWindow.showAtLocation(
                anchor,
                0,
                location[0] - size.width + anchor.width + (size.width - anchor.width) / 2,
                location[1] - size.height/2
            )

            val animTranslateIn =
                ObjectAnimator.ofFloat(rootView, "translationY", 100f, 0f).setDuration(400)
            val animScaleXUp =
                ObjectAnimator.ofFloat(rootView, "scaleX", 0.0f, 1.0f).setDuration(400)
            val animScaleYUp =
                ObjectAnimator.ofFloat(rootView, "scaleY", 0.0f, 1.0f).setDuration(400)
            val animPivotX = ObjectAnimator.ofFloat(rootView, "pivotX", 50f).setDuration(400)
            val animPivotY = ObjectAnimator.ofFloat(rootView, "pivotY", 100f).setDuration(400)
            val animScaleXDown =
                ObjectAnimator.ofFloat(rootView, "scaleX", 1.0f, 0.0f).setDuration(400)
            val animScaleYDown =
                ObjectAnimator.ofFloat(rootView, "scaleY", 1.0f, 0.0f).setDuration(400)
            val animStand =
                ObjectAnimator.ofFloat(rootView, "translationY", 0f, 0f).setDuration(700)
            val animTranslateOut =
                ObjectAnimator.ofFloat(rootView, "translationY", 0f, 100f).apply {
                    duration = 400
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            popupWindow.dismiss()
                        }
                    })
                }

            AnimatorSet().apply {
                playTogether(animTranslateIn, animScaleXUp, animScaleYUp)
                play(animTranslateIn).before(animStand)
                play(animStand).before(animTranslateOut)
                playTogether(
                    animTranslateOut,
                    animScaleXDown,
                    animScaleYDown,
                    animPivotX,
                    animPivotY
                )
            }.start()

        }
    }
}