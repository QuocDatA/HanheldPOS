package com.hanheldpos.ui.screens.home.table

import android.os.SystemClock
import android.util.Log
import android.view.ViewTreeObserver
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.databinding.FragmentTableBinding
import com.hanheldpos.model.home.order.product.ProductModeViewType
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.table.adapter.TableAdapter
import com.hanheldpos.ui.screens.home.table.adapter.TableAdapterHelper


class TableFragment : BaseFragment<FragmentTableBinding, TableVM>(), TableUV {
    override fun layoutRes() = R.layout.fragment_table;

    // Adapter
    private lateinit var tableAdapter: TableAdapter
    private lateinit var tableAdapterHelper: TableAdapterHelper
    // ViewModel
    private val dataVM by activityViewModels<TableDataVM>()

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
        tableAdapterHelper = TableAdapterHelper(callback = object : TableAdapterHelper.AdapterCallBack{
            override fun onListSplitCallBack(list: List<FloorTableItem>) {
                tableAdapter.submitList(list);
                tableAdapter.notifyDataSetChanged();
            }

        })

        tableAdapter = TableAdapter(
            listener = object : BaseItemClickListener<FloorTableItem> {
                override fun onItemClick(adapterPosition: Int, item: FloorTableItem) {
                    Log.d("OrderFragment", "Product Selected");
                    if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick > 100) {
                        viewModel.mLastTimeClick = SystemClock.elapsedRealtime();
                        when (item.uiType) {
                            TableModeViewType.Table -> {

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


        dataVM.floorItemSelected.observe(this,{
            dataVM.floorTableList.value = dataVM.getTableListByFloor(it)?.toMutableList();
            dataVM.floorTableList.value?.let { it1 -> tableAdapterHelper.submitList(it1) };
        });


        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if (binding.recyclerTable.height > 0){
                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this);
                    dataVM.initData();
                }
            }
        })


    }


}