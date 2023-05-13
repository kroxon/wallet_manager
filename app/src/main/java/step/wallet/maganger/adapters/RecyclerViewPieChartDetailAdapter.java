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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kotlin.Triple;
import step.wallet.maganger.R;
import step.wallet.maganger.classes.Quad;
import step.wallet.maganger.data.InfoRepository;

public class RecyclerViewPieChartDetailAdapter extends RecyclerView.Adapter<RecyclerViewPieChartDetailAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Triple<String, Double, Double>> categorySums;
    private ArrayList<Quad<String, String, Double, Double>> subcategorySum;
    String currency;
    private RecyclerViewPieChartDetailNestedAdapter categoriesDetailNestedRVAdapter;

    private ItemClickListener mClickListener;
    private int selectedItem = -1;
    private RecyclerViewPieChartDetailNestedAdapter adapter;


    private LayoutInflater mInflater;

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = (ItemClickListener) itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position);
    }

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
        DecimalFormat format = new DecimalFormat("0.#");
        holder.myValueSumTxt.setText(format.format(categorySums.get(position).getSecond()));
        holder.myValueCurrencyTxt.setText(currency);
        holder.myPercentageTxt.setText(format.format(categorySums.get(position).getThird()) + "%");
        holder.myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItem = position;
                notifyDataSetChanged();
                notifyItemChanged(position);
            }
        });
        holder.myRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.myRecyclerView.setVisibility(View.GONE);
            }
        });
        if (selectedItem == position && holder.myRecyclerView.getVisibility() == View.GONE) {
            holder.myRecyclerView.setVisibility(View.VISIBLE);
            ArrayList<Quad<String, String, Double, Double>> arraySubs = new ArrayList<>();
            for (int i = 0; i < subcategorySum.size(); i++){
                if (categoryId.equals(subcategorySum.get(i).getSecond()))
                    arraySubs.add(subcategorySum.get(i));
            }
            adapter = new RecyclerViewPieChartDetailNestedAdapter(context, arraySubs, currency);
            holder.myRecyclerView.setAdapter(adapter);
        } else
            holder.myRecyclerView.setVisibility(View.GONE);
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
        ConstraintLayout myLayout;
        int position = getLayoutPosition();

        ViewHolder(View itemView) {
            super(itemView);
            myImg = itemView.findViewById(R.id.pieChart_detail_icon);
            myCatNameTxt = itemView.findViewById(R.id.pieChart_detail_cat_name);
            myValueSumTxt = itemView.findViewById(R.id.char_value_detail_txt);
            myValueCurrencyTxt = itemView.findViewById(R.id.char_detail_currency_txt);
            myPercentageTxt = itemView.findViewById(R.id.char_detail_percentage_txt);
            myRecyclerView = itemView.findViewById(R.id.char_detail_nested_RV);
            myLayout = itemView.findViewById(R.id.pieChart_detail_top_layout);
        }

    }

}