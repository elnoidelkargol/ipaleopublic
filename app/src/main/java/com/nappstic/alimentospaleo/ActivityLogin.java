package com.nappstic.alimentospaleo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by O.MIRO.S on 22/03/2018.
 */

public class ActivityLogin extends AppCompatActivity {

    private String TAG = "ACTIVITYLOGIN";
    private final int GOOGLE_SIGN_IN_REQUEST_CODE = 8;

    private EditText mPasswordTextView;
    private EditText mUsernameTextView;
    private TextView mErrorTextView;
    private ImageView imgVw;
    private String Password;
    private String Username;

    //FIREBASE VARS
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleApiClient mGoogleApiClient; // for google sign in

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Typeface typeFaceBarlowBold;
        Button btnEnter;
        Button btnRegister;
        Button btnSignInGoogle;
        Button btnForgotPassword;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }
         imgVw = findViewById(R.id.bgCartNotLogged);
        imgVw.setImageDrawable(resizeImage(R.drawable.background_login));
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(ActivityLogin.this,MainActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "2. Current User" + user);
                }
            }
        };
        //initFBGoogleSignIn();

        typeFaceBarlowBold = Typeface.createFromAsset(this.getAssets(), "fonts/BarlowCondensed-Bold.ttf");

        mPasswordTextView = findViewById(R.id.editTxtPasswordLogin);
        mUsernameTextView =findViewById(R.id.editTxtUsernameLogin);
        mErrorTextView =  findViewById(R.id.txtViewLoginErrors);


        btnEnter =  findViewById(R.id.btnEnterNotLogged);
        btnRegister =  findViewById(R.id.btnRegisterEmail);
        //btnSignInGoogle =  findViewById(R.id.btnSignInGoogle);
        btnForgotPassword =  findViewById(R.id.btnForgotPassword);

        btnEnter.setTypeface(typeFaceBarlowBold);
        btnRegister.setTypeface(typeFaceBarlowBold);
       // btnSignInGoogle.setTypeface(typeFaceBarlowBold);
        btnForgotPassword.setTypeface(typeFaceBarlowBold);

        /*btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogleSignIn();
            }
        });*/
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = mPasswordTextView.getText().toString();
                Username = mUsernameTextView.getText().toString();
                if (Password.equals("") || Username.equals("")){
                    mPasswordTextView.setError(getString(R.string.errBadCredentials));
                    mUsernameTextView.setError(getString(R.string.errBadCredentials));
                    Toast.makeText(ActivityLogin.this,getString(R.string.errBadCredentials),Toast.LENGTH_LONG).show();
                }else{

                    signInWithEmailAndPassword(Username,Password);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogin.this,ActivityRegister.class);
                startActivity(intent);
                //createUserWithEmailAndPassword(Username,Password);
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(ActivityLogin.this,ActivityForgotPassword.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
//        mGoogleApiClient.stopAutoManage(this);
//        mGoogleApiClient.disconnect();
    }
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.d(TAG, "onActivityResult: GOOGLE SUCCES OK");
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                mErrorTextView.setText("failed");
                mErrorTextView.setVisibility(View.VISIBLE);
            }
        } //else if (requestCode == FACEBOOK_LOG_IN_REQUEST_CODE) {
          //  mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        //} else if (requestCode == TWITTER_LOG_IN_REQUEST_CODE) {
          //  mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
        //}
    }
*/

    private void signInWithEmailAndPassword(final String email, String password) {

         mFirebaseAuth.signInWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String message;
                        if (task.isSuccessful()) {
                            message = "success signInWithEmailAndPassword ("+email+")";
                            Log.w(TAG, "signInUserWithEmail:failure", task.getException());
                            Log.d("LOGIN", "Succes: " + message);
                        } else {

                            Log.w(TAG, "signInUserWithEmail:failure", task.getException());
                            try {
                                throw task.getException();
                            /*} catch(FirebaseAuthWeakPasswordException e) {
                                mTxtPassword.setError(getString(R.string.error_weak_password));
                                mTxtPassword.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                mTxtEmail.setError(getString(R.string.error_invalid_email));
                                mTxtEmail.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                mTxtEmail.setError(getString(R.string.error_user_exists));
                                mTxtEmail.requestFocus();*/
                            }catch (FirebaseAuthInvalidCredentialsException e) {
                                mErrorTextView.setText(R.string.errBadCredentials);
                                mErrorTextView.setVisibility(View.VISIBLE);
                                Log.e(TAG, "onComplete: El imail esta mal formatado", e);
                            }catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }


                        }
                        // mPasswordTextView.setText(message);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //  mPasswordTextView.setText(e.getMessage());

                e.printStackTrace();
            }
        });
    }


    // [START auth_with_google]
    private void initFBGoogleSignIn() {
        String WEB_CLIENT_ID = "969828594352-rpdrjpdg1f2hav36p5hm54r72qm8rjnu.apps.googleusercontent.com";
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        Context context = getApplicationContext();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        mErrorTextView.setText(connectionResult.getErrorMessage());
                        // mGoogleSignInTextView.setText(connectionResult.getErrorMessage());
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }
    private void signInWithGoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String message;
                        if (task.isSuccessful()) {
                            message = "success firebaseAuthWithGoogle";
                            Log.d(TAG, "l.264 -> onComplete: " + message);
                        } else {
                            message = "fail firebaseAuthWithGoogle";
                            Log.e(TAG, "l.267 -> onComplete: " + message);
                        }
                        mErrorTextView.setText(message);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mErrorTextView.setText(e.getMessage());
                e.printStackTrace();
            }
        });
    }
    // [END auth_with_google]

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    public Drawable resizeImage(int imageResource) {// R.drawable.large_image
        // Get device dimensions
        Display display = getWindowManager().getDefaultDisplay();
        double deviceWidth = display.getWidth();

        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(
                imageResource);
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), imageResource);
        Drawable drawable = new BitmapDrawable(this.getResources(),
                getResizedBitmap(bMap, newImageHeight, (int) deviceWidth));

        return drawable;
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

}
