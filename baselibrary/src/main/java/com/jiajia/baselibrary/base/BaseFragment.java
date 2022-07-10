package com.jiajia.baselibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jiajia.baselibrary.ioc.ViewUtils;


/**
 * Created by Numen_fan on 2022/3/16
 * Desc:
 */
public abstract class BaseFragment extends Fragment {

    protected View rootView;
    protected Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        rootView = View.inflate(context, getLayoutId(),null);

        // 加入注解
        ViewUtils.inject(rootView,this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        initData();

    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getLayoutId();

}
