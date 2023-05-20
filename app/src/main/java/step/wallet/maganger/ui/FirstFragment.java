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



    public Button bexample4;
    public Button bexample3;
    public Button bexampleDialog;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        bexample4 = (Button) view.findViewById(R.id.example_button_4);
        bexample4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Category_Activity.class);
//                Intent i = new Intent(getActivity(), Categories_Activity.class);
                startActivity(i);
            }
        });

        bexampleDialog = view.findViewById(R.id.btnDialog);
        bexampleDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // display selected transaction
                DialogFragmentTransaction dialog = new DialogFragmentTransaction();
                dialog.setTargetFragment(FirstFragment.this, 1);
                dialog.setInputListener(FirstFragment.this);
                dialog.show(getFragmentManager(), "DialogFragmentTransaction");
            }
        });

        bexample3 = view.findViewById(R.id.example_button_3);
        bexample3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
            }
        });


        findViews(view);

        initItems();

        return view;

    }

    private void initItems() {

    }

    private void findViews(View view) {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Fragment childFragment = new ChartsFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.chartsFrame, childFragment).commit();
    }

    @Override
    public void sendSelected() {
        Toast.makeText(getContext(), "Added transaction successfully!", Toast.LENGTH_SHORT).show();
    }
}