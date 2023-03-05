package step.wallet.maganger.ui;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import step.wallet.maganger.R;
import step.wallet.maganger.adapters.DragAndDropCategoryHelper.CategoryRecycleAdapter;
import step.wallet.maganger.adapters.DragAndDropCategoryHelper.MyItemTouchHelperCallback;
import step.wallet.maganger.adapters.DragAndDropCategoryHelper.OnStartDragListener;


public class ThirdFragment extends Fragment {


    public ThirdFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();
//        generateItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }


}