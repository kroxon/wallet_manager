package step.wallet.maganger.ui

import android.app.DatePickerDialog
import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import step.wallet.maganger.R
import step.wallet.maganger.adapters.ListViewVerticalAdapter
import step.wallet.maganger.adapters.RecyclerViewCategoryActivityAdapter
import step.wallet.maganger.adapters.RecyclerViewPieChartAdapter
import step.wallet.maganger.data.InfoRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChartsFragment : Fragment(), DialogFragmentDatePicker.onDateRangeSelectedListene {


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

    private var monthPosition: Int? = null
    private var yearPosition: Int? = null

    // charts
    private var pieChart: PieChart? = null
    lateinit var categoriesRVAdapter: RecyclerViewPieChartAdapter
    lateinit var recyclerViewCategories: RecyclerView


    //test
    lateinit var listViewSubcategories: ListView


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

        loadPieChart()
        loadCategorieLegend()
//        loadLVSubcategories()

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

        //test
        listViewSubcategories = view.findViewById(R.id.ac_subcat_listView2)

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

        cal.add(Calendar.MONTH, +1)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        endDate = cal.timeInMillis

        monthPosition = cal.get(Calendar.MONTH)
        yearPosition = cal.get(Calendar.YEAR)
    }

    fun loadPieChart() {


        // Set the data and color to the pie chart

        // Set the data and color to the pie chart
        pieChart?.addPieSlice(
            PieModel(
                "R", 3F,
                Color.parseColor("#FFA726")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "Python", 10f,
                Color.parseColor("#66BB6A")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "C++", 5f,
                Color.parseColor("#EF5350")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "Java", 20f,
                Color.parseColor("#29B6F6")
            )
        )

        // To animate the pie chart

        // To animate the pie chart
        pieChart!!.startAnimation()


    }

    fun loadCategorieLegend() {
        val repository = InfoRepository()
        var list: ArrayList<String>? = null
        list?.add("jedzenie")
        list?.add("picie")
        list?.add("edukacja")

//        categoriesRVAdapter = RecyclerViewPieChartAdapter(context, repository.allCategories)
        categoriesRVAdapter = RecyclerViewPieChartAdapter(context, repository.allExpenseCategories)
        recyclerViewCategories!!.adapter = categoriesRVAdapter
        recyclerViewCategories.layoutManager
    }

    private fun loadLVSubcategories() {
        val arrayAdapter: ListViewVerticalAdapter
        val repository = InfoRepository()
        val catName = repository.allCategories!!.get(0)

        var id = (repository.allCategories!!.indexOf(repository.getIdCategory(catName))).toString()
        val labels: List<String> =
            repository.getSubcategories(repository.getIdCategory(catName)) as List<String>
        arrayAdapter = ListViewVerticalAdapter(
            context,
            labels as java.util.ArrayList<String>?,
            repository.getIdCategory(repository.allCategories!!.get(0))
        )
//        arrayAdapter.setOnShareClickedListener(this)
        listViewSubcategories!!.adapter = arrayAdapter
    }

//    fun loadPieChart2() {
//
//        pieChart?.setUsePercentValues(true);
//        pieChart?.getDescription()?.setEnabled(false);
//        pieChart?.setExtraOffsets(5F, 10F, 5F, 5F);
//
//        pieChart?.setDragDecelerationFrictionCoef(0.95f);
//
////        pieChart?.setCenterTextTypeface(tfLight);
////        pieChart?.setCenterText(generateCenterSpannableText());
//
//        pieChart?.setDrawHoleEnabled(true);
//        pieChart?.setHoleColor(Color.WHITE);
//
//        pieChart?.setTransparentCircleColor(Color.WHITE);
//        pieChart?.setTransparentCircleAlpha(110);
//
//        pieChart?.setHoleRadius(58f);
//        pieChart?.setTransparentCircleRadius(61f);
//
//        pieChart?.setDrawCenterText(true);
//
//        pieChart?.setRotationAngle(0F);
//        // enable rotation of the chart by touch
//        pieChart?.setRotationEnabled(true);
//        pieChart?.setHighlightPerTapEnabled(true);
//
//        // chart.setUnit(" €");
//        // chart.setDrawUnitsInChart(true);
//
//        // add a selection listener
//        pieChart?.setOnChartValueSelectedListener(this)
//
//
//        val l: Legend? = pieChart?.getLegend()
//        l?.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//        l?.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//        l?.orientation = Legend.LegendOrientation.VERTICAL
//        l?.setDrawInside(false)
//        l?.xEntrySpace = 7f
//        l?.yEntrySpace = 0f
//        l?.yOffset = 0f
//
//        // entry label styling
//
//        // entry label styling
//        pieChart?.setEntryLabelColor(Color.WHITE)
////        pieChart?.setEntryLabelTypeface(tfRegular)
//        pieChart?.setEntryLabelTextSize(12f)
//
//        // add a lot of colors
//
//        // add a lot of colors
//        val colors: ArrayList<Int> = ArrayList()
//
//        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
//
//        colors.add(ColorTemplate.getHoloBlue())
//
//
//        var visitors = ArrayList<PieEntry>()
//        visitors.add(PieEntry(132.5f, "jedzenie"))
//        visitors.add(PieEntry(52.5f, "podróże"))
//        visitors.add(PieEntry(232.5f, "edukacja"))
//
//        val piedataset = PieDataSet(visitors, "expense")
//        piedataset.setColors(colors)
//
//
//
//        val data = PieData(piedataset)
//        pieChart?.data = data
//    }

//    override fun onValueSelected(e: Entry?, h: Highlight?) {
//        if (e == null)
//            return;
//        Toast.makeText(context,
//            "Value: " + e.getY() + ", index: " + h?.getX()
//                    + ", DataSet index: " + h?.getDataSetIndex(), Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onNothingSelected() {
//        Toast.makeText(context, "Nothing", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun generateCenterSpannableText(): SpannableString? {
//        val s = SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda")
//        s.setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
//        s.setSpan(StyleSpan(Typeface.NORMAL), 14, s.length - 15, 0)
//        s.setSpan(ForegroundColorSpan(Color.GRAY), 14, s.length - 15, 0)
//        s.setSpan(RelativeSizeSpan(.8f), 14, s.length - 15, 0)
//        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 14, s.length, 0)
//        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 14, s.length, 0)
//        return s
//    }


}
