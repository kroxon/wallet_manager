package step.wallet.maganger.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.adapters.RecyclerViewCategoryTrasactionAdapter;
import step.wallet.maganger.adapters.RecyclerViewIconAdapter;
import step.wallet.maganger.data.InfoRepository;

public class DialogFragmentCategorySelect extends DialogFragment implements RecyclerViewCategoryTrasactionAdapter.ItemClickListener {

    private static final String TAG = "DialogFragmentCategorySelect";
    public int imageResource;
    public String categoryName = "";

    public interface OnInputSelected {
        void sendInput(String catName, int catIcon);
    }

    public OnInputSelected mOnInputSelected;

    @Override
    public void onItemClick(String catName, int catIcon) {
        imageResource = catIcon;
        categoryName = catName;
        try {
//            mOnInputSelected.sendInput(categoryName, imageResource);
            sendData();
        } catch (Exception e) {
            Toast.makeText(getContext(), "exception", Toast.LENGTH_SHORT).show();
        }

        getDialog().dismiss();
    }


    private Button bSave, bCancel;
    private RecyclerViewCategoryTrasactionAdapter adapter;
    public RecyclerView recyclerView;
    public List<String> data;
    public ImageView bigSelectedIcon;

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tr_category_select, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // data to populate the RecyclerView with
        InfoRepository infoRepository = new InfoRepository();
        data = infoRepository.getAllCategories();

//        data = new String[]{"ic_home", "ic_add", "ic_coffee", "ic_email", "ic_home", "ic_add", "ic_coffee", "ic_email", "ic_home", "ic_add", "ic_coffee", "ic_email", "ic_home", "ic_add", "ic_coffee", "ic_email"};


        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.trRVCategorySelect);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        loadRecycleViewer(data, getContext());


        if (!categoryName.equals("")) {
            mOnInputSelected.sendInput(categoryName, imageResource);
            getDialog().dismiss();
        }

        return view;
    }

    public void loadRecycleViewer(List<String> icons, Context context) {
        adapter = new RecyclerViewCategoryTrasactionAdapter(getContext(), icons);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected
                    = (OnInputSelected) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }

    public void sendData() {
        mOnInputSelected.sendInput(categoryName, imageResource);
//        Toast.makeText(getContext(), "Send data", Toast.LENGTH_SHORT).show();
    }

}
