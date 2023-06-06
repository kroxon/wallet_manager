package step.wallet.maganger.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import step.wallet.maganger.R;

public class FirstFragment extends Fragment implements DialogFragmentTransaction.OnInputSelected{

    private static final String TAG = "FirstFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Fragment childFragment = new ChartsFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.chartsFrame, childFragment).commit();
    }

    @Override
    public void sendSelected() {
        Toast.makeText(getContext(), getContext().getString(R.string.add_transaction_succesfully), Toast.LENGTH_SHORT).show();
    }
}