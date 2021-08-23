package com.hanheldpos.ui.screens.home.table

import android.view.ViewTreeObserver
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.databinding.FragmentTableBinding
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.table.adapter.TableAdapter


class TableFragment : BaseFragment<FragmentTableBinding, TableVM>(), TableUV {
    override fun layoutRes() = R.layout.fragment_table;

    // Adapter
    private lateinit var tableAdapter: TableAdapter

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
        tableAdapter = TableAdapter(
            listener = object : BaseItemClickListener<FloorTableItem> {
                override fun onItemClick(adapterPosition: Int, item: FloorTableItem) {

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

        //
        dataVM.floorTablePage.observe(this,{
            tableAdapter.submitList(dataVM.getTableListByPage(it));
            tableAdapter.notifyDataSetChanged();
        });

        dataVM.floorTableList.observe(this,{
            dataVM.floorTablePage.value = 1;
        });

        dataVM.floorItemSelected?.observe(this,{
            dataVM.floorTableList.value = dataVM.getTableListByFloor(it)?.toMutableList();
        });



        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            if (binding.recyclerTable.height > 0){
                dataVM.initData();
            }
        }
    }


}