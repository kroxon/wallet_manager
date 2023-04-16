package step.wallet.maganger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.classes.Account;
import step.wallet.maganger.classes.CurrencyStrings;
import step.wallet.maganger.data.CurrencyDatabase;
import step.wallet.maganger.data.InfoRepository;

public class RecyclerViewAccountsAdapter extends RecyclerView.Adapter<RecyclerViewAccountsAdapter.ViewHolder> {

    // new

    private ArrayList<Account> accountsList;
    private Context context;


    // old
    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private static int lastClickedPosition = -1;
    private int selectedItem;


    // data is passed into the constructor
    public RecyclerViewAccountsAdapter(Context context, ArrayList<Account> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.accountsList = data;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_row_account, parent, false);
        return new ViewHolder(view);
    }

    // binds the data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InfoRepository repository = new InfoRepository();
        holder.myNameTxt.setText(accountsList.get(position).getAccountName());
        String myDescription = accountsList.get(position).getAccountDescription();
        if (myDescription.length() > 30)
            myDescription = myDescription.substring(0, 28) + "...";
        holder.myDescriptionTxt.setText(myDescription);
        holder.myBalanceTxt.setText(accountsList.get(position).getAccountBalance());
        CurrencyDatabase currencyDatabase = new CurrencyDatabase(context);
        ArrayList<CurrencyStrings> currentList = currencyDatabase.getCurrenciesList();
        for (int i = 0; i < currentList.size(); i++) {
            if (currentList.get(i).getName().equals(accountsList.get(position).getAccountCurrency())) {
                holder.myCurrencyTxt.setText(currentList.get(i).getSymbol());
                break;
            }
        }

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return accountsList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myImage;
        TextView myNameTxt;
        TextView myDescriptionTxt;
        TextView myBalanceTxt;
        TextView myCurrencyTxt;
        int position = getLayoutPosition();

        ViewHolder(View itemView) {
            super(itemView);
            myImage = itemView.findViewById(R.id.accountOption);
            myNameTxt = itemView.findViewById(R.id.accountNameTxt);
            myDescriptionTxt = itemView.findViewById(R.id.accountDescriptionTxt);
            myBalanceTxt = itemView.findViewById(R.id.accountBalanceTxt);
            myCurrencyTxt = itemView.findViewById(R.id.accountCurrencyTxt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            InfoRepository repository = new InfoRepository();
            if (mClickListener != null)
                mClickListener.onItemClick(mData.get(position), repository.getIdCategoryIcon(mData.get(position)));
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {

        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(String categoryName, int categoryIcon);
    }

}