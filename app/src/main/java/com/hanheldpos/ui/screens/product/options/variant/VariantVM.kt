package com.hanheldpos.ui.screens.product.options.variant

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.data.api.pojo.order.menu.GroupItem
import com.hanheldpos.data.api.pojo.order.menu.VariantStrProduct
import com.hanheldpos.data.api.pojo.order.menu.splitListGroupNameValue
import com.hanheldpos.data.api.pojo.order.menu.splitListOptionValue
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.product.adapter.variant.VariantHeader
import com.hanheldpos.ui.screens.product.adapter.variant.VariantLayoutItem
import java.lang.ref.WeakReference

class VariantVM : BaseUiViewModel<VariantUV>() {

    val listVariantHeader  = MutableLiveData<MutableList<VariantHeader>>(mutableListOf());
    val variantLevelList: MutableList<String> = mutableListOf();

    fun initLifeCycle(owner: LifecycleOwner){
        owner.lifecycle.addObserver(this);

    }

    protected var variantStrProduct: VariantStrProduct? = null
        set(value) {
            field = value
            clearVariantLevelList()
//            variantLevelList.clear()
            value?.option?.forEach { _ ->
                variantLevelList.add("")
            }
        }

    private fun clearVariantLevelList() {
//        val iter: MutableIterator<String> = variantLevelList.iterator()
//
//        while (iter.hasNext()) {
//            iter.remove()
//        }
//        variantLevelList = mutableListOf()
        synchronized(variantLevelList) {
            variantLevelList.clear()
        }
    }

    /**
     * Init first Variant level list
     */
    fun initDefaultVariantHeaderList(variantStrProduct: VariantStrProduct?): MutableList<VariantHeader> {
        this.variantStrProduct = variantStrProduct

        val rs: MutableList<VariantHeader> = mutableListOf()

        this.variantStrProduct?.option?.forEachIndexed { index, it ->
            val header = VariantHeader()
            header.realItem = it
            header.headerPosition = index
            header.name = it?.optionName

            // show first level list
            if (index == 0) {
                header.childList = it?.splitListOptionValue()?.map {
                    VariantLayoutItem(
                        it,
                        WeakReference(header)
                    )
                }
            } else {
                val levelNameList = it?.splitListOptionValue()

                val groupNameLevel = getLevelGroupName()
                val groupItemListStartWithGroupName = getGroupItemListStartWith(groupNameLevel)

                header.childList = levelNameList?.filter { levelName ->
                    groupItemListStartWithGroupName?.forEach { groupItemStartWithLevel ->
                        if (levelName == groupItemStartWithLevel.splitListGroupNameValue()
                                ?.getOrNull(index)
                        ) {
                            return@filter true
                        }
                    }
                    return@filter false
                }?.map {
                    VariantLayoutItem(
                        it,
                        WeakReference(header)
                    )
                }
            }

            header.childList?.firstOrNull()?.let { childItem ->
//                variantLevelList[index] = childItem.name
                variantLevelList.add(index, childItem.name)
            }

            rs.add(header)
        }

        return rs
    }
    /**
     * @return list group item that GroupName start with @variantStr
     * EX : GroupName = Rare•Mushroom sauce•Hot
     *      if variantStr = Rare•Mushroom sauce
     *         then add Group Item to list
     */
    protected fun getGroupItemListStartWith(groupName: String?): List<GroupItem>? {
        if (groupName == null) return null
        return variantStrProduct?.group
            ?.filterNotNull()?.filter { it.groupName?.startsWith(groupName) == true }
    }

    protected fun getLevelGroupName(untilLevel: Int? = null): String {
        var rs = ""
        var index = 0
//        val list = variantLevelList.iterator()
//        while (list.hasNext()){
//            val value = list.next()
//            if (!value.isBlank() && index < untilLevel ?: variantLevelList.size) {
//                if (index != 0) {
//                    rs += Const.SymBol.VariantSeparator
//                }
//                rs += value
//                index++
//            } else {
//                break
//            }
//        }
        variantLevelList.iterator().forEach {
            if (!it.isNullOrBlank() && index < untilLevel ?: variantLevelList.size) {
                if (index != 0) {
                    rs += Const.SymBol.VariantSeparator
                }
                rs += it
                index++
            } else {
                index++
                return@forEach
            }
        }
        return rs
    }
}