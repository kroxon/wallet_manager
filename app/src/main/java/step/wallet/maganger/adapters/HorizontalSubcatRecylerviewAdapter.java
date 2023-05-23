package step.wallet.maganger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import step.wallet.maganger.R;

public class HorizontalSubcatRecylerviewAdapter extends RecyclerView.Adapter<HorizontalSubcatRecylerviewAdapter.HorizontalViewHolder> {

    private String[] items;
    private ItemClickListener mClickListener;
    private int selectedItem;
    private LayoutInflater mInflater;


    public HorizontalSubcatRecylerviewAdapter(Context context, String[] items) {
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
        int selectedItem = 0;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.d_tr_subcat_item_layout, parent, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.myText.setText(items[position]);
        holder.myText.setBackgroundResource(R.drawable.subcat_recycleview_background);
        holder.myText.setTextColor(ContextCompat.getColor(mInflater.getContext(), R.color.olx_color_1));

        int a = position;
        int b = position;

        if (selectedItem == position) {
            holder.myText.setBackgroundResource(R.drawable.subcat_recycleview_selected_background);
            holder.myText.setTextColor(ContextCompat.getColor(mInflater.getContext(), R.color.olx_color_1));
        }
//        else
//            holder.mLinearLayout.setBackgroundColor(R.drawable.subcat_recycleview_background);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Below line is just like a safety check, because sometimes holder could be null,
                // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
//                if (position == RecyclerView.NO_POSITION) return;

                int previousItem = selectedItem;
                selectedItem = position;

//                notifyItemChanged(previousItem);
                notifyDataSetChanged();
                notifyItemChanged(position);
                mClickListener.onItemClick(v, position, holder.myText.getText().toString());
//                notifyItemChanged(position);


            }
        });
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        LinearLayout mLinearLayout;
        TextView myText;
        int position = getLayoutPosition();
        String s = "";


        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
//            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.transactionSubcatRvLayout);
            myText = (TextView) itemView.findViewById(R.id.transactionSubcatRvTv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, position, s);
            myText.setBackgroundResource(R.drawable.subcat_recycleview_background_selected);
            notifyItemChanged(position);

        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, String s);
    }

}