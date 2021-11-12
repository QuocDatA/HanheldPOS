package com.hanheldpos.ui.screens.product.options.modifier

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierHeader
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel

class ModifierVM : BaseUiViewModel<ModifierUV>() {

    val
    var defaultModifierListLD = MutableLiveData<MutableList<ModifierHeader>?>()

    fun initLifeCycle(owner: LifecycleOwner){
        owner.lifecycle.addObserver(this);
    }


}