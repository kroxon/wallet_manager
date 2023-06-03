package step.wallet.maganger.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.api.services.drive.Drive;
import com.squareup.picasso.Picasso;

import org.apache.http.util.ByteArrayBuffer;

import step.wallet.maganger.R;
import step.wallet.maganger.adapters.DragAndDropCategoryHelper.CategoryRecycleAdapter;
import step.wallet.maganger.adapters.DragAndDropCategoryHelper.MyItemTouchHelperCallback;
import step.wallet.maganger.adapters.DragAndDropCategoryHelper.OnStartDragListener;
import step.wallet.maganger.data.DBConstants;
import step.wallet.maganger.data.InfoRepository;
import step.wallet.maganger.google.GoogleDriveActivity;
import step.wallet.maganger.google.GoogleDriveApiDataRepository;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends GoogleDriveActivity implements DialogFragmentTransaction.OnInputSelected, FourthFragment.OnGoogleClick,
        DialogFragmentTransaction.OnUploadListener, ThirdFragment.OnUploadListener {

    private static final String LOG_TAG = "MainActivity";

    private static final String GOOGLE_DRIVE_DB_LOCATION = "database";

    private Button googleSignIn;
    private Button googleSignOut;
    private Group contentViews;
    private EditText inputToDb;
    private Button writeToDb;
    private Button saveToGoogleDrive;
    private Button restoreFromDb;
//    private MaterialToolbar toolbar;
//    private DrawerLayout drawerLayout;
//    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private ImageView btnPlus;

//    MeowBottomNavigation bottomNavigation;

    private GoogleDriveApiDataRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
    }


    @Override
    protected void onGoogleDriveSignedInSuccess(Drive driveApi) {
        showMessage(R.string.message_drive_client_is_ready);
        repository = new GoogleDriveApiDataRepository(driveApi);
//        googleSignIn.setVisibility(View.GONE);
//        contentViews.setVisibility(View.VISIBLE);
//        loadAccountInfo();
        if (bottomNavigationView.getSelectedItemId() == R.id.bnmProfile)
            refreshLogin();
    }

    @Override
    protected void onGoogleDriveSignedInFailed(ApiException exception) {
        showMessage(R.string.message_google_sign_in_failed);
        Log.e(LOG_TAG, "error google drive sign in", exception);
    }

    private void findViews() {
//        googleSignIn = findViewById(R.id.google_sign_in);
//        googleSignOut = findViewById(R.id .google_sign_out);
//        contentViews = findViewById(R.id.content_views);
//        inputToDb = findViewById(R.id.edit_text_db_input);
//        writeToDb = findViewById(R.id.write_to_db);
//        saveToGoogleDrive = findViewById(R.id.save_to_google_drive);
//        restoreFromDb = findViewById(R.id.restore_from_db);
//        toolbar = findViewById(R.id.topAppBar);
//        drawerLayout = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.navigation_view);
//        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        btnPlus = findViewById(R.id.plusButton);

    }

    private void initViews() {
//        googleSignIn.setOnClickListener(v -> {
//            startGoogleDriveSignIn();
//        });
        InfoRepository infoRepository = new InfoRepository();
        if (infoRepository.getCheckAutosync()) {
            startGoogleSignIn();
        }
        GoogleSignInAccount acctStart = GoogleSignIn.getLastSignedInAccount(this.getApplicationContext());
        if (acctStart == null) {
            startGoogleSignIn();
        }
//        else
//            loadAccountInfo();


//        googleSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startGoogleSignOut();
//                signOut();
//                revokeAccess();
//                repository = null;
//                googleSignIn.setVisibility(View.VISIBLE);
//                contentViews.setVisibility(View.GONE);
//                unloadAccountInfo();
//            }
//        });

//        writeToDb.setOnClickListener(v -> {
//            String text = inputToDb.getText().toString();
//            InfoRepository repository = new InfoRepository();
//            repository.writeInfo(text);
//            showMessage("Save to local database");
//        });

//        saveToGoogleDrive.setOnClickListener(v -> {
//            File db = new File(DBConstants.DB_LOCATION);
//            if (repository == null) {
//                showMessage(R.string.message_google_sign_in_failed);
//                return;
//            }
//
//            repository.uploadFile(db, GOOGLE_DRIVE_DB_LOCATION)
//                    .addOnSuccessListener(r -> showMessage("Upload success"))
//                    .addOnFailureListener(e -> {
//                        Log.e(LOG_TAG, "error upload file", e);
//                        showMessage("Error upload");
//                    });
//        });
//
//        restoreFromDb.setOnClickListener(v -> {
//            if (repository == null) {
//                showMessage(R.string.message_google_sign_in_failed);
//                return;
//            }
//
//            File db = new File(DBConstants.DB_LOCATION);
//            db.getParentFile().mkdirs();
//            db.delete();
//            repository.downloadFile(db, GOOGLE_DRIVE_DB_LOCATION)
//                    .addOnSuccessListener(r -> {
//                        InfoRepository repository = new InfoRepository();
//                        String infoText = repository.getInfo();
//                        inputToDb.setText(infoText);
//                        showMessage("Retrieved");
//                    })
//                    .addOnFailureListener(e -> {
//                        Log.e(LOG_TAG, "error download file", e);
//                        showMessage("Error download");
//                    });
//        });

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                drawerLayout.openDrawer(GravityCompat.START);
//
//            }
//        });

//        navigationView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
//
//                int id = item.getItemId();
//                drawerLayout.closeDrawer(GravityCompat.START);
//                File db = new File(DBConstants.DB_LOCATION);
//                switch (id) {
//
//                    case R.id.nav_home:
//                        Toast.makeText(MainActivity.this, "Home is Clicked", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.nav_delete:
//                        Toast.makeText(MainActivity.this, "DELETE is Clicked", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.nav_login:
//                        startGoogleDriveSignIn();
//                        Toast.makeText(MainActivity.this, "LOGIN is Clicked", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.nav_logout:
//                        startGoogleSignOut();
//                        signOut();
//                        revokeAccess();
//                        repository = null;
////                        googleSignIn.setVisibility(View.VISIBLE);
////                        contentViews.setVisibility(View.GONE);
//                        unloadAccountInfo();
//                        Toast.makeText(MainActivity.this, "LOGOUT is Clicked", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.nav_upload: {
//                        Toast.makeText(MainActivity.this, "UPLOAD is Clicked", Toast.LENGTH_SHORT).show();
//                        if (repository == null) {
//                            showMessage(R.string.message_google_sign_in_failed);
//                            break;
//                        }
//
//                        repository.uploadFile(db, GOOGLE_DRIVE_DB_LOCATION)
//                                .addOnSuccessListener(r -> showMessage("Upload success"))
//                                .addOnFailureListener(e -> {
//                                    Log.e(LOG_TAG, "error upload file", e);
//                                    showMessage("Error upload");
//                                });
//                    }
//                    break;
//                    case R.id.nav_download: {
//                        Toast.makeText(MainActivity.this, "DOWNLOAD is Clicked", Toast.LENGTH_SHORT).show();
//                        if (repository == null) {
//                            showMessage(R.string.message_google_sign_in_failed);
//                            break;
//                        }
//                        db.getParentFile().mkdirs();
//                        db.delete();
//                        repository.downloadFile(db, GOOGLE_DRIVE_DB_LOCATION)
//                                .addOnSuccessListener(r -> {
////                                    InfoRepository repository = new InfoRepository();
////                                    String infoText = repository.getInfo();
////                                    inputToDb.setText(infoText);
//                                    showMessage("Retrieved");
//                                })
//                                .addOnFailureListener(e -> {
//                                    Log.e(LOG_TAG, "error download file", e);
//                                    showMessage("Error download");
//                                });
//                    }
//                    break;
//                    case R.id.nav_setting:
//                        Toast.makeText(MainActivity.this, "Settings is Clicked", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.nav_email:
//                        Toast.makeText(MainActivity.this, "Email is Clicked", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.nav_rate:
//                        Toast.makeText(MainActivity.this, "Rate us is Clicked", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        return true;
//
//                }
//                return true;
//            }
//        });

        //        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
//        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_history));
//        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_wallet));
//        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_person));
//        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
//            @Override
//            public void onShowItem(MeowBottomNavigation.Model item) {
//                Fragment fragment = null;
//                //check condition
//                switch (item.getId()) {
//                    case 1:
//                        fragment = new FirstFragment();
//                        break;
//                    case 2:
//                        fragment = new SecondFragment();
//                        break;
//                    case 3:
//                        fragment = new ThirdFragment();
//                        loadAccountInfo();
//                        break;
//                    case 4:
//                        fragment = new FourthFragment();
//                        break;
//                }
//                loadfragment(fragment);
//            }
//        });
//
////        bottomNavigation.setCount(1, "");
//
//        bottomNavigation.show(1, true);
//
//        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
//            @Override
//            public void onClickItem(MeowBottomNavigation.Model item) {
//                //display toast
//                Toast.makeText(getApplicationContext(), item.getId() + " is selected", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
//            @Override
//            public void onReselectItem(MeowBottomNavigation.Model item) {
//                //display toast
//                Toast.makeText(getApplicationContext(), item.getId() + " is reselected", Toast.LENGTH_SHORT).show();
//            }
//        });

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.bnmHome:
                    loadfragment(new FirstFragment());
                    break;
                case R.id.bnmHistory:
                    loadfragment(new SecondFragment());
                    break;
                case R.id.bnmOverview:
                    loadfragment(new ThirdFragment());
                    break;
                case R.id.bnmProfile:
                    loadfragment(new FourthFragment());
                    break;
            }

            return true;
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });

