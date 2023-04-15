package step.wallet.maganger.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import step.wallet.maganger.R;
import step.wallet.maganger.adapters.HorizontalSubcatRecylerviewAdapter;
import step.wallet.maganger.adapters.SpinnerCategoryAdapter;
import step.wallet.maganger.data.CurrencyDatabase;
import step.wallet.maganger.data.InfoRepository;

public class DialogFragmentAccount extends DialogFragment {

    private static final String TAG = "DialogFragmentAccount";
    public String selectedSubcategory;

    private ImageView cancelImg;
    private ImageView saveImg;
    private TextView selectCurrencyTxt;
    private TextView currencyTxt;
    private TextView descriptionTxt;
    private TextView balanceTxt;
    private Dialog currencySelectDialog;
    private ListView listview;
    private ArrayList<String> currencyNames;
    private LinearLayout descLayout;
    private LinearLayout balanceLayout;


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
        View view = inflater.inflate(R.layout.dialog_add_account, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        InfoRepository repository = new InfoRepository();

        findViews(view);
        initiateViews();

        Currency currency = Currency.getInstance(Locale.getDefault());
        currencyTxt.setText(currency.getSymbol());

//        listview.setSelection(currencyNames.indexOf(currency.getCurrencyCode()));
//        listview.setItemChecked(currencyNames.indexOf(currency.getCurrencyCode()), true);
//        Toast.makeText(getContext(),"" + currencyNames.indexOf(currency.getCurrencyCode()), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(),"" + currency.getCurrencyCode(), Toast.LENGTH_SHORT).show();

        Bundle data = getArguments();
        if (data != null) {
            Toast.makeText(getContext(), data.getString("key"), Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void initiateViews() {
        cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        selectCurrencyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currencySelectDialog = new Dialog(getContext());
                currencySelectDialog.setContentView(R.layout.dialog_acc_currency_select);
                currencySelectDialog.getWindow().setLayout(650, 1200);
                currencySelectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                currencySelectDialog.show();

                EditText editText = currencySelectDialog.findViewById(R.id.dAccDCurrEt);
                listview = currencySelectDialog.findViewById(R.id.dAccDCurrRv);

                CurrencyDatabase currencyDatabase = new CurrencyDatabase(getContext());
                currencyNames = currencyDatabase.getCurrenciesNameList();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, currencyNames);
                listview.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String curSymbol = currencyDatabase.getCurrenciesSymbolList().get(currencyDatabase.getCurrenciesNameList().indexOf(adapter.getItem(i)));
                        selectCurrencyTxt.setText(adapter.getItem(i) + "  " + curSymbol);
                        currencyTxt.setText(curSymbol);
                        currencySelectDialog.dismiss();
                    }
                });
            }
        });

        descLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog descpriptionDialog = new Dialog(getContext());
                descpriptionDialog.setContentView(R.layout.dialog_acc_description);
                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);
                descpriptionDialog.getWindow().setLayout(width, height);
                descpriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                descpriptionDialog.show();

                EditText descriptionEt = descpriptionDialog.findViewById(R.id.dAccDDescEt);
                ImageView cancelDescImg = descpriptionDialog.findViewById(R.id.dAccDescCancel);
                ImageView saveDescImg = descpriptionDialog.findViewById(R.id.dAccDescSave);

                cancelDescImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        descpriptionDialog.dismiss();
                    }
                });

                saveDescImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        descriptionTxt.setText(descriptionEt.getText().toString());
                        descpriptionDialog.dismiss();
                    }
                });
            }
        });

        balanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog descpriptionDialog = new Dialog(getContext());
                descpriptionDialog.setContentView(R.layout.dialog_acc_balance);
                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);
                descpriptionDialog.getWindow().setLayout(width, height);
                descpriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                descpriptionDialog.show();

                EditText descriptionEt = descpriptionDialog.findViewById(R.id.dAccDBalanceEt);
                ImageView cancelDescImg = descpriptionDialog.findViewById(R.id.dAccBalanceCancel);
                ImageView saveDescImg = descpriptionDialog.findViewById(R.id.dAccBalanceSave);

                cancelDescImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        descpriptionDialog.dismiss();
                    }
                });

                saveDescImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        balanceTxt.setText(descriptionEt.getText().toString());
                        descpriptionDialog.dismiss();
                    }
                });
            }
        });


    }

    private void findViews(View view) {
        cancelImg = view.findViewById(R.id.dAccCancel);
        selectCurrencyTxt = view.findViewById(R.id.dAccCurrSelect);
        currencyTxt = view.findViewById(R.id.dAccCurrSymbolTxt);
        descriptionTxt = view.findViewById(R.id.dAccDescTxt);
        balanceTxt = view.findViewById(R.id.dAccBalanceTxt);
        descLayout = view.findViewById(R.id.dAccDescLayout);
        balanceLayout = view.findViewById(R.id.dAccBalanceLayout);
    }

}
