package com.hanheldpos.ui.screens.product.options.variant

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.data.api.pojo.order.menu.GroupItem
import com.hanheldpos.data.api.pojo.order.menu.VariantStrProduct
import com.hanheldpos.data.api.pojo.order.menu.splitListGroupNameValue
import com.hanheldpos.data.api.pojo.order.menu.splitListOptionValue
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel


class VariantVM : BaseUiViewModel<VariantUV>() {

    val listVariantGroups : MutableList<VariantsGroup> =mutableListOf();

    val listVariants : MutableList<VariantCart> = mutableListOf();

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
    }


}