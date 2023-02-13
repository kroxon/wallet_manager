package step.wallet.maganger.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import step.wallet.maganger.R;
import step.wallet.maganger.adapters.HorizontalSubcatRecylerviewAdapter;
import step.wallet.maganger.adapters.SpinnerCategoryAdapter;
import step.wallet.maganger.data.InfoRepository;

public class DialogFragmentTransaction extends DialogFragment implements HorizontalSubcatRecylerviewAdapter.ItemClickListener, DialogFragmentCategorySelect.OnInputSelected, DialogNotesTransaction.OnInputSend {

    private static final String TAG = "DialogFragmentTransaction";
    public String selectedSubcategory;

    @Override
    public void onItemClick(View view, int position, String subcatName) {
        Toast.makeText(getContext(), "You clicked number: " + (position + 1) + ", selected subcategory:  " + subcatName, Toast.LENGTH_SHORT).show();
        selectedSubcategory = subcatName;
        InfoRepository repository = new InfoRepository();
        writeIdSubcategory = repository.getIdSubcategory(subcatName, writeIdCategory);
    }

    @Override
    public void sendInput(String catName, int catIcon) {
        iconCategorySelected.setImageResource(catIcon);
        categoryNameSelected.setText(catName);
        InfoRepository repository = new InfoRepository();
        List<String> list = repository.getSubcategories(repository.getIdCategory(catName));
        String[] array = list.toArray(new String[0]);
        loadSubcatRecycleViewer(getContext(), array);
        writeIdCategory = repository.getIdCategory(catName);
        writeIdSubcategory = repository.getIdSubcategory(repository.getSubcategories(writeIdCategory).get(0), writeIdCategory);
    }

    @Override
    public void sendNotes(String sendNotes) {
        notesTv.setText(sendNotes);
        writeNote1 = sendNotes;
    }


    public interface OnInputSelected {
        void sendInput(String input);
    }

    public OnInputSelected mOnInputSelected;

    //widgets
    private ImageView lResultImg, iconCategorySelected;
    private TextView lResultTv, categoryNameSelected, plusMinusTv;
    private TextView tvInput, expensesTv, incomeTv, notesTv, tvInputCache, tvInputAlgerba;
    private TextView bDigit0, bDigit1, bDigit2, bDigit3, bDigit4, bDigit5, bDigit6, bDigit7, bDigit8, bDigit9, bDigitBcksp, bDigitDivide, bDigitMultiply, bDigitPlus, bDigitMinus, bDigitEqual, bDigitComma;
    private TextView btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btn00;
    private TextView btBksp, btDecimal, btClr, dateTv1, dateTv2;
    private Spinner dTCatSpinner, actvSubCat;
    private RecyclerView subcatListRV;
    private LinearLayout lResult, expensesUnderline, incomeUnderline;
    private ConstraintLayout conLayTrDatePick, conLayTrCatSelct;
    private HorizontalSubcatRecylerviewAdapter adapter;
    private AlertDialog alertDialogNotes;

    private String oldAlgebraSymbol, transactionType;
    private String writeIdCategory, writeIdSubcategory, writeDate, writeAmount, writeAccount, writeNote1, writeNote2, writePhoto, writeType;


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_transaction, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        InfoRepository repository = new InfoRepository();


//        mActionOk = view.findViewById(R.id.action_ok);
        tvInput = (TextView) view.findViewById(R.id.tvInput);
        tvInputCache = (TextView) view.findViewById(R.id.tvInputCache);
        tvInputAlgerba = (TextView) view.findViewById(R.id.tvInputAlgebra);
        dateTv1 = (TextView) view.findViewById(R.id.dateTxt1);
        dateTv2 = (TextView) view.findViewById(R.id.dateTxt2);
        lResultImg = (ImageView) view.findViewById(R.id.lResultImg);
        lResultTv = (TextView) view.findViewById(R.id.lResultTxt);
//        mActionCancel = view.findViewById(R.id.action_cancel);

