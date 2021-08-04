package com.hanheldpos.ui.screens.main.home.table

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentTableBinding
import com.hanheldpos.model.home.table.TableModel
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.main.home.table.adapter.TableAdapter


class TableFragment : BaseFragment<FragmentTableBinding, TableVM>(), TableUV {
    override fun layoutRes() = R.layout.fragment_table;

    // Adapter
    private lateinit var tableAdapter : TableAdapter

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
        tableAdapter = TableAdapter(
          listener = object : BaseItemClickListener<TableModel> {
              override fun onItemClick(adapterPosition: Int, item: TableModel) {

              }
          }
        );
        binding.recyclerTable.apply {
            adapter = tableAdapter;
        }
    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun tableListObserve() {
        tableAdapter.submitList(viewModel.getTableList())
        tableAdapter.notifyDataSetChanged();
    }

}