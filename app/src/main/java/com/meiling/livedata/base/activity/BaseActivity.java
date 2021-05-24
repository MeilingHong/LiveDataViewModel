package com.meiling.livedata.base.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.meiling.component.utils.log.Mlog;
import com.meiling.component.utils.statusbar.QMUIStatusBarHelper;

import androidx.annotation.ColorInt;
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
    protected ActivityConfig activityConfig;

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
     * <p>
     * 使用ActivityConfig类进行参数配置
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
     * todo 有必要在onCreate方法中进行实现的
     */
    protected abstract void initViewAfterOnCreate();

    /**
     * todo 当全部生命流程执行完后回调该方法，用来保证需要延迟初始化或者调用的东西可以在这里进行调用
     */
    protected abstract void lazyloadCallback();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setConfiguration();
        applyConfiguration();
        super.onCreate(savedInstanceState);
        if (getLayoutId() == 0) {
            throw new RuntimeException("Invalid activity layout resource id!");
        }
        layoutBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initViewAfterOnCreate();
        // 实际测试发现，该回调的调用在执行完onWindowFocusChanged方法之后【满足当界面显示完成之后进行回调】
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Mlog.d("lazyloadCallback---" + getClass().getName());
                lazyloadCallback();
                return false;
            }
        });
    }

    /**
     * todo 应用相应的配置，为了能够有定制化的需要，将作用域设置成保护的，继承类可直接覆盖来进行定制化的设置
     */

    protected void applyConfiguration() {
        setActivityOrientation();
        setFullScrennAndStatusBar();
        setNavigatorBarColor();
        setKeepScreenOn();
    }

    /*
     *********************************************************************************************************
     * 系统方法
     */

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Mlog.d("onWindowFocusChanged---" + hasFocus + "---" + getClass().getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Mlog.d("onStart---" + getClass().getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Mlog.d("onResume---" + getClass().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Mlog.d("onPause---" + getClass().getName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Mlog.d("onStop---" + getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Mlog.d("onDestroy---" + getClass().getName());
        afterDestroy();
        clearKeepScreenOn();
        if (layoutBinding != null) layoutBinding.unbind();// todo 当页面销毁时，对databinding对象进行解绑操作
        layoutBinding = null;
    }

    /*
     *********************************************************************************************************
     * 屏幕方向
     */

    protected void setActivityOrientation() {
        setRequestedOrientation(activityConfig != null && activityConfig.isPortraitMode() ?
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /*
     *********************************************************************************************************
     * 设置虚拟导航栏的颜色
     */

    protected void setActivityNavigationBarColor(@ColorInt int colorId) {
        // todo 该方法在API21 （Android 5.0 以及以上的版本才支持）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(colorId);
        }
    }

    private void setNavigatorBarColor() {
        if (activityConfig != null && activityConfig.getNavigatorBarColor() != -1) {
            setActivityNavigationBarColor(activityConfig.getNavigatorBarColor());
        }
    }

    /*
     **********************************************************************************
     * 设置全屏和状态栏颜色
     */
    protected Intent newIntent(Class<?> cls) {
        return new Intent(this, cls);
    }

    protected void toActivity(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            activityResultWithData(requestCode, resultCode, data);
            return;
        }
        activityResultWithoutData(requestCode, resultCode);
    }

    protected void activityResultWithData(int requestCode, int resultCode, Intent data) {
        Mlog.d("---(activityResultWithData)" + getClass().getName() + "---" + requestCode + "----" + resultCode);
    }

    protected void activityResultWithoutData(int requestCode, int resultCode) {
        Mlog.d("---(activityResultWithoutData)" + getClass().getName() + "---" + requestCode + "----" + resultCode);
    }

    /*
     *********************************************************************************************************
     * 提供一个手动清除消息的方法
     */

    /**
     * todo 移除Handler消息队列中的全部信息
     */
    public void removeHandlerMessages(Handler handler) {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /*
     *********************************************************************************************************
     */
    private long doubleBackKeyFirstTimestamp = 0; // 双击退出

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (activityConfig != null && activityConfig.isBlockBackKey()) {// todo 屏蔽系统返回按钮的响应，避免通过按系统返回键关闭Activity的情况
                return true;
            } else {
                if (activityConfig != null && activityConfig.isDoubleBackExit()) {
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - doubleBackKeyFirstTimestamp > 2000) {
                        showDoubleBackExitHint();
                        doubleBackKeyFirstTimestamp = secondTime;//更新firstTime
                        return true;
                    } else {
                        //两次按键小于2秒时，退出应用
                        finish();
                        System.exit(0);
                    }
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 双击退出提示，当设置了双击返回的Activity建议重写该方法并进行实现，使得有一个较好的体验
     */
    protected void showDoubleBackExitHint() {

    }

    /*
     **********************************************************************************
     * 设置屏幕常亮，如果需要则
     */
    private void setKeepScreenOn() {
        // todo 是否保持该页面的屏幕处于常亮状态
        if (activityConfig != null && activityConfig.isKeepScreenOn()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void clearKeepScreenOn() {
        if (activityConfig != null && activityConfig.isKeepScreenOn()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    /*
     **********************************************************************************
     * 设置全屏和状态栏颜色
     */
    protected void setFullScreenStatusFontColor(boolean isWhite) {
        QMUIStatusBarHelper.translucent(this);
        if (isWhite) {
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        } else {
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        }
    }

    private void setFullScrennAndStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (activityConfig != null && activityConfig.isFullscreen() && activityConfig.isShowStatusBar()) {
            // 全屏，显示状态栏
            setFullScreenStatusFontColor(activityConfig.isStatusBarFontColorWhite());
        } else if (activityConfig != null && activityConfig.isFullscreen()) {
            // 全屏，但不现实状态栏
            /*
            2.隐藏状态栏
              getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
              参数：
                View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。
                View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。
                View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
                View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。
             */
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }
}
