package com.hanheldpos.ui.screens.main

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.data.DataVersion
import com.hanheldpos.databinding.ActivityMainBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.printer.PrinterException
import com.hanheldpos.ui.base.activity.BaseFragmentBindingActivity
import com.hanheldpos.ui.base.fragment.FragmentNavigator
import com.hanheldpos.ui.screens.pincode.PinCodeFragment
import com.hanheldpos.ui.screens.welcome.WelcomeFragment
import com.hanheldpos.utils.GSonUtils
import com.hanheldpos.utils.NetworkUtils
import com.utils.helper.SystemHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : BaseFragmentBindingActivity<ActivityMainBinding, MainVM>(), MainUV {

    override fun createFragmentNavigator(): FragmentNavigator {
        return FragmentNavigator(supportFragmentManager, R.id.main_fragment_container)
    }

    override fun layoutRes() = R.layout.activity_main

    override fun viewModelClass(): Class<MainVM> {
        return MainVM::class.java
    }

    override fun initViewModel(viewModel: MainVM) {
        viewModel.run {
            init(this@MainActivity)
        }
    }

    override fun initView() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            SystemHelper.hideSystemUI(window);
        }
        viewModel.initView()
    }

    override fun initData() {
        NetworkUtils.checkActiveInternetConnection(listener = object :
            NetworkUtils.NetworkConnectionCallBack {
            override fun onAvailable() {

            }

            override fun onLost() {
                CoroutineScope(Dispatchers.Main).launch {
                    showAlert(
                        title = getString(R.string.notification),
                        message = getString(R.string.no_network_connection)
                    )
                }
            }
        })
    }

    override fun initAction() {
        DataHelper.firebaseSettingLocalStorage?.fireStorePath?.let {
            Firebase.firestore.collection(it.dataVersion ?: "")
                .addSnapshotListener { value, error ->
                    GSonUtils.toObject<DataVersion>(value?.documents?.firstOrNull()?.data?.let { it1 ->
                        JSONObject(
                            it1
                        ).toString()
                    })?.let { dataVersionNew ->
                        if ((DataHelper.dataVersionLocalStorage?.menu ?: 0) < (dataVersionNew.menu
                                ?: 0) || (DataHelper.dataVersionLocalStorage?.discount
                                ?: 0) < (dataVersionNew.discount
                                ?: 0)
                        ) {
                            DataHelper.isNeedToUpdateNewData = true
                        } else {
                            DataHelper.isNeedToUpdateNewData = true
                        }
                    }
                }
        }

    }

    override fun openPinCode() {
        getNavigator().rootFragment = PinCodeFragment()
    }

    override fun openWelcome() {
        getNavigator().rootFragment = WelcomeFragment()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        SystemHelper.hideSystemUI(window);
        super.onWindowFocusChanged(hasFocus)
    }
}