        subcatListRV = (RecyclerView) view.findViewById(R.id.dTransactionSubcatRV);
        subcatListRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        subcatListRV.setAdapter(new HorizontalSubcatRecylerviewAdapter(new String [] {"jedzenie", "chemia", "RTV", "czynsz", "woda", "ubezpieczenie", "narzędzie", "farby"}));
        List<String> cats = new ArrayList<String>();
        cats = repository.getAllCategories();
        List<String> subcategoriesList = repository.getSubcategories(repository.getIdCategory(cats.get(0)));
        String[] subcats = subcategoriesList.toArray(new String[0]);
        loadSubcatRecycleViewer(getActivity(), subcats);

        transactionType = "expense";

        btn0 = (TextView) view.findViewById(R.id.btn0);
        btn1 = (TextView) view.findViewById(R.id.btn1);
        btn2 = (TextView) view.findViewById(R.id.btn2);
        btn3 = (TextView) view.findViewById(R.id.btn3);
        btn4 = (TextView) view.findViewById(R.id.btn4);
        btn5 = (TextView) view.findViewById(R.id.btn5);
        btn6 = (TextView) view.findViewById(R.id.btn6);
        btn7 = (TextView) view.findViewById(R.id.btn7);
        btn8 = (TextView) view.findViewById(R.id.btn8);
        btn9 = (TextView) view.findViewById(R.id.btn9);
        btn00 = (TextView) view.findViewById(R.id.btndouble0);
        lResult = (LinearLayout) view.findViewById(R.id.lResult);
        btBksp = (TextView) view.findViewById(R.id.btnbksp);
        btDecimal = (TextView) view.findViewById(R.id.btndecimal);
        btClr = (TextView) view.findViewById(R.id.btnclr);

        writeIdCategory = repository.getIdCategory(repository.getAllCategories().get(0));
        writeIdSubcategory = repository.getIdSubcategory(repository.getSubcategories(writeIdCategory).get(0), writeIdCategory);
        Calendar calendarWriting = Calendar.getInstance();
        writeDate = calendarWriting.get(Calendar.DATE) + "." + calendarWriting.get(Calendar.MONTH) + 1 + "." + calendarWriting.get(Calendar.YEAR);
        writeAmount = "1000";
        writeAccount = "0";
        writeNote1 = "Notatki....";
        writeNote2 = "note 2";
        writePhoto = "photo1";
        writeType = "expense";

        bDigit0 = (TextView) view.findViewById(R.id.trDigit0);
        bDigit1 = (TextView) view.findViewById(R.id.trDigit1);
        bDigit2 = (TextView) view.findViewById(R.id.trDigit2);
        bDigit3 = (TextView) view.findViewById(R.id.trDigit3);
        bDigit4 = (TextView) view.findViewById(R.id.trDigit4);
        bDigit5 = (TextView) view.findViewById(R.id.trDigit5);
        bDigit6 = (TextView) view.findViewById(R.id.trDigit6);
        bDigit7 = (TextView) view.findViewById(R.id.trDigit7);
        bDigit8 = (TextView) view.findViewById(R.id.trDigit8);
        bDigit9 = (TextView) view.findViewById(R.id.trDigit9);
        bDigitBcksp = (TextView) view.findViewById(R.id.trDigitBcsp);
        bDigitDivide = (TextView) view.findViewById(R.id.trDigitDivide);
        bDigitMultiply = (TextView) view.findViewById(R.id.trDigitMultiply);
        bDigitPlus = (TextView) view.findViewById(R.id.trDigitPlus);
        bDigitMinus = (TextView) view.findViewById(R.id.trDigitMinus);
        bDigitEqual = (TextView) view.findViewById(R.id.trDigitEqual);
        bDigitComma = (TextView) view.findViewById(R.id.trDigitComma);

        conLayTrDatePick = (ConstraintLayout) view.findViewById(R.id.trlayoutDatePicker);
        conLayTrCatSelct = (ConstraintLayout) view.findViewById(R.id.trCategorySelectLayout);

        iconCategorySelected = (ImageView) view.findViewById(R.id.trIconCategory);
        categoryNameSelected = (TextView) view.findViewById(R.id.trNameCategory);

        expensesTv = (TextView) view.findViewById(R.id.trExpenses);
        incomeTv = (TextView) view.findViewById(R.id.trIncome);
        plusMinusTv = (TextView) view.findViewById(R.id.trPlusMinusTv);
        expensesUnderline = (LinearLayout) view.findViewById(R.id.trExpensesUnderline);
        incomeUnderline = (LinearLayout) view.findViewById(R.id.trIncomeUnderline);
        notesTv = (TextView) view.findViewById(R.id.trNotesTv);


