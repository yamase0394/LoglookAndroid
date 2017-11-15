package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import jp.gr.java_conf.snake0394.loglook_android.R
import kotlinx.android.synthetic.main.dialog_filter_battle_statistics.*
import kotlinx.android.synthetic.main.dialog_filter_battle_statistics.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by raide on 2017/11/10.
 */
class BattleStatisticsFilterDialog : android.support.v4.app.DialogFragment(),
        PickDateDialog.OnDateSetListener,
        PickTimeDialog.OnTimeSetListener {

    companion object {
        private val REQ_CODE_FROM_DATE = 1
        private val REQ_CODE_FROM_TIME = 2
        private val REQ_CODE_TO_DATE = 3
        private val REQ_CODE_TO_TIME = 4

        private val ARG_DEFAULT_DATE_FILTER = "defaultDateFilter"
        private val ARG_DEFAULT_FROM_DATE = "defaultFromDate"
        private val ARG_DEFAULT_FROM_TIME = "defaultFromTime"
        private val ARG_DEFAULT_TO_DATE = "defaultToDate"
        private val ARG_DEFAULT_TO_TIME = "defaultToTime"

        val DATE_FILTER_THIS_MONTH = "今月"
        val DATE_FILTER_LAST_MONTH = "先月"
        val DATE_FILTER_ALL = "全期間"
        val DATE_FILTER_SELECT = "期間を指定"

        fun newInstance(defaultDateFilter: String, defaultFromDate: Date? = null, defaultToDate: Date? = null): BattleStatisticsFilterDialog {
            val dialog = BattleStatisticsFilterDialog()
            val args = Bundle()
            args.putString(ARG_DEFAULT_DATE_FILTER, defaultDateFilter)

            val sdfDate = SimpleDateFormat("yyyy-MM-dd")
            val sdfTime = SimpleDateFormat("HH:mm")
            if (defaultFromDate == null) {
                args.putString(ARG_DEFAULT_FROM_DATE, sdfDate.format(Date()))
                args.putString(ARG_DEFAULT_FROM_TIME, "00:00")
            } else {
                args.putString(ARG_DEFAULT_FROM_DATE, sdfDate.format(defaultFromDate))
                args.putString(ARG_DEFAULT_FROM_TIME, sdfTime.format(defaultFromDate))
            }
            if (defaultToDate == null) {
                args.putString(ARG_DEFAULT_TO_DATE, sdfDate.format(Date()))
                args.putString(ARG_DEFAULT_TO_TIME, "23:59")
            } else {
                args.putString(ARG_DEFAULT_TO_DATE, sdfDate.format(defaultToDate))
                args.putString(ARG_DEFAULT_TO_TIME, sdfTime.format(defaultToDate))
            }

            dialog.arguments = args
            return dialog
        }
    }

    interface OnFilterChangedListener {
        fun onFilterChanged(dateFrom: Date, dateTo: Date, checked: String)
    }

    private var listener: OnFilterChangedListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)

        val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_filter_battle_statistics, null)

        rootView.apply {
            text_from_date.text = arguments.getString(ARG_DEFAULT_FROM_DATE)
            text_from_time.text = arguments.getString(ARG_DEFAULT_FROM_TIME)
            text_to_date.text = arguments.getString(ARG_DEFAULT_TO_DATE)
            text_to_time.text = arguments.getString(ARG_DEFAULT_TO_TIME)

            layout_select_from_to_date.visibility = View.GONE
            radio_select_span.setOnCheckedChangeListener { _, isChecked ->
                layout_select_from_to_date.visibility = if (isChecked) View.VISIBLE else View.GONE
            }

            when (arguments.getString(ARG_DEFAULT_DATE_FILTER)) {
                DATE_FILTER_THIS_MONTH -> radio_this_month.isChecked = true
                DATE_FILTER_LAST_MONTH -> radio_last_month.isChecked = true
                DATE_FILTER_ALL -> radio_all_span.isChecked = true
                DATE_FILTER_SELECT -> radio_select_span.isChecked = true
                else -> throw IllegalArgumentException("$ARG_DEFAULT_DATE_FILTER=${arguments.getString(ARG_DEFAULT_DATE_FILTER)}")
            }

            btn_select_from_date.setOnClickListener {
                val split = text_from_date.text.split("-")
                val dialog = PickDateDialog.newInstance(REQ_CODE_FROM_DATE, split[0].toInt(), split[1].toInt(), split[2].toInt())
                dialog.setTargetFragment(this@BattleStatisticsFilterDialog, 0)
                dialog.show(fragmentManager, "dialog")
            }

            btn_select_from_time.setOnClickListener {
                val split = text_from_time.text.split(":")
                val dialog = PickTimeDialog.newInstance(REQ_CODE_FROM_TIME, split[0].toInt(), split[1].toInt())
                dialog.setTargetFragment(this@BattleStatisticsFilterDialog, 0)
                dialog.show(fragmentManager, "dialog")
            }

            btn_select_to_date.setOnClickListener {
                val split = text_to_date.text.split("-")
                val dialog = PickDateDialog.newInstance(REQ_CODE_TO_DATE, split[0].toInt(), split[1].toInt(), split[2].toInt())
                dialog.setTargetFragment(this@BattleStatisticsFilterDialog, 0)
                dialog.show(fragmentManager, "dialog")
            }

            btn_select_to_time.setOnClickListener {
                val split = text_to_time.text.split(":")
                val dialog = PickTimeDialog.newInstance(REQ_CODE_TO_TIME, split[0].replaceFirst("^0+", "").toInt(), split[1].replaceFirst("^0+", "").toInt())
                dialog.setTargetFragment(this@BattleStatisticsFilterDialog, 0)
                dialog.show(fragmentManager, "dialog")
            }
        }

        return builder.setView(rootView)
                .setTitle("フィルター")
                .setPositiveButton("設定") { _, _ ->
                    val toDate: Date
                    val fromDate: Date
                    val dateFilter: String
                    when (rootView.radio_group.checkedRadioButtonId) {
                        R.id.radio_this_month -> {
                            toDate = Date()
                            fromDate = org.apache.commons.lang3.time.DateUtils.truncate(toDate, Calendar.MONTH)
                            dateFilter = DATE_FILTER_THIS_MONTH
                        }
                        R.id.radio_last_month -> {
                            val calendar = Calendar.getInstance()
                            calendar.add(Calendar.MONTH, -1)
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE))

                            toDate = calendar.time
                            fromDate = org.apache.commons.lang3.time.DateUtils.truncate(toDate, Calendar.MONTH)
                            dateFilter = DATE_FILTER_LAST_MONTH
                        }
                        R.id.radio_all_span -> {
                            fromDate = Date(Long.MIN_VALUE)
                            toDate = Date(Long.MAX_VALUE)
                            dateFilter = DATE_FILTER_ALL
                        }
                        R.id.radio_select_span -> {
                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
                            fromDate = sdf.parse("${rootView.text_from_date.text} ${rootView.text_from_time.text}")
                            toDate = sdf.parse("${rootView.text_to_date.text} ${rootView.text_to_time.text}")
                            dateFilter = DATE_FILTER_SELECT
                        }
                        else -> throw IllegalStateException("unknown resource id = ${radio_group.checkedRadioButtonId}")
                    }

                    listener!!.onFilterChanged(fromDate, toDate, dateFilter)
                }
                .setNegativeButton("キャンセル", null)
                .create()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            this.listener = targetFragment as OnFilterChangedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnFilterChangedListener")
        }
    }

    override fun onDateSet(requestCode: Int, year: Int, month: Int, dayOfMonth: Int) {
        when (requestCode) {
            REQ_CODE_FROM_DATE -> dialog.text_from_date.text = "$year-$month-$dayOfMonth"
            REQ_CODE_TO_DATE -> dialog.text_to_date.text = "$year-$month-$dayOfMonth"
        }
    }

    override fun onTimeSet(requestCode: Int, hourOfDay: Int, minute: Int) {
        when (requestCode) {
            REQ_CODE_FROM_TIME -> dialog.text_from_time.text = String.format("%02d:%02d", hourOfDay, minute)
            REQ_CODE_TO_TIME -> dialog.text_to_time.text = String.format("%02d:%02d", hourOfDay, minute)
        }
    }
}