package step.wallet.maganger.ui


import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import step.wallet.maganger.R
import step.wallet.maganger.adapters.RecyclerViewMonthAdapter
import java.util.*

class DialogFragmentDatePicker : DialogFragment() {

    private var rVMonth: RecyclerView? = null
    private var monthAdapter: RecyclerViewMonthAdapter? = null
    private var yearTxt: TextView? = null
    private var monthTxt: TextView? = null
    private var cancelTxt: TextView? = null
    private var saveTxt: TextView? = null
    private var monthLeftImg: ImageView? = null
    private var monthRighttImg: ImageView? = null

    private var selectedMonth: String? = null
    private var selectedMonthInt: Int? = null
    private var selectedYearInt: Int? = null
    private val currentMonthInt = Calendar.getInstance().get(Calendar.MONTH)
    private var monthList: Array<String>? = null

    private lateinit var mDateListener: onDateRangeSelectedListene

    fun setValue(month: Int, year: Int) {
        selectedMonthInt = month
        selectedYearInt = year
    }

    interface onDateRangeSelectedListene {
        fun onDateRange(timeStart: Long, timeEnd: Long, monthPosition: Int, year: Int)
    }

    fun setOnDateRangeListener (listener: onDateRangeSelectedListene) {
        mDateListener = listener
    }


    override fun onStart() {
        super.onStart()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.dialog_date_picker, container, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        findViews(view)

        initialItems()

        loadMonths(Calendar.getInstance().get(Calendar.MONTH))

        return view
    }

    private fun initialItems() {
        monthList = resources.getStringArray(R.array.months)
        selectedMonth =
            resources.getStringArray(R.array.months).get(selectedMonthInt!!)
//        selectedMonthInt = monthList!!.indexOf(selectedMonth)
        yearTxt?.setText("" + selectedYearInt)
//        monthRighttImg!!.visibility = View.GONE
        monthRighttImg!!.setOnClickListener {
            yearTxt!!.setText("" + (yearTxt!!.text.toString().toInt() + 1))
            monthTxt!!.setText(selectedMonth + " " + yearTxt!!.text.toString())
            if (yearTxt!!.text.equals((Calendar.getInstance().get(Calendar.YEAR) + 3).toString())) {
                monthRighttImg!!.visibility = View.GONE
            }
            monthLeftImg!!.visibility = View.VISIBLE
            loadMonths(monthList!!.indexOf(selectedMonth))
        }
        monthLeftImg!!.setOnClickListener {
            yearTxt!!.setText("" + (yearTxt!!.text.toString().toInt() - 1))
            monthTxt!!.setText(selectedMonth + " " + yearTxt!!.text.toString())
            monthRighttImg!!.visibility = View.VISIBLE
            if (yearTxt!!.text.equals("2014")) {
                monthLeftImg!!.visibility = View.GONE
            }
            loadMonths(monthList!!.indexOf(selectedMonth))
        }
    }

    private fun loadMonths(selectedMonthPosiotion: Int) {
        var myArray = resources.getStringArray(R.array.months)
        val layoutManager = GridLayoutManager(context, 3)
        rVMonth!!.layoutManager = layoutManager
        monthAdapter =
            RecyclerViewMonthAdapter(myArray, context, selectedMonthInt!!)
        // on below line we are setting
        // adapter to our recycler view.
        rVMonth!!.adapter = monthAdapter
        monthAdapter!!.setOnItemClickListener(object :
            RecyclerViewMonthAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                selectedMonth = myArray.get(position)
                selectedMonthInt = position
                monthTxt!!.setText(selectedMonth + " " + yearTxt!!.text.toString())
            }
        })
        cancelTxt!!.setOnClickListener {
            dialog.dismiss()
        }

        saveTxt!!.setOnClickListener {
            var cal = Calendar.getInstance()
            var cal2 = Calendar.getInstance()
            var year = yearTxt!!.text.toString().toInt()
            var month = selectedMonthInt!!

            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)


            cal2.set(Calendar.YEAR, year)
            cal2.set(Calendar.MONTH, month)
            cal2.add(Calendar.MONTH, 1)
            cal2.set(Calendar.DAY_OF_MONTH, 1)
            cal2.add(Calendar.DAY_OF_MONTH, -1)
            cal2.set(Calendar.HOUR, 0)
            cal2.set(Calendar.MINUTE, 0)
            cal2.set(Calendar.SECOND, 0)

            mDateListener.onDateRange(cal.timeInMillis, cal2.timeInMillis, month, year)

            dialog.dismiss()
        }
    }

    private fun findViews(view: View) {
        rVMonth = view.findViewById(R.id.dFr_date_month_rv)
        yearTxt = view.findViewById(R.id.dFrDatePickerYear)
        monthTxt = view.findViewById(R.id.dFrDatePickerMonth)
        monthLeftImg = view.findViewById(R.id.dFrDatePickerYearLeft)
        monthRighttImg = view.findViewById(R.id.dFrDatePickerYearRight)
        cancelTxt = view.findViewById(R.id.dFrDatePickerCancel)
        saveTxt = view.findViewById(R.id.dFrDatePickerSave)
    }

    companion object {
        const val TAG = "DialogFragmentDatePicker"
    }

}