//        loadAccountInfo();

        View view = bottomNavigationView.findViewById(R.id.bnmHome);
        view.performClick();
    }

//    private void loadAccountInfo() {
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this.getApplicationContext());
//        if (acct != null) {
//            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            String personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
//
//            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
//            View headerView = navigationView.getHeaderView(0);
//            TextView tvUsername = (TextView) headerView.findViewById(R.id.userName);
//            TextView tvMail = (TextView) headerView.findViewById(R.id.userMail);
//            TextView tvIcon = (TextView) headerView.findViewById(R.id.icon_text);
//            ImageView imgAccount = (ImageView) headerView.findViewById(R.id.account_icon);
//            RelativeLayout relativeLayoutIcon = (RelativeLayout) headerView.findViewById(R.id.icon_container);
//
//            tvMail.setVisibility(View.VISIBLE);
//            tvMail.setText(personEmail);
//            tvUsername.setVisibility(View.VISIBLE);
//            tvUsername.setText(personName);
//            relativeLayoutIcon.setVisibility(View.VISIBLE);
//            tvIcon.setVisibility(View.VISIBLE);
//        }
//    }

//    private void unloadAccountInfo() {
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
//        View headerView = navigationView.getHeaderView(0);
//        TextView tvUsername = (TextView) headerView.findViewById(R.id.userName);
//        TextView tvMail = (TextView) headerView.findViewById(R.id.userMail);
//        TextView tvIcon = (TextView) headerView.findViewById(R.id.icon_text);
//        RelativeLayout relativeLayoutIcon = (RelativeLayout) headerView.findViewById(R.id.icon_container);
//
//        tvMail.setVisibility(View.GONE);
//        tvUsername.setVisibility(View.GONE);
//        relativeLayoutIcon.setVisibility(View.GONE);
//        tvIcon.setVisibility(View.GONE);
//    }


    private void loadfragment(Fragment fragment) {
        //replace fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    private void showBottomSheetDialog() {
        final Dialog bsDialog = new Dialog(this);
        bsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bsDialog.setContentView(R.layout.bottom_sheet_layout);
        bsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout incomelayout = bsDialog.findViewById(R.id.bSheetIncome);
        LinearLayout expenseLayout = bsDialog.findViewById(R.id.bSheetExpense);
        ImageView closeBSheet = bsDialog.findViewById(R.id.closeBottmSheet);

        expenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentTransaction dialog = new DialogFragmentTransaction();
                dialog.setInputListener(MainActivity.this);
                dialog.show(getFragmentManager(), "DialogFragmentTransaction");
                bsDialog.dismiss();
            }
        });

        incomelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "new income transaction");
                DialogFragmentTransaction dialog = new DialogFragmentTransaction();
                dialog.setInputListener(MainActivity.this);
                dialog.setArguments(bundle);
                dialog.getFragmentManager();
                dialog.show(getFragmentManager(), "DialogFragmentTransaction");
                bsDialog.dismiss();
            }
        });

        closeBSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsDialog.dismiss();
            }
        });

        bsDialog.show();
        bsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bsDialog.getWindow().getAttributes().windowAnimations = R.style.DialogSheetAnimation;
        bsDialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    @Override
    public void sendSelected() {
//        View startView = bottomNavigationView.findViewById(bottomNavigationView.getSelectedItemId());
//        View view = bottomNavigationView.findViewById(R.id.bnmHistory);
//        view.performClick();
//        startView.performClick();
    }

    public void refreshLogin() {
        View view1 = bottomNavigationView.findViewById(R.id.bnmHistory);
        View view2 = bottomNavigationView.findViewById(R.id.bnmProfile);
        view1.performClick();
        view2.performClick();
    }

    @Override
    public void googleClick(String google) {
        File db = new File(DBConstants.DB_LOCATION);
        if (google.equals("sign in"))
            startGoogleDriveSignIn();
        if (google.equals("sign out"))
            startGoogleSignOut();
        if (google.equals("download")) {
            if (repository == null) {
                showMessage(R.string.message_google_sign_in_failed);
            } else {
                db.getParentFile().mkdirs();
                db.delete();
                repository.downloadFile(db, GOOGLE_DRIVE_DB_LOCATION)
                        .addOnSuccessListener(r -> {
//                                    InfoRepository repository = new InfoRepository();
//                                    String infoText = repository.getInfo();
//                                    inputToDb.setText(infoText);
                            showMessage("Retrieved");
                        })
                        .addOnFailureListener(e -> {
                            Log.e(LOG_TAG, "error download file", e);
                            showMessage("Error download");
                        });
            }
        }

        if (google.equals("upload")) {
            if (repository == null) {
                showMessage(R.string.message_google_sign_in_failed);
            } else {
                repository.uploadFile(db, GOOGLE_DRIVE_DB_LOCATION)
                        .addOnSuccessListener(r -> showMessage("Upload success"))
                        .addOnFailureListener(e -> {
                            Log.e(LOG_TAG, "error upload file", e);
                            showMessage("Error upload");
                        });
            }
        }

    }

    @Override
    public void sendToUpload() {
        InfoRepository infoRepository = new InfoRepository();
        if (infoRepository.getCheckAutosync()) {
            File db = new File(DBConstants.DB_LOCATION);
            if (repository == null) {
                showMessage(R.string.message_google_sign_in_failed);
            } else {
                repository.uploadFile(db, GOOGLE_DRIVE_DB_LOCATION)
                        .addOnSuccessListener(r -> showMessage("Upload success"))
                        .addOnFailureListener(e -> {
                            Log.e(LOG_TAG, "error upload file", e);
                            showMessage("Error upload");
                        });
            }
            if (bottomNavigationView.getSelectedItemId() == R.id.bnmHistory) {
                View view1 = bottomNavigationView.findViewById(R.id.bnmHistory);
                View view2 = bottomNavigationView.findViewById(R.id.bnmHome);
                view2.performClick();
                view1.performClick();
            }
            if (bottomNavigationView.getSelectedItemId() == R.id.bnmOverview) {
                View view1 = bottomNavigationView.findViewById(R.id.bnmOverview);
                View view2 = bottomNavigationView.findViewById(R.id.bnmHome);
                view2.performClick();
                view1.performClick();
            }
        }
    }
}
