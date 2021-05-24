package com.meiling.livedata.base.dialog.base;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;


import com.meiling.component.utils.log.Mlog;
import com.meiling.livedata.R;
import com.meiling.livedata.base.dialog.config.PopupConfig;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @Author huangzhou@ubanquan.cn
 * @time 2021-05-17 11:22
 */
public abstract class BasePopupWindow<T extends ViewDataBinding> extends PopupWindow {
    protected T layoutDialogBinding;
    protected Context mContext;
    protected PopupConfig config;

    public BasePopupWindow(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        layoutDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), config != null && config.getContentViewLayout() != 0 ? config.getContentViewLayout() : R.layout.dialog_toast_alpha, null, false);
        initComponentView(layoutDialogBinding.getRoot());// 实例化布局对应的子组件
        setContentView(layoutDialogBinding.getRoot());// 设置PopupWindow的View
        // 设置Window的宽高
        if (config != null && (config.getDialogWidth() > 1 ||
                config.getDialogWidth() == ViewGroup.LayoutParams.WRAP_CONTENT ||
                config.getDialogWidth() == ViewGroup.LayoutParams.MATCH_PARENT) && (config.getDialogHeight() > 1 ||
                config.getDialogHeight() == ViewGroup.LayoutParams.WRAP_CONTENT ||
                config.getDialogHeight() == ViewGroup.LayoutParams.MATCH_PARENT)) {
            setWidth(config.getDialogWidth());
            setHeight(config.getDialogHeight());
        } else {
            setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        }
        // 设置显示的动画
        setAnimationStyle(config == null || config.getStyleId() == 0 ? R.style.popupAnimation : config.getStyleId());
        // 设置背景色【如果不设置，将不会显示出来】
        ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(R.color.color_black));
        setBackgroundDrawable(dw);
        // 设置其他属性
        setFocusable(false);
        setOutsideTouchable(config == null ? true : config.isTouchOutside());
        setTouchable(config == null ? true : config.isTouchable());
//        setClippingEnabled(false);// 解决背景超出Fragment的问题-----【实际发现没有效果】
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (config != null && config.getDismissCallback() != null) {
                    config.getDismissCallback().afterDialogDismiss();
                }
            }
        });
        setSoftInputMode(config == null || config.getInputMode() == 0 ? WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN : config.getInputMode());
    }

    protected abstract void initComponentView(View contentView);

    // 避免高版本出现显示位置异常
    @Override
    public void showAsDropDown(View anchor) {//靠左显示
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;// 实际增减没发现背景的下方位置有变化，
            Mlog.d("popup h:" + h + "--heightPixels:" + anchor.getResources().getDisplayMetrics().heightPixels + "--bottom:" + rect.bottom);
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }
}
