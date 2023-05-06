package step.wallet.maganger.ui

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import step.wallet.maganger.R
import java.text.SimpleDateFormat
import java.util.*

class ChartsFragment : Fragment(), DialogFragmentDatePicker.onDateRangeSelectedListene {


    private var periodDayLabel: TextView? = null
    private var periodMonthLabel: TextView? = null
    private var periodYearLabel: TextView? = null
    private var periodCustomLabel: TextView? = null

    private var periodLeftImg: ImageView? = null
    private var periodRightImg: ImageView? = null

    private var radioPeriodGroup: RadioGroup? = null
    private var rbDay: RadioButton? = null
    private var rbMonth: RadioButton? = null
    private var rbYear: RadioButton? = null
    private var rbCustom: RadioButton? = null

    // for filter transaction
    private var startDate: Long? = null
    private var endDate: Long? = null

    private var monthPosition: Int? = Calendar.getInstance().get(Calendar.MONTH)
    private var yearPosition: Int? = Calendar.getInstance().get(Calendar.YEAR)


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


        periodMonthLabel!!.setText(
            resources.getStringArray(R.array.months).get(monthPosition!!) + Calendar.getInstance()
                .get(Calendar.YEAR)
        )

        radioPeriodGroup!!.setOnCheckedChangeListener { group, checkedId ->
            if (R.id.rDay == checkedId) {
                val cal = Calendar.getInstance()
                val cal2 = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                cal.set(Calendar.HOUR, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MINUTE, 0)
                startDate = cal.timeInMillis
                cal2.set(Calendar.HOUR, 23)
                cal2.set(Calendar.MINUTE, 58)
                endDate = cal2.timeInMillis
                periodDayLabel!!.setText(
                    resources.getString(R.string.today) + ", " + dateFormat.format(
                        cal.time
                    )
                )
                setVisibilityLabel(periodDayLabel!!)
            }
            if (R.id.rMonth == checkedId) {
                periodMonthLabel!!.setOnClickListener(View.OnClickListener {
                    val dialog = DialogFragmentDatePicker()
                    dialog.setTargetFragment(this, 1)
                    dialog.setOnDateRangeListener(this)
                    dialog.setValue(monthPosition!!, yearPosition!!)
                    dialog.show(fragmentManager, "DialogFragmentDatePicker")
                })
                setVisibilityLabel(periodMonthLabel!!)
            }
            if (R.id.rYear == checkedId) {
                setVisibilityLabel(periodYearLabel!!)

            }
            if (R.id.rCustom == checkedId) {
                setVisibilityLabel(periodCustomLabel!!)
            }
        }

        periodLeftImg!!.setOnClickListener {
            clickLeft()
        }

        periodRightImg!!.setOnClickListener {
            clickRight()
        }
    }

    private fun findViews(view: View) {
        periodDayLabel = view.findViewById<TextView>(R.id.chPeriodDayLabelTxt)
        periodMonthLabel = view.findViewById<TextView>(R.id.chPeriodMonthLabelTxt)
        periodYearLabel = view.findViewById<TextView>(R.id.chPeriodYearLabelTxt)
        periodCustomLabel = view.findViewById<TextView>(R.id.chPeriodCustomLabelTxt)
        radioPeriodGroup = view.findViewById(R.id.period_button_view)
        rbDay = view.findViewById(R.id.rDay)
        rbMonth = view.findViewById(R.id.rMonth)
        rbYear = view.findViewById(R.id.rYear)
        rbCustom = view.findViewById(R.id.rCustom)
        periodLeftImg = view.findViewById(R.id.chPeriodLeftImg)
        periodRightImg = view.findViewById(R.id.chPeriodRightImg)
    }

    override fun onDateRange(timeStart: Long, timeEnd: Long, monthPosition: Int, year: Int) {
        startDate = timeStart
        endDate = timeEnd
        setVisibilityLabel(periodMonthLabel!!)
        periodMonthLabel!!.setText(
            resources.getStringArray(R.array.months).get(monthPosition) + " " + year
        )
        this.monthPosition = monthPosition
        this.yearPosition = year
    }

    private fun setVisibilityLabel(textView: TextView) {
        periodDayLabel!!.visibility = View.GONE
        periodMonthLabel!!.visibility = View.GONE
        periodYearLabel!!.visibility = View.GONE
        periodCustomLabel!!.visibility = View.GONE
        textView!!.visibility = View.VISIBLE

        if (rbDay!!.isChecked) {
            periodLeftImg!!.visibility = View.VISIBLE
            periodRightImg!!.visibility = View.VISIBLE
        }

        if (rbMonth!!.isChecked) {
            periodLeftImg!!.visibility = View.VISIBLE
            periodRightImg!!.visibility = View.VISIBLE
        }

        if (rbYear!!.isChecked) {
            periodLeftImg!!.visibility = View.VISIBLE
            periodRightImg!!.visibility = View.VISIBLE
        }

        if (rbCustom!!.isChecked) {
            periodLeftImg!!.visibility = View.GONE
            periodRightImg!!.visibility = View.GONE
        }

    }

    fun clickLeft() {
        if (rbDay!!.isChecked) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = startDate!!
            val cal2 = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -1)
            startDate = cal.timeInMillis
            val cal3 = Calendar.getInstance()
            cal3.timeInMillis = endDate!!
            cal3.add(Calendar.DAY_OF_YEAR, -1)
            endDate = cal3.timeInMillis
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
            var day: String? = ""
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                day = resources.getString(R.string.today) + ", "
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) - 1 && cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                day = resources.getString(R.string.yesterday) + ", "
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) + 1 && cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                day = resources.getString(R.string.tomorow) + ", "
            periodDayLabel!!.setText(day + dateFormat.format(cal.time))
            setVisibilityLabel(periodDayLabel!!)
        }

        if (rbMonth!!.isChecked) {

            if (monthPosition == 0) {
                yearPosition = yearPosition!! - 1
                monthPosition = 11
            } else
                monthPosition = monthPosition!! - 1
            periodMonthLabel!!.setText(
                resources.getStringArray(R.array.months).get(monthPosition!!) + " " + yearPosition
            )
            setVisibilityLabel(periodMonthLabel!!)
        }
    }

    fun clickRight() {
        if (rbDay!!.isChecked) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = startDate!!
            val cal2 = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, + 1)
            startDate = cal.timeInMillis
            val cal3 = Calendar.getInstance()
            cal3.timeInMillis = endDate!!
            cal3.add(Calendar.DAY_OF_YEAR, + 1)
            endDate = cal3.timeInMillis
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
            var day: String? = ""
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                day = resources.getString(R.string.today) + ", "
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) - 1 && cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                day = resources.getString(R.string.yesterday) + ", "
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) + 1 && cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                day = resources.getString(R.string.tomorow) + ", "
            periodDayLabel!!.setText(day + dateFormat.format(cal.time))
            setVisibilityLabel(periodDayLabel!!)
        }

        if (rbMonth!!.isChecked) {

            if (monthPosition == 11) {
                yearPosition = yearPosition!! + 1
                monthPosition = 0
            } else
                monthPosition = monthPosition!! + 1

            periodMonthLabel!!.setText(
                resources.getStringArray(R.array.months).get(monthPosition!!) + " " + yearPosition
            )

            setVisibilityLabel(periodMonthLabel!!)
        }
    }

}