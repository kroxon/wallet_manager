package step.wallet.maganger.ui;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.data.InfoRepository;


public class FourthFragment extends Fragment {

    private Button defaultDb;
    private Button btnArchived;
    private Button btnDeleteDb;

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
                String[] colors = getResources().getStringArray(R.array.category_colors);
                String[] accountssList = getResources().getStringArray(R.array.accounts_names);

                InfoRepository repository = new InfoRepository();
                repository.addDefaultDatabase(categories, subcategories, icons, colors, accountssList);
            }
        });

        btnArchived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoRepository infoRepository = new InfoRepository();
                infoRepository.setCategoryArchived(infoRepository.getAllExpenseCategories().get(0));
            }
        });

        btnDeleteDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoRepository infoRepository = new InfoRepository();
                getContext().deleteDatabase("WalletManager.db");
                if (infoRepository.getAllCategories().size() == 0)
                    Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void init(View view) {
        defaultDb = view.findViewById(R.id.btnDefaultDb);
        btnArchived = view.findViewById(R.id.btnRandomArchived);
        btnDeleteDb = view.findViewById(R.id.btnDeleteDb);
    }
}