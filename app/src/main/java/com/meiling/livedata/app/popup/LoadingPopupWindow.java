package com.meiling.livedata.app.popup;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.meiling.component.utils.datahandle.UnitExchangeUtil;
import com.meiling.livedata.R;
import com.meiling.livedata.base.dialog.base.BasePopupWindow;
import com.meiling.livedata.base.dialog.callback.IDismissCallback;
import com.meiling.livedata.base.dialog.callback.IShowCallback;
import com.meiling.livedata.base.dialog.config.PopupConfig;
import com.meiling.livedata.databinding.DialogLoadingBinding;

/**
 * @Author marisareimu
 * @time 2021-05-24 18:10
 */
public class LoadingPopupWindow extends BasePopupWindow<DialogLoadingBinding> {
    private IShowCallback iShowCallback;
    private IDismissCallback iDismissCallback;

    public LoadingPopupWindow(Context context, IShowCallback iShowCallback, IDismissCallback iDismissCallback) {
        super(context);
        this.iShowCallback = iShowCallback;
        this.iDismissCallback = iDismissCallback;
    }

    @Override
    protected void initComponentView(View contentView) {

    }

    @Override
    protected void initConfiguration() {
        this.config = new PopupConfig.Builder().
                setDialogStyle(R.style.popupAnimation).
                setBackgroundDrawableColor(R.color.alpha_percent_30).
                setContentViewLayout(R.layout.dialog_loading).
                setInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN).
                setFocusable(false).
                setTouchable(true).// todo 如果这个值不设置为true，则dismiss回调将会出现问题【在show时就被调用，而不是在dismiss时被调用】
                setTouchOutside(true).
                setCancelForBackKey(false).
                isAutoClose(false).
                setCloseDelayTime(5000).
                setShowCallback(iShowCallback).
                setDismissCallback(iDismissCallback).
                setDialogWidth(UnitExchangeUtil.dip2px(mContext, 125)).
                setDialogHeight(UnitExchangeUtil.dip2px(mContext, 125)).
                build();
    }
}
