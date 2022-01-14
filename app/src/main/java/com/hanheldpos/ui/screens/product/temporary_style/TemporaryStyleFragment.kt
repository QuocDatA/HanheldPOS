package com.hanheldpos.ui.screens.product.temporary_style

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentTemporaryStyleBinding
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.temporary_style.adapter.*

class TemporaryStyleFragment : BaseFragment<FragmentTemporaryStyleBinding, TemporaryStyleVM>(),
    TemporaryStyleUV {

    private lateinit var temporarySauceAdapter: TemporarySauceAdapter
    private lateinit var variantAdapter: TemporaryVariantAdapter;
    private lateinit var cookOptionAdapter: TemporaryCookAdapter;
    private lateinit var optionAdapter: TemporaryOptionAdapter;
    private lateinit var modifierAdapter : TemporaryModifierAdapter;
    private lateinit var modifierTwoAdapter: TemporaryModifierTwoAdapter;

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


        variantAdapter = TemporaryVariantAdapter()
        binding.containerVariant.apply {
            adapter = variantAdapter;
            addItemDecoration(
                GridSpacingItemDecoration(
                    4,
                    resources.getDimensionPixelSize(R.dimen._15sdp),
                    false
                )
            );
        }

        cookOptionAdapter = TemporaryCookAdapter()
        binding.containerCookOption.apply {
            adapter = cookOptionAdapter;
            addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    resources.getDimensionPixelSize(R.dimen._6sdp),
                    false
                )
            );
        }

        temporarySauceAdapter = TemporarySauceAdapter();
        binding.temporarySauceContainer.apply {
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
            binding.temporarySauceContainer.adapter = temporarySauceAdapter;
        };

        optionAdapter = TemporaryOptionAdapter();
        binding.containerOption.apply {
            adapter = optionAdapter;
        }

        modifierAdapter = TemporaryModifierAdapter();
        binding.modifierCheckBoxContainer.apply {
            adapter = modifierAdapter;
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
        }

        modifierTwoAdapter = TemporaryModifierTwoAdapter()
        binding.containerModifierSelect.apply {
            adapter = modifierTwoAdapter;
            addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    resources.getDimensionPixelSize(R.dimen._6sdp),
                    false
                )
            );
        }
    }

    override fun initData() {
        variantAdapter.submitList(mutableListOf("S", "M", "L", "SSL"))
        cookOptionAdapter.submitList(mutableListOf("Rare", "Medium"))
        temporarySauceAdapter.submitList(mutableListOf("Pepper sauce", "Mushroom sauce"));
        optionAdapter.submitList(mutableListOf("Rare", "Medium","Well done"))
        modifierAdapter.submitList(mutableListOf("Extra bruschetta tomato", "Onion","Onion"))
        modifierTwoAdapter.submitList(mutableListOf("Onion","Cheese"));
    }

    override fun initAction() {

    }

    override fun getBack() {

    }


}