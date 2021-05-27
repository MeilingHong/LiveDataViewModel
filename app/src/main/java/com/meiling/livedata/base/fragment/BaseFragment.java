package com.meiling.livedata.base.fragment;

import com.umeng.analytics.MobclickAgent;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * @Author marisareimu
 * @time 2021-05-19 11:13
 *
 * 【androidx.fragment.app.Fragment 已经实现了这个 LifecycleOwner 接口】
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment/* implements LifecycleOwner */ {
    protected T layoutBinding = null;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }
}
