package com.hanheldpos.ui.screens.cashdrawer.startdrawer

import android.graphics.Point
import android.graphics.Rect
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityStartDrawerBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.screens.home.table.input.NumberPadVM
import com.hanheldpos.ui.screens.main.MainActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.roundToInt

class StartDrawerActivity : BaseActivity<ActivityStartDrawerBinding, StartDrawerVM>(),
    StartDrawerUV {
    override fun layoutRes(): Int = R.layout.activity_start_drawer;
    private val numberPadVM = NumberPadVM();
    override fun initViewModel(viewModel: StartDrawerVM) {
        viewModel.run {
            init(this@StartDrawerActivity);
            binding.viewModel = this;

        }
        binding.numberPad.viewModel = numberPadVM;
    }

    override fun initView() {
        viewModel.initContext(context);
        binding.startingCash.let { input ->
            var isEditing = false
            input.doAfterTextChanged {
                if (isEditing) return@doAfterTextChanged;
                if (it.toString().isEmpty()) input.setText("0");
                else {
                    isEditing = true;
                    val dfSymbols = DecimalFormatSymbols()
                    dfSymbols.decimalSeparator = '.'
                    dfSymbols.groupingSeparator = ','
                    val df = DecimalFormat("###", dfSymbols)
                    df.groupingSize = 3
                    df.isGroupingUsed = true
                    val text = df.format(it.toString().replace(",", "").toDouble());
                    input.setText(text);

                }
                input.setSelection(input.length());
                isEditing = false;
            }
        }


    }

    override fun initData() {
        numberPadVM.input.observe(this, {
            viewModel.amountString.value = it;
        });
    }

    override fun initAction() {
        numberPadVM.listener = object : NumberPadVM.NumberPadCallBack {
            override fun onComplete() {
                viewModel.onComplete();
            }

            override fun onCancel() {

            }

        }
    }

    override fun viewModelClass(): Class<StartDrawerVM> {
        return StartDrawerVM::class.java;
    }

    override fun backPress() {
        backPress();
    }

    override fun goHome() {
        navigateTo(MainActivity::class.java, alsoFinishCurrentActivity = true, alsoClearActivity = false);
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

//        // Detect touch inside edit text
//        val touchPoint = Point(ev.rawX.roundToInt(), ev.rawY.roundToInt())
//
//        if (!isPointInsideViewBounds(
//                binding.numberPad.root,
//                touchPoint
//            )
//        ) {
//            binding.numberPad.root.visibility = View.GONE;
//        } else {
//            binding.numberPad.root.visibility = View.VISIBLE;
//        }

        return super.dispatchTouchEvent(ev)
    }

    /**
     * Defines bounds of displayed view and check is it contains [Point]
     * @param view View to define bounds
     * @param point Point to check inside bounds
     * @return `true` if view bounds contains point, `false` - otherwise
     */
    private fun isPointInsideViewBounds(view: View, point: Point): Boolean = Rect().run {
        // get view rectangle
        view.getDrawingRect(this)

        // apply offset
        IntArray(2).also { locationOnScreen ->
            view.getLocationOnScreen(locationOnScreen)
            offset(locationOnScreen[0], locationOnScreen[1])
        }

        // check is rectangle contains point
        contains(point.x, point.y)
    }
}