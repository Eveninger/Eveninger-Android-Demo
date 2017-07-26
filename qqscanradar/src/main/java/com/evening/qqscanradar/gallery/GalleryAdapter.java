package com.evening.qqscanradar.gallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.evening.qqscanradar.R;

import java.util.List;

/**
 * Created by Nighter on 17/7/21.
 */

public class GalleryAdapter extends PagerAdapter {
    private List<Integer> mImgResList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public GalleryAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImgResList == null ? 0 : mImgResList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.item_gallery, container, false);
        ImageView ivPortrait = (ImageView) view.findViewById(R.id.iv);
        ivPortrait.setImageResource(mImgResList.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setData(List<Integer> imgResList) {
        mImgResList = imgResList;
        notifyDataSetChanged();
    }
}
