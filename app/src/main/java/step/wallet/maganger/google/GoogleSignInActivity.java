package step.wallet.maganger.google;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.drive.Drive;

import step.wallet.maganger.app.AppActivity;

public abstract class GoogleSignInActivity extends AppActivity {

    private static final int GOOGLE_SIGN_IN_REQUEST = 1010;

    public GoogleSignInClient googleSignInClient;

    private GoogleApiClient mGoogleApiClient;

    protected abstract GoogleSignInOptions getGoogleSignInOptions();

    protected abstract void onGoogleSignedInSuccess(final GoogleSignInAccount signInAccount);

    protected abstract void onGoogleSignedInFailed(final ApiException exception);

    protected abstract void onGoogleSignedOutSuccess(final GoogleSignInAccount signInAccount);


    protected void startGoogleSignIn() {
        googleSignInClient = GoogleSignIn.getClient(this, getGoogleSignInOptions());
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST);
    }

    protected void startGoogleSignOut() {
        googleSignInClient.signOut();
            showMessage("Google account - log out");

    }

    protected void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
    protected void revokeAccess() {
        googleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        startGoogleSignIn();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            onGoogleSignedInSuccess(account);
        } catch (ApiException e) {
            onGoogleSignedInFailed(e);
        }
    }
}
