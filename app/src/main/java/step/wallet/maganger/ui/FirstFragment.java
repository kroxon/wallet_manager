package step.wallet.maganger.ui;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.lang.reflect.Field;

import step.wallet.maganger.R;
import step.wallet.maganger.data.InfoRepository;

public class FirstFragment extends Fragment implements DialogFragmentTransaction.OnInputSelected {

    private static final String TAG = "FirstFragment";

    @Override
    public void sendInput(String input) {
        mInputDisplay.setText(input);
    }


    public Button bexample;
    public Button bexample2;
    public Button bexample3;
    public Button bexample4;
    public Button bStartDialog;
    public TextView text_example;
    private Button mOpenDialog, mOpenDialogBundle;
    public TextView mInputDisplay;
    public TextView tIdIcon;
    public Button bIdIcon;
    private ImageView imgIdIcon;
    private LinearLayout example_icon;


    public int i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        bexample = (Button) view.findViewById(R.id.example_button);
        bexample2 = (Button) view.findViewById(R.id.example_button_2);
        bexample3 = (Button) view.findViewById(R.id.example_button_3);
        bexample4 = (Button) view.findViewById(R.id.example_button_4);
        bStartDialog = (Button) view.findViewById(R.id.button_dialog);
        text_example = (TextView) view.findViewById(R.id.first_example);
        mOpenDialog = view.findViewById(R.id.open_dialog);
        mOpenDialogBundle = view.findViewById(R.id.open_dialog_bundle);
        mInputDisplay = view.findViewById(R.id.input_display);
        tIdIcon = view.findViewById(R.id.tIdIcon);
        bIdIcon = view.findViewById(R.id.bIdIcon);
        imgIdIcon = view.findViewById(R.id.imgIdIcon);
        example_icon = view.findViewById(R.id.example_icon_1);

        bexample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InfoRepository repository = new InfoRepository();
                repository.writeTransaction("" + i, "" + i + 2, "" + i + 3, "" + i + 100,
                        "" + i + 4, "" + i + 5, "" + i + 6, "" + i + 7, "" + i + 9);
                text_example.setText("" + i + " " + (i + 2) + " " + (i + 3) +
                        " " + (i + 4) + " " + (i + 5) + " " + (i + 6) + " " + (i + 7));
                i++;
            }
        });

        bexample2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoRepository repository = new InfoRepository();
                repository.removeTransaction("7");
            }
        });

        bexample3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoRepository repository = new InfoRepository();
                repository.updateTransaction("4", "24");
            }
        });

        bexample4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Category_Activity.class);
//                Intent i = new Intent(getActivity(), Categories_Activity.class);
                startActivity(i);
            }
        });

        bStartDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startDialog();
            }
        });

        mOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragmentTransaction dialog = new DialogFragmentTransaction();
                dialog.setTargetFragment(FirstFragment.this, 1);
                dialog.show(getFragmentManager(), "DialogFragmentTransaction");

            }
        });

        mOpenDialogBundle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragmentTransaction dialog = new DialogFragmentTransaction();
                dialog.setTargetFragment(FirstFragment.this, 1);
                Bundle data = new Bundle();
                data.putString("key", "income");
                dialog.setArguments(data);
                dialog.show(getFragmentManager(), "DialogFragmentTransaction");

            }
        });

        bIdIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tIdIcon.setText(getId("ic_home", R.drawable.class));
                imgIdIcon.setImageResource(getId("ic_home", R.drawable.class));

            }
        });

        example_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                example_icon.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        });

        example_icon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                example_icon.setBackgroundColor(getResources().getColor(R.color.colorButton));
                return false;
            }
        });

        return view;

    }


    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }


//    private void startDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_add_payment_dialog, (ConstraintLayout)findViewById(R.id.layoutDialogContainer)
//        );
//        builder.setView(view);
//
//        final AlertDialog alertDialog = builder.create();
//
//        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        if (alertDialog.getWindow() != null){
//            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        }
//        alertDialog.show();
//    }

}