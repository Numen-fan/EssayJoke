package com.jiajia.essayjoke;

import android.view.View;

import com.jiajia.baselibrary.ioc.OnClick;
import com.jiajia.baselibrary.ioc.ViewById;
import com.jiajia.essayjoke.fragment.FindFragment;
import com.jiajia.essayjoke.fragment.HomeFragment;
import com.jiajia.essayjoke.fragment.PublicCountFragment;
import com.jiajia.essayjoke.fragment.PractiseFragment;
import com.jiajia.framelibrary.BaseSkinActivity;

/**
 *
 */
public class HomeActivity extends BaseSkinActivity {

    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private PractiseFragment mPractiseFragment;
    private PublicCountFragment mPublicCountFragment;

    private FragmentManagerHelper mFragmentHelper;

    @ViewById(R.id.home_rb)
    private View btnHome;

    @ViewById(R.id.find_rb)
    private View btnFind;

    @ViewById(R.id.public_count_rb)
    private View btnPublic;

    @ViewById(R.id.practise_rb)
    private View btnPractise;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initTitle() {

//        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this)
//                .setTitle(" ")
//                .build();

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

        mFragmentHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.main_tab_fl);
        mHomeFragment = new HomeFragment();
        mFragmentHelper.add(mHomeFragment);


    }

    @OnClick(R.id.home_rb)
    private void homeRbClick(View view) {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        mFragmentHelper.switchFragment(mHomeFragment);
    }

    @OnClick(R.id.find_rb)
    private void findRbClick(View view) {
        if (mFindFragment == null) {
            mFindFragment = new FindFragment();
        }
        mFragmentHelper.switchFragment(mFindFragment);
    }

    @OnClick(R.id.public_count_rb)
    private void newRbClick(View view) {
        if (mPublicCountFragment == null) {
            mPublicCountFragment = new PublicCountFragment();
        }
        mFragmentHelper.switchFragment(mPublicCountFragment);
    }

    @OnClick(R.id.practise_rb)
    private void messageRbClick(View view) {
        if (mPractiseFragment == null) {
            mPractiseFragment = new PractiseFragment();
        }
        mFragmentHelper.switchFragment(mPractiseFragment);
    }
}