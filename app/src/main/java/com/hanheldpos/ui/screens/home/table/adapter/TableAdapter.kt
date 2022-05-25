package com.hanheldpos.ui.screens.home.table.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.floor.FloorTable
import com.hanheldpos.databinding.ItemTableBinding
import com.hanheldpos.databinding.ItemTableDirectionButtonBinding
import com.hanheldpos.databinding.ItemTableEmptyBinding
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.model.home.table.TableStatusType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.utils.DateTimeUtils
import java.util.*
import java.util.concurrent.TimeUnit

class TableAdapter(
    private val listener: BaseItemClickListener<FloorTable>
) : BaseBindingListAdapter<FloorTable>(DiffCallBack(), listener) {

    lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).uiType) {
            TableModeViewType.Table -> R.layout.item_table
            TableModeViewType.Empty -> R.layout.item_table_empty
            else -> {
                R.layout.item_table_direction_button
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<FloorTable> {

        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent, false
        ).also {
            //Log.d("TableAdapter","RecycleView Height:" + parent.height);
            val height = ((parent.height) / 5) - parent.resources.getDimension(R.dimen._6sdp);
            val params: FrameLayout.LayoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height.toInt());
            when (viewType) {
                R.layout.item_table -> (it as ItemTableBinding).layoutMain.layoutParams = params;
                R.layout.item_table_direction_button -> (it as ItemTableDirectionButtonBinding).layoutMain.layoutParams =
                    params
                else -> {
                    (it as ItemTableEmptyBinding).layoutMain.layoutParams = params
                }
            }
            this.context = parent.context

            return BaseBindingViewHolder(it, listener)
        }
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<FloorTable>, position: Int) {
        val item = getItem(position);

        //Color for the Relative Layout Background
        val availableColor = Color.parseColor("#EEEEEE")
        val unavailableColor = Color.parseColor("#007EFF")
        val pendingColor = Color.parseColor("#FBD536")
        if (item.uiType != TableModeViewType.Empty) {
            when (item.tableStatus) {
                TableStatusType.Available -> {
                    when (item.uiType) {
                        TableModeViewType.Table -> {
                            val view = holder.binding as ItemTableBinding
                            view.tableItemBackground.setBackgroundColor(
                                availableColor
                            )
                        }
                        else -> {
                            val dirButtonView = holder.binding as ItemTableDirectionButtonBinding
                            dirButtonView.tableItemBackground.setBackgroundColor(
                                availableColor
                            )
                        }
                    }
                }
                TableStatusType.Pending -> {
                    when (item.uiType) {
                        TableModeViewType.Table -> {
                            val view = holder.binding as ItemTableBinding
                            view.tableItemBackground.setBackgroundColor(
                                pendingColor
                            )
                        }
                        else -> {
                            val dirButtonView = holder.binding as ItemTableDirectionButtonBinding
                            dirButtonView.tableItemBackground.setBackgroundColor(
                                pendingColor
                            )
                        }
                    }
                }
                TableStatusType.Unavailable -> {
                    when (item.uiType) {
                        TableModeViewType.Table -> {
                            val view = holder.binding as ItemTableBinding
                            view.tableItemBackground.setBackgroundColor(
                                unavailableColor
                            )
                            DateTimeUtils.strToDate(
                                item.orderSummary!!.OrderCreateDate,
                                DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE
                            )?.let {
                                val time = Date().time  - it.time
                                view.timeCount.text = String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toHours(time),
                                    TimeUnit.MILLISECONDS.toMinutes(time) -
                                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time))
                                );

                            }

                        }
                        else -> {
                            val dirButtonView = holder.binding as ItemTableDirectionButtonBinding
                            dirButtonView.tableItemBackground.setBackgroundColor(
                                unavailableColor
                            )
                        }
                    }
                }
            }
        }

        //Set Image For the DirButton
        when (item.uiType) {
            TableModeViewType.NextButtonEnable -> {
                val view = holder.binding as ItemTableDirectionButtonBinding
                view.dirButton.setImageResource(R.drawable.ic_direction_down)
                view.dirButtonBackground.setRippleColorResource(R.color.color_2)
            }
            TableModeViewType.PrevButtonEnable -> {
                val view = holder.binding as ItemTableDirectionButtonBinding
                view.dirButton.setImageResource(R.drawable.ic_direction_up)
                view.dirButtonBackground.setRippleColorResource(R.color.color_2)
            }
            TableModeViewType.NextButtonDisable -> {
                val view = holder.binding as ItemTableDirectionButtonBinding
                view.dirButton.setImageResource(R.drawable.ic_direction_down_disable)
                view.dirButtonBackground.rippleColor = ColorStateList.valueOf(TRANSPARENT)
            }
            TableModeViewType.PrevButtonDisable -> {
                val view = holder.binding as ItemTableDirectionButtonBinding
                view.dirButton.setImageResource(R.drawable.ic_direction_up_disable)
                view.dirButtonBackground.rippleColor = ColorStateList.valueOf(TRANSPARENT)
            }
        }
        holder.bindItem(item);
    }

    private class DiffCallBack : DiffUtil.ItemCallback<FloorTable>() {
        override fun areItemsTheSame(oldItem: FloorTable, newItem: FloorTable): Boolean {
            return oldItem._Id.equals(newItem._Id) && ((oldItem.uiType == newItem.uiType) || (oldItem.uiType != TableModeViewType.Table && oldItem.uiType != TableModeViewType.Empty && newItem.uiType != TableModeViewType.Table && newItem.uiType != TableModeViewType.Empty))
        }

        override fun areContentsTheSame(oldItem: FloorTable, newItem: FloorTable): Boolean {
            return oldItem == newItem && (oldItem.uiType == newItem.uiType)
        }

    }
}