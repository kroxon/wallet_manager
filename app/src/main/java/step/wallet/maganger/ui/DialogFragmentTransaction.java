package step.wallet.maganger.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.adapters.HorizontalSubcatRecylerviewAdapter;
import step.wallet.maganger.adapters.SpinnerCategoryAdapter;
import step.wallet.maganger.data.InfoRepository;

public class DialogFragmentTransaction extends DialogFragment {

    private static final String TAG = "DialogFragmentTransaction";

    public interface OnInputSelected {
        void sendInput(String input);
    }

    public OnInputSelected mOnInputSelected;

    //widgets
    private TextView mActionOk, mActionCancel, tvInput;
    private TextView btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btn00;
    private TextView btBksp, btDecimal, btClr;
    private AutoCompleteTextView actvCat, actvSubCat;
    private RecyclerView subcatList;

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

//        mActionOk = view.findViewById(R.id.action_ok);
        tvInput = view.findViewById(R.id.tvInput);
//        mActionCancel = view.findViewById(R.id.action_cancel);

        subcatList = view.findViewById(R.id.dTransactionSubcatRV);
        subcatList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        subcatList.setAdapter(new HorizontalSubcatRecylerviewAdapter(new String [] {"jedzenie", "chemia", "RTV", "czynsz", "woda", "ubezpieczenie", "narzędzie", "farby"}));


        btn0 = view.findViewById(R.id.btn0);
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        btn4 = view.findViewById(R.id.btn4);
        btn5 = view.findViewById(R.id.btn5);
        btn6 = view.findViewById(R.id.btn6);
        btn7 = view.findViewById(R.id.btn7);
        btn8 = view.findViewById(R.id.btn8);
        btn9 = view.findViewById(R.id.btn9);
        btn00 = view.findViewById(R.id.btndouble0);

        btBksp = view.findViewById(R.id.btnbksp);
        btDecimal = view.findViewById(R.id.btndecimal);
        btClr = view.findViewById(R.id.btnclr);


        // testing dropdown autocomplete Text view

        // Category Spinner Drop down elements
        actvCat = view.findViewById(R.id.catAutoCompleteTv);
        TextInputLayout textInputLayout = view.findViewById(R.id.dTransactionCatSpinner);
        InfoRepository repository = new InfoRepository();
        List<String> categories = new ArrayList<String>();
        categories = repository.getAllCategories();
        List<String> finalCategories = categories;

        SpinnerCategoryAdapter adapter = new SpinnerCategoryAdapter (getContext(), categories);
        adapter.setDropDownViewResource(R.layout.dialog_transaction_category_dropdown);
        actvCat.setAdapter(adapter);
        actvCat.setListSelection(0);
//        textInputLayout.setStartIconDrawable(repository.getIdCategoryIcon(finalCategories.get(1)));

        actvCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int w = repository.getIdCategoryIcon(finalCategories.get(i));
                textInputLayout.setStartIconDrawable(w);
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
}
