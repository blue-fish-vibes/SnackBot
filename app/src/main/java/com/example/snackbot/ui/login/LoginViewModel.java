package com.example.snackbot.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.snackbot.data.LoginRepository;
import com.example.snackbot.data.Result;
import com.example.snackbot.data.model.LoggedInUser;
import com.example.snackbot.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    /**
     * Checks user authentication and returns result
     * @param username username
     * @param password password
     * @return returns true if login was a success, otherwise returns false
     */
    public boolean login(String username, String password) {
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
            return true;
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
            return false;
        }
    }

    /**
     * Checks login form state for changes and performs input validation
     * @param username username
     * @param password password
     */
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    /**
     * Checks if username format is valid
     * @param username username
     * @return returns true if username is valid, otherwise returns false
     */
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    /**
     * Placeholder user password validation
     * @param password password
     * @return returns true if password is valid, otherwise returns false
     */
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public String getUsername() {
        return loginRepository.getLoggedInUser().getDisplayName();
    }
}