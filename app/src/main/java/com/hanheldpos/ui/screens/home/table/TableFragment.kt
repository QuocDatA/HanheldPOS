package com.hanheldpos.ui.screens.home.table

import android.annotation.SuppressLint
import android.os.SystemClock
import android.util.Log
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.floor.Floor
import com.hanheldpos.data.api.pojo.floor.FloorTable
import com.hanheldpos.databinding.FragmentTableBinding
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.model.home.table.TableStatusType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.home.ScreenViewModel
import com.hanheldpos.ui.screens.home.table.adapter.TableAdapter
import com.hanheldpos.ui.screens.home.table.adapter.TableAdapterHelper
import com.hanheldpos.ui.screens.home.table.customer_input.TableInputFragment


class TableFragment : BaseFragment<FragmentTableBinding, TableVM>(), TableUV {
    override fun layoutRes() = R.layout.fragment_table;

    // Adapter
    private lateinit var tableAdapter: TableAdapter
    private lateinit var tableAdapterHelper: TableAdapterHelper

    // ViewModel
    private val screenViewModel by activityViewModels<ScreenViewModel>()


    override fun viewModelClass(): Class<TableVM> {
        return TableVM::class.java;
    }

    override fun initViewModel(viewModel: TableVM) {
        viewModel.run {
            init(this@TableFragment)
            binding.viewModel = this;
            initLifecycle(this@TableFragment);
        }
    }

    override fun initView() {

        // table adapter vs listener
        tableAdapterHelper =
            TableAdapterHelper(callback = object : TableAdapterHelper.AdapterCallBack {
                @SuppressLint("NotifyDataSetChanged")
                override fun onListSplitCallBack(list: List<FloorTable>) {
                    tableAdapter.submitList(list);
                    tableAdapter.notifyDataSetChanged();
                }

            })

        tableAdapter = TableAdapter(
            listener = object : BaseItemClickListener<FloorTable> {
                override fun onItemClick(adapterPosition: Int, item: FloorTable) {
                    Log.d("OrderFragment", "Product Selected");

                    when (item.uiType) {
                        TableModeViewType.Table -> {
                            onTableChosen(adapterPosition, item);
                        }
                        TableModeViewType.PrevButtonEnable -> {
                            tableAdapterHelper.previous();
                        }
                        TableModeViewType.NextButtonEnable -> {
                            tableAdapterHelper.next();
                        }
                        else -> {
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

    @SuppressLint("NotifyDataSetChanged")
    override fun initAction() {
        CurCartData.currentTableFocus.observe(this) {
            tableAdapter.notifyDataSetChanged()
        }
        screenViewModel.dropDownSelected.observe(this) {
            val screen = screenViewModel.screenEvent.value?.screen;
            if (screen == HomeFragment.HomePage.Table) {
                if (it.realItem is Floor) {
                    viewModel.floorItemSelected.value = it.realItem;
                } else if (it.realItem == null)
                    viewModel.getTableList()?.toMutableList()
                        ?.let { it1 -> tableAdapterHelper.submitList(it1); };
            }
        }

        viewModel.floorItemSelected.observe(this) {
            viewModel.floorTableList.value = viewModel.getTableListByFloor(it)?.toMutableList();
            viewModel.floorTableList.value?.let { it1 -> tableAdapterHelper.submitList(it1) };
        };

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (binding.recyclerTable.height > 0) {
                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this);
                    viewModel.initData();
                }
            }
        })
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun onTableChosen(adapterPosition: Int, item: FloorTable) {
        when (item.tableStatus) {
            TableStatusType.Available -> {
                // Check list has any pending table, if has change to available
                tableAdapter.currentList.filter { it.tableStatus == TableStatusType.Pending }.let {
                    if (it.isNotEmpty()) {
                        it.forEach { table -> table.tableStatus = TableStatusType.Available };
                        tableAdapter.notifyDataSetChanged()
                        return
                    }
                }

                viewModel.mLastTimeClick = SystemClock.elapsedRealtime()

                // Show Table input number customer
                navigator.goTo(TableInputFragment(listener = object :
                    TableInputFragment.TableInputListener {
                    override fun onCompleteTable(numberCustomer: Int) {
                        // Init cart first time
                        item.updateTableStatus(TableStatusType.Pending);
                        CurCartData.initCart(numberCustomer, item);
                        screenViewModel.showOrderPage();
                    }
                }))

            }
            TableStatusType.Pending -> {
                item.tableStatus = TableStatusType.Available;
                tableAdapter.notifyItemChanged(adapterPosition);
            }
            TableStatusType.Unavailable -> {

            }
        }

    }



    companion object {
        var selectedSort: Int = 0;
    }

}