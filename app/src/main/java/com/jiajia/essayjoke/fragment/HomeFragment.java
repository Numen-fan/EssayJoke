package com.jiajia.essayjoke.fragment;


import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.jiajia.baselibrary.base.BaseFragment;
import com.jiajia.baselibrary.ioc.ViewById;
import com.jiajia.essayjoke.R;
import com.jiajia.framelibrary.indicator.ColorTrackTextView;
import com.jiajia.framelibrary.indicator.IndicatorAdapter;
import com.jiajia.framelibrary.indicator.TrackIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/3.
 */
public class HomeFragment extends BaseFragment {

    private static final String TAG = "ViewPagerActivity";

    private final String[] items = {"直播", "推荐", "视频", "图片", "段子", "精华","同城","游戏"};

    @ViewById(R.id.indicator_view)
    private TrackIndicatorView mIndicatorContainer;

    @ViewById(R.id.view_pager)
    private ViewPager2 mViewPager;

    private List<ColorTrackTextView> mIndicators;


    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mIndicators = new ArrayList<>();
        initIndicator();
        initViewPager();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }


    /**
     * 初始化ViewPager
     */
    private void initViewPager() {

        mViewPager.setAdapter(new FragmentStateAdapter(getActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getItemCount() {
                return items.length;
            }
        });

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.e(TAG, "position --> " + position + " positionOffset --> " + positionOffset);
                if (positionOffset > 0) {
                    // 获取左边
                    ColorTrackTextView left = mIndicators.get(position);
                    // 设置朝向
                    left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                    // 设置进度  positionOffset 是从 0 一直变化到 1 不信可以看打印
                    left.setCurrentProgress(1 - positionOffset);

                    // 获取右边
                    ColorTrackTextView right = mIndicators.get(position + 1);
                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                    right.setCurrentProgress(positionOffset);
                }

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    /**
     * 初始化可变色的指示器
     */
    private void initIndicator() {

        mIndicatorContainer.setAdapter(new IndicatorAdapter<ColorTrackTextView>() {
            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public ColorTrackTextView getView(int position, ViewGroup parent) {
                ColorTrackTextView colorTrackTextView = new ColorTrackTextView(context);
                // 设置颜色
                colorTrackTextView.setTextSize(20);
                colorTrackTextView.setChangeColor(Color.RED);
                colorTrackTextView.setText(items[position]);
                // 加入集合
                mIndicators.add(colorTrackTextView);
                return colorTrackTextView;
            }

            @Override
            public void highLightIndicator(ColorTrackTextView view) {
                // 当前选中的View
                view.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                view.setCurrentProgress(1);
            }

            @Override
            public void restoreIndicator(ColorTrackTextView view) {
                // 上一个View
                view.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                view.setCurrentProgress(0);
            }
        }, mViewPager,false);
    }
}
