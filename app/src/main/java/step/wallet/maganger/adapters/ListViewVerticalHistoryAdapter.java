package step.wallet.maganger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.classes.Transaction;
import step.wallet.maganger.data.InfoRepository;

public class ListViewVerticalHistoryAdapter extends RecyclerView.Adapter<ListViewVerticalHistoryAdapter.ViewHolder> implements View.OnClickListener {

    ArrayList<Transaction> transactionsList;
    Context context;
    private LayoutInflater mInflater;
    public String currentDate;
    private int selectedItem;
    private ItemClickListener mClickListener;


    // View lookup cache
//    private static class ViewHolder {
//        TextView txtDate;
//        TextView txtCategory;
//        TextView txtValue;
//        TextView txtCurrency;
//        ImageView icon;
//    }

    public ListViewVerticalHistoryAdapter(Context context, ArrayList<Transaction> items) {
        this.mInflater = LayoutInflater.from(context);
        transactionsList = items;
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
        if (transactionsList.get(position).getTransactionDate().equals(currentDate)) {
            holder.myDate.setVisibility(View.GONE);
        } else {
            holder.myDate.setVisibility(View.VISIBLE);
            currentDate = transactionsList.get(position).getTransactionDate();
        }
        holder.myDate.setText(transactionsList.get(position).getTransactionDate());
        holder.myValue.setText(transactionsList.get(position).getTransactionValue());
        holder.myCurrency.setText("zł");
        if (transactionsList.get(position).getTransactionType().equals("expense")) {
            holder.myValue.setText("- " + holder.myValue.getText().toString());
            holder.myIcon.setImageResource(Integer.parseInt(infoRepository.getIdCategoryIconById(transactionsList.get(position).getTransactionIdCategory())));
            holder.myCategory.setText(infoRepository.getCategoryName(transactionsList.get(position).getTransactionIdCategory()));
        } else {
            holder.myValue.setTextColor(Color.parseColor("#689F38"));
            holder.myCurrency.setTextColor(Color.parseColor("#689F38"));
            holder.myIcon.setVisibility(View.INVISIBLE);
//            holder.myCategory.setVisibility(View.GONE);
            holder.myCategory.setText("Income");
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
            myValue = itemView.findViewById(R.id.historyValue);
            myCurrency = itemView.findViewById(R.id.historyCurrency);
            myIcon = itemView.findViewById(R.id.historyIcon);
            myHistoryList = itemView.findViewById(R.id.historyListlayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, position, s);
            notifyItemChanged(position);
        }
    }

//    // convenience method for getting data at click position
//    public String getItem(int id) {
//
//        return transactionsList.get(id);
//    }

    // parent activity will implement this method to respond to click events
//    public interface ItemClickListener {
//        void onItemClick(View view, int position);
//    }
//
////    @Override
////    public void onClick(View v) {
////
////        int position = (Integer) v.getTag();
////        Object object = getItem(position);
////        Transaction transaction = (Transaction) object;
////
////        switch (v.getId()) {
////            case R.id.historyIcon:
////                Snackbar.make(v, "Release date " + transaction.getTransactionDate(), Snackbar.LENGTH_LONG)
////                        .setAction("No action", null).show();
////                break;
////        }
////    }
//
//    // allows clicks events to be caught
//    public void setClickListener(ItemClickListener itemClickListener) {
//        this.mClickListener = itemClickListener;
//    }

    public static int getIdDrawable(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = (ItemClickListener) itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, String s);
    }
}

