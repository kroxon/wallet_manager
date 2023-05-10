package step.wallet.maganger.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.classes.Account;
import step.wallet.maganger.classes.CurrencyStrings;
import step.wallet.maganger.data.CurrencyDatabase;
import step.wallet.maganger.data.InfoRepository;

public class RecyclerViewPieChartAdapter extends RecyclerView.Adapter<RecyclerViewPieChartAdapter.ViewHolder> {


    private Context context;
    private List<String> mCategoryNames;
    private LayoutInflater mInflater;




    // data is passed into the constructor
    public RecyclerViewPieChartAdapter(Context context, List<String> dataNames) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mCategoryNames = dataNames;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_row_pie_chart_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InfoRepository repository = new InfoRepository();
        holder.myNameTxt.setText(mCategoryNames.get(position));
        holder.myColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(repository.getCategoryColor(mCategoryNames.get(position)))));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mCategoryNames.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        View myColor;
        TextView myNameTxt;
        int position = getLayoutPosition();

        ViewHolder(View itemView) {
            super(itemView);
            myColor = itemView.findViewById(R.id.pieChart_cat_col);
            myNameTxt = itemView.findViewById(R.id.pieChart_cat_name);
                    }

    }

    public String getItem(int id) {

        return mCategoryNames.get(id);
    }



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

}