package com.nappstic.alimentospaleo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by O.MIRO.S on 23/03/2018.
 */

public class ActivityRegister extends AppCompatActivity {


    private EditText EditTxtName;
    private EditText EditTxtEmail;
    private EditText EditTxtPassword1;
    private EditText EditTxtPassword2;
    private TextView txtVwErrRegister;

    private String name;
    private String email;
    private String password;
    private String password2;
    private ImageView imgView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Button btnRegister;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        btnRegister = findViewById(R.id.btnRegisterNewUser);

        EditTxtEmail =  findViewById(R.id.editTxtRegisterEmail);
        EditTxtName =  findViewById(R.id.editTxtRegisterName);
        EditTxtPassword1 =  findViewById(R.id.editTxtRegisterPassword);
        EditTxtPassword2=  findViewById(R.id.editTxtRegisterPassword2);

        txtVwErrRegister = findViewById(R.id.txtViewErrRegister);

        imgView = findViewById(R.id.bgRegister);

        imgView.setImageDrawable(resizeImage(R.drawable.background_login));


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = EditTxtName.getText().toString();
                email = EditTxtEmail.getText().toString();
                password = EditTxtPassword1.getText().toString();
                password2 = EditTxtPassword2.getText().toString();

                Log.d("ACTIVITYREGISTER", "onClick: click");

                if(name.equals("")){
                    EditTxtName.setError(getString(R.string.emptyField));
                }else{
                    if (email.equals("")){
                        EditTxtEmail.setError(getString(R.string.emptyField));
                    }else{
                        if(password.equals("")){
                            EditTxtPassword1.setError(getString(R.string.emptyField));
                        }else{
                            if(password2.equals("")){
                                EditTxtPassword2.setError(getString(R.string.emptyField));
                            }else{

                                if (password.equals(password2)){
                                    createUserWithEmailAndPassword(email,password);
                                }else {
                                    EditTxtPassword2.setError(getString(R.string.errRegisterPasswordNotEquals));
                                    EditTxtPassword1.setError(getString(R.string.errRegisterPasswordNotEquals));
                                    txtVwErrRegister.setText(R.string.errRegisterPasswordNotEquals);

                                    txtVwErrRegister.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    }
                }


            }
        });

    }
    private void createUserWithEmailAndPassword(String email, String password) {
        FirebaseAuth mFirebaseAuth;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Log.d("ActivityRegister", "Nombre del usuario asignado correctamente");
                                    }
                                }
                            });

                            Toast.makeText(ActivityRegister.this, R.string.registerComplete, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ActivityRegister.this,MainActivity.class);
                            startActivity(intent);

                        } else {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                EditTxtPassword1.setError(getString(R.string.errRegisterPAsswordShort));
                                EditTxtPassword2.setError(getString(R.string.errRegisterPAsswordShort));
                                EditTxtPassword1.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                EditTxtEmail.setError(getString(R.string.errSendNewPassword));
                                //txtVwErrRegister.setText(R.string.errSendNewPassword);
                                //txtVwErrRegister.setVisibility(View.VISIBLE);
                                EditTxtEmail.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                EditTxtEmail.setError(getString(R.string.errExistingMail));
                                EditTxtEmail.requestFocus();

                            }catch(Exception e) {
                                Log.e("ACTIVITYREGISTER", e.getMessage());
                            }


                        }
                        //mPasswordTextView.setText(message);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //mPasswordTextView.setText(e.getMessage());
                e.printStackTrace();
            }
        });
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
