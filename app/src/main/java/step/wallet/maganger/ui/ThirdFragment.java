package step.wallet.maganger.ui;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import step.wallet.maganger.R;
import step.wallet.maganger.data.CurrencyDatabase;


public class ThirdFragment extends Fragment {

    public ThirdFragment() {
        // Required empty public constructor
    }

    private ImageView addAccountImg;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();
//        generateItem();

        CurrencyDatabase currencyDatabase = new CurrencyDatabase(getContext());
        Toast.makeText(getContext(), currencyDatabase.getCurrenciesList().get(99).getSymbol(), Toast.LENGTH_SHORT).show();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        findViews(view);
        initiateViews();

        return view;
    }

    private void initiateViews() {
        addAccountImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentAccount dialogNotes = new DialogFragmentAccount();
//                dialogNotes.setValue(notesTv.getText().toString());
                dialogNotes.setTargetFragment(ThirdFragment.this, 1);
                dialogNotes.show(getFragmentManager(), "DialogFragmentAccount");
            }
        });
    }

    private void findViews(View view) {
        addAccountImg = view.findViewById(R.id.addAccountImg_facc);
    }

}