        iconCategorySelected.setImageResource(repository.getIdCategoryIcon(repository.getAllCategories().get(0)));
        categoryNameSelected.setText(repository.getAllCategories().get(0));

        // Category Spinner Drop down elements
        dTCatSpinner = (Spinner) view.findViewById(R.id.dTransactionCatSpinner);
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        List<String> categories = new ArrayList<String>();
        categories = repository.getAllCategories();
        List<String> finalCategories = categories;

        SpinnerCategoryAdapter adapter = new SpinnerCategoryAdapter(getContext(), finalCategories);
        adapter.setDropDownViewResource(R.layout.dialog_transaction_category_dropdown);
        dTCatSpinner.setAdapter(adapter);
//        textInputLayout.setStartIconDrawable(repository.getIdCategoryIcon(finalCategories.get(1)));

        dTCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "" + finalCategories.get(i), Toast.LENGTH_SHORT).show();
                List<String> subcategoriesList = repository.getSubcategories(repository.getIdCategory(finalCategories.get(i)));
                String[] subcategories = subcategoriesList.toArray(new String[0]);
//                subcatListRV.setAdapter(new HorizontalSubcatRecylerviewAdapter(subcategories));
                loadSubcatRecycleViewer(getActivity(), subcategories);
                try {
                    selectedSubcategory = subcategoriesList.get(0);
                } catch (Exception e) {
                    selectedSubcategory = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dateTv1.setText(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));
        dateTv2.setText(new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(new Date()));
        conLayTrDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month = month + 1;
                        SimpleDateFormat format = new SimpleDateFormat("MMMM");// also you can use: "yyyy-MMMM-dd"
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(year, month - 1, dayOfMonth);
                        String monthTwoDigit = format.format(calendar1.getTime());
                        dateTv1.setText(dayOfMonth + "");
                        dateTv2.setText(monthTwoDigit + " " + year);
                        writeDate = dayOfMonth + "." + month + "." + year;
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        lResultImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "" + finalCategories.get(dTCatSpinner.getSelectedItemPosition()), Toast.LENGTH_SHORT).show();
//                repository.writeTransaction(repository.getIdCategory(finalCategories.get(dTCatSpinner.getSelectedItemPosition())), repository.getIdSubcategory(selectedSubcategory, repository.getIdCategory(finalCategories.get(dTCatSpinner.getSelectedItemPosition()))),
//                        dateTv1.getText().toString(), tvInput.getText().toString(), "0", "note 1", "note 2", "photo1", "expense");
            }
        });

        conLayTrCatSelct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentCategorySelect dialog = new DialogFragmentCategorySelect();
                dialog.setTargetFragment(DialogFragmentTransaction.this, 1);
                dialog.show(getFragmentManager(), "DialogFragmentCategorySelect");
            }
        });

        expensesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomeUnderline.setVisibility(View.INVISIBLE);
                expensesUnderline.setVisibility(View.VISIBLE);
                expensesTv.setTextColor(Color.WHITE);
                incomeTv.setTextColor(Color.parseColor("#C1BFBF"));
                conLayTrCatSelct.setVisibility(View.VISIBLE);
                plusMinusTv.setText("-");
                writeType = "expense";
            }
        });

        incomeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesUnderline.setVisibility(View.INVISIBLE);
                incomeUnderline.setVisibility(View.VISIBLE);
                incomeTv.setTextColor(Color.WHITE);
                expensesTv.setTextColor(Color.parseColor("#C1BFBF"));
                conLayTrCatSelct.setVisibility(View.GONE);
                plusMinusTv.setText("+");
                writeType = "income";
            }
        });

        bDigitDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAlgebraSymbol("÷");
            }
        });
        bDigitMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAlgebraSymbol("×");
            }
        });
        bDigitPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAlgebraSymbol("+");
            }
        });
        bDigitMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAlgebraSymbol("-");
            }
        });
        bDigitEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (bDigitEqual.getText().toString().equals("✓"))
//                    repository.writeTransaction();
                makeAlgebraSymbol("=");
                if (bDigitEqual.getText().toString().equals("✓"))
                    writeTransaction(getContext());
                if (bDigitEqual.getText().toString().equals("="))
                    bDigitEqual.setText("✓");
            }
        });

