package step.wallet.maganger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.data.InfoRepository;

public class RecyclerViewCategoryTrasactionAdapter extends RecyclerView.Adapter<RecyclerViewCategoryTrasactionAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    public String[] idDrawable;
    private static int lastClickedPosition = -1;
    private int selectedItem;

    // data is passed into the constructor
    public RecyclerViewCategoryTrasactionAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        int selectedItem = 0;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_category_select_tr, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InfoRepository repository = new InfoRepository();
//        holder.myImage.setText("mData[position]");
        holder.myImage.setImageResource(repository.getIdCategoryIcon(mData.get(position)));
        holder.myText.setText(mData.get(position));

//        if (selectedItem == position) {
//            holder.myLinearLayout.setBackgroundColor(Color.parseColor("#00743D"));
//        }


        holder.myLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int previousItem = selectedItem;
                selectedItem = position;

//                notifyItemChanged(previousItem);
                notifyDataSetChanged();
                notifyItemChanged(position);
                mClickListener.onItemClick(mData.get(position), repository.getIdCategoryIcon(mData.get(position)));
            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        ImageView myImage;
        ImageView myImage;
        TextView myText;
        ConstraintLayout myLinearLayout;
        int position = getLayoutPosition();

        ViewHolder(View itemView) {
            super(itemView);
            myImage = itemView.findViewById(R.id.trCatIconAdapterImg);
            myText = itemView.findViewById(R.id.trCatIconAdapterTxt);
            myLinearLayout = itemView.findViewById(R.id.trCatIconLayoutAdapter);
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

    public static int getIdDrawable(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }
}