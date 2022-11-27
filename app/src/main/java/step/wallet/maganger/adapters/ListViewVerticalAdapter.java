package step.wallet.maganger.adapters;


//adapter displays subcategories in Category Activity

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import step.wallet.maganger.R;
import step.wallet.maganger.data.InfoRepository;

public class ListViewVerticalAdapter extends ArrayAdapter<String>  {

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

        TextView number = convertView.findViewById(R.id.acAdapterSubcatIdNo);
        number.setText(position + 1 + ".");

        TextView name = convertView.findViewById(R.id.acAdapterSubcatName);
        name.setText(list.get(position));

        ImageView moreOptions = convertView.findViewById(R.id.acAdapterSubcatOption);
        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                switch (view.getId()) {
                    case R.id.acAdapterSubcatOption:
                        PopupMenu popup = new PopupMenu(context, view);
                        popup.getMenuInflater().inflate(R.menu.menu_ac,
                                popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.ac_subcat_edit:

                                        //Or Some other code you want to put here.. This is just an example.
                                        Toast.makeText(context, " Edit Clicked", Toast.LENGTH_LONG).show();
                                        mCallback.ShareClicked("1" + list.get(position));

                                        break;
                                    case R.id.ac_subcat_delete:
//                                        InfoRepository repository = new InfoRepository();
//                                        repository.removeSubategoryByName(list.get(position), idCategory);
                                        Toast.makeText(context, list.get(position) + " deleted", Toast.LENGTH_LONG).show();
                                        mCallback.ShareClicked("0" + list.get(position));
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
        public void ShareClicked(String url);
    }
}
