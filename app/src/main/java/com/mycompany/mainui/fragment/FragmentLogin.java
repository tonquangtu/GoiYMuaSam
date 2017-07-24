package com.mycompany.mainui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mycompany.mainui.R;
import com.mycompany.mainui.actiivity.LoginNormalActivity;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ResponseFromServerData;
import com.mycompany.mainui.network.Account;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.Arrays;

/**
 * Created by Dell on 18-May-16.
 */
public class FragmentLogin extends Fragment {

    EditText editUsername, editPass;
    Account account;
    LoginNormalActivity activity;
    ProgressDialog progressDialog;
    AppCompatButton btnLogin;
    LoginButton btnLoginFacebook;
    Button btnSignin;
    String mes;
    CallbackManager callbackManager;

    public static FragmentLogin newInstance() {
        FragmentLogin fragmentLogin = new FragmentLogin();
        return fragmentLogin;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account();
        activity = (LoginNormalActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_layout, container, false);
        editUsername = (EditText) view.findViewById(R.id.input_username);
        editPass = (EditText) view.findViewById(R.id.input_password);

        btnLogin = (AppCompatButton) view.findViewById(R.id.btn_login);
        btnLoginFacebook = (LoginButton) view.findViewById(R.id.btn_login_face);
        btnSignin = (Button) view.findViewById(R.id.btn_sign_in);
        callbackManager = CallbackManager.Factory.create();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!InfoAccount.isLogin(activity)) {
                    handleLogin();
                } else {
                    DisplayNotification.toast(activity, "Bạn đã đăng nhập rồi !");
                }
            }
        });
        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    InfoAccount.logout(activity);
                }
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignin();
            }
        });

        handleLoginFacebook();
        return view;
    }

    public void handleLogin() {

        final String username = editUsername.getText().toString();
        final String pass = editPass.getText().toString();
        if (checkValidUsername(username)) {
            if (pass.length() > 0) {
                // valid
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        // init loading
                        progressDialog = new ProgressDialog(activity);
                        progressDialog.setMessage("Login...");
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();

                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        account.loginNormal(activity, username, pass);
                        while (!account.isLoginComplete()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        progressDialog.cancel();
                        ResponseFromServerData resultLogin = account.getLoginResult();
                        if (resultLogin == null) {
                            DisplayNotification.toast(activity,
                                    "Có lỗi xảy ra, đăng nhập thất bại !");
                        } else {
                            if (resultLogin.getSuccess() == RequestId.FAILURE) {
                                DisplayNotification.toast(activity,
                                        resultLogin.getMessage());
                            } else {
                                DisplayNotification.toast(activity,
                                        "Đăng nhập thành công");
                                InfoAccount.login(activity, username);
                                activity.finish();
                            }
                        }
                    }
                }.execute();
            } else {
                DisplayNotification.toast(activity, "Mật khẩu không được rỗng !");
            }
        } else {
            DisplayNotification.toast(activity, mes);
        }
    }

    public void handleSignin() {

    }


    public boolean checkValidUsername(String username) {

        // if username contain white space
        String mes = "";
        boolean isValid = true;
        if (username.length() == 0) {
            mes = "Username không được rỗng !";
            isValid = false;
        } else {

            int n = username.length();
            for (int i = 0; i < n; i++) {
                if (username.charAt(i) == ' ') {
                    mes = "Username không được chứa dấu cách !";
                    isValid = false;
                    break;
                }
            }
        }
        this.mes = mes;

        return isValid;
    }

    public void handleLoginFacebook() {

        btnLoginFacebook.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        btnLoginFacebook.setFragment(this);
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(final LoginResult loginResult) {

                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {

                            mProfileTracker.stopTracking();
                            Profile newProfile = Profile.getCurrentProfile();
                            if (newProfile != null) {
                                handleLoginSuccess(newProfile);
                            }
                        }
                    };
                    mProfileTracker.startTracking();
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    handleLoginSuccess(profile);
                }
            }

            @Override
            public void onCancel() {
                Log.v("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                DisplayNotification.toast(activity, "Có lỗi xảy ra ! Không thể đăng nhập");
            }
        });

    }

    public void handleLoginSuccess(Profile profile) {

        String username = profile.getId();
        String pass = username;
        account.loginFacebook(username, pass);
        InfoAccount.login(activity, username);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
