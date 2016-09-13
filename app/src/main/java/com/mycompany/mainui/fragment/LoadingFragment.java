package com.mycompany.mainui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mycompany.mainui.R;


/**
 * Created by Dell on 4/21/2016.
 */
public class LoadingFragment extends Fragment {

    public static LoadingFragment newInstance() {
        LoadingFragment loadingFragment = new LoadingFragment();
        return loadingFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar_loading);
        progressBar.setIndeterminate(true);
        return view;
    }
}
