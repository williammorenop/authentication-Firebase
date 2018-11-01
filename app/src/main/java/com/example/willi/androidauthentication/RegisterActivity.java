package com.example.willi.androidauthentication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    final String TAG = "LOLOLA";
    TextView mUser;
    TextView mUserName;
    TextView mUserLastName;
    TextView mPassword;
    TextView mConfirmPassword;
    Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUser = findViewById(R.id.correo);
        mUserName = findViewById(R.id.nombre);
        mUserLastName = findViewById(R.id.apellido);
        mPassword = findViewById(R.id.contrasena);
        mConfirmPassword = findViewById(R.id.recontrasena);
        mRegister = findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();
        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerUser();
            }
        });

    }

    public void registerUser()
    {

        String email = mUser.getText().toString();
        String password = mPassword.getText().toString();

        if(validateForm())
        {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null)   //Update user Info
                                        {
                                            UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                                            upcrb.setDisplayName(mUserName.getText().toString() + " " + mUserLastName.getText().toString());
                                            //upcrb.setPhotoUri(Uri.parse("path/to/pic"));//fake uri, real one coming soon
                                            user.updateProfile(upcrb.build());
                                            startActivity(new Intent(RegisterActivity.this, MapActivity.class)); //o en el listener
                                        }
                                    }
                                    if (!task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, R.string.auth_failed + task.getException().toString(),
                                                Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, task.getException().getMessage());
                                    }
                                }
                            }
                    );
        }
    }

    private boolean isEmailValid(String email)
    {
        boolean isValid = true;
        if ((email.length() != 0) && (!email.contains("@") || !email.contains(".") || email.length() < 5))
            isValid = false;
        return isValid;
    }

    private boolean validateForm()
    {
        boolean valid = true;
        String email = mUser.getText().toString();
        boolean result = isEmailValid(email);
        if (TextUtils.isEmpty(email) || result==false)
        {
            mUser.setError(null);
            if (result==false)
            {
                mUser.setError("Invalido.");
            }
            else
            {
                mUser.setError("Requerido.");
            }
            valid = false;
        }
        else
        {
            mUser.setError(null);
        }
        String password = mPassword.getText().toString();
        String rePassword = mConfirmPassword.getText().toString();
        if (!password.equals(rePassword))
        {
            mPassword.setError("No son iguales.");
            mConfirmPassword.setError("No son iguales.");
            valid = false;
        }
        else
        {
            mPassword.setError(null);
            mConfirmPassword.setError(null);
        }
        if (TextUtils.isEmpty(password))
        {
            mPassword.setError("Requerido.");

            valid = false;
        }
        else
        {
            mPassword.setError(null);
        }
        String name = mUserName.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            mUserName.setError("Requerido.");
            valid = false;
        }
        else
        {
            mUserName.setError(null);
        }
        String lastname = mUserLastName.getText().toString();

        if (TextUtils.isEmpty(lastname))
        {
            mUserLastName.setError("Requerido.");
            valid = false;
        }
        else
        {
            mUserLastName.setError(null);
        }
        return valid;
    }


}
