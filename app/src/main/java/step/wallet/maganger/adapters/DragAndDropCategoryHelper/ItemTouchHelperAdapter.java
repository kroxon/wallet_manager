package step.wallet.maganger.adapters.DragAndDropCategoryHelper;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
