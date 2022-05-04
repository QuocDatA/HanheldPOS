package com.hanheldpos.ui.screens.menu.report.current_drawer.payin_payout

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out.PaidInOutListResp
import com.hanheldpos.databinding.FragmentPayInPayOutBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.current_drawer.payin_payout.adapter.PaidInOutAdapter
import com.hanheldpos.utils.PriceUtils

class PayInPayOutFragment(private val listener : PayInOutCallback) : BaseFragment<FragmentPayInPayOutBinding, PayInPayOutVM>(),
    PayInPayOutUV {

    private lateinit var paidInOutAdapter: PaidInOutAdapter;

    override fun layoutRes() = R.layout.fragment_pay_in_pay_out

    override fun viewModelClass(): Class<PayInPayOutVM> {
        return PayInPayOutVM::class.java
    }

    override fun initViewModel(viewModel: PayInPayOutVM) {
        viewModel.run {
            init(this@PayInPayOutFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        binding.amountInput.let { input ->
            var isEditing = false
            input.doAfterTextChanged {
                if (isEditing) return@doAfterTextChanged;
                if (it.toString().isEmpty()) input.setText("0");
                else {
                    isEditing = true;
                    input.setText(PriceUtils.formatStringPrice(it.toString()))
                }
                input.setSelection(input.length());
                isEditing = false;
            }
        }

        viewModel.isValid.observe(this) {
            if (it) {
                binding.textBtnPayIn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_0
                    )
                );
                binding.textBtnPayOut.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_0
                    )
                );
                binding.btnPayIn.background =
                    ContextCompat.getDrawable(requireContext(), R.color.color_11);
                binding.btnPayOut.background =
                    ContextCompat.getDrawable(requireContext(), R.color.color_11);

            } else {
                binding.textBtnPayIn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_8
                    )
                );
                binding.textBtnPayOut.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_8
                    )
                );
                binding.btnPayIn.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_outline);
                binding.btnPayOut.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_outline);
            }
        };

        viewModel.isActiveButton.observe(this) {
            when (it) {
                PayInPayOutVM.ActiveButton.PayIn -> {
                    binding.textBtnPayIn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_11
                        )
                    );
                    binding.textBtnPayOut.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_0
                        )
                    );
                    binding.btnPayIn.background =
                        ContextCompat.getDrawable(requireContext(), R.color.color_0);
                    binding.btnPayOut.background =
                        ContextCompat.getDrawable(requireContext(), R.color.color_11);
                }
                PayInPayOutVM.ActiveButton.PayOut -> {
                    binding.textBtnPayIn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_0
                        )
                    );
                    binding.textBtnPayOut.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_11
                        )
                    );
                    binding.btnPayIn.background =
                        ContextCompat.getDrawable(requireContext(), R.color.color_11);
                    binding.btnPayOut.background =
                        ContextCompat.getDrawable(requireContext(), R.color.color_0);
                }
                else -> {
                    viewModel.isValid.postValue(viewModel.isValid.value);
                }
            }
        };

        paidInOutAdapter = PaidInOutAdapter();
        binding.paidInOutList.apply {
            adapter = paidInOutAdapter;
            addItemDecoration(
                DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            ).apply {
                setDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.divider_vertical
                    )!!
                )
            })
        }
    }

    override fun initData() {
        viewModel.loadPaidInOut(requireContext());
    }

    override fun initAction() {

    }

    override fun getBack() {
        listener.onLoadReport();
        onFragmentBackPressed()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onLoadPaidInOutListToUI(list: List<PaidInOutListResp>?) {
        paidInOutAdapter.submitList(list);
        paidInOutAdapter.notifyDataSetChanged();
        val totalPrice = list?.sumOf { (it.Receivable?:0.0).minus(it.Payable?:0.0) };
        binding.totalPaid.text =PriceUtils.formatStringPrice( totalPrice.toString() );
    }

    interface PayInOutCallback {
        fun onLoadReport();
    }
}