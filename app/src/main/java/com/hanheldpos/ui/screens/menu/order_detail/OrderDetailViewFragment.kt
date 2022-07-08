package com.hanheldpos.ui.screens.menu.order_detail

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.hanheldpos.R
import com.hanheldpos.databinding.DialogChoosePrinterBinding
import com.hanheldpos.databinding.FragmentOrderDetailViewBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.printer.BillPrinterManager
import com.hanheldpos.printer.dialogs.PrinterChooseAdapter
import com.hanheldpos.printer.dialogs.PrinterChooseModel
import com.hanheldpos.ui.base.dialog.AppFunctionDialog
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.order_detail.adapter.OrderDetailPaymentAdapter
import com.hanheldpos.ui.screens.menu.order_detail.adapter.ProductBuyItem
import com.hanheldpos.ui.screens.menu.order_detail.adapter.ProductBuyParentAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class OrderDetailViewFragment(private val orderId: String) :
    BaseFragment<FragmentOrderDetailViewBinding, OrderDetailViewVM>(), OrderDetailViewUV {
    private lateinit var orderDetailPaymentViewAdapter: OrderDetailPaymentAdapter
    private lateinit var orderProductAdapter: ProductBuyParentAdapter
    override fun layoutRes(): Int {
        return R.layout.fragment_order_detail_view
    }

    override fun viewModelClass(): Class<OrderDetailViewVM> {
        return OrderDetailViewVM::class.java
    }

    override fun initViewModel(viewModel: OrderDetailViewVM) {
        viewModel.run {
            init(this@OrderDetailViewFragment)
            binding.viewModel = this
            binding.lifecycleOwner = this@OrderDetailViewFragment
        }
    }

    override fun initView() {
        orderProductAdapter = ProductBuyParentAdapter()
        binding.itemList.adapter = orderProductAdapter

        orderDetailPaymentViewAdapter = OrderDetailPaymentAdapter()
        binding.paymentList.adapter = orderDetailPaymentViewAdapter
    }

    override fun initData() {
        context?.let { viewModel.getOrderDetail(it, orderId) }
    }

    override fun initAction() {
        binding.btnPrint.setOnClickDebounce {
            showDialogChoosePrinter()
        }
    }


    private fun showDialogChoosePrinter() {
        val binding = DataBindingUtil.inflate<DialogChoosePrinterBinding>(
            layoutInflater,
            R.layout.dialog_choose_printer,
            null,
            false
        )

        val printerList = DataHelper
            .hardwareSettingLocalStorage?.printerList
            ?.map { PrinterChooseModel(it.id, it.name) }
            ?.toMutableList() ?: mutableListOf()

        val adapter = PrinterChooseAdapter { position, checked ->
            val printer = printerList[position]
            printerList[position] = printer.copy(isChecked = checked)
            binding.enableReprint = printerList.any { it.isChecked }
            if (!checked) binding.allDeviceCheck.isChecked = false
            else if (printerList.filter { it.isChecked }.size == printerList.size) {
                binding.allDeviceCheck.isChecked = true
            }
        }

        binding.rv.adapter = adapter
        adapter.submitList(printerList)

        binding.allDeviceCheck.setOnClickListener {
            if (binding.allDeviceCheck.isChecked) binding.enableReprint = true
            for ((index, value) in printerList.withIndex()) {
                printerList[index] =
                    value.copy(isChecked = binding.allDeviceCheck.isChecked)
            }
            adapter.notifyDataSetChanged()

        }
        binding.allDeviceCheck.performClick()

        binding.rv.addItemDecoration(
            MaterialDividerItemDecoration(
                fragmentContext,
                MaterialDividerItemDecoration.VERTICAL
            )
        )

        val dialog = AppFunctionDialog.get()
            .showCustomLayout(
                binding,
                maxWidth = 0.8,
                maxHeight = 0.8
            )

        binding.okOnClick = View.OnClickListener { _ ->

            val order = viewModel.orderModel.value

            lifecycleScope.launch(Dispatchers.IO) {
                BillPrinterManager.get().printBill(
                    order ?: return@launch,
                    isReprint = true,
                    limitToThesePrinterId =
                    printerList
                        .filter { it.isChecked }
                        .map { it.printerId }
                        .toList()
                )
            }

            dialog?.dismiss()
            onFragmentBackPressed()
        }

        binding.dismissOnClick = View.OnClickListener {
            dialog?.dismiss()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onShowOrderDetail(order: OrderModel) {
        binding.order = order
//        orderDetailItemViewAdapter.submitList(order.OrderDetail.OrderProducts)
//        orderDetailItemViewAdapter.notifyDataSetChanged()
        orderProductAdapter.submitList(order.OrderDetail.OrderProducts.map {
            ProductBuyItem(
                chosenProduct = it,
                level = 0
            )
        })
        orderDetailPaymentViewAdapter.submitList(order.OrderDetail.PaymentList)
        orderDetailPaymentViewAdapter.notifyDataSetChanged()
    }

}