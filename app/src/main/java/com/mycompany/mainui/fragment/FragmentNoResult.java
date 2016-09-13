package com.mycompany.mainui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycompany.mainui.R;

/**
 * Created by Dell on 19-May-16.
 */
public class FragmentNoResult extends Fragment {

    public static FragmentNoResult newInstance() {
        FragmentNoResult fragment = new FragmentNoResult();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.no_result_search_layout, container, false);
        TextView txt = (TextView)view.findViewById(R.id.txt_title);
        txt.setText("Bạn chưa ghim sản phẩm, hay cửa hàng nào");
        return view;
    }
}
