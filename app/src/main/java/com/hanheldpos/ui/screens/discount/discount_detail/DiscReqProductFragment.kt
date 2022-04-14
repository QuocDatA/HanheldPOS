package com.hanheldpos.ui.screens.discount.discount_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDiscReqProductBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discount_detail.adapter.RequirementProductAdapter

class DiscReqProductFragment(private val title: String, private val listItemShow: List<Any>) :
    BaseFragment<FragmentDiscReqProductBinding, DiscReqProductVM>(), DiscReqProductUV {

    private lateinit var requirementProductAdapter: RequirementProductAdapter

    override fun layoutRes(): Int {
        return R.layout.fragment_disc_req_product
    }

    override fun viewModelClass(): Class<DiscReqProductVM> {
        return DiscReqProductVM::class.java
    }

    override fun initViewModel(viewModel: DiscReqProductVM) {
        viewModel.run {
            init(this@DiscReqProductFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        binding.title.text = title

        requirementProductAdapter = RequirementProductAdapter()
        binding.listReqProduct.apply {
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
                }
            )
        }.adapter = requirementProductAdapter
    }

    override fun initData() {
        requirementProductAdapter.submitList(listItemShow)
    }

    override fun initAction() {
        binding.btnOk.setOnClickListener { onFragmentBackPressed() }
    }

}