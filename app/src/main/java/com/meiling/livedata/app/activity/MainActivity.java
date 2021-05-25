package com.meiling.livedata.app.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.meiling.component.utils.log.Mlog;
import com.meiling.livedata.R;
import com.meiling.livedata.app.dialog.loading.LoadingDialog;
import com.meiling.livedata.app.popup.LoadingPopupWindow;
import com.meiling.livedata.base.activity.ActivityConfig;
import com.meiling.livedata.base.activity.BaseActivity;
import com.meiling.livedata.base.dialog.callback.IDismissCallback;
import com.meiling.livedata.base.dialog.callback.IShowCallback;
import com.meiling.livedata.databinding.ActivityDatabindMainBinding;

import androidx.annotation.NonNull;

/**
 * @Author marisareimu
 * @time 2021-05-19 10:52
 */
public class MainActivity extends BaseActivity<ActivityDatabindMainBinding> {

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void setConfiguration() {
        activityConfig = new ActivityConfig.Builder()
                .setFullscreen(true)
                .setShowStatusBar(true)
                .setWhiteStatusFont(false)
                .setBlockBackKey(false)// 屏蔽返回键
                .setDoubleBackExit(true)
                .setKeepScreenOn(false)
                .setNavigatorBarColor(getResources().getColor(R.color.color_white))
                .setPortraitMode(true)
                .build();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_databind_main;
    }

    @Override
    protected void afterDestroy() {
        removeHandlerMessages(mHandler);
    }

    @Override
    protected void initViewAfterOnCreate() {
        layoutBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
            }
        });
        layoutBinding.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toActivity(newIntent(Main1Activity.class), 2);
                showLoadingPopupWindow();
            }
        });
    }

    @Override
    protected void lazyloadCallback() {

    }

    /*
     **************************************************************************************
     */
    private LoadingDialog loadingDialog;

    private void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog();
            loadingDialog.setDialogConfig(getApplicationContext(), new IShowCallback() {
                @Override
                public void afterDialogShow() {
                    Mlog.d("afterDialogShow---");
                }
            }, new IDismissCallback() {
                @Override
                public void afterDialogDismiss() {
                    Mlog.d("afterDialogDismiss---");
                    if (loadingDialog != null) {
                        loadingDialog = null;
                    }
                }
            });
            loadingDialog.show(getSupportFragmentManager(), "loading", 2000);
        }
    }

    private LoadingPopupWindow loadingPopupWindow;

    private void showLoadingPopupWindow() {
        if (loadingPopupWindow == null) {
            loadingPopupWindow = new LoadingPopupWindow(getApplicationContext());// 直接在构造方法中设置dismiss回调似乎有问题
            loadingPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {// 但显示设置就不会有这个问题了
                @Override
                public void onDismiss() {
                    Mlog.d("onDismiss【popupWindow】---");
                    if (loadingPopupWindow != null) {
                        loadingPopupWindow = null;
                    }
                }
            });
            loadingPopupWindow.showAsDropDown(layoutBinding.click);

            /*
             *
             */
//            int[] positionInScreen = new int[2];
//            // todo  使用 findViewById 可以拿到所在的屏幕位置，但直接使用 databind.view 的方式拿不到
//            TextView textView = findViewById(R.id.click);
//            textView.getLocationOnScreen(positionInScreen);// 获取在屏幕中的位置【全屏时，无偏差】
//            //            layoutBinding.click.getLocationInWindow(positionInScreen);
//            Mlog.w(positionInScreen[0] + "---" + positionInScreen[1] + "---" + textView.getMeasuredWidth() + "---" + textView.getMeasuredHeight());
//            loadingPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.LEFT | Gravity.TOP,
//                    positionInScreen[0], positionInScreen[1] + textView.getMeasuredHeight());
        }
    }

    /*
     **************************************************************************************
     * 页面跳转的返回结果
     */

    @Override
    protected void activityResultWithData(int requestCode, int resultCode, Intent data) {
        super.activityResultWithData(requestCode, resultCode, data);
    }

    @Override
    protected void activityResultWithoutData(int requestCode, int resultCode) {
        super.activityResultWithoutData(requestCode, resultCode);
    }

    /*
     **************************************************************************************
     */

    @Override
    protected void showDoubleBackExitHint() {
        super.showDoubleBackExitHint();
        Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
    }
}