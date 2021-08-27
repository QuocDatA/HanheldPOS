package com.hanheldpos.ui.screens.product

import android.R.attr.spacing
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.ProductOption
import com.hanheldpos.data.api.pojo.product.ProductOptionExtra
import com.hanheldpos.data.api.pojo.product.SliderData
import com.hanheldpos.databinding.FragmentProductDetailBinding
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.base.widget.AppBarStateChangeListener
import com.hanheldpos.ui.screens.product.adapter.ProductExtraOptionsAdapter
import com.hanheldpos.ui.screens.product.adapter.ProductOptionsAdapter
import com.hanheldpos.ui.screens.product.adapter.ProductSizeOptionsAdapter
import com.hanheldpos.ui.screens.product.adapter.SliderAdapter
import com.smarteist.autoimageslider.SliderView


class ProductDetailFragment(
    private val listener: ProductDetailListener? = null
) : BaseFragment<FragmentProductDetailBinding, ProductDetailVM>(),
    ProductDetailUV {

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
    }

    override fun initView() {
        binding.appBarProductDetail.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                when(state) {
                    State.COLLAPSED ->{
                        viewModel.isToolbarExpand.value = false
                    }
                    else -> {
                        viewModel.isToolbarExpand.value = true

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

            /*sliderView.startAutoCycle()*/
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
            binding.recyclerSizeOption.addItemDecoration(
                GridSpacingItemDecoration(
                    4, 55,
                    false
                )
            )
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
            binding.layoutExtraOption.recyclerExtraOption.adapter = it
        }

        // Litener Add
        binding.addCartBtn.setOnClickListener {

            viewModel.productCompleteLD.value?.let { it1 -> listener?.onAddCart(it1) };
        }
    }

    override fun initData() {
        arguments?.let {
            val a: ProductCompleteModel? = it.getParcelable(ARG_PRODUCT_DETAIL_FRAGMENT)
            val quantityCanChoose: Int = it.getInt(ARG_PRODUCT_DETAIL_QUANTITY)
            viewModel.quantityCanChoose.value = quantityCanChoose;
            viewModel.productCompleteLD.value = a;
            viewModel.productDetailResp.value = a?.productDetail;

        }
    }

    override fun initAction() {
        //Quantity Setup
        viewModel.numberQuantityLD.observe(this, {
            binding.txtQuantity.text = it.toString()
        })
        binding.btnMinusQuantity.setOnClickListener {
            viewModel.numberQuantityLD.value?.let { a ->
                if (a > 0)
                    viewModel.numberQuantityLD.value = a - 1
            }
        }
        binding.btnAddQuantity.setOnClickListener {
            viewModel.numberQuantityLD.value?.let { a ->
                viewModel.numberQuantityLD.value = a + 1
            }
        }

        viewModel.sizeOptionsList.observe(this, {
            sizeOptionsAdapter.submitList(viewModel.getOptionList("Size").toMutableList())
            sizeOptionsAdapter.notifyDataSetChanged()
        })

        viewModel.cookOptionsList.observe(this, {
            cookOptionsAdapter.submitList(viewModel.getOptionList("Cook").toMutableList())
            cookOptionsAdapter.notifyDataSetChanged()
        })

        viewModel.sauceOptionsList.observe(this, {
            sauceOptionsAdapter.submitList(viewModel.getOptionList("Sauce").toMutableList())
            sauceOptionsAdapter.notifyDataSetChanged()
        })

        viewModel.extraOptionsList.observe(this, {
            extraOptionsAdapter.submitList(viewModel.getExtraOptionList().toMutableList())
            extraOptionsAdapter.notifyDataSetChanged()
        })

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                viewModel.initData()
            }
        })
    }

    override fun goBack() {
        navigator.goOneBack()
    }

    override fun onAddCart() {
        viewModel.productCompleteLD.value?.apply {
            quantity = viewModel.numberQuantityLD.value!!;
            productDetail = viewModel.productDetailResp.value?.clone();
            listener?.onAddCart(this);
        }
        goBack();
    }

    interface ProductDetailListener {
        fun onAddCart(productComplete : ProductCompleteModel)
    }

    companion object {
        private const val ARG_PRODUCT_DETAIL_FRAGMENT = "ARG_PRODUCT_DETAIL_FRAGMENT"
        private const val ARG_PRODUCT_DETAIL_QUANTITY = "ARG_PRODUCT_DETAIL_QUANTITY"
        fun getInstance(
            item: ProductCompleteModel,
            quantityCanChoose: Int = -1,
            listener: ProductDetailListener? = null
        ): ProductDetailFragment {
            return ProductDetailFragment(
                listener = listener
            ).apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PRODUCT_DETAIL_FRAGMENT, item)
                    putInt(ARG_PRODUCT_DETAIL_QUANTITY, quantityCanChoose)
                }
            }
        }
    }
}