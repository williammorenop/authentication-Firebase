package com.example.willi.androidauthentication;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity
{


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    final String TAG = "LOLOLA";

    Button login;
    Button create;
    Button boton;
    TextView mUser;
    TextView mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boton = findViewById(R.id.button);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),MapActivity.class));
            }
        });

        login = findViewById(R.id.Login);
        mUser = findViewById(R.id.mUser);
        mPassword = findViewById(R.id.mPassword);
        create = findViewById(R.id.create);

        create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(v.getContext(),RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signInUser();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
// User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(LoginActivity.this, LoginSuccessActivity.class));
                }
                else
                {
// User is signed out

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    protected void signInUser()
    {
        if(validateForm())
        {
            String email = mUser.getText().toString();
            String password = mPassword.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            Log.d(TAG,
                                    "signInWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful())
                            {
                                Log.w(TAG,
                                        "signInWithEmail:failed"
                                        , task.getException());
                                Toast.makeText(LoginActivity.this, "Correo o contraseña invalida.",
                                        Toast.LENGTH_SHORT).show();
                                mUser.setText("");
                                mPassword.setText("");
                            }
                        }
                    });
        }
    }

    private boolean isEmailValid(String email)
    {
        boolean isValid = true;
        if (!email.contains("@") || !email.contains(".") || email.length() < 5)
            isValid = false;
        return isValid;
    }

    private boolean validateForm()
    {
        boolean valid = true;
        String email = mUser.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            mUser.setError("Requerido.");
            valid = false;
        }
        else if(!isEmailValid(email))
        {
            mUser.setError("Correo invalido.");
            valid = false;
        }
        else
        {
            mUser.setError(null);
        }
        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password))
        {
            mPassword.setError("Requerido.");
            valid = false;
        }
        else
        {
            mPassword.setError(null);
        }
        return valid;
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}


