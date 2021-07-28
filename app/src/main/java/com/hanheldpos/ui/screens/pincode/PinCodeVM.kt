package com.hanheldpos.ui.screens.pincode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.R
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.screens.notifyValueChange
import okhttp3.internal.notify
import java.util.*

class PinCodeVM : BaseUiViewModel<PinCodeUV>() {

    // Data
    private val lstResultLD = MutableLiveData<MutableList<String>?>(mutableListOf())

    val listSize: MutableLiveData<Int> = Transformations.map(lstResultLD) { listResult: List<String>? ->
                if (listResult != null) {
                    return@map listResult.size
                } else {
                    return@map -1
                }
            } as MutableLiveData<Int>


    fun backPress() {
        uiCallback?.goBack();
    }

    fun initNumberPadList(): List<PinCodeRecyclerElement> {
        val list: MutableList<PinCodeRecyclerElement> = mutableListOf();
        // Line 1
        list.add(PinCodeRecyclerElement("3", null))
        list.add(PinCodeRecyclerElement("2", null))
        list.add(PinCodeRecyclerElement("1", null))
        // Line 2
        list.add(PinCodeRecyclerElement("6", null))
        list.add(PinCodeRecyclerElement("5", null))
        list.add(PinCodeRecyclerElement("4", null))
        // Line 3
        list.add(PinCodeRecyclerElement("9", null))
        list.add(PinCodeRecyclerElement("8", null))
        list.add(PinCodeRecyclerElement("7", null))
        // Line 4
        list.add(PinCodeRecyclerElement(null, R.drawable.ic_baseline_arrow_back_l))
        list.add(PinCodeRecyclerElement("0", null))

        return list;
    }

    private fun addItem(item: Int) {
        if (lstResultLD.value != null) {
            lstResultLD.value!!.add(item.toString() + "")
            lstResultLD.notifyValueChange();
        }
        if (listSize.value != null && listSize.value == PIN_MAX_LENGTH) {
            checkPinInput()
        }
    }

    private fun removeItem() {
        if (!lstResultLD.value.isNullOrEmpty()) {
            lstResultLD.value!!.removeAt(lstResultLD.value!!.size.minus(1));
            lstResultLD.notifyValueChange();
        }
    }

    private fun checkPinInput() {
        if (lstResultLD.value != null && lstResultLD.value!!.size == PIN_MAX_LENGTH) {
            val passCodeBuilder = StringBuilder()

            for (i in 0 until PIN_MAX_LENGTH) {
                if (lstResultLD.value != null) {
                    passCodeBuilder.append(Objects.requireNonNull<List<String>?>(lstResultLD.value)[i])
                }
            }
            // Fetch data
        }
    }

    fun onClick(item: PinCodeRecyclerElement?) {
        if (item?.resource != null) {
            removeItem()
            return
        }
        if (listSize.value != null && listSize.value!! < PIN_MAX_LENGTH) {
            for (i in 0..9) {
                if (item?.text == i.toString() + "") {
                    addItem(i)
                    return
                }
            }
        }
    }


    companion object {
        private const val PIN_MAX_LENGTH = 4
    }


}