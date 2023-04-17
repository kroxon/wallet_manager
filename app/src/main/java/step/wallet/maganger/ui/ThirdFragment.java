package step.wallet.maganger.ui;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import step.wallet.maganger.R;
import step.wallet.maganger.adapters.RecyclerViewAccountsAdapter;
import step.wallet.maganger.classes.Account;
import step.wallet.maganger.data.CurrencyDatabase;
import step.wallet.maganger.data.InfoRepository;


public class ThirdFragment extends Fragment {

    public ThirdFragment() {
        // Required empty public constructor
    }

    private ImageView addAccountImg;
    private RecyclerView rvAccount;
    private RecyclerViewAccountsAdapter rvAccountsAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();
//        generateItem();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        findViews(view);
        initiateViews();

//        InfoRepository repository = new InfoRepository();
//        rvAccountsAdapter = new RecyclerViewAccountsAdapter(getContext(), repository.readAccounts());
//        rvAccount.setAdapter(rvAccountsAdapter);
//        Log.d("adapter", "onCreateView: " + rvAccountsAdapter.getItemCount());
//        Log.d("adapter", "accountns: " + repository.readAccounts().get(1).getAccountCurrency());

        loadRVAccounts();

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
        rvAccount = view.findViewById(R.id.rVAccounts_facc);
    }

    private void loadRVAccounts() {
        // on below line we are initializing adapter
        InfoRepository repository = new InfoRepository();
        ArrayList<Account> arrayAccountList = repository.readAccounts();
        if (arrayAccountList.size() != 0) {
            rvAccountsAdapter = new RecyclerViewAccountsAdapter(getContext(), arrayAccountList);
            rvAccount.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvAccount.setAdapter(rvAccountsAdapter);
            rvAccountsAdapter.setOnClickListener(new RecyclerViewAccountsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    // open account dialog with extra input
                    Toast.makeText(getContext(), arrayAccountList.get(position).getAccountName() + " " + arrayAccountList.get(position).getAccountCurrency(), Toast.LENGTH_SHORT).show();
                    DialogFragmentAccount dialog = new DialogFragmentAccount();
                    dialog.setTargetFragment(ThirdFragment.this, 1);
                    Bundle data = new Bundle();
                    data.putString("name", arrayAccountList.get(position).getAccountName());
                    data.putString("description", arrayAccountList.get(position).getAccountDescription());
                    data.putString("balance", arrayAccountList.get(position).getAccountBalance());
                    data.putString("currency", arrayAccountList.get(position).getAccountCurrency());

                    dialog.setArguments(data);
                    dialog.show(getFragmentManager(), "DialogFragmentAccount");
                }
            });
        } else
            Toast.makeText(getContext(), "no accounts", Toast.LENGTH_SHORT).show();
    }
}