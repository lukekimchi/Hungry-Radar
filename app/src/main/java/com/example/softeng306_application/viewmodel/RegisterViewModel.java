package com.example.softeng306_application.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.softeng306_application.usecase.RegisterUserUseCase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class RegisterViewModel extends AndroidViewModel {
    private RegisterUserUseCase registerUserUseCase;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        registerUserUseCase = registerUserUseCase.getInstance();
    }

    public Task<AuthResult> register(String email, String password, String username){
        return registerUserUseCase.register(email, password, username);
    }

    public void addUserToDb(String email, String password, String username) {
        registerUserUseCase.addUserToDb(email, password, username);
    }
}
