package step.wallet.maganger.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import step.wallet.maganger.R;

public class FirstFragment extends Fragment{

    private static final String TAG = "FirstFragment";



    public Button bexample4;




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

}