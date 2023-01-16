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

import step.wallet.maganger.R;
import step.wallet.maganger.adapters.RecyclerViewIconAdapter;

public class DialogFragmentIconSelect extends DialogFragment implements RecyclerViewIconAdapter.ItemClickListener {

    private static final String TAG = "DialogFragmentIconSelect";
    public int imageResource;

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked number " + position, Toast.LENGTH_SHORT).show();
        bigSelectedIcon.setImageResource(position);
        imageResource = position;
    }

    public interface OnInputSelected {
        void sendInput(int input);
    }

    public OnInputSelected mOnInputSelected;

    public interface OnInputListener {
        void sendInput(int input);
    }
    public OnInputListener mOnInputListener;

    //widgets
//    private TextView mActionOk, mActionCancel, tvInput;
//    private TextView btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btn00;
//    private TextView btBksp, btDecimal, btClr;
//    private AutoCompleteTextView actvCat, actvSubCat;
//    private Spinner subCatSpinner;
    private Button bSave, bCancel;
    private RecyclerViewIconAdapter adapter;
    public RecyclerView recyclerView;
    public String[] data;
    public ImageView bigSelectedIcon;

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_icon_select, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bSave = view.findViewById(R.id.bIconSave);
        bCancel = view.findViewById(R.id.bIconCancel);
        bigSelectedIcon = view.findViewById(R.id.acIconCategory);

        // data to populate the RecyclerView with
        data = new String[]{"ic_home", "ic_add", "ic_coffee", "ic_email", "ic_home", "ic_add", "ic_coffee", "ic_email", "ic_home", "ic_add", "ic_coffee", "ic_email", "ic_home", "ic_add", "ic_coffee", "ic_email"};

        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.acRVIconSelect);
        int numberOfColumns = 6;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        loadRecycleViewer(data, getActivity());

//

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageResource != 0);
                mOnInputListener.sendInput(imageResource);
                getDialog().dismiss();
            }
        });

        return view;
    }

    public void loadRecycleViewer (String[] icons, Context context) {
        adapter = new RecyclerViewIconAdapter(getContext(), icons);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            mOnInputListener
                    = (OnInputListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }

}
