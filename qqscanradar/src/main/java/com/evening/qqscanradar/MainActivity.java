package com.evening.qqscanradar;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.evening.commonutils.DisplayMetricsUtil;
import com.evening.qqscanradar.gallery.GalleryAdapter;
import com.evening.qqscanradar.gallery.ZoomOutPageTransformer;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final Integer[] IMG_RES = {R.mipmap.len, R.mipmap.leo, R.mipmap.lep,
            R.mipmap.leq, R.mipmap.ler, R.mipmap.les, R.mipmap.mln, R.mipmap.mmz, R.mipmap.mna,
            R.mipmap.mnj, R.mipmap.leo, R.mipmap.leq, R.mipmap.les, R.mipmap.lep};

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
        GalleryAdapter adapter = new GalleryAdapter(this);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(IMG_RES.length);
        adapter.setData(Arrays.asList(IMG_RES));
        ((View) mViewPager.getParent()).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setPageMargin(DisplayMetricsUtil.dp2px(this, 2));
    }
}
