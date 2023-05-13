package step.wallet.maganger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kotlin.Triple;
import step.wallet.maganger.R;
import step.wallet.maganger.classes.Quad;
import step.wallet.maganger.data.InfoRepository;

public class RecyclerViewPieChartDetailNestedAdapter extends RecyclerView.Adapter<RecyclerViewPieChartDetailNestedAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Quad<String, String, Double, Double>> categorySums;
    private String currency;
    private LayoutInflater mInflater;


    // data is passed into the constructor
    public RecyclerViewPieChartDetailNestedAdapter(Context context, ArrayList<Quad<String, String, Double, Double>> categorySums, String currency) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.categorySums = categorySums;
        this.currency = currency;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_row_pie_chart_detail_nested_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InfoRepository repository = new InfoRepository();
        holder.myNameTxt.setText(repository.getSubcategoryName(categorySums.get(position).getFirst()));
        DecimalFormat format = new DecimalFormat("0.#");
        holder.myValueTxt.setText(format.format(categorySums.get(position).getThird()));
        holder.myCurrencyTxt.setText(currency);
        holder.myPercentageTxt.setText(format.format(categorySums.get(position).getFourth()) + "%");
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return categorySums.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myNameTxt;
        TextView myValueTxt;
        TextView myCurrencyTxt;
        TextView myPercentageTxt;
        int position = getLayoutPosition();

        ViewHolder(View itemView) {
            super(itemView);
            myNameTxt = itemView.findViewById(R.id.pieChart_detail_nested_cat_name);
            myValueTxt = itemView.findViewById(R.id.char_value_detail_nested_txt);
            myCurrencyTxt = itemView.findViewById(R.id.char_detail_nested_currency_txt);
            myPercentageTxt = itemView.findViewById(R.id.char_detail_nested_percentage_txt);
        }

    }

}