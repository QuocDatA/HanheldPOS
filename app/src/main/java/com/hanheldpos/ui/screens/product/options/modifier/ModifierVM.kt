package com.hanheldpos.ui.screens.product.options.modifier

import androidx.lifecycle.LifecycleOwner
import com.hanheldpos.model.product.GroupExtra
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class ModifierVM : BaseUiViewModel<ModifierUV>() {


    val listGroupExtra :  MutableList<GroupExtra> = mutableListOf();

    fun initLifeCycle(owner: LifecycleOwner){
        owner.lifecycle.addObserver(this);
    }


}