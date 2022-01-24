package com.hanheldpos.ui.screens.menu.option.report.current_drawer.payin_payout

import com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out.PaidInOutListResp
import com.hanheldpos.ui.base.BaseUserView

interface PayInPayOutUV: BaseUserView {
    fun getBack()
    fun onLoadPaidInOutListToUI(list : List<PaidInOutListResp>?)
}