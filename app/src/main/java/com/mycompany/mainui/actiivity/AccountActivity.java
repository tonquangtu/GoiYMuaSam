package com.mycompany.mainui.actiivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.mycompany.mainui.R;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initToolBar();
        initButton();
        initProfile();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
        }
        return true;
    }
    public void initToolBar() {
        ActionBar actionBar;
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    public void initButton() {

        Button btnLogin = (Button)this.findViewById(R.id.btn_login);
        Button btnSignin = (Button)this.findViewById(R.id.btn_sign_in);
        Button btnEditInfo = (Button)this.findViewById(R.id.btn_edit_info_account);
        Button btnManageShop = (Button)this.findViewById(R.id.btn_manager_shop);
        Button btnManagePersonalStore = (Button)this.findViewById(R.id.btn_manager_personal_store);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    public void initProfile() {

        ImageView backgroundProfile = (ImageView)findViewById(R.id.image_background);
        CircleImageView avatar = (CircleImageView)findViewById(R.id.profile_image);
        TextView name = (TextView)findViewById(R.id.txt_profile_name);

        Profile profile = Profile.getCurrentProfile();
        if(profile != null) {
            int width = 62;
            int height = 62;
            String url = profile.getProfilePictureUri(width, height).toString();
            LoadImageFromUrl.loadAvatar(url, avatar, AccountActivity.this);
            name.setText(profile.getName());

        }
    }

    public void handleLogin() {

        Intent intent = new Intent(this, LoginNormalActivity.class);
        startActivity(intent);
    }

}
