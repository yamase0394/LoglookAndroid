package jp.gr.java_conf.snake0394.loglook_android.view.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import java.util.*


/**
 * Created by raide on 2017/11/10.
 */
class PickDateDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        private val ARG_REQUEST_CODE = "requestCode"
        private val ARG_DEFAULT_YEAR = "defaultYear"
        private val ARG_DEFAULT_MONTH = "defaultMonth"
        private val ARG_DEFAULT_DAY_OF_MONTH = "defaultDayOfMonth"

        fun newInstance(requestCode: Int, defaultYear: Int, defaultMonth: Int, defaultDayOfMonth: Int): DialogFragment {
            val dialog = PickDateDialog()
            val args = Bundle()
            args.putInt(ARG_REQUEST_CODE, requestCode)
            args.putInt(ARG_DEFAULT_YEAR, defaultYear)
            //月が0~11の範囲なので-1する
            args.putInt(ARG_DEFAULT_MONTH, defaultMonth - 1)
            args.putInt(ARG_DEFAULT_DAY_OF_MONTH, defaultDayOfMonth)
            dialog.arguments = args
            return dialog
        }
    }

    private var listener: OnDateSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = arguments.getInt(ARG_DEFAULT_YEAR)
        val month = arguments.getInt(ARG_DEFAULT_MONTH)
        val day = arguments.getInt(ARG_DEFAULT_DAY_OF_MONTH)

        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = targetFragment as OnDateSetListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnDateSetListener")
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener!!.onDateSet(arguments.getInt(ARG_REQUEST_CODE), year, month + 1, dayOfMonth)
    }

    interface OnDateSetListener {
        fun onDateSet(requestCode: Int, year: Int, month: Int, dayOfMonth: Int)
    }
}