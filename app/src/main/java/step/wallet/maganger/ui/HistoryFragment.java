package step.wallet.maganger.ui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import step.wallet.maganger.R;
import step.wallet.maganger.adapters.ListViewVerticalHistoryAdapter;
import step.wallet.maganger.classes.CurrencyStrings;
import step.wallet.maganger.classes.Transaction;
import step.wallet.maganger.data.CurrencyDatabase;
import step.wallet.maganger.data.InfoRepository;

public class HistoryFragment extends Fragment implements ListViewVerticalHistoryAdapter.ItemClickListener, DialogFragmentFilterHistoryTransaction.OnInputSend, DialogFragmentTransaction.OnInputSelected {

    private OnFragmentInteractionListener mListener;
    private ListViewVerticalHistoryAdapter adapter;
    private RecyclerView recyclerViewTransactions;
    private ImageView btnFilter;


    ArrayList<Transaction> transactions;


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

        Transaction[] deleteTransaction = new Transaction[1];

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteTransaction[0] = transactions.get(position);
                InfoRepository repository = new InfoRepository();
                repository.removeTransaction(deleteTransaction[0].getTransactionId());
                transactions.remove(position);
                adapter.notifyDataSetChanged();

                // below line is to display our snackbar with action.
                Snackbar.make(recyclerViewTransactions, getResources().getString(R.string.delete), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.

                        repository.writeTransaction(deleteTransaction[0].getTransactionIdCategory(), deleteTransaction[0].getTransactionIdSubcategory(), String.valueOf(deleteTransaction[0].getTransactionDateInMilis()),
                                deleteTransaction[0].getTransactionValue(), deleteTransaction[0].getIdAccount(), deleteTransaction[0].getCurency(), deleteTransaction[0].getTransactionNote1(),
                                deleteTransaction[0].getTransactionNote2(), deleteTransaction[0].getTransactionPhoto(), deleteTransaction[0].getTransactionType());
                        transactions.add(position, deleteTransaction[0]);

                        // below line is to notify item is
                        // added to our adapter class.
                        adapter.notifyItemChanged(position);
                        adapter.notifyDataSetChanged();

                    }
                }).show();

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_red_light))
                        .addSwipeRightActionIcon(R.drawable.ic_delete)
                        .setSwipeRightActionIconTint(ContextCompat.getColor(getActivity(), R.color.white))
                        .addSwipeRightLabel(getResources().getString(R.string.delete))
                        .setSwipeRightLabelColor(ContextCompat.getColor(getActivity(), R.color.white))
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerViewTransactions);

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
    public void onItemClick(View view, int position, String s, List<Transaction> tr) {
//        Toast.makeText(getContext(), "position: " + position, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "ID transaction: " + tr.get(position).getTransactionId(), Toast.LENGTH_SHORT).show();

        Transaction sendTransaction = tr.get(position);

        // display selected transaction
        DialogFragmentTransaction dialog = new DialogFragmentTransaction();
        dialog.setTargetFragment(HistoryFragment.this, 1);
//        repository.writeTransaction(writeIdCategory, writeIdSubcategory, writeDate, tvInput.getText().toString(), writeAccount, notesTv.getText().toString(), writeNote2, writePhoto, writeType);

        Bundle data = new Bundle();
        data.putString("category", sendTransaction.getTransactionIdCategory());
        data.putString("subcategory", sendTransaction.getTransactionIdSubcategory());
        data.putString("value", sendTransaction.getTransactionValue());
        data.putString("date", String.valueOf(sendTransaction.getTransactionDateFormat()));
        data.putString("account", sendTransaction.getIdAccount());
        data.putString("currency", sendTransaction.getCurency());
        data.putString("note1", sendTransaction.getTransactionNote1());
        data.putString("type", sendTransaction.getTransactionType());
        data.putString("ID_transaction", sendTransaction.getTransactionId());
        dialog.setInputListener(HistoryFragment.this);
        dialog.setArguments(data);
        dialog.show(getFragmentManager(), "DialogFragmentTransaction");

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
        Toast.makeText(getContext(), "received: " + pAmountFrom + " " + pAmountTo + " " + pCurrency + " " + pPeriod
                + " " + pPeriodFrom + " " + pPeriodTo + " " + pTypeOperation + " " + pAccount, Toast.LENGTH_LONG).show();
        loadSpecificTransaciotns();
    }

    @Override
    public void sendSelected() {
        loadTransaciotns();
        Toast.makeText(getContext(), "Added new!", Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }

    private void loadTransaciotns() {
        // on below line we are initializing adapter
        List<String> cats = new ArrayList<String>();
        InfoRepository infoRepository = new InfoRepository();
        transactions = infoRepository.readTransactions();
        if (transactions.size() == 0)
            Toast.makeText(getContext(), getResources().getString(R.string.empty_history), Toast.LENGTH_SHORT).show();
        else {
            CurrencyDatabase currencyDatabase = new CurrencyDatabase(getContext());
            ArrayList<CurrencyStrings> currentList = currencyDatabase.getCurrenciesList();
            adapter = new ListViewVerticalHistoryAdapter(getActivity(), transactions, currentList);
            adapter.setClickListener(this);
            adapter.notifyDataSetChanged();
            recyclerViewTransactions.setAdapter(adapter);
        }
    }

    private void loadSpecificTransaciotns() {
        // on below line we are initializing adapter
        List<String> cats = new ArrayList<String>();
        InfoRepository infoRepository = new InfoRepository();
        ArrayList<Transaction> transactions2;

            transactions = infoRepository.getSpecificTransactions(pAccount, pAmountFrom, pAmountTo, pCurrency, pPeriodFrom, pPeriodTo, pTypeOperation);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                transactions.sort((e1, e2) -> new Long(e1.getTransactionDateFormat()).compareTo(new Long(e2.getTransactionDateFormat())));
            }
            Collections.reverse(transactions);

        CurrencyDatabase currencyDatabase = new CurrencyDatabase(getContext());
        ArrayList<CurrencyStrings> currentList = currencyDatabase.getCurrenciesList();
        adapter = new ListViewVerticalHistoryAdapter(getActivity(), transactions, currentList);
        adapter.setClickListener(this);
        recyclerViewTransactions.setAdapter(adapter);
    }

}