package com.hanheldpos.ui.screens.cashdrawer.startdrawer

import android.graphics.Point
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityStartDrawerBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerHelper
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerVM
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerUV
import com.hanheldpos.ui.input.KeyBoardVM
import com.hanheldpos.ui.screens.main.MainActivity
import com.utils.helper.SystemHelper
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.roundToInt

class StartDrawerActivity : BaseActivity<ActivityStartDrawerBinding, CashDrawerVM>(), CashDrawerUV {


    override fun layoutRes(): Int = R.layout.activity_start_drawer;

    private val keyBoardVM = KeyBoardVM();
    override fun initViewModel(viewModel: CashDrawerVM) {
        viewModel.run {
            init(this@StartDrawerActivity);
            binding.viewModel = this;

        }
        binding.numberPad.viewModel = keyBoardVM;
    }

    override fun initView() {

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            SystemHelper.hideSystemUI(window);
        }

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
        keyBoardVM.input.observe(this, {
            viewModel.amountString.value = it;
        });
    }

    override fun initAction() {
        keyBoardVM.listener = object : KeyBoardVM.KeyBoardCallBack {
            override fun onComplete() {
                viewModel.startDrawer(this@StartDrawerActivity);
            }

            override fun onCancel() {

            }

            override fun onSwitch(keyBoardType: KeyBoardType) {

            }

            override fun onCapLock() {

            }

        }
        binding.btnStartDrawer.setOnClickListener {
            viewModel.startDrawer(this);
        }
    }

    override fun viewModelClass(): Class<CashDrawerVM> {
        return CashDrawerVM::class.java;
    }

    override fun backPress() {
        backPress();
    }

    override fun goMain() {
        CashDrawerHelper.isStartDrawer = true;
        navigateTo(
            MainActivity::class.java,
            alsoFinishCurrentActivity = true,
            alsoClearActivity = true,
        );
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        // Detect touch inside edit text
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val touchPoint = Point(ev.rawX.roundToInt(), ev.rawY.roundToInt())
            val viewNum = !isPointInsideViewBounds(
                binding.numberPad.root,
                touchPoint
            );
            val viewEdit = !isPointInsideViewBounds(
                binding.startingCash,
                touchPoint
            );
            val viewBtn = !isPointInsideViewBounds(
                binding.btnStartDrawer,
                touchPoint
            );
            if (binding.numberPad.root.visibility == View.VISIBLE && viewNum && viewEdit && viewBtn
            ) {
                binding.numberPad.root.visibility = View.GONE;
            } else if (!viewEdit || !viewNum) {
                binding.numberPad.root.visibility = View.VISIBLE;
            }
        }


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