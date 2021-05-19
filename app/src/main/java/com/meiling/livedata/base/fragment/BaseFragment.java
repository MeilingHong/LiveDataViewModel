package com.meiling.livedata.base.fragment;

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
}
