package com.hanheldpos.ui.screens.home.table

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.databinding.DialogCategoryBinding
import com.hanheldpos.databinding.DialogTableOrderBinding
import com.hanheldpos.databinding.FragmentTableBinding
import com.hanheldpos.model.home.order.product.ProductModeViewType
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.ScreenViewModel
import com.hanheldpos.ui.screens.home.table.adapter.TableAdapter
import com.hanheldpos.ui.screens.home.table.adapter.TableAdapterHelper


class TableFragment : BaseFragment<FragmentTableBinding, TableVM>(), TableUV {
    override fun layoutRes() = R.layout.fragment_table;

    // Adapter
    private lateinit var tableAdapter: TableAdapter
    private lateinit var tableAdapterHelper: TableAdapterHelper
    // ViewModel
    private val dataVM by activityViewModels<TableDataVM>()
    private val screenViewModel by activityViewModels<ScreenViewModel>()

    // Dialog Category
    private lateinit var dialogTable: AlertDialog;
    private lateinit var dialogTableBinding : DialogTableOrderBinding;

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

        // Init Dialog
        dialogTableBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_table_order,
            null,
            false
        );
        dialogTableBinding.viewModel = viewModel;

        dialogTableBinding.cancelBtn.setOnClickListener {
            dialogTable.dismiss();
            dialogTableBinding.numberCustomer.text?.clear();
        }
        dialogTableBinding.acceptBtn.setOnClickListener {
            screenViewModel.showOrderPage();
            dialogTable.dismiss();
            dialogTableBinding.numberCustomer.text?.clear();
        }


        val builder = AlertDialog.Builder(context);
        builder.setView(dialogTableBinding.root);

        dialogTable = builder.create();
        dialogTable.setCanceledOnTouchOutside(false);
        dialogTable.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialogTable.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    fun openDialogInputCustomer(table : FloorTableItem){
        dialogTableBinding.tableSelected = table;
        dialogTable.show();
    }

}