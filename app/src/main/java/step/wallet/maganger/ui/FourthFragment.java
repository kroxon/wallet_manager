package step.wallet.maganger.ui;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.data.InfoRepository;


public class FourthFragment extends Fragment {

    private Button defaultDb;

    public FourthFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);

        init(view);

        defaultDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] categories = getResources().getStringArray(R.array.categories);
                List<String[]> subcategories = new ArrayList<String[]>();
                subcategories.add(getResources().getStringArray(R.array.subcategories1));
                subcategories.add(getResources().getStringArray(R.array.subcategories2));
                subcategories.add(getResources().getStringArray(R.array.subcategories3));
                subcategories.add(getResources().getStringArray(R.array.subcategories4));
                subcategories.add(getResources().getStringArray(R.array.subcategories5));
                subcategories.add(getResources().getStringArray(R.array.subcategories6));
                subcategories.add(getResources().getStringArray(R.array.subcategories7));
                subcategories.add(getResources().getStringArray(R.array.subcategories8));
                subcategories.add(getResources().getStringArray(R.array.subcategories9));
                subcategories.add(getResources().getStringArray(R.array.subcategories10));
                subcategories.add(getResources().getStringArray(R.array.subcategories11));
                subcategories.add(getResources().getStringArray(R.array.subcategories12));
                subcategories.add(getResources().getStringArray(R.array.subcategories13));
                subcategories.add(getResources().getStringArray(R.array.subcategories14));
                subcategories.add(getResources().getStringArray(R.array.subcategories15));
                String[] icons = getResources().getStringArray(R.array.category_icon);
                InfoRepository repository = new InfoRepository();
                repository.addDefaultDatabase(categories, subcategories, icons);

            }
        });


        return view;
    }

    private void init(View view) {
        defaultDb = view.findViewById(R.id.btnDefaultDb);
    }
}