package com.mycompany.mainui.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mycompany.mainui.R;
import com.mycompany.mainui.actiivity.SplashActivity;
import com.mycompany.mainui.model.NavigationCatalogData;

/**
 * Created by Dell on 4/21/2016.
 */
public class ChooseCityFragment extends Fragment {

    SplashActivity mContext;
    Spinner spinner;
    Button btnNext;

    public static ChooseCityFragment newInstance() {

        ChooseCityFragment chooseCityFragment = new ChooseCityFragment();
        return chooseCityFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (SplashActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_city, container, false);

        spinner = (Spinner)view.findViewById(R.id.city_spinner);
        btnNext = (Button)view.findViewById(R.id.btn_next);

        ArrayAdapter<String > adapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, NavigationCatalogData.CITY_SET);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.msgFromFragToActivity((String)spinner.getSelectedItem());
            }
        });

        return view;
    }
}
