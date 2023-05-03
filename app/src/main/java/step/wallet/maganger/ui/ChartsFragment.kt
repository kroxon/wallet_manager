package step.wallet.maganger.ui

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kal.rackmonthpicker.RackMonthPicker
import step.wallet.maganger.R

class ChartsFragment : Fragment() {

    private var testDatePickerImg: ImageView? = null

    private var periodMonthTxt: TextView? = null
    private var periodLabel: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_charts, container, false)

        findViews(view)

        initItems()



        return view
    }

    private fun initItems() {
        testDatePickerImg!!.setOnClickListener(View.OnClickListener {
            val dialog = DialogFragmentDatePicker()
            dialog.setTargetFragment(this, 1)
            dialog.show(fragmentManager, "DialogFragmentDatePicker")
        })
        periodMonthTxt!!.setOnClickListener(View.OnClickListener {
            RackMonthPicker(context)
                .setPositiveButton { month, startDate, endDate, year, monthLabel ->
                    periodLabel!!.setText(
                        monthLabel
                    )
                }
                .setNegativeButton { }.show()
        })
    }

    private fun findViews(view: View) {
        testDatePickerImg = view.findViewById<ImageView>(R.id.chAddTransactionImg)
        periodMonthTxt = view.findViewById<TextView>(R.id.chPeriodMonthTxt)
        periodLabel = view.findViewById<TextView>(R.id.chPeriodLabelTxt)
    }

}