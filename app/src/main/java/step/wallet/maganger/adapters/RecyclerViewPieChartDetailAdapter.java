package step.wallet.maganger.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kotlin.Triple;
import step.wallet.maganger.R;
import step.wallet.maganger.classes.CurrencyStrings;
import step.wallet.maganger.classes.Quad;
import step.wallet.maganger.classes.Transaction;
import step.wallet.maganger.data.InfoRepository;

public class RecyclerViewPieChartDetailAdapter extends RecyclerView.Adapter<RecyclerViewPieChartDetailAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Triple<String, Double, Double>> categorySums;
    private ArrayList<Quad<String, String, Double, Double>> subcategorySum;
    String currency;
    private RecyclerViewPieChartDetailNestedAdapter categoriesDetailNestedRVAdapter;

    private ListViewVerticalHistoryAdapter.ItemClickListener mClickListener;


    private LayoutInflater mInflater;


    // data is passed into the constructor
    public RecyclerViewPieChartDetailAdapter(Context context, ArrayList<Triple<String, Double, Double>> categorySums,
                                             ArrayList<Quad<String, String, Double, Double>> subcategorySum, String currency) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.categorySums = categorySums;
        this.subcategorySum = subcategorySum;
        this.currency = currency;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_row_pie_chart_detail_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InfoRepository repository = new InfoRepository();
        String categoryId = categorySums.get(position).getFirst();
        holder.myImg.setImageResource(Integer.parseInt(repository.getIdCategoryIconById(categoryId)));
        holder.myImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(repository.getCategoryColor(repository.getCategoryName(categoryId)))));
        holder.myImg.setColorFilter(Color.parseColor(repository.getCategoryColor(repository.getCategoryName(categoryId))));
        holder.myCatNameTxt.setText(repository.getCategoryName(categoryId));
        holder.myValueSumTxt.setText(categorySums.get(position).getSecond() + "");
        holder.myValueCurrencyTxt.setText(currency);
        holder.myPercentageTxt.setText(categorySums.get(position).getThird() + "");
//        holder.myRecyclerView
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return categorySums.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myImg;
        TextView myCatNameTxt;
        TextView myValueSumTxt;
        TextView myValueCurrencyTxt;
        TextView myPercentageTxt;
        RecyclerView myRecyclerView;
        int position = getLayoutPosition();

        ViewHolder(View itemView) {
            super(itemView);
            myImg = itemView.findViewById(R.id.pieChart_detail_icon);
            myCatNameTxt = itemView.findViewById(R.id.pieChart_detail_cat_name);
            myValueSumTxt = itemView.findViewById(R.id.char_value_detail_txt);
            myValueCurrencyTxt = itemView.findViewById(R.id.char_detail_currency_txt);
            myPercentageTxt = itemView.findViewById(R.id.char_detail_percentage_txt);
            myRecyclerView = itemView.findViewById(R.id.char_detail_nested_RV);
        }

    }

//    public String getItem(int id) {
//
//        return transactionsList.get(id);
//    }


    private void displayDeleteDialog(String name) {
        Dialog descpriptionDialog = new Dialog(context);
        descpriptionDialog.setContentView(R.layout.dialog_acc_dialog_delete);
//        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
//        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
//        descpriptionDialog.getWindow().setLayout(width, height);
        descpriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        descpriptionDialog.show();

        TextView dDeleteTitle = descpriptionDialog.findViewById(R.id.dAccDeletTitle);
        TextView dDeleteNo = descpriptionDialog.findViewById(R.id.dAccDeletDNo);
        TextView dDeleteYes = descpriptionDialog.findViewById(R.id.dAccDeletDYes);
        String deleteString1 = context.getString(R.string.delete_ask_1);
        String deleteString2 = context.getString(R.string.delete_ask_2);

        dDeleteTitle.setText(deleteString1 + " \"" + name + "\" " + deleteString2);


        dDeleteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descpriptionDialog.dismiss();
            }
        });

        dDeleteYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descpriptionDialog.dismiss();
            }
        });
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = (ListViewVerticalHistoryAdapter.ItemClickListener) itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, String s, List<Transaction> tr);
    }

}