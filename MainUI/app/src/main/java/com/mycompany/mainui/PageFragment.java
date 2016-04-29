package com.mycompany.mainui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Mr.T on 4/6/2016.
 */
// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment {
    TextView textView;
    public static final String ARG_PAGE = "ARG_PAGE";
    Integer[] shopIcons = {R.drawable.favorite_slc, R.drawable.favorite_slc, R.drawable.favorite_slc
                            , R.drawable.favorite_slc, R.drawable.favorite_slc};

    private int mPage;
//    ArrayList<DataShopView> listData;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        // ...
        textView = (TextView)view.findViewById(R.id.text);
        textView.setText("Page" + mPage);
        // Lookup the recycler icon_view in fragment layout
        //RecyclerView listShopNearest = (RecyclerView)icon_view.findViewById(R.id.list);
/*
        // init list data
        for(int i = 0; i < 15; i++){
            listData.add(new DataShopView("shop" + i, "Ha Noi", shopIcons[i]));
        }
        // Create adapter passing in the sample user data
        RecyclerAdapterShop adapter = new RecyclerAdapterShop(listData);
        // Attach the adapter to the recycler icon_view to populate items
        listShopNearest.setAdapter(adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // Control orientation of the items
        // also supports LinearLayoutManager.HORIZONTAL
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // Optionally customize the position you want to default scroll to
        layoutManager.scrollToPosition(1);
        // Attach layout manager to the RecyclerView

        listShopNearest.setLayoutManager(layoutManager);
        // That's all! */
        return view;
    }
}
