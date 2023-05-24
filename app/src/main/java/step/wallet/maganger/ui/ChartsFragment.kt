package step.wallet.maganger.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.Fragment
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import step.wallet.maganger.R
import step.wallet.maganger.adapters.RecyclerViewPieChartAdapter
import step.wallet.maganger.adapters.RecyclerViewPieChartDetailAdapter
import step.wallet.maganger.classes.Quad
import step.wallet.maganger.classes.Transaction
import step.wallet.maganger.data.CurrencyDatabase
import step.wallet.maganger.data.InfoRepository
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ChartsFragment : Fragment(), DialogFragmentDatePicker.onDateRangeSelectedListene {


    private var accountTxt: TextView? = null
    private var expenseTxt: TextView? = null
    private var incomeTxt: TextView? = null
    private var periodDayLabel: TextView? = null
    private var periodMonthLabel: TextView? = null
    private var periodYearLabel: TextView? = null
    private var periodCustomLabel1: TextView? = null
    private var periodCustomLabel2: TextView? = null
    private var periodCustomLabel: LinearLayout? = null

    private var periodLeftImg: ImageView? = null
    private var periodRightImg: ImageView? = null

    private var radioPeriodGroup: RadioGroup? = null
    private var rbDay: RadioButton? = null
    private var rbMonth: RadioButton? = null
    private var rbYear: RadioButton? = null
    private var rbCustom: RadioButton? = null

    private var scrollView: NestedScrollView? = null

    // for filter transaction
    private var startDate: Long? = null
    private var endDate: Long? = null
    private var selectedIdAccount: Int? = null
    private var currencySymbol: String? = null

    private var monthPosition: Int? = null
    private var yearPosition: Int? = null

    // charts
    private var pieChart: PieChart? = null
    lateinit var categoriesRVAdapter: RecyclerViewPieChartAdapter
    lateinit var recyclerViewCategories: RecyclerView
    lateinit var categoriesDetailRVAdapter: RecyclerViewPieChartDetailAdapter
    lateinit var recyclerViewCategoriesDetal: RecyclerView
    private var centerSum: TextView? = null
    private var barChart: BarChart? = null
    lateinit var barEntriesList: ArrayList<BarEntry>
    lateinit var xAxisLabels: ArrayList<String>
    private var pieChartLayout: LinearLayout? = null
    private var detailLegenLayout: ConstraintLayout? = null

    // testing
    private var btnTest: Button? = null


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

        if (expenseTxt!!.currentTextColor.equals(resources.getColor(R.color.white)))
            loadChartAndLegend("expense")
        else
            loadChartAndLegend("income")


        return view
    }

    private fun initItems() {

        initStartAndEndDate()


        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        periodMonthLabel!!.setText(
            sdf.format(Calendar.getInstance().time)
//            resources.getStringArray(R.array.months)
//                .get(monthPosition!!) + " " + Calendar.getInstance()
//                .get(Calendar.YEAR)
        )

        radioPeriodGroup!!.setOnCheckedChangeListener { group, checkedId ->
            if (R.id.rDay == checkedId) {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                startDate = cal.timeInMillis
                val cal2 = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                cal2.set(Calendar.HOUR_OF_DAY, 23)
                cal2.set(Calendar.MINUTE, 59)
                cal2.set(Calendar.SECOND, 59)
                endDate = cal2.timeInMillis
                periodDayLabel!!.setText(
                    resources.getString(R.string.today) + ", " + dateFormat.format(
                        cal.time
                    )
                )
                setVisibilityLabel(periodDayLabel!!)
            }
            if (R.id.rMonth == checkedId) {
                val cal = Calendar.getInstance()
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                startDate = cal.timeInMillis
                val cal2 = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                cal2.set(Calendar.DAY_OF_MONTH, 1)
                cal2.set(Calendar.HOUR_OF_DAY, 23)
                cal2.set(Calendar.MINUTE, 59)
                cal2.set(Calendar.SECOND, 59)
                cal2.add(Calendar.MONTH, +1)
                cal2.add(Calendar.DAY_OF_YEAR, -1)
                endDate = cal2.timeInMillis
                periodMonthLabel!!.setOnClickListener(View.OnClickListener {
                    val dialog = DialogFragmentDatePicker()
////                    dialog.setTargetFragment(this, 1)
                    dialog.setOnDateRangeListener(this)
                    val calendar = Calendar.getInstance()
                    dialog.setValue(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
////                    dialog.show(fragmentManager, "DialogFragmentDatePicker")
//                    fragmentManager?.let { dialog.show(it, "DialogFragmentDatePicker") }

                    val fm = this@ChartsFragment.fragmentManager
                    dialog.show(fragmentManager, "DialogFragmentDatePicker")
//                    dialog.show(childFragmentManager,"DialogFragmentDatePicker")

                })
                setVisibilityLabel(periodMonthLabel!!)
            }
            if (R.id.rYear == checkedId) {
                val cal = Calendar.getInstance()
                val cal2 = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                cal.set(Calendar.DAY_OF_YEAR, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MINUTE, 0)
                startDate = cal.timeInMillis
                cal2.add(Calendar.YEAR, +1)
                cal2.set(Calendar.DAY_OF_YEAR, 0)
                cal2.set(Calendar.HOUR_OF_DAY, 23)
                cal2.set(Calendar.MINUTE, 59)
                cal2.set(Calendar.SECOND, 59)
                endDate = cal2.timeInMillis
                periodYearLabel!!.setText(dateFormat.format(cal.time))
                setVisibilityLabel(periodYearLabel!!)
            }
            if (R.id.rCustom == checkedId) {
                var cal = Calendar.getInstance()
                var cal2 = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.add(Calendar.MONTH, -1)
                startDate = cal.timeInMillis
                cal2.set(Calendar.HOUR_OF_DAY, 23)
                cal2.set(Calendar.MINUTE, 59)
                cal2.set(Calendar.SECOND, 59)
                endDate = cal2.timeInMillis
                periodCustomLabel1!!.setText(dateFormat.format(cal.time))
                periodCustomLabel2!!.setText(dateFormat.format(cal2.time))
                setVisibilityLabel(periodCustomLabel!!)
            }
            if (expenseTxt!!.currentTextColor.equals(resources.getColor(R.color.white)))
                loadChartAndLegend("expense")
            else
                loadChartAndLegend("income")
        }

        rbDay!!.performClick()
        rbMonth!!.performClick()
//        initStartAndEndDate()


        periodLeftImg!!.setOnClickListener {
            clickLeft()
        }

        periodRightImg!!.setOnClickListener {
            clickRight()
        }

        periodDayLabel!!.setOnClickListener {
            selectDay()
        }

        periodCustomLabel1!!.setOnClickListener {
            selectCustomDate(periodCustomLabel1!!, "start")
            if (expenseTxt!!.currentTextColor.equals(resources.getColor(R.color.white)))
                loadChartAndLegend("expense")
            else
                loadChartAndLegend("income")
        }

        periodCustomLabel2!!.setOnClickListener {
            selectCustomDate(periodCustomLabel2!!, "end")
            if (expenseTxt!!.currentTextColor.equals(resources.getColor(R.color.white)))
                loadChartAndLegend("expense")
            else
                loadChartAndLegend("income")
        }


        accountTxt?.setOnClickListener {
            selectAccount()
        }


        scrollView?.isNestedScrollingEnabled = false

        // testing
        btnTest!!.setOnClickListener {
            loadDetailsLegen()
        }

        expenseTxt!!.setOnClickListener {
            expenseClick()
        }

        incomeTxt!!.setOnClickListener {
            incomeClick()
        }
    }

    private fun findViews(view: View) {
        periodDayLabel = view.findViewById<TextView>(R.id.chPeriodDayLabelTxt)
        periodMonthLabel = view.findViewById<TextView>(R.id.chPeriodMonthLabelTxt)
        periodYearLabel = view.findViewById<TextView>(R.id.chPeriodYearLabelTxt)
        periodCustomLabel = view.findViewById<LinearLayout>(R.id.chPeriodCustomLabelTxt)
        periodCustomLabel1 = view.findViewById<TextView>(R.id.chPeriodCustomLabelTxt1)
        periodCustomLabel2 = view.findViewById<TextView>(R.id.chPeriodCustomLabelTxt2)
        radioPeriodGroup = view.findViewById(R.id.period_button_view)
        rbDay = view.findViewById(R.id.rDay)
        rbMonth = view.findViewById(R.id.rMonth)
        rbYear = view.findViewById(R.id.rYear)
        rbCustom = view.findViewById(R.id.rCustom)
        periodLeftImg = view.findViewById(R.id.chPeriodLeftImg)
        periodRightImg = view.findViewById(R.id.chPeriodRightImg)
        pieChart = view.findViewById(R.id.pieChart)
        recyclerViewCategories = view.findViewById(R.id.categoryLegenRV)
        accountTxt = view.findViewById(R.id.chartFrAccountTxt)
        expenseTxt = view.findViewById(R.id.expensesChartTxt)
        incomeTxt = view.findViewById(R.id.incomeChartTxt)
        recyclerViewCategoriesDetal = view.findViewById(R.id.char_detail_RV)
        centerSum = view.findViewById(R.id.chart_center_sum_txt)
        barChart = view.findViewById(R.id.barchart_chart)
        pieChartLayout = view.findViewById(R.id.pieChart_layout)
        detailLegenLayout = view.findViewById(R.id.detailLegend_layout)
        scrollView = view.findViewById(R.id.chart_scrollView);

        //test
        btnTest = view.findViewById(R.id.btn_test_chart)

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
        if (expenseTxt!!.currentTextColor.equals(resources.getColor(R.color.white)))
            loadChartAndLegend("expense")
        else
            loadChartAndLegend("income")
    }

    private fun setVisibilityLabel(view: View) {
        periodDayLabel!!.visibility = View.GONE
        periodMonthLabel!!.visibility = View.GONE
        periodYearLabel!!.visibility = View.GONE
        periodCustomLabel!!.visibility = View.GONE
        view!!.visibility = View.VISIBLE

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
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            startDate = cal.timeInMillis
            val cal3 = Calendar.getInstance()
            cal3.timeInMillis = endDate!!
            cal3.add(Calendar.DAY_OF_YEAR, -1)
            cal3.set(Calendar.HOUR_OF_DAY, 23)
            cal3.set(Calendar.MINUTE, 59)
            cal3.set(Calendar.SECOND, 59)
            endDate = cal3.timeInMillis
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
            var day: String? = ""
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal.get(Calendar.YEAR) == cal2.get(
                    Calendar.YEAR
                )
            )
                day = resources.getString(R.string.today) + ", "
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) - 1 && cal.get(
                    Calendar.YEAR
                ) == cal2.get(Calendar.YEAR)
            )
                day = resources.getString(R.string.yesterday) + ", "
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) + 1 && cal.get(
                    Calendar.YEAR
                ) == cal2.get(Calendar.YEAR)
            )
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
            val cal = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            cal.timeInMillis = startDate!!
            cal2.timeInMillis = endDate!!
            cal.add(Calendar.MONTH, -1)
            cal2.set(Calendar.DAY_OF_MONTH, 1)
            cal2.add(Calendar.DAY_OF_YEAR, -1)
            startDate = cal.timeInMillis
            endDate = cal2.timeInMillis
            setVisibilityLabel(periodMonthLabel!!)
        }

        if (rbYear!!.isChecked) {
            val cal = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            cal.timeInMillis = startDate!!
            cal2.timeInMillis = endDate!!
            val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            cal.add(Calendar.YEAR, -1)
            startDate = cal.timeInMillis
            cal2.add(Calendar.YEAR, -1)
            endDate = cal2.timeInMillis
            periodYearLabel!!.setText(dateFormat.format(cal.time))
            setVisibilityLabel(periodYearLabel!!)
        }
        if (expenseTxt!!.currentTextColor.equals(resources.getColor(R.color.white)))
            loadChartAndLegend("expense")
        else
            loadChartAndLegend("income")
    }

    fun clickRight() {
        if (rbDay!!.isChecked) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = startDate!!
            val cal2 = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, +1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            startDate = cal.timeInMillis
            val cal3 = Calendar.getInstance()
            cal3.timeInMillis = endDate!!
            cal3.add(Calendar.DAY_OF_YEAR, +1)
            cal3.set(Calendar.HOUR_OF_DAY, 23)
            cal3.set(Calendar.MINUTE, 59)
            cal3.set(Calendar.SECOND, 59)
            cal3.set(Calendar.MILLISECOND, 99)
            endDate = cal3.timeInMillis
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
            var day: String? = ""
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal.get(Calendar.YEAR) == cal2.get(
                    Calendar.YEAR
                )
            )
                day = resources.getString(R.string.today) + ", "
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) - 1 && cal.get(
                    Calendar.YEAR
                ) == cal2.get(Calendar.YEAR)
            )
                day = resources.getString(R.string.yesterday) + ", "
            if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) + 1 && cal.get(
                    Calendar.YEAR
                ) == cal2.get(Calendar.YEAR)
            )
                day = resources.getString(R.string.tomorow) + ", "
            periodDayLabel!!.setText(day + dateFormat.format(cal.time))
            setVisibilityLabel(periodDayLabel!!)
        }

        if (rbMonth!!.isChecked) {
            val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

            if (monthPosition == 11) {
                yearPosition = yearPosition!! + 1
                monthPosition = 0
            } else
                monthPosition = monthPosition!! + 1

            val cal = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            cal.timeInMillis = startDate!!
            cal2.timeInMillis = endDate!!
            cal.add(Calendar.MONTH, +1)
            cal2.set(Calendar.DAY_OF_MONTH, 1)
            cal2.add(Calendar.MONTH, +2)
            cal2.add(Calendar.DAY_OF_YEAR, -1)
            startDate = cal.timeInMillis
            endDate = cal2.timeInMillis
            periodMonthLabel!!.setText(dateFormat.format(cal.time))
            setVisibilityLabel(periodMonthLabel!!)
        }

        if (rbYear!!.isChecked) {
            val cal = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            cal.timeInMillis = startDate!!
            cal2.timeInMillis = endDate!!
            val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            cal.add(Calendar.YEAR, +1)
            startDate = cal.timeInMillis
            cal2.add(Calendar.YEAR, +1)
            endDate = cal2.timeInMillis
            periodYearLabel!!.setText(dateFormat.format(cal.time))
            setVisibilityLabel(periodYearLabel!!)
        }
        if (expenseTxt!!.currentTextColor.equals(resources.getColor(R.color.white)))
            loadChartAndLegend("expense")
        else
            loadChartAndLegend("income")
    }

    fun selectDay() {
        var cal = Calendar.getInstance()
        var cal2 = Calendar.getInstance()
        cal.timeInMillis = startDate!!

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                startDate = cal.timeInMillis
                var cal3 = Calendar.getInstance()
                cal3.timeInMillis = cal.timeInMillis
                cal3.set(Calendar.HOUR_OF_DAY, 23)
                cal3.set(Calendar.MINUTE, 59)
                cal3.set(Calendar.SECOND, 59)
                cal3.set(Calendar.MILLISECOND, 59)
                endDate = cal3.timeInMillis

                val myFormat = "d MMMM yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

                var day: String? = ""
                if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal.get(
                        Calendar.YEAR
                    ) == cal2.get(Calendar.YEAR)
                )
                    day = resources.getString(R.string.today) + ", "
                if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) - 1 && cal.get(
                        Calendar.YEAR
                    ) == cal2.get(Calendar.YEAR)
                )
                    day = resources.getString(R.string.yesterday) + ", "
                if (cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) + 1 && cal.get(
                        Calendar.YEAR
                    ) == cal2.get(Calendar.YEAR)
                )
                    day = resources.getString(R.string.tomorow) + ", "

                periodDayLabel!!.setText(day + sdf.format(cal.time))
            }

        DatePickerDialog(
            context, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun selectCustomDate(textView: TextView, position: String) {
        var cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())

        if (position.equals("start"))
            cal.timeInMillis = startDate!!
        else
            cal.timeInMillis = endDate!!

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                if (position.equals("start")) {
                    startDate = cal.timeInMillis
                    if (startDate!! > endDate!!) {
                        cal.timeInMillis = startDate!!
                        cal.add(Calendar.MONTH, +1)
                        endDate = cal.timeInMillis
                        periodCustomLabel2!!.setText(dateFormat.format(cal.time))
                    }
                } else {
                    endDate = cal.timeInMillis
                    if (startDate!! > endDate!!) {
                        var cal = Calendar.getInstance()
                        cal.timeInMillis = endDate!!
                        cal.add(Calendar.MONTH, -1)
                        startDate = cal.timeInMillis
                        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                        periodCustomLabel1!!.setText(dateFormat.format(cal.time))
                    }

                }
                if (expenseTxt!!.currentTextColor.equals(resources.getColor(R.color.white)))
                    loadChartAndLegend("expense")
                else
                    loadChartAndLegend("income")
                textView!!.setText(dateFormat.format(cal.time))
//                textView!!.setText(cal.time.toString())
            }

        DatePickerDialog(
            context, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun initStartAndEndDate() {

        defaultDataBase()

        var cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        startDate = cal.timeInMillis
        var cal2 = cal
        cal2.add(Calendar.MONTH, +1)
        cal2.add(Calendar.DAY_OF_YEAR, -1)
        endDate = cal2.timeInMillis
        val repository = InfoRepository()

        selectedIdAccount = repository.getIdAccount(repository.allAccountsNames?.get(0))?.toInt()

        var currency = repository.getAccount(selectedIdAccount.toString())!!.accountCurrency
        val currencyDatabase = CurrencyDatabase(context)
        val currentList = currencyDatabase.currenciesList
        for (j in currentList.indices) {
            if (currentList.get(j).getName().equals(currency)) {
                currencySymbol = currentList.get(j).symbol
                break
            }
        }


        accountTxt?.setText(repository.getAccount(selectedIdAccount.toString())?.accountName)
        monthPosition = cal.get(Calendar.MONTH)
        yearPosition = cal.get(Calendar.YEAR)

    }

    fun loadPieChart(type: String = "expense") {

        val transactionList = loadRangeTransactions(type)
        val transactionSummary = arrayListOf<Pair<String, Double>>()

        for (transaction in transactionList!!) {
            val categoryId = transaction.transactionIdCategory
            val amount = transaction.transactionValue.toDouble()

            var categorySum = transactionSummary.find { it.first == categoryId }?.second ?: 0.0
            categorySum += amount

            val existingCategorySumIndex =
                transactionSummary.indexOfFirst { it.first == categoryId }
            if (existingCategorySumIndex != -1) {
                transactionSummary[existingCategorySumIndex] = categoryId to categorySum
            } else {
                transactionSummary.add(categoryId to categorySum)
            }
        }

        // Set the data and color to the pie chart
        pieChart?.clearChart()
        val repository = InfoRepository()

        for ((categoryId, sum) in transactionSummary) {
            pieChart?.addPieSlice(
                PieModel(
                    repository.getCategoryName(categoryId),
                    sum.toFloat(),
                    Color.parseColor(
                        repository.getCategoryColor(
                            repository.getCategoryName(categoryId)
                        )
                    )
                )
            )
        }

        // To animate the pie chart
        pieChart!!.startAnimation()

        // set general value in chart
        var sum = 0.0
        for (transaction in transactionList!!) {
            sum += transaction.transactionValue.toDouble()
        }
        if (sum.equals(0.0))
            centerSum?.visibility = View.INVISIBLE
        else
            centerSum?.visibility = View.VISIBLE
        val format = DecimalFormat("0.#")
        centerSum?.setText(format.format(sum) + " " + currencySymbol)

    }

    fun loadCategorieLegend(type: String = "expense") {
        val repository = InfoRepository()

        val transactions = loadRangeTransactions(type)
        val uniqueCategories = arrayListOf<String>()
        for (transaction in transactions!!) {
            val category = repository.getCategoryName(transaction.transactionIdCategory)
            if (!uniqueCategories.contains(category)) {
                uniqueCategories.add(category!!)
            }
        }

        categoriesRVAdapter = RecyclerViewPieChartAdapter(context, uniqueCategories)
        recyclerViewCategories!!.adapter = categoriesRVAdapter
        recyclerViewCategories.layoutManager
    }


    private fun selectAccount() {
        val repository = InfoRepository()
        val descpriptionDialog = Dialog(context)
        descpriptionDialog.setContentView(R.layout.dialog_tr_account_select)
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        descpriptionDialog.window!!.setLayout(width, height)
        descpriptionDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        descpriptionDialog.show()

        val accountListView = descpriptionDialog.findViewById<ListView>(R.id.d_tr_acc_list)
        var accList: List<String?> = java.util.ArrayList()
        accList = repository.getAllAccountsNames() as List<String?>
        val accountAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(context, android.R.layout.simple_list_item_activated_1, accList)
        accountListView.adapter = accountAdapter
        val finalAccList = accList
        accountListView.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                var accNameTxt = finalAccList[i]
                selectedIdAccount = repository.getIdAccount(accNameTxt)?.toInt()
//                if (accNameTxt!!.length > 15) accNameTxt = accNameTxt!!.substring(0, 30) + "..."
                accountTxt?.setText(accNameTxt)
                Toast.makeText(
                    context,
                    "selected: " + i + ", id: " + repository.getIdAccount(accList.get(i)),
                    Toast.LENGTH_SHORT
                ).show()
                var currency = repository.getAccount(
                    repository.getIdAccount(accList.get(i)).toString()
                )!!.accountCurrency
                val currencyDatabase = CurrencyDatabase(context)
                val currentList = currencyDatabase.currenciesList
                for (j in currentList.indices) {
                    if (currentList.get(j).getName().equals(currency)) {
                        currencySymbol = currentList.get(j).symbol
                        break
                    }
                }
                descpriptionDialog.dismiss()
            }
    }

    fun loadRangeTransactions(transactionType: String): ArrayList<Transaction>? {
        val repository = InfoRepository()

        return repository.getSpecificTransactions(
            selectedIdAccount!!.toString(), "0".toDouble(), "999999999".toDouble(), "All",
            startDate!!, endDate!!, transactionType
        )
    }

    fun loadChartAndLegend(type: String = "expense") {

        if (loadRangeTransactions(type)?.size != 0) {
            pieChartLayout!!.visibility = View.VISIBLE
            detailLegenLayout!!.visibility = View.VISIBLE
            barChart!!.visibility = View.VISIBLE
            loadPieChart(type)
            loadCategorieLegend(type)
            loadDetailsLegen(type)
            if (rbYear?.isChecked == true || rbMonth?.isChecked == true)
                loadBarChart(type)
            else
                barChart!!.visibility = View.GONE
        } else {
            pieChartLayout!!.visibility = View.GONE
            detailLegenLayout!!.visibility = View.GONE
            barChart!!.visibility = View.GONE
        }

    }

    // detail legen
    fun loadDetailsLegen(type: String = "expense") {

        val transactions = loadRangeTransactions(type)

        // data for RecyclerView
        val categorySums = arrayListOf<Triple<String, Double, Double>>()
        var totalSum = 0.0

        for (transaction in transactions!!) {
            val categoryId = transaction.transactionIdCategory
            val amount = transaction.transactionValue.toDouble()

            totalSum += amount

            val existingCategorySumIndex = categorySums.indexOfFirst { it.first == categoryId }
            if (existingCategorySumIndex != -1) {
                val (existingCategoryId, existingSum, _) = categorySums[existingCategorySumIndex]
                categorySums[existingCategorySumIndex] =
                    Triple(existingCategoryId, existingSum + amount, 0.0)
            } else {
                categorySums.add(Triple(categoryId, amount, 0.0))
            }
        }

        for (index in categorySums.indices) {
            val (categoryId, sum, _) = categorySums[index]
            var percentage = sum / totalSum * 100.0
            percentage = Math.round(percentage * 100.0) / 100.0
            categorySums[index] = Triple(categoryId, sum, percentage)
        }
        categorySums.sortByDescending { it.third }

        // data for nested RecyclerView
        var categorySubcategorySums = arrayListOf<Quad<String, String, Double, Double>>()
        val categoryMap = transactions?.groupBy { it.transactionIdCategory }
        if (categoryMap != null) {
            for ((categoryId, categoryTransactions) in categoryMap) {
                val categorySum =
                    categoryTransactions.sumByDouble { it.transactionValue.toDouble() }

                val subcategoryMap = categoryTransactions.groupBy { it.transactionIdSubcategory }

                for ((subcategoryId, subcategoryTransactions) in subcategoryMap) {
                    val subcategorySum =
                        subcategoryTransactions.sumByDouble { it.transactionValue.toDouble() }
                    var percentage = (subcategorySum / categorySum) * 100.0
                    percentage = Math.round(percentage * 100.0) / 100.0

                    val quad = Quad(subcategoryId, categoryId, subcategorySum, percentage)
                    categorySubcategorySums.add(quad)
                }
            }

        }

        val sortedCategorySubcategorySums = categorySubcategorySums.sortedWith(
            compareByDescending<Quad<String, String, Double, Double>> { it.third }
                .thenByDescending { it.fourth }
        )

        val categoryGroupedSums = sortedCategorySubcategorySums.groupBy { it.second }

        categorySubcategorySums.clear()

        for ((categoryId, subcategoryList) in categoryGroupedSums) {
            for (quad in subcategoryList) {
                categorySubcategorySums.add(quad)
//                Log.d("print:", "Subcategory ID: ${quad.first}, Category ID: ${quad.second}, Sum: ${quad.third}, Percentage: ${quad.fourth}%")
            }
        }

        // test
        for (quad in categorySubcategorySums) {
            Log.d(
                "print:",
                "Subcategory ID: ${quad.first}, Category ID: ${quad.second}, Sum: ${quad.third}, Percentage: ${quad.fourth}%"
            )
        }

        categoriesDetailRVAdapter =
            RecyclerViewPieChartDetailAdapter(
                context,
                categorySums,
                categorySubcategorySums,
                currencySymbol
            )
        recyclerViewCategoriesDetal!!.adapter = categoriesDetailRVAdapter
        recyclerViewCategoriesDetal.layoutManager

    }

    // load Bar Chart
    private fun loadBarChart(type: String = "expense") {
        barEntriesList(type)

        val set = BarDataSet(barEntriesList, "BarDataSet")
        set.valueTextSize = 12f
//        set.setValueFormatter(object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                val format: DecimalFormat = DecimalFormat("0.#")
//                return if (value > 0) {
//                    return format.format(value) + " " + currencySymbol
//                } else {
//                    ""
//                }
//            }
//        })
        set.setDrawValues(false)
        set.setGradientColor(Color.parseColor("#1C033838"), Color.parseColor("#054949"))

        barChart?.description?.isEnabled = false
        barChart?.legend?.isEnabled = false
        barChart?.data = BarData(set)
        barChart!!.setDrawGridBackground(false)
        barChart!!.setDrawBarShadow(false)
        barChart!!.setDrawBorders(false)

        val xAxis = barChart!!.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)

        val leftAxis = barChart!!.axisLeft
        leftAxis.setDrawAxisLine(false)

        val rightAxis = barChart!!.axisRight
        rightAxis.setDrawAxisLine(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        barChart?.invalidate()
        barChart?.animateY(2500)
        barChart?.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                val y = e.y
                val format: DecimalFormat = DecimalFormat("0.#")
                Toast.makeText(context, format.format(y), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected() {}
        })

    }

    // prepare data for bar Chart
    private fun barEntriesList(type: String = "expense") {
//    private fun dataBarChart() {
        barEntriesList = ArrayList()
        xAxisLabels = ArrayList()
        var barData: ArrayList<BarEntry>? = null
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startDate!!
        val transactionList = loadRangeTransactions(type)
        if (rbMonth?.isChecked == true) {
            val numDays = calendar.getActualMaximum(Calendar.DATE)
            for (i in 1..numDays) {
                var sumDay = 0.0
                calendar.set(Calendar.DAY_OF_MONTH, i)
                for (transaction in transactionList!!) {
                    val calendarDay = Calendar.getInstance()
                    calendarDay.timeInMillis = transaction.transactionDateInMilis
                    if (calendarDay.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR))
                        sumDay += transaction.transactionValue.toDouble()
                }
                barEntriesList?.add(BarEntry(i.toFloat(), sumDay.toFloat()))
                xAxisLabels?.add(i.toString() + "")
            }
        } else if (rbYear?.isChecked == true) {
            for (i in 0..11) {
                var sumDay = 0.0
                calendar.set(Calendar.MONTH, i)
                for (transaction in transactionList!!) {
                    val calendarMonth = Calendar.getInstance()
                    calendarMonth.timeInMillis = transaction.transactionDateInMilis
                    if (calendarMonth.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
                        sumDay += transaction.transactionValue.toDouble()
                }
                barEntriesList?.add(BarEntry(i.toFloat(), sumDay.toFloat()))
                xAxisLabels?.add(resources.getStringArray(R.array.months).get(i))
            }
        }
    }

    fun expenseClick() {
        expenseTxt!!.setTextColor(Color.WHITE)
        expenseTxt!!.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context!!,
                    R.color.olx_color_1
                )
            )
        )
        incomeTxt!!.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context!!,
                    R.color.olx_color_1
                )
            )
        )
        incomeTxt!!.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context!!,
                    R.color.white
                )
            )
        )
        loadChartAndLegend("expense")
    }

    fun incomeClick() {
        expenseTxt!!.setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context!!,
                    R.color.olx_color_1
                )
            )
        )
        expenseTxt!!.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context!!,
                    R.color.white
                )
            )
        )
        incomeTxt!!.setTextColor(Color.WHITE)
        incomeTxt!!.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context!!,
                    R.color.olx_color_1
                )
            )
        )
        loadChartAndLegend("income")
    }

    fun defaultDataBase() {
        val categories = resources.getStringArray(R.array.categories)
        val subcategories: MutableList<Array<String>> = java.util.ArrayList()
        subcategories.add(resources.getStringArray(R.array.subcategories1))
        subcategories.add(resources.getStringArray(R.array.subcategories2))
        subcategories.add(resources.getStringArray(R.array.subcategories3))
        subcategories.add(resources.getStringArray(R.array.subcategories4))
        subcategories.add(resources.getStringArray(R.array.subcategories5))
        subcategories.add(resources.getStringArray(R.array.subcategories6))
        subcategories.add(resources.getStringArray(R.array.subcategories7))
        subcategories.add(resources.getStringArray(R.array.subcategories8))
        subcategories.add(resources.getStringArray(R.array.subcategories9))
        subcategories.add(resources.getStringArray(R.array.subcategories10))
        subcategories.add(resources.getStringArray(R.array.subcategories11))
        subcategories.add(resources.getStringArray(R.array.subcategories12))
        subcategories.add(resources.getStringArray(R.array.subcategories13))
        subcategories.add(resources.getStringArray(R.array.subcategories14))
        subcategories.add(resources.getStringArray(R.array.subcategories15))
        val icons = resources.getStringArray(R.array.category_icon)
        val colors = resources.getStringArray(R.array.category_colors)
        val accountssList = resources.getStringArray(R.array.accounts_names)
        val repository = InfoRepository()
        repository.addDefaultDatabase(categories, subcategories, icons, colors, accountssList)
    }

}


