package com.example.ccreview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ccreview.databinding.ActivityLoginBinding;
import com.example.ccreview.utils.Constants;
import com.example.ccreview.utils.DialogUtil;
import com.example.ccreview.utils.SnackBarUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
            }
        });
        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUpActivity(v);
            }
        });
    }

    @Override
    protected void onStart() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startHomeActivity(null);
        }
        super.onStart();
    }

    private void setListeners() {
        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                disableErrorIfNotEmpty(binding.textInputLayoutEmail, s);
                isEmailValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                disableErrorIfNotEmpty(binding.textInputLayoutPassword, s);
                isPasswordLengthValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    boolean isEmailValid() {
        String email = binding.etEmail.getText().toString();
        boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!isValid) {
            binding.textInputLayoutEmail.setError("Email is invalid");
            return false;
        }
        binding.textInputLayoutEmail.setError("");
        return true;

    }

    private boolean isPasswordLengthValid() {
        String password = binding.etPassword.getText().toString();
        if (password.length() < 8) {
            binding.textInputLayoutPassword.setError("Password should be at least 8 characters");
            return false;
        }
        binding.textInputLayoutPassword.setError("");
        return true;
    }


    private void disableErrorIfNotEmpty(TextInputLayout textInputLayout, CharSequence s) {
        if (!s.toString().isEmpty()) {
            textInputLayout.setError("");
        }
    }

    private boolean hasEmptyFields() {
        boolean isError = false;

        if (binding.etEmail.getText().toString().isEmpty()) {
            binding.textInputLayoutEmail.setError("This field is required");
            isError = true;
        }
        if (binding.etPassword.getText().toString().isEmpty()) {

            binding.textInputLayoutPassword.setError("This field is required");
            isError = true;
        }
        return isError;
    }

    public void startSignUpActivity(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void startHomeActivity(View view) {
        startActivity(new Intent(this, BottomNavActivity.class));
    }

    public void finishActivity(View view) {
        finish();
    }

    public void loginUser(View view) {
        if (hasEmptyFields() || !hasValidFields()) {
            Log.d(Constants.TAG, "loginUser: hasEmptyFields()");
            SnackBarUtils.showSnackBar(binding.getRoot(), "Please fix all errors and try again");
            return;
        }
        loginUserNow();
    }

    private void loginUserNow() {
        DialogUtil.showSimpleProgressDialog(this);
        String email = binding.etEmail.getText().toString().trim();
        String pass = binding.etPassword.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DialogUtil.closeProgressDialog();
                        if (task.isSuccessful()) {
                            startHomeActivity(null);
                            finish();
                        } else {
                            if (task.getException() != null)
                                SnackBarUtils.showSnackBar(binding.getRoot(), task.getException().getMessage());
                            else
                                SnackBarUtils.showSnackBar(binding.getRoot(), "Something went wrong");
                        }
                    }
                });
    }

    private boolean hasValidFields() {
        return isPasswordLengthValid() && isEmailValid();
    }
}