package com.example.rightodelivery.AppUI.Login;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.rightodelivery.AppUI.AppVersionCheck;
import com.example.rightodelivery.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginOptions extends Fragment {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;



    Button googleLoginButton;
    Button otpLoginButton;
    Button emailLoginButton;
    Button guestLoginButton;
    ProgressBar loadingImage;
    TextView logText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Righto", "LoginOptions - Fragment created.");
        View view = inflater.inflate(R.layout.fragment_login_options, container, false);

        googleLoginButton = view.findViewById(R.id.button_login_with_google);
        otpLoginButton = view.findViewById(R.id.button_login_with_otp);
        emailLoginButton = view.findViewById(R.id.button_login_with_email);
        guestLoginButton = view.findViewById(R.id.button_login_as_guest);
        loadingImage = view.findViewById(R.id.login_progress_loading);
        logText = view.findViewById(R.id.login_progress_log);

        loadingImage.setVisibility(View.GONE);



        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("Righto", "Google login button clicked.");
                Toast.makeText(getActivity(),"Not available. Use guest login.",Toast.LENGTH_SHORT).show();
                //initGoogleLogin();


                /*
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add( R.id.fragment_login_options,new LoginGoogle());
                fragmentTransaction.commitNow();
                 */
            }
        });

        guestLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("Righto", "guest login button clicked.");
                Toast.makeText(getActivity(),"Not available. Use google login.",Toast.LENGTH_SHORT).show();
                loginAsGuest();
            }
        });

        otpLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("Righto", "guest login button clicked.");
                Toast.makeText(getActivity(),"Not available. Use google login.",Toast.LENGTH_SHORT).show();
                //loginAsGuest();
            }
        });

        emailLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("Righto", "guest login button clicked.");
                Toast.makeText(getActivity(),"Not available. Use google login.",Toast.LENGTH_SHORT).show();
                //loginAsGuest();
            }
        });

        return view;
    }

    void initGoogleLogin(){

        loadingImage.setVisibility(View.VISIBLE);
        logText.setText("Connecting with Google account ...");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            logText.setText("");
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new AppVersionCheck())
                                    .commit();

                        } else {
                            // If sign in fails, display a message to the user.
                            logText.setText("signIn :failed"+ task.getException());
                        }
                        loadingImage.setVisibility(View.GONE);
                    }
                });
    }





    void loginAsGuest(){

        loadingImage.setVisibility(View.VISIBLE);
        logText.setText("Connecting as guest user ...");

        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("righto", "signInAnonymously:success");

                            logText.setText("");
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new AppVersionCheck())
                                    .commit();
                        } else {
                            // If sign in fails, display a message to the user.
                            logText.setText("signIn :failed"+ task.getException());
                            Toast.makeText(getContext(), "Login Failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        loadingImage.setVisibility(View.GONE);
                    }
                });
    }
}
