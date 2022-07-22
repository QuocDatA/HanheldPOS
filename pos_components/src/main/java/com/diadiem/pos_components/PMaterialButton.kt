package com.diadiem.pos_components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import com.diadiem.pos_components.enumtypes.FontStyleEnum
import com.diadiem.pos_components.enumtypes.TextColorEnum
import com.diadiem.pos_components.enumtypes.TextHeaderEnum
import com.diadiem.pos_config.AppConfigModel
import com.diadiem.pos_config.FontManagement
import com.google.android.material.button.MaterialButton

class PMaterialButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

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
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.PMaterialButton, 0, 0)

        try {

            with(typedArray) {

                //textPrimaryColor from config file
//                getBoolean(
//                    R.styleable.PMaterialButton_pbtn_usePrimaryTextColor,
//                    false
//                ).also {
//                    if (it) {
//                        setTextColor(Color.parseColor(appConfig?.styles?.colors?.onPrimary))
//                    }
//                }

                getInt(R.styleable.PMaterialButton_pbtn_useTextColor, -1).also {
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
                getInt(R.styleable.PMaterialButton_pbtn_useTextHeader, -1).also {
                    when (TextHeaderEnum.fromInt(it)) {
                        TextHeaderEnum.H1 -> {
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX, resources.getDimension(
                                    getDimenSize(
                                        appConfig?.styles?.fonts?.sizes?.h1?.toInt() ?: 0
                                    )
                                )
                            )
                        }
                        TextHeaderEnum.H2 -> {
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX, resources.getDimension(
                                    getDimenSize(
                                        appConfig?.styles?.fonts?.sizes?.h2?.toInt() ?: 0
                                    )
                                )
                            )
                        }
                        TextHeaderEnum.H3 -> {
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX, resources.getDimension(
                                    getDimenSize(
                                        appConfig?.styles?.fonts?.sizes?.h3?.toInt() ?: 0
                                    )
                                )
                            )
                        }
                        TextHeaderEnum.H4 -> {
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX, resources.getDimension(
                                    getDimenSize(
                                        appConfig?.styles?.fonts?.sizes?.h4?.toInt() ?: 0
                                    )
                                )
                            )
                        }
                        TextHeaderEnum.H5 -> {
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX, resources.getDimension(
                                    getDimenSize(
                                        appConfig?.styles?.fonts?.sizes?.h5?.toInt() ?: 0
                                    )
                                )
                            )
                        }
                        TextHeaderEnum.H6 -> {
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX, resources.getDimension(
                                    getDimenSize(
                                        appConfig?.styles?.fonts?.sizes?.h6?.toInt() ?: 0
                                    )
                                )
                            )
                        }
                        TextHeaderEnum.H7 -> {
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX, resources.getDimension(
                                    getDimenSize(
                                        appConfig?.styles?.fonts?.sizes?.h7?.toInt() ?: 0
                                    )
                                )
                            )
                        }
                        TextHeaderEnum.H8 -> {
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX, resources.getDimension(
                                    getDimenSize(
                                        appConfig?.styles?.fonts?.sizes?.h8?.toInt() ?: 0
                                    )
                                )
                            )
                        }
                        else -> {
                        }
                    }
                }

                //set fontFamilies and textStyle from config file
                getInt(R.styleable.PMaterialButton_pbtn_useFontStyle, -1).also {
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
                                appConfig?.styles?.fonts?.families?.italic?.plus(appConfig?.styles?.fonts?.families?.fileType)
                            )
                        }
                        FontStyleEnum.BOLD -> {
                            typeface = FontManagement.getFontFromAssets(
                                context,
                                appConfig?.styles?.fonts?.families?.bold.plus(appConfig?.styles?.fonts?.families?.fileType)
                            )
                        }
                        FontStyleEnum.SEMI_BOLD->{
                            typeface = FontManagement.getFontFromAssets(
                                context,
                                appConfig?.styles?.fonts?.families?.semibold.plus(appConfig?.styles?.fonts?.families?.fileType)
                            )
                        }
                        else -> {
                        }
                    }
                }

                //set background from config file
//                getBoolean(R.styleable.PMaterialButton_pbtn_useBackgroundColor, false).also {
//                    if (it) {
//                        val types = arrayOf(
//                            intArrayOf(android.R.attr.state_enabled),
//                            intArrayOf(-android.R.attr.state_enabled)
//                        )
//
//                        val colors = intArrayOf(
//                            Color.parseColor(appConfig?.styles?.colors?.primary),
//                            Color.parseColor(appConfig?.styles?.colors?.divider),
//                        )
//
//                        backgroundTintList = ColorStateList(types, colors)
//                    }
//                }


            }

