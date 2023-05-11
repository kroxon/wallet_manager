package step.wallet.maganger.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.Fragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import step.wallet.maganger.R
import step.wallet.maganger.adapters.RecyclerViewPieChartAdapter
import step.wallet.maganger.classes.Transaction
import step.wallet.maganger.data.InfoRepository
import java.text.SimpleDateFormat
import java.util.*


class ChartsFragment : Fragment(), DialogFragmentDatePicker.onDateRangeSelectedListene {


    private var accountTxt: TextView? = null
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

    // for filter transaction
    private var startDate: Long? = null
    private var endDate: Long? = null
    private var selectedIdAccount: Int? = null

    private var monthPosition: Int? = null
    private var yearPosition: Int? = null

    // charts
    private var pieChart: PieChart? = null
    lateinit var categoriesRVAdapter: RecyclerViewPieChartAdapter
    lateinit var recyclerViewCategories: RecyclerView

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

        loadChartAndLegend()


        return view
    }

    private fun initItems() {

        initStartAndEndDate()


        periodMonthLabel!!.setText(
            resources.getStringArray(R.array.months)
                .get(monthPosition!!) + " " + Calendar.getInstance()
                .get(Calendar.YEAR)
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
                periodMonthLabel!!.setOnClickListener(View.OnClickListener {
                    val dialog = DialogFragmentDatePicker()
////                    dialog.setTargetFragment(this, 1)
                    dialog.setOnDateRangeListener(this)
                    dialog.setValue(monthPosition!!, yearPosition!!)
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
        }

        rbDay!!.performClick()
        rbMonth!!.performClick()
        initStartAndEndDate()


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
        }

        periodCustomLabel2!!.setOnClickListener {
            selectCustomDate(periodCustomLabel2!!, "end")
        }


        accountTxt?.setOnClickListener {
            selectAccount()
        }


        // testing
        btnTest!!.setOnClickListener {
            Toast.makeText(
                context,
                loadRangeTransactions("expense")?.size.toString(),
                Toast.LENGTH_SHORT
            ).show()
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
            cal.set(Calendar.HOUR, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            startDate = cal.timeInMillis
            val cal3 = Calendar.getInstance()
            cal3.timeInMillis = endDate!!
            cal3.add(Calendar.DAY_OF_YEAR, -1)
            cal3.set(Calendar.HOUR, 23)
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
            cal2.add(Calendar.MONTH, -1)
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
        loadChartAndLegend()
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
            cal2.add(Calendar.MONTH, +1)
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
        loadChartAndLegend()
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
        accountTxt?.setText(repository.getAccount(selectedIdAccount.toString())?.accountName)
        monthPosition = cal.get(Calendar.MONTH)
        yearPosition = cal.get(Calendar.YEAR)

    }

    fun loadPieChart() {

        val transactionList = loadRangeTransactions("expense")
        val transactionSummary = arrayListOf<Pair<String, Double>>()

        for (transaction in transactionList!!) {
            val categoryId = transaction.transactionIdCategory
            val amount = transaction.transactionValue.toDouble()

            var categorySum = transactionSummary.find { it.first == categoryId }?.second ?: 0.0
            categorySum += amount

            val existingCategorySumIndex = transactionSummary.indexOfFirst { it.first == categoryId }
            if (existingCategorySumIndex != -1) {
                transactionSummary[existingCategorySumIndex] = categoryId to categorySum
            } else {
                transactionSummary.add(categoryId to categorySum)
            }
        }
// testing
//        for ((categoryId, sum) in transactionSummary) {
//            Toast.makeText(context, "Category ID: $categoryId, Sum: $sum", Toast.LENGTH_SHORT).show()
//        }

        // Set the data and color to the pie chart
        pieChart?.clearChart()
        val repository = InfoRepository()

        for ((categoryId, sum) in transactionSummary) {
            pieChart?.addPieSlice(PieModel(repository.getCategoryName(categoryId),
                sum.toFloat(), Color.parseColor(repository.getCategoryColor(repository.getCategoryName(categoryId)))))
        }

        // To animate the pie chart
        pieChart!!.startAnimation()


    }

    fun loadCategorieLegend() {
        val repository = InfoRepository()

        val transactions = loadRangeTransactions("expense")
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
                if (accNameTxt!!.length > 15) accNameTxt = accNameTxt!!.substring(0, 30) + "..."
                accountTxt?.setText(accNameTxt)
                Toast.makeText(
                    context,
                    "selected: " + i + ", id: " + repository.getIdAccount(accList.get(i)),
                    Toast.LENGTH_SHORT
                ).show()
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

    fun loadChartAndLegend() {
        loadPieChart()
        loadCategorieLegend()
    }

}
