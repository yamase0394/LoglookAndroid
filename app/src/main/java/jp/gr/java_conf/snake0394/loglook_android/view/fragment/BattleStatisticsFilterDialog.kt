package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import jp.gr.java_conf.snake0394.loglook_android.R
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
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

    private var listener: OnFilterChangedListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)

        val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_filter_battle_statistics, null)

        rootView.apply {
            val sdf = SimpleDateFormat("yyy-MM-dd")
            val date = Date()
            text_from_date.text = sdf.format(date)
            text_to_date.text = sdf.format(date)

            //今月を初期範囲に指定
            radio_group.check(R.id.radio_this_month)

            layout_select_from_to_date.visibility = View.GONE
            radio_select_span.setOnCheckedChangeListener { _, isChecked ->
                layout_select_from_to_date.visibility = if (isChecked) View.VISIBLE else View.GONE
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
                    var toDate: Date? = null
                    var fromDate: Date? = null
                    var checkedText: String? = null

                    when (rootView.radio_group.checkedRadioButtonId) {
                        R.id.radio_this_month -> {
                            toDate = Date()
                            fromDate = org.apache.commons.lang3.time.DateUtils.truncate(toDate, Calendar.MONTH)
                            checkedText = "今月"
                        }
                        R.id.radio_last_month -> {
                            val calendar = Calendar.getInstance()
                            calendar.add(Calendar.MONTH, -1)
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE))

                            toDate = calendar.time
                            fromDate = org.apache.commons.lang3.time.DateUtils.truncate(toDate, Calendar.MONTH)
                            checkedText = "先月"
                        }
                        R.id.radio_all_span -> {
                            fromDate = Date(Long.MIN_VALUE)
                            toDate = Date(Long.MAX_VALUE)
                            checkedText = "全期間"
                        }
                        R.id.radio_select_span -> {
                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
                            fromDate = sdf.parse("${rootView.text_from_date.text} ${rootView.text_from_time.text}")
                            toDate = sdf.parse("${rootView.text_to_date.text} ${rootView.text_to_time.text}")
                        }
                    }

                    listener!!.onFilterChanged(fromDate!!, toDate!!, checkedText)
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

    companion object {
        private val REQ_CODE_FROM_DATE = 1
        private val REQ_CODE_FROM_TIME = 2
        private val REQ_CODE_TO_DATE = 3
        private val REQ_CODE_TO_TIME = 4

        fun newInstance(): BattleStatisticsFilterDialog = BattleStatisticsFilterDialog()
    }

    interface OnFilterChangedListener {
        fun onFilterChanged(dateFrom: Date, dateTo: Date, checked: String?)
    }
}