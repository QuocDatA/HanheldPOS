package com.hanheldpos.ui.screens.home.table

import android.app.AlertDialog
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.table.FloorItem
import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.databinding.FragmentTableBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.home.ScreenViewModel
import com.hanheldpos.ui.screens.home.table.adapter.TableAdapter
import com.hanheldpos.ui.screens.home.table.adapter.TableAdapterHelper
import com.hanheldpos.ui.screens.home.table.input.TableInputFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment


class TableFragment : BaseFragment<FragmentTableBinding, TableVM>(), TableUV {
    override fun layoutRes() = R.layout.fragment_table;

    // Adapter
    private lateinit var tableAdapter: TableAdapter
    private lateinit var tableAdapterHelper: TableAdapterHelper

    // ViewModel
    private val dataVM by activityViewModels<TableDataVM>()
    private val screenViewModel by activityViewModels<ScreenViewModel>()
    private val cartDataVM by activityViewModels<CartDataVM>()
    // Dialog Category
    private lateinit var dialogTable: AlertDialog;


    override fun viewModelClass(): Class<TableVM> {
        return TableVM::class.java;
    }

    override fun initViewModel(viewModel: TableVM) {
        viewModel.run {
            init(this@TableFragment)
            binding.viewModel = this;
            initLifecycle(this@TableFragment);
        }

        this.dataVM.run {
            binding.dataVM = this;
        }

    }

    override fun initView() {

        // table adapter vs listener
        tableAdapterHelper =
            TableAdapterHelper(callback = object : TableAdapterHelper.AdapterCallBack {
                override fun onListSplitCallBack(list: List<FloorTableItem>) {
                    tableAdapter.submitList(list);
                    tableAdapter.notifyDataSetChanged();
                }

            })

        tableAdapter = TableAdapter(
            listener = object : BaseItemClickListener<FloorTableItem> {
                override fun onItemClick(adapterPosition: Int, item: FloorTableItem) {
                    Log.d("OrderFragment", "Product Selected");
                    if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick > 1000) {
                        when (item.uiType) {
                            TableModeViewType.Table -> {
                                viewModel.mLastTimeClick = SystemClock.elapsedRealtime();
                                dataVM.curTableLD.value = item;
                                openDialogInputCustomer(item);
                            }
                            TableModeViewType.PrevButton -> {

                                tableAdapterHelper.previous();
                            }
                            TableModeViewType.NextButton -> {
                                tableAdapterHelper.next();
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
        ).also {
            binding.recyclerTable.apply {
                adapter = it;
            }
        }


    }


    override fun initData() {

    }

    override fun initAction() {
        screenViewModel.dropDownSelected.observe(this, {
            val screen = screenViewModel.screenEvent.value?.screen;
            if (screen == HomeFragment.HomePage.Table) {
                if (it.realItem is FloorItem) {
                    dataVM.floorItemSelected.value = it.realItem as FloorItem;
                } else if (it.realItem == null)
                    dataVM.getTableList()?.toMutableList()
                        ?.let { it1 -> tableAdapterHelper.submitList(it1); };
            }
        })

        dataVM.curTableLD.observe(this,{
            if (it == null)
                cartDataVM.cartModelLD.value = null
        })

        dataVM.floorItemSelected.observe(this, {
            dataVM.floorTableList.value = dataVM.getTableListByFloor(it)?.toMutableList();
            dataVM.floorTableList.value?.let { it1 -> tableAdapterHelper.submitList(it1) };
        });


        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (binding.recyclerTable.height > 0) {
                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this);
                    dataVM.initData();
                }
            }
        })
    }

    private fun openDialogInputCustomer(item: FloorTableItem) {
        navigator.goTo(TableInputFragment.getInstance(listener = object :
            TableInputFragment.TableInputListener {
            override fun onCompleteTable(numberCustomer: Int) {
                cartDataVM.cartModelLD.value?.customerQuantity = numberCustomer;
                cartDataVM.cartModelLD.notifyValueChange();
                screenViewModel.showOrderPage();
            }
        }));
    }
    companion object {
        var selectedSort : Int = 0;

    }

}