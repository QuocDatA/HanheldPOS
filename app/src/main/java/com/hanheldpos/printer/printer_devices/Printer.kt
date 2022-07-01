package com.hanheldpos.printer.printer_devices

import android.os.StrictMode
import com.hanheldpos.PosApp
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.data.api.pojo.setting.hardware.HardwarePrinter
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.report.SaleReportFilter
import com.hanheldpos.printer.PrintConstants
import com.hanheldpos.printer.layouts.LayoutType
import com.hanheldpos.printer.layouts.order.CashierLayout
import com.hanheldpos.printer.layouts.order.KitchenLayout
import com.hanheldpos.printer.layouts.report.InventoryLayout
import com.hanheldpos.printer.layouts.report.OverviewLayout
import com.hanheldpos.printer.printer_setup.PrintConfig
import com.hanheldpos.printer.printer_setup.PrinterConnectionTypes
import com.hanheldpos.printer.printer_setup.PrinterRecipeType
import com.hanheldpos.printer.printer_setup.device_info.DeviceType
import com.hanheldpos.printer.printer_setup.interfaces.BluetoothPrinterManager
import com.hanheldpos.printer.printer_setup.interfaces.LanPrinterManager
import com.hanheldpos.printer.printer_setup.interfaces.UrovoPrinterManager
import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager

class Printer private constructor(
    private val printer: BasePrinterManager?,
    val printConfig: PrintConfig,
    val printingSpecification: HardwarePrinter,
) {
    fun isConnected(): Boolean {
        var isConnected = false
        printer?.performPrinterAction {
            isConnected = printer.isConnected()
        }

        return isConnected
    }

    fun openCashDrawer() {
        printer?.performPrinterAction {
            printer.openCashDrawer()
        }
    }

    fun printBill(
        order: OrderModel,
        isReprint: Boolean,
    ) {
        printer?.performPrinterAction {
            printingSpecification.receiptList?.forEach {
                for (i in 0 until (it.quantity ?: 0)) {
                    when (it.receiptTypeId) {
                        PrinterRecipeType.CASHIER.value -> CashierLayout(
                            order, printer, printConfig, isReprint
                        )
                        PrinterRecipeType.KITCHEN.value -> KitchenLayout(
                            order, printer, printConfig, isReprint
                        )
                        else -> CashierLayout(
                            order, printer, printConfig, isReprint
                        )
                    }.print()
                }
            }
        }
    }

    fun printReport(
        layoutType: LayoutType.Report,
        report: ReportSalesResp?,
        filterOptions: SaleReportFilter?,
    ) {
        printer?.performPrinterAction {
            when (layoutType) {
                LayoutType.Report.Inventory -> {
                    InventoryLayout(
                        printer,
                        printConfig,
                        report,
                        filterOptions,
                    )
                }
                LayoutType.Report.Overview -> {
                    OverviewLayout(
                        printer,
                        printConfig,
                        report,
                        filterOptions
                    )
                }
            }.print()
        }
    }


    companion object {
        fun getInstance(printerConfig: HardwarePrinter): Printer {

            // TODO this is automatically assign to LAN connection
            val printConfig: PrintConfig =
                when (printerConfig.connectionList?.firstOrNull { it.isChecked == true }?.connectionTypeId
                    ?: 1) {
                    3 -> PrintConfig.bluetooth(DeviceType.NO_SDK.Types.HANDHELD)
                    4 -> PrintConfig.urovo()
                    else -> {
                        PrintConfig.lan(
                            ipAddress = printerConfig.connectionList?.first { connection -> connection.connectionTypeId == PrinterConnectionTypes.LAN.value }?.port
                                ?: PrintConstants.LAN_ADDRESS,
                            deviceType = DeviceType.NO_SDK.Types.HANDHELD
                        )
                    }
                }



            try {
                var printer: BasePrinterManager? = null
                when (printConfig.deviceType) {
                    is DeviceType.SDK -> {
                        when ((printConfig.deviceType as DeviceType.SDK).type) {
                            DeviceType.SDK.Types.UROVO -> {
                                printer = UrovoPrinterManager()
                                printer.connect()
                            }
                            else -> {}
                        }
                    }
                    is DeviceType.NO_SDK -> {
                        printer = when (printConfig.connectionType) {
                            PrinterConnectionTypes.LAN -> {
                                val policy = StrictMode
                                    .ThreadPolicy
                                    .Builder()
                                    .permitAll()
                                    .build()

                                StrictMode.setThreadPolicy(policy)

                                LanPrinterManager(
                                    ipAddress = printConfig.lanConfig.ipAddress,
                                    port = printConfig.lanConfig.port,
                                    timeout = printConfig.lanConfig.timeOut,
                                    printerDPI = printConfig.deviceInfo.printerDPI(),
                                    printerPaperWidth = printConfig.deviceInfo.paperWidth(),
                                    charsPerLine = printConfig.deviceInfo.charsPerLineNormal()
                                )
                            }
                            PrinterConnectionTypes.BLUETOOTH -> {
                                BluetoothPrinterManager(
                                    context = PosApp.instance.applicationContext,
                                    printerDPI = printConfig.deviceInfo.printerDPI(),
                                    printerPaperWidth = printConfig.deviceInfo.paperWidth(),
                                    charsPerLine = printConfig.deviceInfo.charsPerLineNormal(),
                                )
                            }
                        }
                    }
                }

                return Printer(printer, printConfig, printingSpecification = printerConfig)
            } catch (e: Exception) {
                return Printer(null, printConfig, printingSpecification = printerConfig)
            }
        }
    }
}