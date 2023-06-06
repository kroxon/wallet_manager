package step.wallet.maganger.ui;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import step.wallet.maganger.R;
import step.wallet.maganger.data.InfoRepository;


public class FourthFragment extends Fragment {

    public OnGoogleClick googleClick;

    public interface OnGoogleClick {
        void googleClick(String google);
    }


    private TextView accInitials;
    private TextView accName;
    private TextView accMail;

//    private Button testBtn;

    private com.google.android.gms.common.SignInButton gsiBtn;
    private ConstraintLayout googleAccLayout;
    private ConstraintLayout deleteLayout;
    private ConstraintLayout defaultLayout;
    private ConstraintLayout uploadLayout;
    private ConstraintLayout downloadLayout;
    private ConstraintLayout loggedLayout;
    private ConstraintLayout notLoggedLayout;
    private ConstraintLayout autosyncLayout;

    private Switch autologSwitch;

    // buttons for testing
//    private Button defaultDb;
//    private Button btnArchived;
//    private Button btnDeleteDb;

    public FourthFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);

        findViews(view);
        init(view);


        return view;
    }

    private void findViews(View view) {
        accInitials = view.findViewById(R.id.gsi_account_nitials);
        accName = view.findViewById(R.id.gsi_account_name);
        accMail = view.findViewById(R.id.gsi_account_mail);
        gsiBtn = view.findViewById(R.id.gsiButton);
        googleAccLayout = view.findViewById(R.id.googleAccountLayout);
        deleteLayout = view.findViewById(R.id.constraintLayoutDeleteDb);
        defaultLayout = view.findViewById(R.id.constraintLayoutDefaultDb);
        uploadLayout = view.findViewById(R.id.constraintLayoutDriveUpload);
        downloadLayout = view.findViewById(R.id.constraintLayoutDriveDownload);
        autologSwitch = view.findViewById(R.id.switchAutoLog);
        loggedLayout = view.findViewById(R.id.loggedLayout);
        notLoggedLayout = view.findViewById(R.id.notLoggedLayout);
        autosyncLayout = view.findViewById(R.id.autosyncLayout);

        // test
//        defaultDb = view.findViewById(R.id.btnDefaultDb);
//        btnArchived = view.findViewById(R.id.btnRandomArchived);
//        btnDeleteDb = view.findViewById(R.id.btnDeleteDb);
//        testBtn = view.findViewById(R.id.testSync);
    }

    // testing database
    private void init(View view) {
        InfoRepository repository = new InfoRepository();

        gsiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
                if (acct != null) {
                    googleClick.googleClick("sign out");
                    autologSwitch.setChecked(false);
                } else {
                    googleClick.googleClick("sign in");
                    autologSwitch.setChecked(true);
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadAccountInfo();
                    }
                }, 1000);
            }
        });


        defaultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDefaultDb();
            }
        });

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDeleteDialog();
            }
        });

        autologSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (autologSwitch.isChecked())
                    repository.setAutosync("true");
                else
                    repository.setAutosync("false");
            }
        });

        // setting Google button text

        try {
            ((TextView) gsiBtn.getChildAt(0)).setText(R.string.log_in);
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
        }

        if (repository.getCheckAutosync()) {
            autologSwitch.setChecked(true);
            loadAccountInfo();
        } else
            autologSwitch.setChecked(false);


        uploadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleClick.googleClick("upload");
            }
        });

        downloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleClick.googleClick("download");
            }
        });

        // test
