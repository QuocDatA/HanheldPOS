package com.hanheldpos.ui.screens.menu.option.report.drawer.payin_payout

import androidx.lifecycle.ViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PayInPayOutVM : BaseUiViewModel<PayInPayOutUV>() {
   fun backPress() {
       uiCallback?.getBack()
   }
}