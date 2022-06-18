package step.wallet.maganger.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.api.services.drive.Drive;
import com.squareup.picasso.Picasso;

import org.apache.http.util.ByteArrayBuffer;

import step.wallet.maganger.R;
import step.wallet.maganger.data.DBConstants;
import step.wallet.maganger.data.InfoRepository;
import step.wallet.maganger.google.GoogleDriveActivity;
import step.wallet.maganger.google.GoogleDriveApiDataRepository;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends GoogleDriveActivity {

    private static final String LOG_TAG = "MainActivity";

    private static final String GOOGLE_DRIVE_DB_LOCATION = "database";

    private Button googleSignIn;
    private Button googleSignOut;
    private Group contentViews;
    private EditText inputToDb;
    private Button writeToDb;
    private Button saveToGoogleDrive;
    private Button restoreFromDb;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    MeowBottomNavigation bottomNavigation;

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
        googleSignIn.setVisibility(View.GONE);
        contentViews.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onGoogleDriveSignedInFailed(ApiException exception) {
        showMessage(R.string.message_google_sign_in_failed);
        Log.e(LOG_TAG, "error google drive sign in", exception);
    }

    private void findViews() {
        googleSignIn = findViewById(R.id.google_sign_in);
        googleSignOut = findViewById(R.id.google_sign_out);
        contentViews = findViewById(R.id.content_views);
        inputToDb = findViewById(R.id.edit_text_db_input);
        writeToDb = findViewById(R.id.write_to_db);
        saveToGoogleDrive = findViewById(R.id.save_to_google_drive);
        restoreFromDb = findViewById(R.id.restore_from_db);
        toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigation = findViewById(R.id.bottom_navigation);


    }

    private void initViews() {
        googleSignIn.setOnClickListener(v -> {
            startGoogleDriveSignIn();
        });

        googleSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGoogleSignOut();
                signOut();
                revokeAccess();
                repository = null;
                googleSignIn.setVisibility(View.VISIBLE);
                contentViews.setVisibility(View.GONE);
                unloadAccountInfo();
            }
        });

        writeToDb.setOnClickListener(v -> {
            String text = inputToDb.getText().toString();
            InfoRepository repository = new InfoRepository();
            repository.writeInfo(text);
            showMessage("Save to local database");
        });

        saveToGoogleDrive.setOnClickListener(v -> {
            File db = new File(DBConstants.DB_LOCATION);
            if (repository == null) {
                showMessage(R.string.message_google_sign_in_failed);
                return;
            }

            repository.uploadFile(db, GOOGLE_DRIVE_DB_LOCATION)
                    .addOnSuccessListener(r -> showMessage("Upload success"))
                    .addOnFailureListener(e -> {
                        Log.e(LOG_TAG, "error upload file", e);
                        showMessage("Error upload");
                    });
        });

        restoreFromDb.setOnClickListener(v -> {
            if (repository == null) {
                showMessage(R.string.message_google_sign_in_failed);
                return;
            }

            File db = new File(DBConstants.DB_LOCATION);
            db.getParentFile().mkdirs();
            db.delete();
            repository.downloadFile(db, GOOGLE_DRIVE_DB_LOCATION)
                    .addOnSuccessListener(r -> {
                        InfoRepository repository = new InfoRepository();
                        String infoText = repository.getInfo();
                        inputToDb.setText(infoText);
                        showMessage("Retrieved");
                    })
                    .addOnFailureListener(e -> {
                        Log.e(LOG_TAG, "error download file", e);
                        showMessage("Error download");
                    });
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {

                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this, "Home is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_delete:
                        Toast.makeText(MainActivity.this, "DELETE is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_login:
                        Toast.makeText(MainActivity.this, "login is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_setting:
                        Toast.makeText(MainActivity.this, "Settings is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_email:
                        Toast.makeText(MainActivity.this, "Email is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_rate:
                        Toast.makeText(MainActivity.this, "Rate us is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;

                }
                return true;
            }
        });

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_history));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_wallet));
//        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_charts));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                //check condition
                switch (item.getId()) {
                    case 1:
                        //when id i 1
                        //initialize first fragment
                        fragment = new FirstFragment();
                        break;
                    case 2:
                        //when id i 2
                        //initialize second fragment
                        fragment = new SecondFragment();
                        break;
                    case 3:
                        //when id i 3
                        //initialize third fragment
                        fragment = new ThirdFragment();
                        loadAccountInfo();
                        break;
//                    case 4:
//                        //when id i 3
//                        //initialize third fragment
//                        fragment = new FourthFragment();
//                        break;
                }
                loadfragment(fragment);
            }
        });

        bottomNavigation.setCount(1, "");

        bottomNavigation.show(1, true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //display toast
                Toast.makeText(getApplicationContext(), item.getId() + " is selected", Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //display toast
                Toast.makeText(getApplicationContext(), item.getId() + " is reselected", Toast.LENGTH_SHORT).show();
            }
        });

//        loadAccountInfo();
    }

    private void loadAccountInfo() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this.getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
            View headerView = navigationView.getHeaderView(0);
            TextView tvUsername = (TextView) headerView.findViewById(R.id.userName);
            TextView tvMail = (TextView) headerView.findViewById(R.id.userMail);
            TextView tvIcon = (TextView) headerView.findViewById(R.id.icon_text);
            RelativeLayout relativeLayoutIcon = (RelativeLayout) headerView.findViewById(R.id.icon_container);

            tvMail.setVisibility(View.VISIBLE);
            tvMail.setText(personEmail);
            tvUsername.setVisibility(View.VISIBLE);
            tvUsername.setText(personName);
            relativeLayoutIcon.setVisibility(View.VISIBLE);
            tvIcon.setVisibility(View.VISIBLE);
            tvIcon.setText(String.valueOf(String.valueOf(personGivenName.charAt(0))));
        }
    }

    private void unloadAccountInfo() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tvUsername = (TextView) headerView.findViewById(R.id.userName);
        TextView tvMail = (TextView) headerView.findViewById(R.id.userMail);
        TextView tvIcon = (TextView) headerView.findViewById(R.id.icon_text);
        RelativeLayout relativeLayoutIcon = (RelativeLayout) headerView.findViewById(R.id.icon_container);

        tvMail.setVisibility(View.GONE);
        tvUsername.setVisibility(View.GONE);
        relativeLayoutIcon.setVisibility(View.GONE);
        tvIcon.setVisibility(View.GONE);
    }


    private void loadfragment(Fragment fragment) {
        //replace fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }


}
