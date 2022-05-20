package com.hanheldpos.ui.screens.payment.completed

import com.handheld.printer.PrintConstants
import com.handheld.printer.printer_manager.BasePrinterManager
import com.hanheldpos.R
import com.hanheldpos.binding.setPriceView
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.databinding.FragmentPaymentCompletedBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.printer.BillPrinterManager
import com.hanheldpos.model.printer.layouts.BaseLayoutPrinter
import com.hanheldpos.ui.base.fragment.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class PaymentCompletedFragment(
    private val isHasEmployee: Boolean,
    private val payable: Double,
    private val overPay: Double,
    private val listener: PaymentCompletedCallBack,
) : BaseFragment<FragmentPaymentCompletedBinding, PaymentCompletedVM>(), PaymentCompletedUV {
    override fun layoutRes(): Int {
        return R.layout.fragment_payment_completed
    }

    override fun viewModelClass(): Class<PaymentCompletedVM> {
        return PaymentCompletedVM::class.java
    }

    override fun initViewModel(viewModel: PaymentCompletedVM) {
        viewModel.run {
            this.init(this@PaymentCompletedFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        showLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseHelper.ordersCompleted.getAll().take(1).collectLatest {
                it.lastOrNull()?.let { completedEntity ->

                    launch(Dispatchers.IO) {
                        try {
                            BillPrinterManager.get(
                                onConnectionFailed = { ex ->
                                    launch(Dispatchers.Main) {
                                        showMessage(ex.message)
                                    }

                                }
                            ).apply {
                                print(
                                    fragmentContext,
                                    DatabaseMapper.mappingOrderReqFromEntity(completedEntity),
                                    layoutType = BaseLayoutPrinter.LayoutType.Kitchen
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    launch(Dispatchers.Main) {
                        showLoading(false)
                    }
                }
            }
        }

        setPriceView(binding.textChangeValue, overPay - payable)
        setPriceView(binding.textReceived, overPay)
    }

    override fun initData() {
        viewModel.isAlreadyHasEmployee.postValue(isHasEmployee)

    }

    override fun initAction() {
        binding.btnNewSale.setOnClickDebounce {
            listener.newSale()
            super.onFragmentBackPressed()
        }
        binding.btnBecomeAMember.setOnClickDebounce {
            listener.becomeAMember()
        }
    }

    override fun onFragmentBackPressed() {

    }

    interface PaymentCompletedCallBack {
        fun becomeAMember()
        fun newSale()
    }
}