//            textSize = appConfig?.styles?.fonts?.sizes?.h3?.toFloat() ?: 0.0F

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    private fun getDimenSize(i: Int): Int {
        val result = when (i) {
            1 -> R.dimen._1ssp
            2 -> R.dimen._2ssp
            3 -> R.dimen._3ssp
            4 -> R.dimen._4ssp
            5 -> R.dimen._5ssp
            6 -> R.dimen._6ssp
            7 -> R.dimen._7ssp
            8 -> R.dimen._8ssp
            9 -> R.dimen._9ssp
            10 -> R.dimen._10ssp
            11 -> R.dimen._11ssp
            12 -> R.dimen._12ssp
            13 -> R.dimen._13ssp
            14 -> R.dimen._14ssp
            15 -> R.dimen._15ssp
            16 -> R.dimen._16ssp
            17 -> R.dimen._17ssp
            18 -> R.dimen._18ssp
            19 -> R.dimen._19ssp
            20 -> R.dimen._20ssp
            21 -> R.dimen._21ssp
            22 -> R.dimen._22ssp
            23 -> R.dimen._23ssp
            24 -> R.dimen._24ssp
            25 -> R.dimen._25ssp
            26 -> R.dimen._26ssp
            27 -> R.dimen._27ssp
            28 -> R.dimen._28ssp
            29 -> R.dimen._29ssp
            30 -> R.dimen._30ssp
            31 -> R.dimen._31ssp
            32 -> R.dimen._32ssp
            33 -> R.dimen._33ssp
            34 -> R.dimen._34ssp
            35 -> R.dimen._35ssp
            36 -> R.dimen._36ssp
            37 -> R.dimen._37ssp
            38 -> R.dimen._38ssp
            39 -> R.dimen._39ssp
            40 -> R.dimen._40ssp
            41 -> R.dimen._41ssp
            42 -> R.dimen._42ssp
            43 -> R.dimen._43ssp
            44 -> R.dimen._44ssp
            45 -> R.dimen._45ssp
            46 -> R.dimen._46ssp
            47 -> R.dimen._47ssp
            48 -> R.dimen._48ssp
            49 -> R.dimen._49ssp
            50 -> R.dimen._50ssp
            51 -> R.dimen._51ssp
            52 -> R.dimen._52ssp
            53 -> R.dimen._53ssp
            54 -> R.dimen._54ssp
            55 -> R.dimen._55ssp
            56 -> R.dimen._56ssp
            57 -> R.dimen._57ssp
            58 -> R.dimen._58ssp
            59 -> R.dimen._59ssp
            60 -> R.dimen._60ssp
            61 -> R.dimen._61ssp
            62 -> R.dimen._62ssp
            63 -> R.dimen._63ssp
            64 -> R.dimen._64ssp
            65 -> R.dimen._65ssp
            66 -> R.dimen._66ssp
            67 -> R.dimen._67ssp
            68 -> R.dimen._68ssp
            69 -> R.dimen._69ssp
            70 -> R.dimen._70ssp
            71 -> R.dimen._71ssp
            72 -> R.dimen._72ssp
            73 -> R.dimen._73ssp
            74 -> R.dimen._74ssp
            75 -> R.dimen._75ssp
            76 -> R.dimen._76ssp
            77 -> R.dimen._77ssp
            78 -> R.dimen._78ssp
            79 -> R.dimen._79ssp
            80 -> R.dimen._80ssp
            81 -> R.dimen._81ssp
            82 -> R.dimen._82ssp
            83 -> R.dimen._83ssp
            84 -> R.dimen._84ssp
            85 -> R.dimen._85ssp
            86 -> R.dimen._86ssp
            87 -> R.dimen._87ssp
            88 -> R.dimen._88ssp
            89 -> R.dimen._89ssp
            90 -> R.dimen._90ssp
            91 -> R.dimen._91ssp
            92 -> R.dimen._92ssp
            93 -> R.dimen._93ssp
            94 -> R.dimen._94ssp
            95 -> R.dimen._95ssp
            96 -> R.dimen._96ssp
            97 -> R.dimen._97ssp
            98 -> R.dimen._98ssp
            99 -> R.dimen._99ssp
            100 -> R.dimen._100ssp
            else -> R.dimen._16ssp
        }

        return result

    }
}