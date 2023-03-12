package step.wallet.maganger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import step.wallet.maganger.R;
import step.wallet.maganger.adapters.DragAndDropCategoryHelper.CategoryRecycleAdapter;
import step.wallet.maganger.adapters.DragAndDropCategoryHelper.MyItemTouchHelperCallback;
import step.wallet.maganger.adapters.DragAndDropCategoryHelper.OnStartDragListener;

public class Categories_Activity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        init();
        generateItems();
    }

    private void init() {
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void generateItems() {
        List<String> data = new ArrayList<>();
        data.addAll(Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven",
                "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fiveteen", "Sixteen"));
        CategoryRecycleAdapter adapter = new CategoryRecycleAdapter(this, data, viewHolder -> {
            itemTouchHelper.startDrag(viewHolder);
        });
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}