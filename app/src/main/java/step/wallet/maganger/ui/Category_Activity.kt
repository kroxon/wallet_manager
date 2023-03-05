package step.wallet.maganger.ui

import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnChooseColorListener
import step.wallet.maganger.R
import step.wallet.maganger.adapters.ListViewVerticalAdapter
import step.wallet.maganger.adapters.RecyclerViewCategoryActivityAdapter
import step.wallet.maganger.data.InfoRepository
import java.util.*


class Category_Activity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    ListViewVerticalAdapter.OnShareClickedListener, DialogFragmentIconSelect.OnInputListener,
    RecyclerViewCategoryActivityAdapter.OnCategoryListener {

    // new view of Activity

    lateinit var recyclerViewCategories: RecyclerView
    lateinit var categoriesRVAdapter: RecyclerViewCategoryActivityAdapter
    var btnShowExpenses: TextView? = null
    var btnShowIncome: TextView? = null
    lateinit var listViewSubcategories: ListView
    lateinit var subcategoriesLVAdapter: ListViewVerticalAdapter
    var txtCategoryName: TextView? = null
    var mainCatInfoLayout: ConstraintLayout? = null
    //end new view

    var spinner: Spinner? = null
    var spinner_toolbar: Spinner? = null
    var spinnerSubCat: Spinner? = null
    var textView_msg: TextView? = null
    var textView_idSelectedCat: TextView? = null
    var bAdd: Button? = null
    var etNewCategory: EditText? = null
    var etNewCategoryName: EditText? = null
    var etCatName: EditText? = null
    var etNewSubcat: EditText? = null
    var bRemove: Button? = null
    var bSaveCatName: ImageButton? = null
    var bCancelcatName: ImageButton? = null
    var bUpdate: Button? = null
    var imgEdit: ImageView? = null
    var imgAddSubcat: ImageView? = null
    var imgCategoryIcon: ImageView? = null
    var imgAddnewCategory: ImageView? = null
    var imgMenuOption: ImageView? = null
    lateinit var listView: ListView
    var toolbar: Toolbar? = null
    var layoutIcon: LinearLayout? = null
    var layoutButtonsCatName: LinearLayout? = null

    var selectegCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        init()
        loadRVExpenseCategories()
        generateItem()


        layoutIcon = findViewById(R.id.layoutIconSelect)
        layoutButtonsCatName = findViewById(R.id.loButtonsChangeCatName)
        imgCategoryIcon = findViewById(R.id.acIconCategory)
        textView_msg = findViewById(R.id.msg)
        textView_idSelectedCat = findViewById(R.id.idSelectCategory)
        imgEdit = findViewById(R.id.acEditIconCatName)
        imgMenuOption = findViewById(R.id.acToolbarOptions)
        imgAddnewCategory = findViewById(R.id.acToolbarAdd)
        etNewSubcat = findViewById(R.id.acEtNewSubcat)
        imgAddSubcat = findViewById(R.id.acImgAddSubcat)
        spinner = findViewById(R.id.spinner_sample)
        spinnerSubCat = findViewById(R.id.spinner_subCategoriers)
        loadSpinnerData()
        spinner!!.setOnItemSelectedListener(this)

        toolbar = findViewById(R.id.toolbar)
        spinner_toolbar = findViewById(R.id.spinner_nav_toolbar_ac)
        loadToolbarSpinnerData()
        spinner_toolbar!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedToolbarSpinner(position)
            }
        }


        bAdd = findViewById(R.id.bAddCategory)
        bUpdate = findViewById(R.id.bUpdateCategory)
        etNewCategory = findViewById(R.id.etCategory)
        etNewCategoryName = findViewById(R.id.etNewNameCategory)
        etCatName = findViewById(R.id.acNameEt)
        bSaveCatName = findViewById(R.id.bSaveNewCatName)
        bCancelcatName = findViewById(R.id.bCancelNewCatName)

        imgAddSubcat!!.setOnClickListener {
            if (!etNewSubcat!!.text.toString().equals("")) {
                addSubcategory(etNewSubcat!!.text.toString())
                etNewSubcat!!.onEditorAction(EditorInfo.IME_ACTION_DONE)
                etNewSubcat!!.setText("")
            }
        }

        listView = findViewById<ListView>(R.id.acListview)

        listView!!.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this, "" + i + " + " + l, Toast.LENGTH_SHORT).show()
        }


        bRemove = findViewById(R.id.bRemoveCategory) as Button
        bRemove!!.setOnClickListener {
            removeSelectedCategory(textView_msg!!.text as String)
            loadSpinnerData()
        }

        bAdd!!.setOnClickListener {
            addCategory(etNewCategory!!.text.toString())
            loadSpinnerData()

        }

        bUpdate!!.setOnClickListener {
            updateCategoryName(etNewCategoryName!!.text.toString(), textView_msg!!.text.toString())
            loadSpinnerData()
//            loadListViewSubcat()
        }

        imgEdit!!.setOnClickListener {
            etCatName!!.setEnabled(true)
            layoutButtonsCatName?.visibility = View.VISIBLE
            imgEdit?.visibility = View.GONE
        }

        bSaveCatName!!.setOnClickListener {
            layoutButtonsCatName?.visibility = View.GONE
            imgEdit?.visibility = View.VISIBLE
            etCatName!!.setEnabled(false)
            //update edited Category Name
            val repository = InfoRepository()
            repository.updateCategoryName(etCatName!!.text.toString(), selectegCategory)
            loadToolbarSpinnerData()
            selectegCategory = etCatName!!.text.toString()
        }

        bCancelcatName!!.setOnClickListener {
            layoutButtonsCatName?.visibility = View.GONE
            imgEdit?.visibility = View.VISIBLE
            etCatName!!.setEnabled(false)
            etCatName!!.setText(selectegCategory)
        }

        layoutIcon!!.setOnClickListener {
            val dialog = DialogFragmentIconSelect()
            val fm = this@Category_Activity.fragmentManager
            dialog.show(fm, "DialogFragmentIconSelect")
        }

        imgAddnewCategory!!.setOnClickListener {

            val repository = InfoRepository()
            val labels: List<String> = repository.allCategories as List<String>
            var i = 1
            while (true) {
                if (labels.contains("New Category " + i))
                    i++
                else {
                    addCategory("New Category " + i)
                    selectegCategory = "New Category " + i
                    break
                }
            }
            loadToolbarSpinnerData()
            setSpinnerToolbarSelectedValue(spinner_toolbar!!, selectegCategory!!)
            addSubcategory("subcategory")
            loadRVExpenseCategories()
        }

        imgMenuOption!!.setOnClickListener {
            val popupMenu = PopupMenu(this, imgMenuOption)
            popupMenu.menuInflater.inflate(R.menu.ac_menu_option, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.ac_mo_delete -> {
                        Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
                            .show()

                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("Are you sure you want to delete \"" + selectegCategory + "\" ?")
                            .setCancelable(false)
                            .setPositiveButton(
                                "Yes",
                                DialogInterface.OnClickListener { dialog, id ->
                                    dialog.cancel()
                                    val repository = InfoRepository()
                                    repository.removeCategory(selectegCategory)
                                    loadToolbarSpinnerData()
                                    try {
                                        selectegCategory = spinner_toolbar!!.get(0).toString()
                                    } catch (ex: Exception) {
                                        //your error handling code here
                                        //here, consider adding Log.e("SmsReceiver", ex.localizedMessage)
                                        //this log statement simply prints errors to your android studio terminal and will help with debugging, alternatively leave it out
                                        Toast.makeText(
                                            this,
                                            ex.localizedMessage,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                            .setNegativeButton(
                                "No",
                                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                        val alert = builder.create()
                        alert.show()

                    }

                    R.id.ac_mo_merge ->
                        Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
                            .show()
                }
                true
            })
            popupMenu.show()
        }
    }


    /**
     * Function to load the spinner data from SQLite database
     */
    private fun loadSpinnerData() {
        val repository = InfoRepository()
        val labels: List<String> = repository.allCategories as List<String>

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, labels)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinner!!.adapter = dataAdapter

    }

    private fun loadToolbarSpinnerData() {
        val repository = InfoRepository()
        val labels: List<String> = repository.allCategories as List<String>

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, labels)

        // Drop down layout style - list view with  button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinner_toolbar!!.adapter = dataAdapter
    }

    private fun selectedToolbarSpinner(position: Int) {
        val repository = InfoRepository()
        val labels: List<String> = repository.allCategories as List<String>
        etCatName!!.setText(labels.get(position))
        etCatName!!.setEnabled(false)
        selectegCategory = labels.get(position)

        //add load category icon
        try {
            imgCategoryIcon?.setImageResource(repository.getIdCategoryIcon((labels.get(position))))
        } catch (ex: Exception) {
            //your error handling code here
            //here, consider adding Log.e("SmsReceiver", ex.localizedMessage)
            //this log statement simply prints errors to your android studio terminal and will help with debugging, alternatively leave it out
            Toast.makeText(this, ex.localizedMessage, Toast.LENGTH_SHORT).show()
        }

        loadListViewSubcat(position)
    }

    private fun loadSpinnerSUbcat(idCategory: String) {
        val repository = InfoRepository()
        val labels: List<String> = repository.getSubcategories(idCategory) as List<String>

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, labels)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinnerSubCat!!.adapter = dataAdapter
    }

    override fun onItemSelected(
        parent: AdapterView<*>, view: View?, position: Int, id: Long
    ) {
        val repository = InfoRepository()
//        loadSpinnerData()
        val label = parent.getItemAtPosition(position).toString()

        textView_msg!!.text = "$label"
        // On selecting a spinner item

        textView_idSelectedCat!!.text = "" + repository.getIdCategory("$label")

        loadSpinnerSUbcat(textView_idSelectedCat!!.text.toString())

    }

    fun loadListViewSubcat(position: Int) {
        val arrayAdapter: ListViewVerticalAdapter
        val repository = InfoRepository()
        val labels: List<String> =
            repository.getSubcategories(repository.getIdCategory(selectegCategory)) as List<String>
        arrayAdapter =
            ListViewVerticalAdapter(this, labels as ArrayList<String>?, "" + (position + 1))
        arrayAdapter.setOnShareClickedListener(this)
        listView!!.adapter = arrayAdapter
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
        // TODO Auto-generated method stub
    }

    private fun removeSelectedCategory(name: String) {
        val repository = InfoRepository()
        repository.removeCategory(name)
    }

    private fun addCategory(name: String) {
        val repository = InfoRepository()
        val labels: List<String> = repository.allCategories as List<String>
        var flag = true
        for (i in labels)
            if (i.equals(name))
                flag = false
        if (flag == true && labels.size < 12)
            repository.addCategory(name)
        else if (flag == false)
            Toast.makeText(this, "\"$name\" already exists", Toast.LENGTH_SHORT).show()
        else if (labels.size >= 12)
            Toast.makeText(this, "to much categories", Toast.LENGTH_SHORT).show()
    }

    private fun addSubcategory(name: String) {
        val repository = InfoRepository()
        val labels: List<String> =
            repository.getSubcategories(repository.getIdCategory(spinner_toolbar!!.selectedItem.toString())) as List<String>
        var flag = true
        for (i in labels)
            if (i.equals(name))
                flag = false
        if (flag == true) {
            repository.addSubcategory(
                name,
                repository.getIdCategory(spinner_toolbar!!.selectedItem.toString())
            )
            Toast.makeText(this, "\"$name\" added", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this, "\"$name\" already exists", Toast.LENGTH_SHORT).show()
        loadListViewSubcat(spinner_toolbar!!.selectedItemPosition)
    }

    private fun updateCategoryName(newName: String, oldName: String) {
        val repository = InfoRepository()
        val labels: List<String> = repository.allCategories as List<String>
        var flag = true
        for (i in labels)
            if (i.equals(newName))
                flag = false
        if (flag == true)
            repository.updateCategoryName(newName, oldName)
        else
            Toast.makeText(this, "\"$newName\" already exists", Toast.LENGTH_SHORT).show()
    }

    override fun ShareClicked(url: String?) {
        var newUrl = url!!.substring(1)
        val repository = InfoRepository()
        if (url!!.get(0).equals('0')) {
            Toast.makeText(
                this,
                newUrl + " " + spinner_toolbar!!.selectedItem.toString(), Toast.LENGTH_SHORT
            ).show()
            val repository = InfoRepository()
            repository.removeSubategoryByName(
                newUrl, repository.getIdCategory(spinner_toolbar!!.selectedItem.toString())
            )
            loadListViewSubcat(spinner_toolbar!!.selectedItemPosition)
        } else {
            Toast.makeText(
                this,
                newUrl + " edit " + spinner_toolbar!!.selectedItem.toString(), Toast.LENGTH_SHORT
            ).show()

            showEditItemDialog(
                this@Category_Activity, newUrl,
                repository.getIdCategory(spinner_toolbar!!.selectedItem.toString())!!
            )
//            loadListViewSubcat(spinner_toolbar!!.selectedItemPosition)
        }
//        loadListViewSubcat(spinner_toolbar!!.selectedItemPosition)
    }

    override fun sendInput(input: String) {
        Toast.makeText(this, "send: " + input, Toast.LENGTH_SHORT).show()
        val repository = InfoRepository()
        imgCategoryIcon?.setImageResource(repository.getIdDrawable(input))
        repository.updateCategoryIcon(input, etCatName?.text.toString())
    }

    private fun setSpinnerToolbarSelectedValue(spinner: Spinner, value: Any) {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == value) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun showEditItemDialog(c: Context, oldSubcatrgory: String, idCategory: String) {
        val taskEditText = EditText(c)
        val dialog = AlertDialog.Builder(c)
            .setTitle("Edit subcategory name")
            .setMessage("What do you want to do next?")
            .setView(taskEditText)
            .setPositiveButton("Add") { dialog, which ->
                val task = taskEditText.text.toString()
                val repository = InfoRepository()

                val labels: List<String> =
                    repository.getSubcategories(repository.getIdCategory(spinner_toolbar!!.selectedItem.toString())) as List<String>
                var flag = true
                for (i in labels)
                    if (i.equals(taskEditText.text.toString()))
                        flag = false
                if (flag == true) {
                    repository.updateSubcategoryName(
                        taskEditText.text.toString(),
                        oldSubcatrgory,
                        idCategory
                    )
                    Toast.makeText(
                        this,
                        taskEditText.text.toString() + " added",
                        Toast.LENGTH_SHORT
                    ).show()
                } else
                    Toast.makeText(
                        this,
                        taskEditText.text.toString() + " already exists",
                        Toast.LENGTH_SHORT
                    ).show()

//                repository.updateSubcategoryName(taskEditText.text.toString(), oldSubcatrgory, idCategory)
                loadListViewSubcat(spinner_toolbar!!.selectedItemPosition)
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun init() {
        recyclerViewCategories = findViewById<RecyclerView>(R.id.ac_categories_rv)
        val layoutManager = GridLayoutManager(this, 5)
        recyclerViewCategories.layoutManager = layoutManager
        btnShowExpenses = findViewById(R.id.btnShowExpense)
        btnShowIncome = findViewById(R.id.btnShowIncome)
        listViewSubcategories = findViewById(R.id.ac_subcat_listView)
        txtCategoryName = findViewById(R.id.ac_info_layout_cat_name)
        mainCatInfoLayout = findViewById(R.id.ac_info_layout)
    }

    private fun loadRVExpenseCategories() {
        // on below line we are initializing adapter
        val repository = InfoRepository()
        categoriesRVAdapter =
            RecyclerViewCategoryActivityAdapter(this, repository.allExpenseCategories, this)
        recyclerViewCategories!!.adapter = categoriesRVAdapter
        mainCatInfoLayout!!.visibility = View.GONE
        listViewSubcategories!!.visibility = View.GONE
//        categoriesRVAdapter.notifyDataSetChanged()
    }

    private fun loadRVIncomeCategories() {
        val repository = InfoRepository()
        categoriesRVAdapter =
            RecyclerViewCategoryActivityAdapter(this, repository.allIncomeCategories, this)
        recyclerViewCategories!!.adapter = categoriesRVAdapter
        mainCatInfoLayout!!.visibility = View.GONE
        listViewSubcategories!!.visibility = View.GONE
//        categoriesRVAdapter.notifyDataSetChanged()
    }

    private fun loadLVSubcategories(catName: String) {
        val arrayAdapter: ListViewVerticalAdapter
        val repository = InfoRepository()
        var id = (repository.allCategories!!.indexOf(repository.getIdCategory(catName))).toString()
        val labels: List<String> =
            repository.getSubcategories(repository.getIdCategory(catName)) as List<String>
        arrayAdapter =
            ListViewVerticalAdapter(
                this,
                labels as ArrayList<String>?,
                (repository.allCategories!!.indexOf(repository.getIdCategory(catName))).toString()
            )
        arrayAdapter.setOnShareClickedListener(this)
        listViewSubcategories!!.adapter = arrayAdapter
    }

    // interface from Categories Recycleview
    override fun onCategoryClick(catName: String) {
        Toast.makeText(this, "category: " + catName, Toast.LENGTH_SHORT).show()
        loadLVSubcategories(catName)
        mainCatInfoLayout!!.visibility = View.VISIBLE
        listViewSubcategories!!.visibility = View.VISIBLE
        txtCategoryName!!.setText(catName)
    }

    private fun generateItem() {
        btnShowExpenses!!.setOnClickListener {
            loadRVExpenseCategories()
        }
        btnShowIncome!!.setOnClickListener {
            loadRVIncomeCategories()
            val testLayout: LinearLayout
            testLayout = findViewById(R.id.ac_layoutIconSelect)
            val colorPicker = ColorPicker(this@Category_Activity)
            val colors: ArrayList<String> = ArrayList()
            colors.add("#82B926")
            colors.add("#a276eb")
            colors.add("#6a3ab2")
            colors.add("#666666")
            colors.add("#FFFF00")
            colors.add("#3C8D2F")
            colors.add("#FA9F00")
            colors.add("#FF0000")

            colorPicker
                .setDefaultColorButton(Color.parseColor("#f84c44"))
                .setColors(colors)
                .setColumns(5)
                .setTitle("wybierz kolor")
                .setRoundColorButton(true)
                .disableDefaultButtons(true)
//                .setOnChooseColorListener(object : OnChooseColorListener {
//                    override fun onChooseColor(position: Int, color: Int) {
//                        Toast.makeText(this@Category_Activity, "" + position + " + " + color, Toast.LENGTH_SHORT).show()
//                        val hex = Integer.toString(color, 16)
//                        testLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#$hex")))
//                    }
//
//                    override fun onCancel() {}
//                })
                .addListenerButton(
                    "newButton"
                ) { v, position, color ->
                    Toast.makeText(this, "" + position + " + " + color, Toast.LENGTH_SHORT).show()
                    val hex = Integer.toString(color, 16)
                    testLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#$hex")))
                    colorPicker.dismissDialog()
                }
                .show()
        }
    }

}




