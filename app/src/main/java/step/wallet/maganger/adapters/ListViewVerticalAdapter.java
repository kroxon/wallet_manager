package step.wallet.maganger.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.data.InfoRepository;

public class ListViewVerticalAdapter extends ArrayAdapter<String> {

    ArrayList<String> list;
    Context context;
    String idCategory;
    OnShareClickedListener mCallback;

    public ListViewVerticalAdapter(Context context, ArrayList<String> items, String idCategory) {
        super(context, R.layout.list_row_ac_subcat, items);
        this.context = context;
        list = items;
        this.idCategory = idCategory;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_ac_subcat, null);
        }

        TextView name = convertView.findViewById(R.id.acAdapterSubcatName);
        name.setText(list.get(position));

        ImageView moreOptions = convertView.findViewById(R.id.acAdapterSubcatOption);
        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                switch (view.getId()) {
                    case R.id.acAdapterSubcatOption:
                        PopupMenu popup = new PopupMenu(context, view);
                        popup.getMenuInflater().inflate(R.menu.menu_ac_subcats,
                                popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                InfoRepository infoRepository = new InfoRepository();

                                switch (item.getItemId()) {

                                    case R.id.ac_subcat_edit:
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                        EditText taskEditText = new EditText(context);
                                        alertDialogBuilder.setTitle("Edit subcategory");
                                        alertDialogBuilder
                                                .setCancelable(false)
                                                .setView(taskEditText)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        List<String> labels = infoRepository.getSubcategories(idCategory);
                                                        if (!labels.contains(taskEditText.getText().toString())) {
                                                            Toast.makeText(context, "Updated!", Toast.LENGTH_LONG).show();
                                                            infoRepository.updateSubcategoryName(taskEditText.getText().toString(), list.get(position), idCategory);
                                                        } else
                                                            Toast.makeText(context, "\"" + taskEditText.getText().toString() + "\" already exists!", Toast.LENGTH_LONG).show();
                                                        mCallback.ShareClicked(list.get(position));
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                        break;
                                    case R.id.ac_subcat_delete:
                                        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);
                                        alertDialogBuilder2.setTitle("Do you want to delete \"" + list.get(position) + "\"?");
                                        alertDialogBuilder2
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        infoRepository.removeSubategoryByName(list.get(position), idCategory);
                                                        mCallback.ShareClicked(list.get(position));
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // if this button is clicked, just close
                                                        // the dialog box and do nothing
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alertDialog2 = alertDialogBuilder2.create();
                                        alertDialog2.show();
                                        break;

                                    default:
                                        break;
                                }
                                return true;
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });

        return convertView;
    }

    public void setOnShareClickedListener(OnShareClickedListener mCallback) {
        this.mCallback = mCallback;
    }

    public interface OnShareClickedListener {
        public void ShareClicked(String subcategoryName);
    }
}
