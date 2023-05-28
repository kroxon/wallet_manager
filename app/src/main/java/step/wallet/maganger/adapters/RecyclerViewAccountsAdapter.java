package step.wallet.maganger.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.classes.Account;
import step.wallet.maganger.classes.CurrencyStrings;
import step.wallet.maganger.data.CurrencyDatabase;
import step.wallet.maganger.data.InfoRepository;

public class RecyclerViewAccountsAdapter extends RecyclerView.Adapter<RecyclerViewAccountsAdapter.ViewHolder> {

    // new

    private ArrayList<Account> accountsList;
    private Context context;
    private OnItemClickListener mListener;

    // old
    private List<String> mData;
    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;
    private static int lastClickedPosition = -1;
    private int selectedItem;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
     }


    // data is passed into the constructor
    public RecyclerViewAccountsAdapter(Context context, ArrayList<Account> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.accountsList = data;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_row_account, parent, false);
        return new ViewHolder(view, mListener);
    }

    // binds the data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InfoRepository repository = new InfoRepository();
        holder.myNameTxt.setText(accountsList.get(position).getAccountName());
        holder.myBalanceTxt.setText(accountsList.get(position).getAccountBalance());
        CurrencyDatabase currencyDatabase = new CurrencyDatabase(context);
        ArrayList<CurrencyStrings> currentList = currencyDatabase.getCurrenciesList();
        for (int i = 0; i < currentList.size(); i++) {
            if (currentList.get(i).getName().equals(accountsList.get(position).getAccountCurrency())) {
                holder.myCurrencyTxt.setText(currentList.get(i).getSymbol());
                break;
            }
        }
//        holder.myDescriptonTxt.setText(accountsList.get(position).getAccountDescription());

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return accountsList.size();
    }


    // stores and recycles views as they are scrolled off screen
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView myImage;
        TextView myNameTxt;
        TextView myBalanceTxt;
        TextView myCurrencyTxt;

        ConstraintLayout myLayout;
//        TextView myDescriptonTxt;
        int position = getLayoutPosition();

        ViewHolder(View itemView, OnItemClickListener listner) {
            super(itemView);
//            myImage = itemView.findViewById(R.id.accountOption);
            myNameTxt = itemView.findViewById(R.id.accountNameTxt);
            myBalanceTxt = itemView.findViewById(R.id.accountBalanceTxt);
            myCurrencyTxt = itemView.findViewById(R.id.accountCurrencyTxt);
            myLayout = itemView.findViewById(R.id.idAccountContiner);
//            myDescriptonTxt = itemView.findViewById(R.id.accountDescriptionTxt);
//            itemView.setOnClickListener(this);

            myLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, myLayout);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.menu_account);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_acc_edit:
                                    //handle menu1 click
                                    if (listner != null && getPosition() != RecyclerView.NO_POSITION) {
                                        listner.onItemClick(getAdapterPosition());
                                    }
                                    break;
                                case R.id.menu_acc_delete:
                                    //handle delete click
//
//                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                                    alertDialogBuilder.setTitle(context.getResources().getString(R.string.delete_ask_1) + " \"" + accountsList.get(getAdapterPosition()).getAccountName() + "\"\n"
//                                            + context.getResources().getString(R.string.delete_ask_2));
//                                    alertDialogBuilder
//                                            .setCancelable(false)
//                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    InfoRepository repository = new InfoRepository();
//                                                    repository.removeAccountByName(accountsList.get(getBindingAdapterPosition()).getAccountName());
//                                                    dialog.cancel();
//                                                }
//                                            })
//                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    // if this button is clicked, just close
//                                                    // the dialog box and do nothing
//                                                    dialog.cancel();
//                                                }
//                                            });
//                                    AlertDialog alertDialog = alertDialogBuilder.create();
//                                    alertDialog.show();
                                    displayDeleteDialog(accountsList.get(getAdapterPosition()).getAccountName());
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });

        }

    }

    // convenience method for getting data at click position
    public String getItem(int id) {

        return mData.get(id);
    }



    private void displayDeleteDialog(String name) {
        Dialog descpriptionDialog = new Dialog(context);
        descpriptionDialog.setContentView(R.layout.dialog_acc_dialog_delete);
//        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
//        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
//        descpriptionDialog.getWindow().setLayout(width, height);
        descpriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        descpriptionDialog.show();

        TextView dDeleteTitle = descpriptionDialog.findViewById(R.id.dAccDeletTitle);
        TextView dDeleteNo = descpriptionDialog.findViewById(R.id.dAccDeletDNo);
        TextView dDeleteYes = descpriptionDialog.findViewById(R.id.dAccDeletDYes);
        String deleteString1 = context.getString(R.string.delete_ask_1);
        String deleteString2 = context.getString(R.string.delete_ask_2);

        dDeleteTitle.setText(deleteString1 + " \"" + name + "\" " + deleteString2);


        dDeleteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descpriptionDialog.dismiss();
            }
        });

        dDeleteYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoRepository repository = new InfoRepository();
                repository.removeAccountByName(name);
//
                descpriptionDialog.dismiss();
            }
        });
    }

}