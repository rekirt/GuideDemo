package com.example.lc.guidedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ud.client.app_api.utils.Logger;
import com.ud.client.app_api.utils.ScreenUtil;
import com.ud.client.app_api.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Author: lc
 * Email: rekirt@163.com
 * Date: 16-7-10
 */
public class GuideActivity1 extends BaseActivity implements ViewPager.OnPageChangeListener {

    @InjectView(R.id.vp_pager)
    ViewPager vp_pager;
    @InjectView(R.id.bt_use)
    Button bt_use;
    @InjectView(R.id.ll_indicator)
    LinearLayout ll_indicator;
    @InjectView(R.id.pointed)
    View pointed;

    List<ImageView> mGuides;
    int basicWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide1);
        initGuideData();
    }

    private void initGuideData() {
        int[] images = new int[]{
                R.mipmap.guide1,
                R.mipmap.guide2,
                R.mipmap.guide3,
                R.mipmap.guide4
        };
        mGuides = new ArrayList<ImageView>();
        for(int i=0;i<images.length;i++){
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setImageResource(images[i]);
            mGuides.add(image);

            View v = new View(this);
            v.setBackgroundResource(R.drawable.shape_dot_red);
            int width = UIUtil.getDimens(R.dimen.guide_point_width);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            if(i!=0)params.leftMargin=width;
            v.setLayoutParams(params);
            ll_indicator.addView(v);
        }
        GuideAdapter adapter = new GuideAdapter();
        vp_pager.setAdapter(adapter);
        vp_pager.addOnPageChangeListener(this);

        pointed.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pointed.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                basicWidth = ll_indicator.getChildAt(1).getLeft() - ll_indicator.getChildAt(0).getLeft();
            }
        });
        bt_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixel) {
        float offset = basicWidth*(position+positionOffset);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)pointed.getLayoutParams();
        params.leftMargin =(int)offset;
        pointed.setLayoutParams(params);

    }

    @Override
    public void onPageSelected(int position) {
        if(position==mGuides.size()-1)bt_use.setVisibility(View.VISIBLE);
        else bt_use.setVisibility(View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    class GuideAdapter extends PagerAdapter {

        public GuideAdapter() {
        }

        @Override
        public int getCount() {
            return mGuides.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view==o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mGuides.get(position));
            return mGuides.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mGuides.get(position));
        }
    }
}
