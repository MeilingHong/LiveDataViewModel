package com.meiling.livedata.base.activity;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @Author marisareimu
 * @time 2021-05-19 11:07
 * <p>
 * 【androidx.activity.ComponentActivity  已经实现了这个 LifecycleOwner 接口】
 * 方面后续使用LiveData相关联的方式进行绑定与释放
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity/* implements LifecycleOwner */ {
    protected T layoutBinding = null;

    /**
     * todo 方法用于配置相关的参数，
     * 例如：
     * 1、设置顶部状态栏的样式【颜色】
     * 2、是否全屏【】
     * 3、底部虚拟导航栏颜色【如果有的话】
     * 4、横屏还是竖屏
     * 5、是否屏幕常亮
     * 6、是否屏蔽返回键
     * 6、其他可能需要配置的信息【基于Activity】
     */
    protected abstract void setConfiguration();

    /**
     * todo 设置布局资源文件ID
     */
    protected abstract int getLayoutId();

    /**
     * todo 一些需要手动释放的资源可以在这里进行释放
     */
    protected abstract void afterDestroy();

    /**
     * 有必要在onCreate方法中进行实现的
     */
    protected abstract void initViewAfterOnCreate();

    /**
     *
     */
    protected abstract void lazyloadCallback();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setConfiguration();
        applyConfiguration();
        super.onCreate(savedInstanceState);
        layoutBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initViewAfterOnCreate();
        // 实际测试发现，该回调的调用在执行完onWindowFocusChanged方法之后【满足当界面显示完成之后进行回调】
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                lazyloadCallback();
                return false;
            }
        });
    }

    /**
     * todo 应用相应的配置，为了能够有定制化的需要，将作用域设置成保护的，继承类可直接覆盖来进行定制化的设置
     */
    protected void applyConfiguration() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        afterDestroy();
        if (layoutBinding != null) layoutBinding.unbind();// todo 当页面销毁时，对databinding对象进行解绑操作
        layoutBinding = null;
    }
}
