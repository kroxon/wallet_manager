package step.wallet.maganger.ui;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class DialogFragmentTransaction extends DialogFragment implements HorizontalSubcatRecylerviewAdapter.ItemClickListener {

    private static final String TAG = "DialogFragmentTransaction";
    public String selectedSubcategory;

    @Override
    public void onItemClick(View view, int position, String subcatName) {
        Toast.makeText(getContext(), "You clicked number: " + (position + 1) + ", selected subcategory:  " + subcatName, Toast.LENGTH_SHORT).show();
        selectedSubcategory = subcatName;
    }

    public interface OnInputSelected {
        void sendInput(String input);
    }

    public OnInputSelected mOnInputSelected;

    //widgets
    private ImageView lResultImg;
    private TextView lResultTv;
    private TextView tvInput;
    private TextView btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btn00;
    private TextView btBksp, btDecimal, btClr, dateTv;
    private Spinner dTCatSpinner, actvSubCat;
    private RecyclerView subcatListRV;
    private LinearLayout lResult;
    private HorizontalSubcatRecylerviewAdapter adapter;


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

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
        dateTv = (TextView) view.findViewById(R.id.dateTxt);
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


        // testing dropdown autocomplete Text view

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

        dateTv.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month = month + 1;
                        String dayTwoDigit = "";
                        String monthTwoDigit = "";
                        if (dayOfMonth < 10)
                            dayTwoDigit = "0" + dayOfMonth;
                        else dayTwoDigit = "" + dayOfMonth;
                        if (month < 10)
                            monthTwoDigit = "0" + month;
                        else
                            monthTwoDigit = "" + month;
                        String date = dayTwoDigit + "." + monthTwoDigit + "." + year;
                        dateTv.setText(date);

                    }
                }, year, month, day);
                dialog.show();
            }
        });

        lResultImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "" + finalCategories.get(dTCatSpinner.getSelectedItemPosition()), Toast.LENGTH_SHORT).show();
                repository.writeTransaction(repository.getIdCategory(finalCategories.get(dTCatSpinner.getSelectedItemPosition())), repository.getIdSubcategory(selectedSubcategory, repository.getIdCategory(finalCategories.get(dTCatSpinner.getSelectedItemPosition()))),
                        dateTv.getText().toString(), tvInput.getText().toString(), "0", "note 1", "note 2", "photo1");
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


        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("0");
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("1");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("2");
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("3");
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("4");
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("5");
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("6");
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("7");
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("8");
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInput.getText().toString().equals("0")) {
                    tvInput.setText("");
                }
                tvInput.append("9");
            }
        });

        btBksp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvInput.getText().toString().isEmpty()) {
                    tvInput.setText(tvInput.getText().toString().substring(0, (tvInput.getText().toString().length()) - 1));
                }
            }
        });

        btDecimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvInput.getText().toString().contains(".")) {
                    if (tvInput.getText().toString().equals(""))
                        tvInput.setText("0.");
                    else
                        tvInput.append(".");
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
}
