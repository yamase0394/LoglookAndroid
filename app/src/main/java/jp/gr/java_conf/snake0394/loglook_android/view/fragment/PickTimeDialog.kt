package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.TimePicker
import jp.gr.java_conf.snake0394.loglook_android.logger.Logger
import java.util.*

/**
 * Created by raide on 2017/11/10.
 */
class PickTimeDialog : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    companion object {
        private val ARG_REQUEST_CODE = "requestCode"
        private val ARG_DEFAULT_HOUR_OF_DAY = "hourOfDay"
        private val ARG_MINUTE = "minute"

        fun newInstance(requestCode: Int, defaultHourOfDay: Int, defaultMinute: Int): DialogFragment {
            val dialog = PickTimeDialog()
            val args = Bundle()
            args.putInt(ARG_REQUEST_CODE, requestCode)
            args.putInt(ARG_DEFAULT_HOUR_OF_DAY, defaultHourOfDay)
            args.putInt(ARG_MINUTE, defaultMinute)
            dialog.arguments = args
            return dialog
        }
    }

    private var listener: OnTimeSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, this, arguments.getInt(ARG_DEFAULT_HOUR_OF_DAY), arguments.getInt(ARG_MINUTE), true)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = targetFragment as OnTimeSetListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnTimeSetListener")
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener!!.onTimeSet(arguments.getInt(ARG_REQUEST_CODE), hourOfDay, minute)
    }

    interface OnTimeSetListener {
        fun onTimeSet(requestCode: Int, hourOfDay: Int, minute: Int)
    }
}