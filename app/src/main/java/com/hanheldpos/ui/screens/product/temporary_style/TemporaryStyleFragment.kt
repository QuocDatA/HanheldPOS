package com.hanheldpos.ui.screens.product.temporary_style

import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentTemporaryStyleBinding
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.screens.product.temporary_style.adapter.TemporaryVariantAdapter
import com.hanheldpos.ui.screens.product.temporary_style.temporary_adapter.TemporarySauceAdapter

class TemporaryStyleFragment : BaseFragment<FragmentTemporaryStyleBinding, TemporaryStyleVM>(), TemporaryStyleUV {

    private lateinit var temporarySauceAdapter: TemporarySauceAdapter

    private val temporaryStyleVM by activityViewModels<TemporaryStyleVM>();

    private lateinit var variantAdapter: TemporaryVariantAdapter;

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
            GridSpacingItemDecoration(4, resources.getDimensionPixelSize(R.dimen._15sdp), false)
        );
        }
    }

    override fun initData() {
        variantAdapter.submitList(mutableListOf(0, 1, 2, 3, 4,6,7,8));
        //region init payment suggestion data
        val sauceItemList: MutableList<TemporarySauceItem> =
            (temporaryStyleVM.initSauceItem() as List<TemporarySauceItem>).toMutableList();
        temporarySauceAdapter.submitList(sauceItemList);
        //endregion
    }

    override fun initAction() {

    }

    override fun getBack() {
        onFragmentBackPressed()
    }
}
data class TemporarySauceItem(val name: String)