//        testBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InfoRepository repo = new InfoRepository();
//                Toast.makeText(getContext(), String.valueOf(repo.getCheckAutosync()), Toast.LENGTH_SHORT).show();
//            }
//        });

        autosyncLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autologSwitch.performClick();
            }
        });

    }

    private void loadAccountInfo() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();

            accName.setText(personName);
            accMail.setText(personEmail);
            String[] strArray = personName.split(" ");
            StringBuilder builder = new StringBuilder();
            if (strArray.length > 0)
                builder.append(strArray[0], 0, 1);
            if (strArray.length > 1)
                builder.append(strArray[1], 0, 1);
            accInitials.setText(builder.toString());
            notLoggedLayout.setVisibility(View.GONE);
            loggedLayout.setVisibility(View.VISIBLE);
            try {
                ((TextView) gsiBtn.getChildAt(0)).setText(R.string.log_out);
            } catch (ClassCastException | NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            notLoggedLayout.setVisibility(View.VISIBLE);
            loggedLayout.setVisibility(View.GONE);
            try {
                ((TextView) gsiBtn.getChildAt(0)).setText(R.string.log_in);
            } catch (ClassCastException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayDeleteDialog() {
        Dialog descpriptionDialog = new Dialog(getContext());
        descpriptionDialog.setContentView(R.layout.dialog_acc_dialog_delete);
        descpriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        descpriptionDialog.show();

        TextView dDeleteTitle = descpriptionDialog.findViewById(R.id.dAccDeletTitle);
        TextView dDeleteNo = descpriptionDialog.findViewById(R.id.dAccDeletDNo);
        TextView dDeleteYes = descpriptionDialog.findViewById(R.id.dAccDeletDYes);
        String deleteString1 = getContext().getString(R.string.delete_db_ask);

        dDeleteTitle.setText(deleteString1);


        dDeleteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descpriptionDialog.dismiss();
            }
        });

        dDeleteYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoRepository infoRepository = new InfoRepository();
                getContext().deleteDatabase("WalletManager.db");
                if (infoRepository.getAllCategories().size() == 0)
                    Toast.makeText(getContext(), getContext().getString(R.string.removed_db), Toast.LENGTH_SHORT).show();
                descpriptionDialog.dismiss();
            }
        });
    }

    private void displayDefaultDb() {
        Dialog descpriptionDialog = new Dialog(getContext());
        descpriptionDialog.setContentView(R.layout.dialog_acc_dialog_delete);
        descpriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        descpriptionDialog.show();

        TextView dDeleteTitle = descpriptionDialog.findViewById(R.id.dAccDeletTitle);
        TextView dDeleteNo = descpriptionDialog.findViewById(R.id.dAccDeletDNo);
        TextView dDeleteYes = descpriptionDialog.findViewById(R.id.dAccDeletDYes);
        String deleteString1 = getContext().getString(R.string.default_db_ask);

        dDeleteTitle.setText(deleteString1);


        dDeleteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descpriptionDialog.dismiss();
            }
        });

        dDeleteYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // deleting
                InfoRepository infoRepository = new InfoRepository();
                getContext().deleteDatabase("WalletManager.db");
                if (infoRepository.getAllCategories().size() == 0)
                    Toast.makeText(getContext(), getContext().getString(R.string.removed_db), Toast.LENGTH_SHORT).show();

                // add default db

                String[] categories = getResources().getStringArray(R.array.categories);
                List<String[]> subcategories = new ArrayList<String[]>();
                subcategories.add(getResources().getStringArray(R.array.subcategories1));
                subcategories.add(getResources().getStringArray(R.array.subcategories2));
                subcategories.add(getResources().getStringArray(R.array.subcategories3));
                subcategories.add(getResources().getStringArray(R.array.subcategories4));
                subcategories.add(getResources().getStringArray(R.array.subcategories5));
                subcategories.add(getResources().getStringArray(R.array.subcategories6));
                subcategories.add(getResources().getStringArray(R.array.subcategories7));
                subcategories.add(getResources().getStringArray(R.array.subcategories8));
                subcategories.add(getResources().getStringArray(R.array.subcategories9));
                subcategories.add(getResources().getStringArray(R.array.subcategories10));
                subcategories.add(getResources().getStringArray(R.array.subcategories11));
                subcategories.add(getResources().getStringArray(R.array.subcategories12));
                subcategories.add(getResources().getStringArray(R.array.subcategories13));
                subcategories.add(getResources().getStringArray(R.array.subcategories14));
                subcategories.add(getResources().getStringArray(R.array.subcategories15));
                String[] icons = getResources().getStringArray(R.array.category_icon);
                String[] colors = getResources().getStringArray(R.array.category_colors);
                String[] accountssList = getResources().getStringArray(R.array.accounts_names);

                InfoRepository repository = new InfoRepository();
                repository.addDefaultDatabase(categories, subcategories, icons, colors, accountssList);

                descpriptionDialog.dismiss();
            }
        });
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            googleClick = (OnGoogleClick) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}