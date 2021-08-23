package com.hanheldpos.ui.screens.product

import android.R.attr.spacing
import android.view.ViewTreeObserver
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.appbar.AppBarLayout
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.ProductOption
import com.hanheldpos.data.api.pojo.product.ProductOptionExtra
import com.hanheldpos.data.api.pojo.product.SliderData
import com.hanheldpos.databinding.FragmentProductDetailBinding
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.base.widget.AppBarStateChangeListener
import com.hanheldpos.ui.screens.product.adapter.ProductExtraOptionsAdapter
import com.hanheldpos.ui.screens.product.adapter.ProductOptionsAdapter
import com.hanheldpos.ui.screens.product.adapter.ProductSizeOptionsAdapter
import com.hanheldpos.ui.screens.product.adapter.SliderAdapter
import com.smarteist.autoimageslider.SliderView


class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding, ProductDetailVM>(),
    ProductDetailUV {

    //View Model
    private val dataVM by activityViewModels<ProductDetailDataVM>()

    // Adapter
    private lateinit var sizeOptionsAdapter: ProductSizeOptionsAdapter
    private lateinit var cookOptionsAdapter: ProductOptionsAdapter
    private lateinit var sauceOptionsAdapter: ProductOptionsAdapter
    private lateinit var extraOptionsAdapter: ProductExtraOptionsAdapter

    override fun layoutRes() = R.layout.fragment_product_detail

    override fun viewModelClass(): Class<ProductDetailVM> {
        return ProductDetailVM::class.java
    }

    override fun initViewModel(viewModel: ProductDetailVM) {
        viewModel.apply {
            init(this@ProductDetailFragment)
            initLifeCycle(this@ProductDetailFragment)
            binding.viewModel = this
        }
        binding.dataVM = this.dataVM
    }

    override fun initView() {
        binding.appBarProductDetail.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                when(state) {
                    State.COLLAPSED ->{
                        viewModel.isToolbarExpand.value = false;
                    }
                    else -> {
                        viewModel.isToolbarExpand.value = true;

                    }
                }
            }

        })
        //Slider Setup
        val sliderDataArrayList = ArrayList<SliderData>()
        val sliderView = binding.slider

        // adding the urls inside array list
        sliderDataArrayList.add(SliderData(viewModel.url))
        sliderDataArrayList.add(SliderData(viewModel.url))
        sliderDataArrayList.add(SliderData(viewModel.url))

        // passing this array list inside our adapter class.
        SliderAdapter(sliderDataArrayList).also {
            sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            sliderView.setSliderAdapter(it)

            sliderView.scrollTimeInSec = 3
            sliderView.isAutoCycle = true

            sliderView.startAutoCycle()
        }
        // cook option adapter vs listener
        sizeOptionsAdapter = ProductSizeOptionsAdapter(
            listener = object : BaseItemClickListener<ProductOption> {
                override fun onItemClick(adapterPosition: Int, item: ProductOption) {
                    //System.out.println("Clicked Option Cook: ${item.name}")
                }

            }
        ).also {
            binding.recyclerSizeOption.layoutManager=GridLayoutManager(context,4)
            binding.recyclerSizeOption.adapter = it
        }

        // cook option adapter vs listener
        cookOptionsAdapter = ProductOptionsAdapter(
            listener = object : BaseItemClickListener<ProductOption> {
                override fun onItemClick(adapterPosition: Int, item: ProductOption) {
                    //System.out.println("Clicked Option Cook: ${item.name}")
                }

            }
        ).also {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            binding.recyclerCookOption.layoutManager = layoutManager
            binding.recyclerCookOption.adapter = it
        }

        // sauce option adapter vs listener
        sauceOptionsAdapter = ProductOptionsAdapter(
            listener = object : BaseItemClickListener<ProductOption> {
                override fun onItemClick(adapterPosition: Int, item: ProductOption) {
                    //System.out.println("Clicked Option Sauce: ${item.name}")
                }

            }
        ).also {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            binding.recyclerSauceOption.layoutManager = layoutManager
            binding.recyclerSauceOption.adapter = it
        }

        // extra option adapter vs listener
        extraOptionsAdapter = ProductExtraOptionsAdapter(
            listener = object : BaseItemClickListener<ProductOptionExtra> {
                override fun onItemClick(adapterPosition: Int, item: ProductOptionExtra) {
                    //System.out.println("Clicked Option Extra: ${item.name}")
                }

            }
        ).also {
            val itemDecorator = DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
            binding.layoutExtraOption.recyclerExtraOption.adapter = it
        }
    }

    override fun initData() {

    }

    override fun initAction() {
        //Quantity Setup
        viewModel.amount.observe(this, {
            binding.txtQuantity.text = it.toString()
        })
        binding.btnMinusQuantity.setOnClickListener {
            viewModel.amount.value?.let { a ->
                if (a > 0)
                    viewModel.amount.value = a - 1
            }
        }
        binding.btnAddQuantity.setOnClickListener {
            viewModel.amount.value?.let { a ->
                viewModel.amount.value = a + 1
            }
        }

        dataVM.sizeOptionsList.observe(this, {
            sizeOptionsAdapter.submitList(dataVM.getOptionList("Size").toMutableList())
            sizeOptionsAdapter.notifyDataSetChanged()
        })

        dataVM.cookOptionsList.observe(this, {
            cookOptionsAdapter.submitList(dataVM.getOptionList("Cook").toMutableList())
            cookOptionsAdapter.notifyDataSetChanged()
        })

        dataVM.sauceOptionsList.observe(this, {
            sauceOptionsAdapter.submitList(dataVM.getOptionList("Sauce").toMutableList())
            sauceOptionsAdapter.notifyDataSetChanged()
        })

        dataVM.extraOptionsList.observe(this, {
            extraOptionsAdapter.submitList(dataVM.getExtraOptionList().toMutableList())
            extraOptionsAdapter.notifyDataSetChanged()
        })

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                dataVM.initData()
            }
        })
    }

    override fun goBack() {
        navigator.goOneBack()
    }
}