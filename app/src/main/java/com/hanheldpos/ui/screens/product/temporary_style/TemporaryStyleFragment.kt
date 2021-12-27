package com.hanheldpos.ui.screens.product.temporary_style

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentTemporaryStyleBinding
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.screens.product.temporary_style.adapter.TemporaryCookAdapter
import com.hanheldpos.ui.screens.product.temporary_style.adapter.TemporaryVariantAdapter
import com.hanheldpos.ui.screens.product.temporary_style.temporary_adapter.TemporarySauceAdapter

class TemporaryStyleFragment : BaseFragment<FragmentTemporaryStyleBinding, TemporaryStyleVM>(), TemporaryStyleUV {

    private lateinit var temporarySauceAdapter: TemporarySauceAdapter

    private val temporaryStyleVM by activityViewModels<TemporaryStyleVM>();

    private lateinit var variantAdapter: TemporaryVariantAdapter;
    private lateinit var cookOptionAdapter: TemporaryCookAdapter;

    override fun layoutRes(): Int {
        return R.layout.fragment_temporary_style;
    }

    override fun viewModelClass(): Class<TemporaryStyleVM> {
        return TemporaryStyleVM::class.java;
    }

    override fun initViewModel(viewModel: TemporaryStyleVM) {
        viewModel.run {
            init(this@TemporaryStyleFragment);
        }
        binding.viewModel = viewModel;
    }

    override fun initView() {
        //region setup sauce recycler view
        temporarySauceAdapter = TemporarySauceAdapter(
            onTemporarySauceItemClickListener = object : BaseItemClickListener<TemporarySauceItem> {
                override fun onItemClick(adapterPosition: Int, item: TemporarySauceItem) {
                }
            },
        );
        binding.temporarySauceContainer.apply {
            addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount = 1,
                    includeEdge = false,
                    spacing = resources.getDimensionPixelSize(R.dimen._10sdp)
                )
            )
            binding.temporarySauceContainer.adapter = temporarySauceAdapter;
        };
        //endregion
        variantAdapter = TemporaryVariantAdapter()
        binding.containerVariant.apply {
            adapter = variantAdapter; addItemDecoration(
            GridSpacingItemDecoration(
                4,
                resources.getDimensionPixelSize(R.dimen._15sdp),
                false
            )
        );
        }

        cookOptionAdapter = TemporaryCookAdapter()
        binding.containerCookOption.apply {
            adapter = cookOptionAdapter; addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimensionPixelSize(R.dimen._6sdp),
                false
            )
        );
        }
    }

    override fun initData() {
        variantAdapter.submitList(mutableListOf(0, 1, 2, 3))
        cookOptionAdapter.submitList(mutableListOf(0, 1, 2, 3))

        //region init payment suggestion data
        val sauceItemList: MutableList<TemporarySauceItem> =
            (temporaryStyleVM.initSauceItem() as List<TemporarySauceItem>).toMutableList();
        temporarySauceAdapter.submitList(sauceItemList);
        //endregion
    }

    override fun initAction() {

    }


}