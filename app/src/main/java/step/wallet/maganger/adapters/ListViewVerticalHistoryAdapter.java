package step.wallet.maganger.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.classes.CurrencyStrings;
import step.wallet.maganger.classes.Transaction;
import step.wallet.maganger.data.InfoRepository;

public class ListViewVerticalHistoryAdapter extends RecyclerView.Adapter<ListViewVerticalHistoryAdapter.ViewHolder> implements View.OnClickListener {

    ArrayList<Transaction> transactionsList;
    ArrayList<CurrencyStrings> currencytList;
    Context context;
    private LayoutInflater mInflater;
    public String currentDate = "";
    private int selectedItem;
    private ItemClickListener mClickListener;


    public ListViewVerticalHistoryAdapter(Context context, ArrayList<Transaction> items, ArrayList<CurrencyStrings> currencytList) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        transactionsList = items;
        this.currencytList = currencytList;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_row_history_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        InfoRepository infoRepository = new InfoRepository();
        if (position == 0)
            currentDate = transactionsList.get(0).getTransactionDate();
        if (transactionsList.get(position).getTransactionDate().equals(currentDate) && position != 0) {
            holder.myDate.setVisibility(View.GONE);
        } else {
            holder.myDate.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 40, 0, 0);
            holder.myDate.setLayoutParams(params);
            currentDate = transactionsList.get(position).getTransactionDate();
        }
        Calendar cal = Calendar.getInstance();
        Calendar calCurrenct = Calendar.getInstance();
        cal.setTimeInMillis(transactionsList.get(position).getTransactionDateInMilis());
        if (cal.get(Calendar.DAY_OF_YEAR) == calCurrenct.get(Calendar.DAY_OF_YEAR) && cal.get(Calendar.YEAR) == calCurrenct.get(Calendar.YEAR))
            holder.myDate.setText(context.getResources().getString(R.string.today) + ", " + currentDate);
        else if (cal.get(Calendar.DAY_OF_YEAR) == calCurrenct.get(Calendar.DAY_OF_YEAR) - 1 && cal.get(Calendar.YEAR) == calCurrenct.get(Calendar.YEAR))
            holder.myDate.setText(context.getResources().getString(R.string.yesterday) + ", " + currentDate);
        else
            holder.myDate.setText(currentDate);
        holder.myValue.setText(transactionsList.get(position).getTransactionValue());
        String idCat = transactionsList.get(position).getTransactionIdCategory();

        holder.myIcon.setImageResource(Integer.parseInt(infoRepository.getIdCategoryIconById(transactionsList.get(position).getTransactionIdCategory())));
//        holder.myIcon.setColorFilter(Integer.parseInt(infoRepository.getIdCategoryIconById(idCat)), android.graphics.PorterDuff.Mode.SRC_IN);
        holder.myIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(infoRepository.getCategoryColor(infoRepository.getCategoryName(idCat)))));
        holder.myIcon.setColorFilter(Color.parseColor(infoRepository.getCategoryColor(infoRepository.getCategoryName(idCat))));

        holder.myCategory.setText(infoRepository.getCategoryName(transactionsList.get(position).getTransactionIdCategory()));
        holder.mySubategory.setText(infoRepository.getSubcategoryName(transactionsList.get(position).getTransactionIdSubcategory()));

        for (int j = 0; j < currencytList.size(); j++) {
            if (currencytList.get(j).getName().equals(infoRepository.getAccount(transactionsList.get(position).getIdAccount()).getAccountCurrency())) {
                holder.myCurrency.setText(currencytList.get(j).getSymbol());
                break;
            }
        }
        if (transactionsList.get(position).getTransactionType().equals("expense")) {
            holder.myValue.setTextColor(Color.parseColor("#004d40"));
            holder.myCurrency.setTextColor(Color.parseColor("#004d40"));
            holder.myValue.setText("- " + holder.myValue.getText().toString());
        }
        if (transactionsList.get(position).getTransactionType().equals("income")) {
            holder.myValue.setTextColor(Color.parseColor("#689F38"));
            holder.myCurrency.setTextColor(Color.parseColor("#689F38"));

        }

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    @Override
    public void onClick(View view) {

    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myIcon;
        TextView myDate;
        TextView myCategory;
        TextView mySubategory;
        TextView myValue;
        TextView myCurrency;
        ConstraintLayout myHistoryList;
//        int position = getAdapterPosition();

        int position = getLayoutPosition();
        String s = "";


        ViewHolder(View itemView) {
            super(itemView);
            myDate = itemView.findViewById(R.id.historyDate);
            myCategory = itemView.findViewById(R.id.historyCategoryName);
            mySubategory = itemView.findViewById(R.id.historySubcategoryName);
            myValue = itemView.findViewById(R.id.historyValue);
            myCurrency = itemView.findViewById(R.id.historyCurrency);
            myIcon = itemView.findViewById(R.id.historyIcon);
            myHistoryList = itemView.findViewById(R.id.historyListlayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition(), s, transactionsList);
            notifyItemChanged(position);
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = (ItemClickListener) itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, String s, List<Transaction> tr);
    }

}


