package step.wallet.maganger.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.data.InfoRepository;

public class SpinnerCategoryAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> categoriesList;

    public SpinnerCategoryAdapter(Context context, List<String> categories) {
        super(context, R.layout.my_selected_item, categories);
        this.context = context;
        this.categoriesList = categories;
    }

    // Override these methods and instead return our custom view (with image and text)
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // Function to return our custom View (View with an image and text)
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.dialog_transaction_category_dropdown, parent, false);

        // Image and TextViews
        TextView name = (TextView) row.findViewById(R.id.dialogTransactionCategoryDropdownTxt);
        ImageView icon = (ImageView) row.findViewById(R.id.dialogTransactionCategoryDropdownImg);


//        // Get category image from drawables folder
//        Resources res = context.getResources();
//        String drawableName = statesList.get(position).toLowerCase(); // tx
//        int resId = res.getIdentifier(drawableName, "drawable", context.getPackageName());
//        Drawable drawable = res.getDrawable(resId);
//
//        //Set state abbreviation and state flag
//        state.setText(statesList.get(position));
//        flag.setImageDrawable(drawable);

        //Set name and category icon
        InfoRepository repository = new InfoRepository();
        name.setText(categoriesList.get(position));
        icon.setImageResource(repository.getIdCategoryIcon(categoriesList.get(position)));
        return row;
    }
}
