package com.diadiem.pos_components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import com.diadiem.pos_components.enumtypes.FontStyleEnum
import com.diadiem.pos_components.enumtypes.TextColorEnum
import com.diadiem.pos_components.enumtypes.TextHeaderEnum
import com.diadiem.pos_config.AppConfigModel
import com.diadiem.pos_config.FontManagement

class PRadioButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatRadioButton(context, attrs, defStyleAttr) {
    //    initPTextView()
    companion object {

        private var appConfig: AppConfigModel? = null

        fun initConfig(appConfig: AppConfigModel) {
            this.appConfig = appConfig
        }
    }

    init {
        initPTextView(attrs)
    }


    private fun initPTextView(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.PTextView, 0, 0)

        try {

            with(typedArray) {


                getInt(R.styleable.PTextView_useTextColor, -1).also {
                    when (TextColorEnum.fromInt(it)) {
                        TextColorEnum.Color0 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color0))
                        }
                        TextColorEnum.Color1 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color1))
                        }
                        TextColorEnum.Color2 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color2))
                        }
                        TextColorEnum.Color3 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color3))
                        }
                        TextColorEnum.Color4 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color4))
                        }
                        TextColorEnum.Color5 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color5))
                        }
                        TextColorEnum.Color6 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color6))
                        }
                        TextColorEnum.Color7 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color7))
                        }
                        TextColorEnum.Color8 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color8))
                        }
                        TextColorEnum.Color9 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color9))
                        }
                        TextColorEnum.Color10 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color10))
                        }
                        TextColorEnum.Color11 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color11))
                        }
                        TextColorEnum.Color12 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color12))
                        }
                        TextColorEnum.Color13 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color13))
                        }
                        TextColorEnum.Color14 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color14))
                        }
                        TextColorEnum.Color15 -> {
                            setTextColor(Color.parseColor(appConfig?.styles?.colors?.color15))
                        }
                        else -> {
                        }
                    }
                }

                //setTextSize from config file
                getInt(R.styleable.PTextView_useTextHeader, -1).also {
                    when (TextHeaderEnum.fromInt(it)) {
                        TextHeaderEnum.H1 -> {
                            textSize = appConfig?.styles?.fonts?.sizes?.h1?.toFloat() ?: 0.0f
                        }
                        TextHeaderEnum.H2 -> {
                            textSize = appConfig?.styles?.fonts?.sizes?.h2?.toFloat() ?: 0.0f
                        }
                        TextHeaderEnum.H3 -> {
                            textSize = appConfig?.styles?.fonts?.sizes?.h3?.toFloat() ?: 0.0f
                        }
                        TextHeaderEnum.H4 -> {
                            textSize = appConfig?.styles?.fonts?.sizes?.h4?.toFloat() ?: 0.0f
                        }
                        TextHeaderEnum.H5 -> {
                            textSize = appConfig?.styles?.fonts?.sizes?.h5?.toFloat() ?: 0.0f
                        }
                        TextHeaderEnum.H6 -> {
                            textSize = appConfig?.styles?.fonts?.sizes?.h6?.toFloat() ?: 0.0f
                        }
                        TextHeaderEnum.H7 -> {
                            textSize = appConfig?.styles?.fonts?.sizes?.h7?.toFloat() ?: 0.0f
                        }
                        else -> {
                        }
                    }
                }

                //set fontFamilies and textStyle from config file
                getInt(R.styleable.PTextView_useFontStyle, -1).also {
                    when (FontStyleEnum.fromInt(it)) {
                        FontStyleEnum.NORMAL -> {
                            typeface = FontManagement.getFontFromAssets(
                                context,
                                appConfig?.styles?.fonts?.families?.normal.plus(appConfig?.styles?.fonts?.families?.fileType)
                            )
                        }
                        FontStyleEnum.ITALIC -> {
                            typeface = FontManagement.getFontFromAssets(
                                context,
                                appConfig?.styles?.fonts?.families?.italic.plus(appConfig?.styles?.fonts?.families?.fileType)
                            )
                        }
                        FontStyleEnum.BOLD -> {
                            typeface = FontManagement.getFontFromAssets(
                                context,
                                appConfig?.styles?.fonts?.families?.bold.plus(appConfig?.styles?.fonts?.families?.fileType)
                            )
                        }
                        else -> {
                        }
                    }
                }


            }

//            textSize = appConfig?.styles?.fonts?.sizes?.h3?.toFloat() ?: 0.0F

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }


}