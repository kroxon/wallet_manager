package step.wallet.maganger.ui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Movie;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.client.util.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.adapters.HorizontalSubcatRecylerviewAdapter;
import step.wallet.maganger.adapters.ListViewVerticalAdapter;
import step.wallet.maganger.adapters.ListViewVerticalHistoryAdapter;
import step.wallet.maganger.classes.Transaction;
import step.wallet.maganger.data.InfoRepository;

public class HistoryFragment extends Fragment implements ListViewVerticalHistoryAdapter.ItemClickListener {

    private OnFragmentInteractionListener mListener;
    private ListViewVerticalHistoryAdapter adapter;
    private RecyclerView recyclerViewTransactions;
    ArrayList<Transaction> transactions;
    private ImageView btnFilter;

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

        InfoRepository infoRepository = new InfoRepository();
        transactions = infoRepository.readTransactions("0");

        btnFilter = view.findViewById(R.id.btnHistoryFilter);

        //start test
        recyclerViewTransactions = (RecyclerView) view.findViewById(R.id.historyTransactionList);
        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//
        List<String> cats = new ArrayList<String>();
        cats = infoRepository.getAllCategories();
        List <String> subcategoriesList = infoRepository.getSubcategories(infoRepository.getIdCategory(cats.get(0)));
        String [] subcats = subcategoriesList.toArray(new String[0]);
        adapter = new ListViewVerticalHistoryAdapter(getActivity(), transactions);
        adapter.setClickListener(this);
        recyclerViewTransactions.setAdapter(adapter);


        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentFilterTransaction dialog = new DialogFragmentFilterTransaction();
                dialog.setTargetFragment(HistoryFragment.this, 1);
                dialog.show(getFragmentManager(), "DialogFragmentFilterTra");
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }


}