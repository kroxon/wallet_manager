package step.wallet.maganger.ui


import android.app.DialogFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
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
    private var monthLeftImg: ImageView? = null
    private var monthRighttImg: ImageView? = null

    private var selectedMonth: String? = null


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

        loadMonths()

        return view
    }

    private fun initialItems() {
        selectedMonth = resources.getStringArray(R.array.months).get(Calendar.getInstance().get(Calendar.MONTH))
        yearTxt?.setText((Calendar.getInstance().get(Calendar.YEAR)).toString())
        monthRighttImg!!.visibility = View.GONE
        monthRighttImg!!.setOnClickListener {
            yearTxt!!.setText("" + (yearTxt!!.text.toString().toInt() + 1))
            monthTxt!!.setText(selectedMonth + " " + yearTxt!!.text.toString())
            if (yearTxt!!.text.equals((Calendar.getInstance().get(Calendar.YEAR)).toString()))
                monthRighttImg!!.visibility = View.GONE
        }
        monthLeftImg!!.setOnClickListener {
            yearTxt!!.setText("" + (yearTxt!!.text.toString().toInt() - 1))
            monthTxt!!.setText(selectedMonth + " " + yearTxt!!.text.toString())
            monthRighttImg!!.visibility = View.VISIBLE
        }
    }

    private fun loadMonths() {
        val myArray = resources.getStringArray(R.array.months)
        val layoutManager = GridLayoutManager(context, 3)
        rVMonth!!.layoutManager = layoutManager
        monthAdapter = RecyclerViewMonthAdapter(myArray, context)
        // on below line we are setting
        // adapter to our recycler view.
        rVMonth!!.adapter = monthAdapter
        monthAdapter!!.setOnItemClickListener(object :
            RecyclerViewMonthAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                selectedMonth = myArray.get(position)
                monthTxt!!.setText(selectedMonth + " " + yearTxt!!.text.toString())
            }
        })

    }

    private fun findViews(view: View) {
        rVMonth = view.findViewById(R.id.dFr_date_month_rv)
        yearTxt = view.findViewById(R.id.dFrDatePickerYear)
        monthTxt = view.findViewById(R.id.dFrDatePickerMonth)
        monthLeftImg = view.findViewById(R.id.dFrDatePickerYearLeft)
        monthRighttImg = view.findViewById(R.id.dFrDatePickerYearRight)
    }

    companion object {
        const val TAG = "DialogFragmentDatePicker"
    }

}