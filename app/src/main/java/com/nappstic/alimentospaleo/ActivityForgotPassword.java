package com.nappstic.alimentospaleo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ActivityForgotPassword extends AppCompatActivity {

    private String TAG ="F_FORGOT_PASS";
    private String email ="";

    private FirebaseAuth mFirebaseAuth;

    private TextView txtViewErrSendNewPassword;
    private EditText editTxtEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Button btnSendNewPassword;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }
        mFirebaseAuth = FirebaseAuth.getInstance();

        btnSendNewPassword = findViewById(R.id.btnSendNewPassword);
        txtViewErrSendNewPassword = findViewById(R.id.txtViewInfoMsgSendNewPassword);
        editTxtEmail = findViewById(R.id.editTxtForgotPassword);

        btnSendNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTxtEmail.getText().toString().trim();

                if (email.matches("")){
                    editTxtEmail.setError(getString(R.string.errEmptyEmailPassword));
                    txtViewErrSendNewPassword.setVisibility(View.VISIBLE);
                }else{
                    mFirebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), R.string.confirmEmailSent, Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(ActivityForgotPassword.this,ActivityLogin.class);
                                        startActivity(i);
                                    }else{
                                        try {
                                            throw task.getException();
                                        }catch (FirebaseAuthInvalidCredentialsException e){
                                            editTxtEmail.setError(getString(R.string.errSendNewPassword));
                                        } catch(FirebaseAuthInvalidUserException e) {
                                            editTxtEmail.setError(getString(R.string.errUserNoExist));
                                        }  catch(Exception e) {
                                            Log.e(TAG, e.getMessage());
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }
}
