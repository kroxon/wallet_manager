package step.wallet.maganger.google;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import step.wallet.maganger.R;

import java.util.ArrayList;
import java.util.List;

public abstract class GoogleDriveActivity extends GoogleSignInActivity {

    protected void startGoogleDriveSignIn() {
        startGoogleSignIn();
    }

    protected abstract void onGoogleDriveSignedInSuccess(final Drive driveApi);

    protected abstract void onGoogleDriveSignedInFailed(final ApiException exception);

    @Override
    protected GoogleSignInOptions getGoogleSignInOptions() {
        Scope scopeDriveAppFolder = new Scope(Scopes.DRIVE_APPFOLDER);
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(scopeDriveAppFolder)
                .build();
    }

    @Override
    protected void onGoogleSignedInSuccess(final GoogleSignInAccount signInAccount) {
        initializeDriveClient(signInAccount);
//        initializeAccountInfo(signInAccount);
    }


    @Override
    protected void onGoogleSignedInFailed(final ApiException exception) {
        onGoogleDriveSignedInFailed(exception);
    }

    @Override
    protected void onGoogleSignedOutSuccess(GoogleSignInAccount signInAccount) {

    }

    private void initializeDriveClient(GoogleSignInAccount signInAccount) {
        List<String> scopes = new ArrayList<>();
        scopes.add(DriveScopes.DRIVE_APPDATA);

        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(this, scopes);
        credential.setSelectedAccount(signInAccount.getAccount());
        Drive.Builder builder = new Drive.Builder(
                new NetHttpTransport(),
                new GsonFactory(),
                credential
        );
        String appName = getString(R.string.app_name);
        Drive driveApi = builder
                .setApplicationName(appName)
                .build();
        onGoogleDriveSignedInSuccess(driveApi);
    }

}
