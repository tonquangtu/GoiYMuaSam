package com.mycompany.mainui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mycompany.mainui.R;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.LoadFavorite;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.ArrayList;

/**
 * Created by Dell on 18-May-16.
 */
public class FragmentFavorite extends Fragment {

    AppCompatActivity activity;
    LoadFavorite loadFavorite;
    ArrayList listData;
    boolean loaded = false;

    public static FragmentFavorite newInstance() {

        FragmentFavorite fragment = new FragmentFavorite();
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (AppCompatActivity)getActivity();
        loadFavorite = new LoadFavorite();
        listData = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite_layout, container, false);
        return view;
    }

    public class LoadFavoriteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            LoadingFragment fragment = LoadingFragment.newInstance();
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.place_holder_favorite, fragment);
            ft.commit();
        }

        @Override
        protected Void doInBackground(Void... params) {

            loadFavorite.loadFavorite(activity);
            while(!loadFavorite.isLoadComplete()) {
                try {
                    Thread.sleep(ConfigData.TIME_SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            loadFavorite.addFavorite();
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            FragmentFavoriteHaveResult fragment = FragmentFavoriteHaveResult.newInstance(loadFavorite);
            ft.replace(R.id.place_holder_favorite, fragment);
            ft.commit();
        }
    }

    /**
     * load favorite.
     */
    public void loadFavorite() {


        if(!loaded) {
            Toast.makeText(activity, "false", Toast.LENGTH_SHORT).show();
            if(InfoAccount.isLogin(activity)) {
                if(StatusInternet.isInternet(activity)) {
                    loaded = true;
                    new LoadFavoriteTask().execute();

                }else {
                    DisplayNotification.toast(activity, "Không có kết nối mạng !");
                }
            }else {
                DisplayNotification.toast(activity, "Bạn chưa đăng nhập !");
            }
        }else {
            Toast.makeText(activity, "true", Toast.LENGTH_SHORT).show();
        }

//
//
//
//        if (InfoAccount.isLogin(activity)) {
//            if(StatusInternet.isInternet(activity)) {
//                if(!loaded) {
//                    loaded = true;
//                    new LoadFavoriteTask().execute();
//
//                }
//            }else {
//                DisplayNotification.toast(activity, "Không có kết nối mạng !");
//            }
//        }else {
//            DisplayNotification.toast(activity, "Bạn chưa đăng nhập !");
//        }
    }


}
