package step.wallet.maganger.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
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

import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.data.InfoRepository;

public class RecyclerViewCategoryActivityAdapter extends RecyclerView.Adapter<RecyclerViewCategoryActivityAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewCategoryAct";

    private List<String> mData;
    private OnCategoryListener mOnCategoryListener;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    public RecyclerViewCategoryActivityAdapter(Context context, List<String> data, OnCategoryListener onCategoryListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.mOnCategoryListener = onCategoryListener;
    }

    public RecyclerViewCategoryActivityAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }


    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ac_item_category, parent, false);
        return new ViewHolder(view, mOnCategoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position != mData.size()) {
            InfoRepository repository = new InfoRepository();
            String categorynameText = mData.get(position);
//            if (categorynameText.length() > 13)
//                categorynameText = categorynameText.substring(0, 10) + "...";
            holder.myText.setText(categorynameText);
            holder.myImage.setImageResource(repository.getIdCategoryIcon(mData.get(position)));
            holder.myLinearLayoutIconBckg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(repository.getCategoryColor(mData.get(position)))));
        } else {
            holder.myText.setText("");
            holder.myText.setVisibility(View.GONE);
            holder.myImage.setImageResource(R.drawable.ic_add);
            holder.myLinearLayoutIconBckg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#054949")));
        }
    }

    @Override
    public int getItemCount() {
        if (mData.size() < 10 && mOnCategoryListener != null)
            return mData.size() + 1;
        else
            return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        ImageView myImage;
        OnCategoryListener onCategoryListener;
        ImageView myImage;
        TextView myText;
        ConstraintLayout myLinearLayout;
        LinearLayout myLinearLayoutIconBckg;

        ViewHolder(View itemView, OnCategoryListener onCategoryListener) {
            super(itemView);
            myImage = itemView.findViewById(R.id.ac_categroies_rv_img);
            myText = itemView.findViewById(R.id.ac_categroies_rv_catname);
            myLinearLayout = itemView.findViewById(R.id.ac_categroies_rv_layout);
            myLinearLayoutIconBckg = itemView.findViewById(R.id.ac_categroies_rv_bckg);
            this.onCategoryListener = onCategoryListener;

            if (mOnCategoryListener != null)
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mData.size() == getAdapterPosition())
                onCategoryListener.onCategoryClick("");
            else
                onCategoryListener.onCategoryClick(mData.get(getAdapterPosition()));
        }
    }

    public interface OnCategoryListener {
        void onCategoryClick(String catName);
    }
}