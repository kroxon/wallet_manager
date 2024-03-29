package step.wallet.maganger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;

import step.wallet.maganger.R;

public class RecyclerViewIconAdapter extends RecyclerView.Adapter<RecyclerViewIconAdapter.ViewHolder> {

    private String[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    public String[] idDrawable;
    private static int lastClickedPosition = -1;
    private int selectedItem;
    private Context contextIcon;

    // data is passed into the constructor
    public RecyclerViewIconAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        int selectedItem = 0;
        contextIcon = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_icon_select, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        holder.myImage.setText("mData[position]");
        holder.myImage.setImageResource(getIdDrawable(mData[position], R.drawable.class));

        if (selectedItem == position) {
            final float scale = holder.myImage.getResources().getDisplayMetrics().density;
            int dpWidthInPx  = (int) (55 * scale);
            int dpHeightInPx = (int) (55 * scale);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpWidthInPx, dpHeightInPx);
            holder.myImage.setLayoutParams(layoutParams);
            holder.myImage.setColorFilter(ContextCompat.getColor(contextIcon, R.color.colorApp1), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            final float scale = holder.myImage.getResources().getDisplayMetrics().density;
            int dpWidthInPx  = (int) (45 * scale);
            int dpHeightInPx = (int) (45 * scale);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpWidthInPx, dpHeightInPx);
            holder.myImage.setLayoutParams(layoutParams);
            holder.myImage.setColorFilter(ContextCompat.getColor(contextIcon, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int previousItem = selectedItem;
                selectedItem = position;

//                notifyItemChanged(previousItem);
                notifyDataSetChanged();
                notifyItemChanged(position);
                mClickListener.onItemClick(v, getIdDrawable(mData[position], R.drawable.class), mData[position]);


            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        ImageView myImage;
        ImageView myImage;
        LinearLayout myLinearLayout;
        int position = getAdapterPosition();

        ViewHolder(View itemView) {
            super(itemView);
            myImage = itemView.findViewById(R.id.acCatIconAdapter);
            myLinearLayout = itemView.findViewById(R.id.acCatIconlayoutAdapter);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, position, mData[position]);
//            myLinearLayout.setBackgroundColor(Color.parseColor("#00743D"));
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {

        return mData[id];
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, String iconName);
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