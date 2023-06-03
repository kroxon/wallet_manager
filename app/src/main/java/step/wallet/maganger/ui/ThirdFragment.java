package step.wallet.maganger.ui;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.adapters.RecyclerViewAccountsAdapter;
import step.wallet.maganger.adapters.RecyclerViewCategoryActivityAdapter;
import step.wallet.maganger.classes.Account;
import step.wallet.maganger.data.CurrencyDatabase;
import step.wallet.maganger.data.InfoRepository;


public class ThirdFragment extends Fragment implements DialogFragmentAccount.OnSaveListener, DialogFragmentAccount.OnInputListener {

    public OnUploadListener mOnUploadListener;


    public interface OnUploadListener {
        void sendToUpload();
    }

    public ThirdFragment() {
        // Required empty public constructor
    }

    private ImageView addAccountImg;
    private ImageView editCategoryImg;
    private RecyclerView rvAccount;
    private RecyclerView rvExpenses;
    private RecyclerView rvIncomes;
    private RecyclerViewAccountsAdapter rvAccountsAdapter;
    private RecyclerViewCategoryActivityAdapter adapterExpenses;
    private RecyclerViewCategoryActivityAdapter adapterIncomes;


    private int uploadConter = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();
//        generateItem();
    }

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;


    @Override
    public void onResume() {
        //start handler as activity become visible

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                //do something
                loadRVExpenses();
                loadRVIncomes();
                handler.postDelayed(runnable, delay);
                if (uploadConter == 0) {
                    mOnUploadListener.sendToUpload();
                    uploadConter = 1;
                }
            }
        }, delay);

        super.onResume();
    }


    @Override
    public void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        uploadConter = 0;
        super.onPause();
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
        loadRVExpenses();
        loadRVIncomes();

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

        editCategoryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Category_Activity.class);
                startActivity(i);
            }
        });
    }

    private void findViews(View view) {
        addAccountImg = view.findViewById(R.id.addAccountImg_facc);
        editCategoryImg = view.findViewById(R.id.third_edit_category_img);
        rvAccount = view.findViewById(R.id.rVAccounts_facc);
        rvExpenses = view.findViewById(R.id.third_expenses_rv);
        rvIncomes = view.findViewById(R.id.third_income_rv);
    }

    private void loadRVAccounts() {
        // on below line we are initializing adapter
        InfoRepository repository = new InfoRepository();
        ArrayList<Account> arrayAccountList = repository.readAccounts();
        if (arrayAccountList.size() != 0) {
            rvAccountsAdapter = new RecyclerViewAccountsAdapter(getContext(), arrayAccountList);
            rvAccount.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            rvAccount.setAdapter(rvAccountsAdapter);

            // interface item click

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

    private void loadRVExpenses() {

        InfoRepository repository = new InfoRepository();
        List<String> arrayList = repository.getAllExpenseCategories();
        if (arrayList.size() != 0) {
            rvExpenses.setVisibility(View.VISIBLE);
            adapterExpenses = new RecyclerViewCategoryActivityAdapter(getContext(), arrayList);
            rvExpenses.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            rvExpenses.setAdapter(adapterExpenses);
        } else rvExpenses.setVisibility(View.GONE);
    }

    private void loadRVIncomes() {

        InfoRepository repository = new InfoRepository();
        List<String> arrayList = repository.getAllIncomeCategories();
        if (arrayList.size() != 0) {
            rvIncomes.setVisibility(View.VISIBLE);
            adapterIncomes = new RecyclerViewCategoryActivityAdapter(getContext(), arrayList);
            rvIncomes.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            rvIncomes.setAdapter(adapterIncomes);
        } else rvIncomes.setVisibility(View.GONE);
    }

    @Override
    public void onSaveClick(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendInput(String input) {
        loadRVAccounts();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (uploadConter == 0)
                mOnUploadListener
                        = (OnUploadListener) getActivity();
            uploadConter = -1;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }


}