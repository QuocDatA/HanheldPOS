package com.hanheldpos.ui.screens.product.options.modifier

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.model.product.GroupExtra
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierHeader
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel

class ModifierVM : BaseUiViewModel<ModifierUV>() {


    val listGroupExtra :  MutableList<GroupExtra> = mutableListOf();

    fun initLifeCycle(owner: LifecycleOwner){
        owner.lifecycle.addObserver(this);
    }


}