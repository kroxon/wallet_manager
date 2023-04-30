package step.wallet.maganger.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import step.wallet.maganger.R;
import step.wallet.maganger.data.CurrencyDatabase;
import step.wallet.maganger.data.InfoRepository;

public class DialogFragmentAccount extends DialogFragment {

    private static final String TAG = "DialogFragmentAccount";
    public String currencyCode;

    private ImageView cancelImg;
    private ImageView saveImg;
    private TextView selectCurrencyTxt;
    private TextView titleTxt;
    private TextView currencyTxt;
    private TextView descriptionTxt;
    private TextView balanceTxt;
    private AppCompatEditText accNameEt;
    private Dialog currencySelectDialog;
    private ListView listview;
    private ArrayList<String> currencyNames;
    private LinearLayout descLayout;
    private LinearLayout balanceLayout;
    private Bundle data;

    private OnSaveListener saveListener;

    // test interface
    public interface OnInputListener {
        void sendInput(String input);
    }
    public OnInputListener onInputListener;



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
        currencyCode = currency.getCurrencyCode();

        Log.d(TAG, "currency: " + currencyCode);

//        listview.setSelection(currencyNames.indexOf(currency.getCurrencyCode()));
//        listview.setItemChecked(currencyNames.indexOf(currency.getCurrencyCode()), true);
//        Toast.makeText(getContext(),"" + currencyNames.indexOf(currency.getCurrencyCode()), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(),"" + currency.getCurrencyCode(), Toast.LENGTH_SHORT).show();

        data = getArguments();
        if (data != null) {
            loadBundle(data);
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

        saveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data != null) {
                    updateAccount(data);
                } else
                    addAccount();
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
                        int indexCur = currencyDatabase.getCurrenciesNameList().indexOf(adapter.getItem(i));
                        String curSymbol = currencyDatabase.getCurrenciesSymbolList().get(indexCur);
                        selectCurrencyTxt.setText(adapter.getItem(i) + "  " + curSymbol);
                        currencyTxt.setText(curSymbol);
                        currencyCode = currencyDatabase.getCurrenciesList().get(indexCur).getName();
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
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
                descpriptionDialog.getWindow().setLayout(width, height);
                descpriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                descpriptionDialog.show();

                EditText descriptionEt = descpriptionDialog.findViewById(R.id.dAccDDescEt);
                String defaultString = getContext().getString(R.string.description);
                if (descriptionTxt.getText().toString().equals(defaultString)) {
                    descriptionEt.setHint(defaultString);
                    descriptionEt.setSelection(descriptionEt.getText().length());
                } else
                    descriptionEt.setText(descriptionTxt.getText().toString());
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
                        if (descriptionEt.getText().toString().equals(""))
                            descriptionTxt.setText(R.string.description);
                        else
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
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
                descpriptionDialog.getWindow().setLayout(width, height);
                descpriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                descpriptionDialog.show();

                EditText balanceEt = descpriptionDialog.findViewById(R.id.dAccDBalanceEt);
                if (!balanceTxt.getText().toString().equals("0")) {
                    balanceEt.setText(balanceTxt.getText().toString());
                    balanceEt.setSelection(balanceEt.getText().length());
                }
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
                        float valueEt = 0;
                        try {
                            valueEt = Float.parseFloat(balanceEt.getText().toString());
                        } catch (Exception e) {
                            valueEt = 0;
                        }
                        balanceTxt.setText(valueEt + "");
                        descpriptionDialog.dismiss();
                    }
                });
            }
        });


    }

    private void findViews(View view) {
        cancelImg = view.findViewById(R.id.dAccCancel);
        saveImg = view.findViewById(R.id.dAccSave);
        selectCurrencyTxt = view.findViewById(R.id.dAccCurrSelect);
        titleTxt = view.findViewById(R.id.dAccTitleTxt);
        currencyTxt = view.findViewById(R.id.dAccCurrSymbolTxt);
        descriptionTxt = view.findViewById(R.id.dAccDescTxt);
        balanceTxt = view.findViewById(R.id.dAccBalanceTxt);
        descLayout = view.findViewById(R.id.dAccDescLayout);
        balanceLayout = view.findViewById(R.id.dAccBalanceLayout);
        accNameEt = view.findViewById(R.id.accountNameEt);
    }

    private void addAccount() {
        InfoRepository repo = new InfoRepository();
        if (accNameEt.getText().toString().length() != 0) {
            if (!repo.getAllAccountsNames().contains((accNameEt.getText().toString()))) {
                if (repo.getAllAccountsNames().size() < 6) {
                    repo.addAccount(accNameEt.getText().toString(), currencyCode, descriptionTxt.getText().toString(), balanceTxt.getText().toString());
//                    saveListener.onSaveClick("add");
                    onInputListener.sendInput("test");
                    getDialog().dismiss();
                } else
                    Toast.makeText(getContext(), getContext().getString(R.string.to_many_acc), Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getContext(), getContext().getString(R.string.exists), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), getContext().getString(R.string.write_name), Toast.LENGTH_SHORT).show();
    }

    private void updateAccount(Bundle bundle) {
        InfoRepository repo = new InfoRepository();
        if (accNameEt.getText().toString().length() != 0) {
            if (!repo.getAllAccountsNames().contains((accNameEt.getText().toString())) || accNameEt.getText().toString().equals(bundle.getString("name"))) {
                repo.updateAccout(accNameEt.getText().toString(), currencyCode, descriptionTxt.getText().toString(), balanceTxt.getText().toString(), bundle.getString("name"));
                Toast.makeText(getContext(), getContext().getString(R.string.updated), Toast.LENGTH_SHORT).show();
//                saveListener.onSaveClick("update");
                onInputListener.sendInput("test");
                getDialog().dismiss();
            } else
                Toast.makeText(getContext(), getContext().getString(R.string.exists), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), getContext().getString(R.string.write_name), Toast.LENGTH_SHORT).show();
    }

    private void loadBundle(Bundle bundle) {
        titleTxt.setText(getContext().getResources().getString(R.string.edit));
        accNameEt.setText(bundle.getString("name"));
        accNameEt.setSelection(accNameEt.getText().length());
        descriptionTxt.setText(bundle.getString("description"));
        balanceTxt.setText(bundle.getString("balance"));
        currencyCode = bundle.getString("currency");
        CurrencyDatabase currencyDb = new CurrencyDatabase(getContext());
        int indexCurency = currencyDb.getCurrenciesNameList().indexOf(currencyCode);
        String curSymbol = currencyDb.getCurrenciesSymbolList().get(indexCurency);
        currencyTxt.setText(curSymbol);
        selectCurrencyTxt.setText(bundle.getString("currency") + " " + curSymbol);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            saveListener = (OnSaveListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() +
//                    "must implement OnSaveListener");
//        }
//
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onInputListener = (OnInputListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }

    public interface OnSaveListener {
        void onSaveClick(String s);
    }

}
