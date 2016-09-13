package com.mycompany.mainui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mycompany.mainui.R;
import com.mycompany.mainui.model.StringUtil;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;

import java.util.List;

/**
 * Created by Dell on 07-May-16.
 */
public class SlidingImageProductAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    public Context context;
    private List<String> imageURLs;
    private View.OnClickListener listener;
    Bitmap bitmap;


    public SlidingImageProductAdapter(Context context,
                               List<String> imageURLs,
                               View.OnClickListener listener) {
        this.context = context;
        this.imageURLs = imageURLs;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageURLs.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_images_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        String url = StringUtil.convertURL(imageURLs.get(position));
        LoadImageFromUrl.loadImageFromURL(url, imageView, context);

        if(listener != null) {
            imageView.setOnClickListener(listener);
        }
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

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