//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        View alertNotesView = getLayoutInflater().inflate(R.layout.dialog_tr_notes, null);
//        EditText notesEt = (EditText) alertNotesView.findViewById(R.id.trEtNotes);
//        ImageButton notesCancelImg = (ImageButton) alertNotesView.findViewById(R.id.trNotesCancel);
//        Button notesOkButton = (Button) alertNotesView.findViewById(R.id.trNotesOk);
//        notesOkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                notesTv.setText(notesEt.getText().toString());
//                alertDialogNotes.dismiss();
//            }
//        });
//        notesCancelImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialogNotes.dismiss();
//            }
//        });
//        builder.setView(alertNotesView);
//        alertDialogNotes = builder.create();
//
//
        notesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogNotesTransaction dialogNotes = new DialogNotesTransaction();
                dialogNotes.setValue(notesTv.getText().toString());
                dialogNotes.setTargetFragment(DialogFragmentTransaction.this, 1);
                dialogNotes.show(getFragmentManager(), "DialogFragmentNotes");
            }
        });


//        mActionCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDialog().dismiss();
//            }
//        });
//
//        mActionOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String input = mInput.getText().toString();
//                if (!input.equals("")) {
//
//                    //Easiest way: just set the value.
////                    FirstFragment firstFragment = (FirstFragment) getActivity().getFragmentManager().findFragmentByTag("FirstFragment");
////                    firstFragment.mInputDisplay.setText(input);
//
//                    mOnInputSelected.sendInput(input);
//                }
//
//
//                getDialog().dismiss();
//            }
//        });


        bDigit0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("0");
            }
        });

        bDigit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("1");
            }
        });

        bDigit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("2");
            }
        });

        bDigit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("3");
            }
        });

        bDigit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("4");
            }
        });

        bDigit5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("5");
            }
        });

        bDigit6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("6");
            }
        });

        bDigit7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("7");
            }
        });

        bDigit8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("8");
            }
        });

        bDigit9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("9");
            }
        });

        bDigitBcksp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvInput.getText().toString().isEmpty() && !tvInput.getText().toString().equals("0")) {
                    tvInput.setText(tvInput.getText().toString().substring(0, (tvInput.getText().toString().length()) - 1));
                }
                if (tvInput.getText().toString().endsWith("."))
                    bDigitEqual.setText("=");
                if (tvInput.getText().toString().equals(""))
                    tvInput.setText("0");
                else if (tvInput.getText().toString().equals("0")) {
                    tvInputAlgerba.setText("");
                    tvInputCache.setText("");
                }
            }
        });

        bDigitComma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvInput.getText().toString().contains(".")) {
                    if (tvInput.getText().toString().equals(""))
                        tvInput.setText("0.");
                    else
                        tvInput.append(".");
                    bDigitEqual.setText("=");
                }
            }
        });

        btn00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvInput.getText().toString().equals("0") && !tvInput.getText().toString().equals(""))
                    tvInput.append("00");

            }
        });

        btClr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvInput.setText("");
            }
        });


        return view;
    }

    public void loadSubcategiriesList(String categoryId) {

    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            mOnInputSelected = (OnInputSelected) getTargetFragment();
//        } catch (ClassCastException e) {
//            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage());
//        }
//    }
//
//    public void onDigit(TextView textView) {                                            // This function gets executed when a number is clicked
//        if (tvInput.getText().toString().equals("0")) {
//            tvInput.setText("");
//        }
//        tvInput.append(textView.getText());
//        num = true;
//        dot = false;
//    }
//
//    public void onBksp(TextView textView) {                                                             // Deletes the previous elements
//        if (!tvInput.getText().toString().isEmpty()) {
//            tvInput.setText(tvInput.getText().toString().substring(0, (tvInput.getText().toString().length()) - 1));
//        }
//    }
//
//    public void onClear(TextView textView) {                                          // Clears the screen
//        tvInput.setText("");
//        num = false;
//        dot = false;
//    }
//
//    public void decimalPoint(TextView textView) {                                   // Checks whether the decimal is present or not
//        if (num && !dot) {
//            tvInput.append(".");
//            dot = true;
//            num = false;
//        }
//    }
//
//    private String reducezeroes(String result) {                      // This reduces the unnecessary decimals
//        String finalvalue = result;
//
//        if (result.contains(".0")) {
//            finalvalue = result.substring(0, result.length() - 2);
//        }
//        return finalvalue;
//    }
//
//    public void onOperator(TextView textView) {                                     // This function gets executed when operators are clicked
//        if (num && !isoperatorthere(tvInput.getText().toString())) {
//            tvInput.append(textView.getText().toString());
//            num = false;
//            dot = false;
//        }
//    }
//
//
//    private Boolean isoperatorthere(String value) {       // This shit checks whether the no. has a - sign before it
//        if (value.startsWith("-")) {
//            return false;
//        } else {
//            return value.contains("+") || value.contains("-") || value.contains("*") || value.contains("÷") || value.contains("%");
//        }
//    }
//
//    public void onEqual() {                       // This is where the calculator does its shit
//        if (num) {
//
//            String value = tvInput.getText().toString();
//            String prefixcheckker = "";
//
//            try {                                      // In try block cuz some people do division by zero....
//
//                if (value.startsWith("-")) {
//                    prefixcheckker = "-";
//                    value = value.substring(1);
//                }
//
//                if (value.contains("-")) {
//                    String splitvalue[] = value.split("-");
//                    String num1 = splitvalue[0];
//                    String num2 = splitvalue[1];
//
//                    if (!prefixcheckker.isEmpty()) {
//                        num1 = prefixcheckker + num1;
//                    }
//
//                    tvInput.setText(reducezeroes(String.valueOf(Double.parseDouble(num1) - Double.parseDouble(num2))));
//                } else if (value.contains("+")) {
//                    String[] splitvalue = value.split("\\+");
//                    String num1 = splitvalue[0];
//                    String num2 = splitvalue[1];
//
//                    if (!prefixcheckker.isEmpty()) {
//                        num1 = prefixcheckker + num1;
//                        tvInput.setText(reducezeroes(String.valueOf(Double.parseDouble(num1) + Double.parseDouble(num2))));
//                    } else {
//                        tvInput.setText((reducezeroes(String.valueOf(Double.parseDouble(num1) + Double.parseDouble(num2)))));
//                    }
//                } else if (value.contains("*")) {
//                    String splitvalue[] = value.split("\\*");
//                    String num1 = splitvalue[0];
//                    String num2 = splitvalue[1];
//
//                    if (!prefixcheckker.isEmpty()) {
//                        num1 = prefixcheckker + num1;
//                        tvInput.setText(reducezeroes(String.valueOf(Double.parseDouble(num1) * Double.parseDouble(num2))));
//                    } else {
//                        tvInput.setText(reducezeroes(String.valueOf(Double.parseDouble(num1) * Double.parseDouble(num2))));
//                    }
//                } else if (value.contains("÷")) {
//                    String [] splitvalue = value.split("÷");
//                    String num1 = splitvalue[0];
//                    String num2 = splitvalue[1];
//
//                    if (!prefixcheckker.isEmpty()) {
//                        num1 = prefixcheckker + num1;
//                        tvInput.setText(reducezeroes(String.valueOf(Double.parseDouble(num1) / Double.parseDouble(num2))));
//                    } else {
//                        tvInput.setText(reducezeroes(String.valueOf(Double.parseDouble(num1) / Double.parseDouble(num2))));
//                    }
//                } else if (value.contains("%")) {
//                    String [] splitvalue = value.split("%");
//                    String num1 = splitvalue[0];
//                    String num2 = splitvalue[1];
//
//                    tvInput.setText(reducezeroes(String.valueOf(Double.parseDouble(num1) * Double.parseDouble(num2) / 100)));
//                }
//            } catch (ArithmeticException e){
//                e.printStackTrace();
//            }
//
//        }
//    }

    public void loadSubcatRecycleViewer(Context context, String[] items) {
        adapter = new HorizontalSubcatRecylerviewAdapter(getActivity(), items);
        adapter.setClickListener(this);
        subcatListRV.setAdapter(adapter);
    }

    public void makeAlgebraSymbol(String newAlgebraSymbol) {
        String inputValue = tvInput.getText().toString();
        inputValue = inputValue.replace(",", "");
        if (inputValue.endsWith("."))
            inputValue = inputValue.replace(".", "");
        if (!inputValue.equals(""))
            inputValue = makeRound(inputValue);
        String cacheValue = tvInputCache.getText().toString();
        oldAlgebraSymbol = tvInputAlgerba.getText().toString();

        if (!inputValue.equals("") && cacheValue.equals("") && !newAlgebraSymbol.equals("=")) {
            tvInputCache.setText(inputValue);
            tvInputAlgerba.setText(newAlgebraSymbol);
            tvInput.setText("");
            bDigitEqual.setText("=");
        }
        if (!inputValue.equals("") && cacheValue.equals("") && newAlgebraSymbol.equals("=")) {
            tvInputAlgerba.setText("");
            bDigitEqual.setText("✓");
        }
        if (!inputValue.equals("") && !cacheValue.equals("") && !newAlgebraSymbol.equals("=")) {
            tvInputCache.setText(makeMath(cacheValue, inputValue, oldAlgebraSymbol));
            tvInput.setText("");
            tvInputAlgerba.setText(newAlgebraSymbol);
            bDigitEqual.setText("=");
        }
        if (!inputValue.equals("") && !cacheValue.equals("") && newAlgebraSymbol.equals("=")) {
            tvInputCache.setText("");
            tvInput.setText(makeMath(cacheValue, inputValue, oldAlgebraSymbol));
            tvInputAlgerba.setText("");
            bDigitEqual.setText("✓");
        }
        if (inputValue.equals("") && cacheValue.equals("")) {
            tvInputAlgerba.setText("");
            bDigitEqual.setText("=");
        }
        if (inputValue.equals("") && !cacheValue.equals("") && !newAlgebraSymbol.equals("=")) {
            tvInputAlgerba.setText(newAlgebraSymbol);
            bDigitEqual.setText("=");
        }
        if (inputValue.equals("") && !cacheValue.equals("") && newAlgebraSymbol.equals("=")) {
            tvInputCache.setText("");
            tvInputAlgerba.setText("");
            tvInput.setText(cacheValue);
            bDigitEqual.setText("✓");
        }
        if (!inputValue.equals("") && cacheValue.equals("") && oldAlgebraSymbol.equals("") && newAlgebraSymbol.equals("=")) {
            tvInput.setText(makeRound(inputValue));
            bDigitEqual.setText("✓");
        }
    }

    public String makeMath(String variableA, String variableB, String algebraSymb) {
        float mResult = 0;
        String resultString;
        float a = Float.parseFloat(variableA);
        float b = Float.parseFloat(variableB);
        switch (algebraSymb) {
            case "÷":
                if (b != 0)
                    mResult = a / b;
                else
                    mResult = 0;
                break;
            case "×":
                mResult = a * b;
                break;

            case "+":
                mResult = a + b;
                break;
            case "-":
                mResult = a - b;
                break;
        }
        String decimalPart = String.format("%.2f", mResult % 1);
        decimalPart = decimalPart.substring(decimalPart.length() - 2);
        String integerPart = String.format("%.0f", mResult / 1);
        if (decimalPart.equals("00"))
            resultString = integerPart;
        else
            resultString = integerPart + "." + decimalPart;
        return resultString;
    }

    public String makeRound(String variable) {
        String resultString;
        float a = Float.parseFloat(variable);
        String decimalPart = String.format("%.2f", a % 1);
        decimalPart = decimalPart.substring(decimalPart.length() - 2);
        String integerPart = String.format("%.0f", a / 1);
        if (decimalPart.equals("00"))
            resultString = integerPart;
        else
            resultString = integerPart + "." + decimalPart;

        plusMinusTv.setText(integerPart + " - " + decimalPart);

        return resultString;
    }

    public void writeTransaction(Context context) {
        AlertDialog.Builder alertWriteTransactionConfirm = new AlertDialog.Builder(context);

        //message
        alertWriteTransactionConfirm.setMessage("Save?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "Successfully saved!", Toast.LENGTH_SHORT).show();

                        InfoRepository repository = new InfoRepository();
                        repository.writeTransaction(writeIdCategory, writeIdSubcategory, writeDate, tvInput.getText().toString(), writeAccount, notesTv.getText().toString(), writeNote2, writePhoto, writeType);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "Entry cancelled!", Toast.LENGTH_SHORT).show();
//                        getDialog().cancel();
                    }
                }).show();
    }

}
