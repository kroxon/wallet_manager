package step.wallet.maganger.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import step.wallet.maganger.R;
import step.wallet.maganger.classes.Transaction;
import step.wallet.maganger.data.InfoRepository;

public class SecondFragment extends Fragment {


    public SecondFragment() {
        // Required empty public constructor
    }

    private OnFragmentInteractionListener mListener;
    private TextView testTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        testTextView = (TextView) view.findViewById(R.id.testTextView2);

        InfoRepository infoRepository = new InfoRepository();

        ArrayList<Transaction> transactions = infoRepository.readTransactions("0");

        for (int i = 0; i < 2; i++){
            testTextView.setText(testTextView.getText().toString() + "transaction value: " + transactions.get(i).getTransactionValue() + "\n");
        }

//        //child fragment
//        FragmentManager childFragMan = getChildFragmentManager();
//        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
//        HistoryFragment fragB = new HistoryFragment ();
//        childFragTrans.add(R.id.historyFrame, fragB);
//        childFragTrans.addToBackStack("B");
//        childFragTrans.commit();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Fragment childFragment = new HistoryFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.historyFrame, childFragment).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromParentFragment(Uri uri);
    }
}