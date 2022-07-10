package com.jiajia.essayjoke;

import android.view.View;
import android.widget.ImageView;

import com.jiajia.baselibrary.ioc.OnClick;
import com.jiajia.baselibrary.ioc.ViewById;
import com.jiajia.essayjoke.fragment.FindFragment;
import com.jiajia.essayjoke.fragment.HomeFragment;
import com.jiajia.essayjoke.fragment.MessageFragment;
import com.jiajia.essayjoke.fragment.NewFragment;
import com.jiajia.framelibrary.BaseSkinActivity;
import com.jiajia.framelibrary.DefaultNavigationBar;

/**
 *
 */
public class HomeActivity extends BaseSkinActivity {

    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private NewFragment mNewFragment;
    private MessageFragment mMessageFragment;

    private FragmentManagerHelper mFragmentHelper;

    @ViewById(R.id.home_rb)
    private View btnHome;

    @ViewById(R.id.find_rb)
    private View btnFind;

    @ViewById(R.id.new_rb)
    private View btnNew;

    @ViewById(R.id.message_rb)
    private View btnMessage;


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

    @OnClick(R.id.new_rb)
    private void newRbClick(View view) {
        if (mNewFragment == null) {
            mNewFragment = new NewFragment();
        }
        mFragmentHelper.switchFragment(mNewFragment);
    }

    @OnClick(R.id.message_rb)
    private void messageRbClick(View view) {
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
        }
        mFragmentHelper.switchFragment(mMessageFragment);
    }
}