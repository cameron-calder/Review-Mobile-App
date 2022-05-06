package com.example.ccreview.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ccreview.databinding.ActivitySignupBinding;
import com.example.ccreview.models.User;
import com.example.ccreview.utils.DialogUtil;
import com.example.ccreview.utils.SnackBarUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {

        binding.etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                disableErrorIfNotEmpty(binding.textInputLayoutUsername, s);
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

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser(v);
            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void disableErrorIfNotEmpty(TextInputLayout textInputLayout, CharSequence s) {
        if (!s.toString().isEmpty()) {
            textInputLayout.setError("");
        }
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

    private boolean hasValidFields() {
        return isPasswordLengthValid() && isEmailValid();
    }

    private boolean hasEmptyFields() {
        boolean isError = false;
        if (binding.etUsername.getText().toString().isEmpty()) {
            binding.textInputLayoutUsername.setError("This field is required");
            isError = true;
        }
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


    public void signUpUser(View view) {
        if (hasEmptyFields()) {
            SnackBarUtils.showSnackBar(binding.getRoot(), "Please fix all errors and try again");
            return;
        } else if (!hasValidFields()) {
            SnackBarUtils.showSnackBar(binding.getRoot(), "Please fix all errors and try again");
            return;
        }
        registerUser();
    }

    private void registerUser() {
        DialogUtil.showSimpleProgressDialog(this);
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String pass = binding.etPassword.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            if (firebaseUser != null) {
                                firebaseUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                DialogUtil.closeProgressDialog();
                                                FirebaseAuth.getInstance().signOut();
                                                Toast.makeText(SignupActivity.this, "Successfully registered. You can now login with your credentials", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });

                            }


                        } else {
                            DialogUtil.closeProgressDialog();
                            if (task.getException() != null)
                                SnackBarUtils.showSnackBar(binding.getRoot(), task.getException().getMessage());
                            else
                                SnackBarUtils.showSnackBar(binding.getRoot(), "Something went wrong");
                        }
                    }
                });
    }
}