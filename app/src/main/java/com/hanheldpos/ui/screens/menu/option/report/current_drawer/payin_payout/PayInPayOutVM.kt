package com.hanheldpos.ui.screens.menu.option.report.current_drawer.payin_payout

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PayInPayOutVM : BaseUiViewModel<PayInPayOutUV>() {
   fun backPress() {
       uiCallback?.getBack()
   }
}