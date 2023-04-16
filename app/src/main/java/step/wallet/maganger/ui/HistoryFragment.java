package step.wallet.maganger.ui;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.adapters.ListViewVerticalHistoryAdapter;
import step.wallet.maganger.classes.Transaction;
import step.wallet.maganger.data.InfoRepository;

public class HistoryFragment extends Fragment implements ListViewVerticalHistoryAdapter.ItemClickListener, DialogFragmentFilterHistoryTransaction.OnInputSend {

    private OnFragmentInteractionListener mListener;
    private ListViewVerticalHistoryAdapter adapter;
    private RecyclerView recyclerViewTransactions;
    private ImageView btnFilter;
    private Button btnTest;

    // ititial filter
    private double pAmountFrom = 0.0;
    private double pAmountTo = 99999999.99;
    private String pCurrency = "0";
    private String pPeriod = "0";
    private Long pPeriodFrom = Long.valueOf(0);
    private Long pPeriodTo = Long.valueOf(0);
    private String pTypeOperation = "All";
    private String pAccount = "0";

    public HistoryFragment() {
        // Required empty public constructor
    }

    private TextView testTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        btnFilter = view.findViewById(R.id.btnHistoryFilter);
        btnTest = view.findViewById(R.id.btnHistoryTest);

        //start test
        recyclerViewTransactions = (RecyclerView) view.findViewById(R.id.historyTransactionList);
        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        loadTransaciotns();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentFilterHistoryTransaction dialog = new DialogFragmentFilterHistoryTransaction();
                dialog.setValue(pAmountFrom, pAmountTo, pCurrency, pPeriod, pPeriodFrom, pPeriodTo, pTypeOperation, pAccount);
                dialog.setTargetFragment(HistoryFragment.this, 1);
                dialog.show(getFragmentManager(), "DialogFragmentFilterTra");
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTransaciotns();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(View view, int position, String s) {

    }

    @Override
    public void sendData(double sAmountFrom, double sAmountTo, @NotNull String sCurrency, @NonNull String sPeriod, @org.jetbrains.annotations.Nullable Long sPeriodFrom, @org.jetbrains.annotations.Nullable Long sPeriodTo, @NotNull String sTypeOper, @NotNull String sAccount) {
        pAmountFrom = sAmountFrom;
        pAmountTo = sAmountTo;
        pCurrency = sCurrency;
        pPeriod = sPeriod;
        pPeriodFrom = sPeriodFrom;
        pPeriodTo = sPeriodTo;
        pTypeOperation = sTypeOper;
        pAccount = sAccount;
        Toast.makeText(getContext(), "received: " + pAmountFrom + pAmountTo + pCurrency + pPeriod + pPeriodFrom + pPeriodTo + pTypeOperation + pAccount, Toast.LENGTH_LONG);
        loadSpecificTransaciotns();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }

    private void loadTransaciotns() {
        // on below line we are initializing adapter
        List<String> cats = new ArrayList<String>();
        InfoRepository infoRepository = new InfoRepository();
        ArrayList<Transaction> transactions;
        transactions = infoRepository.readTransactions("0");

        adapter = new ListViewVerticalHistoryAdapter(getActivity(), transactions);
        adapter.setClickListener(this);
        recyclerViewTransactions.setAdapter(adapter);
    }

    private void loadSpecificTransaciotns() {
        // on below line we are initializing adapter
        List<String> cats = new ArrayList<String>();
        InfoRepository infoRepository = new InfoRepository();
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        ArrayList<Transaction> transactions2;

        if (pTypeOperation.equals("All")) {
            transactions = infoRepository.getSpecificTransactions("0", pAmountFrom, pAmountTo, "PLN", pPeriodFrom, pPeriodTo, "expense");
            transactions2 = infoRepository.getSpecificTransactions("0", pAmountFrom, pAmountTo, "PLN", pPeriodFrom, pPeriodTo, "income");
            transactions.addAll(transactions2);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                transactions.sort((e1, e2) -> new Long(e1.getTransactionDateFormat()).compareTo(new Long(e2.getTransactionDateFormat())));
            }
            Collections.reverse(transactions);
        }
        if (pTypeOperation.equals("debit")) {
            transactions = infoRepository.getSpecificTransactions("0", pAmountFrom, pAmountTo, "PLN", pPeriodFrom, pPeriodTo, "expense");
        }
        if (pTypeOperation.equals("credit")) {
            transactions = infoRepository.getSpecificTransactions("0", pAmountFrom, pAmountTo, "PLN", pPeriodFrom, pPeriodTo, "income");
        }



        adapter = new ListViewVerticalHistoryAdapter(getActivity(), transactions);
        adapter.setClickListener(this);
        recyclerViewTransactions.setAdapter(adapter);
    }

}