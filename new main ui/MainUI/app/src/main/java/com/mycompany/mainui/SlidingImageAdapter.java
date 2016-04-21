package com.mycompany.mainui;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mycompany.mainui.model.AdProductCatalog;
import com.mycompany.mainui.model.StringUtil;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;

import java.util.ArrayList;

/**
 * Created by Mr.T on 4/5/2016.
 */
public class SlidingImageAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    public Context context;
    private ArrayList<AdProductCatalog> adProductCatalogs;
    private View.OnClickListener adListener;


    public SlidingImageAdapter(Context context,
                               ArrayList<AdProductCatalog> adProductCatalogs,
                               View.OnClickListener adListener) {
        this.context = context;
        this.adListener=adListener;
        this.adProductCatalogs = adProductCatalogs;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return adProductCatalogs.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_images_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        String url = StringUtil.convertURL(adProductCatalogs.get(position).getImageURL());
        LoadImageFromUrl.loadImageFromURL(url, imageView, context);
        imageView.setOnClickListener(adListener);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
