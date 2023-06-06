package step.wallet.maganger.ui

import android.app.DatePickerDialog
import android.app.DialogFragment
import android.content.Context
import android.icu.text.IDNA.Info
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import step.wallet.maganger.R
import step.wallet.maganger.classes.Account
import step.wallet.maganger.data.InfoRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DialogFragmentFilterHistoryTransaction : DialogFragment() {

    interface OnInputSend {
        fun sendData(
            sAmountFrom: Double,
            sAmountTo: Double,
            sCurrency: String,
            sPeriod: String,
            sPeriodFrom: Long?,
            sPeriodTo: Long?,
            sTypeOper: String,
            sAccount: String
        )
    }


    var etAmountFrom: TextInputEditText? = null
    var etAmountTo: TextInputEditText? = null
    var aCTvCurrency: Spinner? = null
    lateinit var arrayAdapterCurrency: ArrayAdapter<String>
    var aCTvPeriod: Spinner? = null
    lateinit var arrayAdapterPeriod: ArrayAdapter<String>
    var inputDateFrom: TextInputLayout? = null
    var etDateFrom: AutoCompleteTextView? = null
    var inputDateTo: TextInputLayout? = null
    var etDateTo: AutoCompleteTextView? = null
    var checkBoxAll: CheckBox? = null
    var checkBoxDebit: CheckBox? = null
    var checkBoxCredit: CheckBox? = null
    var aCTvAccount: Spinner? = null
    lateinit var arrayAdapterAccount: ArrayAdapter<String>
    var btnClear: TextView? = null
    var btnCancel: TextView? = null
    var btnSave: TextView? = null

    // iterface
    var amountFrom: Double = 0.00
    var amountTo: Double = 99999999.99
    var currency: String = "PLN"
    var period: String = "0"
    var periodFrom: Long = Calendar.getInstance().timeInMillis
    var periodTo: Long = Calendar.getInstance().timeInMillis
    var typeOperation: String = "All"
    var account: String = "0"

    //date pickers
    val myFormat = "dd MMM yyyy"
    var cal1 = Calendar.getInstance()
    var cal2 = Calendar.getInstance()
    var calendar2 = Calendar.getInstance()

    // iterface
    var mOnInputSend: OnInputSend? = null

    // currency
    var currenciesList: List<String> = emptyList()


    override fun onStart() {
        super.onStart()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.dialog_filter_transaction, container, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )

        currenciesList = ArrayList()
        (currenciesList as ArrayList<String>).add(getString(R.string.filter_currency_all))

        var repo = InfoRepository()
        for (i in repo.readAccounts()!!) {
            if (!currenciesList.contains(i.getAccountCurrencySymbol(context)))
                (currenciesList as ArrayList<String>).add(i.getAccountCurrencySymbol(context))
        }

        findViews(view)
        initializeViews()


        setInitialValue()

        return view
    }

    private fun initializeViews() {
        etAmountFrom?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (s.equals("") || s.length == 0)
                    amountFrom = 0.0
                else
                    amountFrom = s.toString().toDouble()
            }
        })

        etAmountTo?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (s.equals("") || s.length == 0)
                    amountTo = 99999999.99
                else
                    amountTo = s.toString().toDouble()
            }
        })

        // currency picker

        aCTvCurrency?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val repository = InfoRepository()
                for (i in repository.readAccounts()!!) {
                    if (i.getAccountCurrencySymbol(context).equals(currenciesList.get(position)))
                        currency = i.accountCurrency
                }
                if (position == 0)
                    currency = "All"
            }
        }

        // period picker

        aCTvPeriod?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    calendar2 = Calendar.getInstance()
                    calendar2.add(Calendar.YEAR, -20)
                    periodFrom = calendar2.timeInMillis
                    val calendar3 = Calendar.getInstance()
                    calendar3.add(Calendar.WEEK_OF_YEAR, +20)
                    periodTo = calendar3.timeInMillis
                    etDateFrom?.setText("-")
                    etDateTo?.setText("-")
                }
                if (position == 1) {
                    calendar2 = Calendar.getInstance()
                    calendar2.add(Calendar.WEEK_OF_YEAR, -1)
                    periodFrom = calendar2.timeInMillis
                    periodTo = Calendar.getInstance().timeInMillis
                }
                if (position == 2) {
                    calendar2 = Calendar.getInstance()
                    calendar2.add(Calendar.MONTH, -1)
                    periodFrom = calendar2.timeInMillis
                    periodTo = Calendar.getInstance().timeInMillis
                }
                if (position == 3) {
                    calendar2 = Calendar.getInstance()
                    calendar2.add(Calendar.YEAR, -1)
                    periodFrom = calendar2.timeInMillis
                    periodTo = Calendar.getInstance().timeInMillis
                }
                if (position == 4) {
//                    Toast.makeText(context, "custom", Toast.LENGTH_SHORT).show()
                }
                if (position != 4 && position != 0) {
                    val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                    etDateFrom?.setText(sdf.format(calendar2.getTime()))
                    etDateTo?.setText(sdf.format(Calendar.getInstance().getTime()))
                    Toast.makeText(
                        context,
                        etDateFrom?.text.toString() + " - " + etDateTo?.text.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                period = position.toString()
            }
        }


        // initial date pickers

        calendar2 = Calendar.getInstance()
        calendar2.add(Calendar.WEEK_OF_YEAR, -1)
        etDateFrom!!.setText(
            SimpleDateFormat(
                myFormat,
                Locale.getDefault()
            ).format(calendar2.getTime())
        )
        etDateTo!!.setText(
            SimpleDateFormat(
                myFormat,
                Locale.getDefault()
            ).format(Calendar.getInstance().getTime())
        )

        //first date picker

        // create an OnDateSetListener
        val dateSetListener1 = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal1.set(Calendar.YEAR, year)
                cal1.set(Calendar.MONTH, monthOfYear)
                cal1.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                val textCal1 = sdf.format(cal1.getTime())
                etDateFrom?.setText(textCal1)
                periodFrom = cal1.timeInMillis
                aCTvPeriod!!.setSelection(4)
            }
        }

        // when you click on the view, show DatePickerDialog that is set with OnDateSetListener
        etDateFrom!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    context,
                    dateSetListener1,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal1.get(Calendar.YEAR),
                    cal1.get(Calendar.MONTH),
                    cal1.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })

        //second date picker

        // create an OnDateSetListener
        val dateSetListener2 = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal2.set(Calendar.YEAR, year)
                cal2.set(Calendar.MONTH, monthOfYear)
                cal2.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                val textCal1 = sdf.format(cal2.getTime())
                etDateTo?.setText(textCal1)
                periodTo = cal2.timeInMillis
                aCTvPeriod!!.setSelection(4)
            }
        }

        // when you click on the view, show DatePickerDialog that is set with OnDateSetListener
        etDateTo!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    context,
                    dateSetListener2,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal2.get(Calendar.YEAR),
                    cal2.get(Calendar.MONTH),
                    cal2.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })

        //checkboxs
        checkBoxAll!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (checkBoxAll?.isChecked == true) {
                checkBoxCredit?.isChecked = true
                checkBoxDebit?.isChecked = true
                typeOperation = "All"
            } else {
                checkBoxCredit?.isChecked = false
                checkBoxDebit?.isChecked = false
            }
        }

        checkBoxDebit!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (checkBoxDebit?.isChecked == true)
                typeOperation = "expense"
            if (checkBoxDebit?.isChecked == true && checkBoxCredit?.isChecked == true) {
                checkBoxAll?.isChecked = true
            } else if (checkBoxCredit?.isChecked == true) {
                checkBoxAll?.isChecked = false
                checkBoxCredit?.isChecked = true
            }
        }

        checkBoxCredit!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (checkBoxCredit?.isChecked == true)
                typeOperation = "income"
            if (checkBoxDebit?.isChecked == true && checkBoxCredit?.isChecked == true) {
                checkBoxAll?.isChecked = true
            } else if (checkBoxDebit?.isChecked == true) {
                checkBoxAll?.isChecked = false
                checkBoxDebit?.isChecked = true
            }
        }

        aCTvAccount!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val accountsList: List<String>
                var repo = InfoRepository()
                if (position != 0)
                    account = repo.getIdAccount(repo.allAccountsNames?.get(position - 1)).toString()
                else
                    account = position.toString()
            }
        }

        btnClear!!.setOnClickListener {
            etAmountFrom?.setText("")
            etAmountTo?.setText("")
            aCTvCurrency!!.setSelection(0)
            aCTvPeriod!!.setSelection(0)
            checkBoxAll!!.isChecked = true
            typeOperation = "All"
            aCTvAccount!!.setSelection(0)
//            Toast.makeText(
//                context,
//                "SAVE: amountFrom: " + amountFrom + "\namountTo: " + amountTo + "\ncurrency: " + currency
//                        + "\nperiodFrom: " + periodFrom + "\nperiodTo: " + periodTo + "\ntypeOperation: " + typeOperation + "\naccount: " + account,
//                Toast.LENGTH_SHORT
//            ).show()
        }

        btnCancel!!.setOnClickListener {
            dialog.dismiss()
        }

        btnSave!!.setOnClickListener {
//            Toast.makeText(
//                context,
//                "SAVE: amountFrom: " + amountFrom + "\namountTo: " + amountTo + "\ncurrency: " + currency + "\nperiod: " + period
//                        + "\nperiodFrom: " + periodFrom + "\nperiodTo: " + periodTo + "\ntypeOperation: " + typeOperation + "\naccount: " + account,
//                Toast.LENGTH_SHORT
//            ).show()
            mOnInputSend?.sendData(
                amountFrom,
                amountTo,
                currency,
                period,
                periodFrom,
                periodTo,
                typeOperation,
                account
            )
            dialog.dismiss()
        }

    }

    private fun loadPeriodSpinner() {
        // on below line we are initializing adapter
        // Period spinner
        val periodsList = resources.getStringArray(R.array.periods)
//        aCTvPeriod?.setText(periodsList.get(0))
        arrayAdapterPeriod =
            ArrayAdapter(context, R.layout.dialog_filter_tr_currency_dropdown_item, periodsList)
        arrayAdapterPeriod.notifyDataSetChanged()
        aCTvPeriod?.setAdapter(arrayAdapterPeriod)
    }

    private fun loadCurrencySpinner() {
        // on below line we are initializing adapter
        // Currency spinner

        arrayAdapterCurrency =
            ArrayAdapter(context, R.layout.dialog_filter_tr_currency_dropdown_item, currenciesList)
        arrayAdapterCurrency.notifyDataSetChanged()
        aCTvCurrency?.setAdapter(arrayAdapterCurrency)
    }

    private fun loadAccountSpinner() {
        // on below line we are initializing adapter
        // Account spinner
        var accountsList: MutableList<String>
        var repo = InfoRepository()
        val nameAll = resources.getString(R.string.filter_currency_all)
        accountsList = repo.allAccountsNames!!
        accountsList.add(0, nameAll)
        val arrayAdapterAccount =
            ArrayAdapter(context, R.layout.dialog_filter_tr_currency_dropdown_item, accountsList)
        arrayAdapterAccount.notifyDataSetChanged()
        aCTvAccount?.setAdapter(arrayAdapterAccount)
    }


    fun setInitialValue() {
        loadPeriodSpinner()
        loadCurrencySpinner()
        loadAccountSpinner()

        if (amountFrom != 0.0)
            etAmountFrom?.setText(amountFrom.toString())

        if (amountTo != 99999999.99)
            etAmountTo?.setText(amountFrom.toString())

        var currencySymbol = ""
        val repo = InfoRepository()
        for (i in repo.readAccounts()) {
            if (i.accountCurrency.equals(currency))
                currencySymbol = i.getAccountCurrencySymbol(context)
        }
        aCTvCurrency?.setSelection(currenciesList.indexOf(currencySymbol))

        aCTvPeriod?.setSelection(period.toInt())

        if (typeOperation.equals("All")) {
            checkBoxAll!!.isChecked = true
        }
        if (typeOperation.equals("credit")) {
            checkBoxCredit!!.isChecked = true
        }
        if (typeOperation.equals("debit")) {
            checkBoxDebit!!.isChecked = true
        }

        aCTvAccount?.setSelection(account.toInt())


    }

    fun setValue(
        amountFrom: Double,
        amountTo: Double,
        currency: String,
        period: String,
        periodFrom: Long,
        periodTo: Long,
        typeOperation: String,
        account: String
    ) {
        this.amountFrom = amountFrom
        this.amountTo = amountTo
        this.currency = currency
        this.period = period
        this.periodFrom = periodFrom
        this.periodTo = periodTo
        this.typeOperation = typeOperation
        this.account = account

//        setInitialValue()

    }

    private fun findViews(view: View) {
        etAmountFrom = view.findViewById(R.id.hTrEtAmountFrom)
        etAmountTo = view.findViewById(R.id.hTrEtAmountTo)
        aCTvCurrency = view.findViewById(R.id.hTrFilterCurrencyACTv)
        aCTvPeriod = view.findViewById(R.id.hTrFilterPeriodACTv)
        inputDateFrom = view.findViewById(R.id.hTrFilterDateStart)
        etDateFrom = view.findViewById(R.id.hTrFilterDateStartInput)
        inputDateTo = view.findViewById(R.id.hTrFilterDateEnd)
        etDateTo = view.findViewById(R.id.hTrFilterDateEndInput)
        checkBoxAll = view.findViewById(R.id.hTrCheckBoxAll)
        checkBoxDebit = view.findViewById(R.id.hTrCheckBoxDebits)
        checkBoxCredit = view.findViewById(R.id.hTrCheckBoxCredits)
        aCTvAccount = view.findViewById(R.id.hTrFilterAccountACTv)
        btnClear = view.findViewById(R.id.hTrFilterClear)
        btnCancel = view.findViewById(R.id.hTrFilterCancel)
        btnSave = view.findViewById(R.id.hTrFilterSave)
    }

    companion object {
        private const val TAG = "DialogFragmentFilterTra"
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mOnInputSend = targetFragment as OnInputSend
        } catch (e: ClassCastException) {
            Log.e(
                DialogFragmentFilterHistoryTransaction.TAG, "onAttach: ClassCastException: "
                        + e.message
            )
        }
    }
}