package com.hanheldpos.ui.screens.product.options

import com.hanheldpos.data.api.pojo.order.menu.GroupItem
import com.hanheldpos.model.product.ExtraDoneModel
import com.hanheldpos.ui.base.BaseUserView
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel

class OptionVM : BaseUiViewModel<OptionVM.OptionListener>() {

    /**
     *  Recovery option choose when use EDIT mode
    * */
    var extraDoneModel : ExtraDoneModel? = null;

    fun modifierItemChange(item : ModifierSelectedItemModel){
        uiCallback?.onModifierItemChange(item);
    }
    fun variantItemChange(item : GroupItem){
        uiCallback?.onVariantItemChange(item);
    }

    interface OptionListener : BaseUserView{
        fun onModifierItemChange(item : ModifierSelectedItemModel)
        fun onVariantItemChange(item : GroupItem)
    }
}