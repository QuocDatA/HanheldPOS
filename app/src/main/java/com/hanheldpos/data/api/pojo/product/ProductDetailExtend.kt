package com.hanheldpos.data.api.pojo.product

import com.hanheldpos.data.api.pojo.product.ProductDetailResp

internal fun ProductDetailResp.getModel() = this.model?.firstOrNull()

internal fun ProductDetailResp.getCookOptionsList() = this.getModel()?.listCookOptions

internal fun ProductDetailResp.getSauceOptionsList() = this.getModel()?.listSauceOptions

internal fun ProductDetailResp.getSizeOptionsList() = this.getModel()?.listSizeOptions

internal fun ProductDetailResp.getExtraOptionsList() = this.getModel()?.listExtraOptions