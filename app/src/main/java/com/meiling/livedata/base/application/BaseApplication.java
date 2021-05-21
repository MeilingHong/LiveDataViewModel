package com.meiling.livedata.base.application;


import com.meiling.component.utils.log.Mlog;
import com.meiling.livedata.BuildConfig;

import androidx.multidex.MultiDexApplication;

/**
 * @Author marisareimu
 * @time 2021-05-19 10:52
 */
public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
        initComponent();
    }

    private void initLog() {
        Mlog.setConfig(BuildConfig.LOG_DEBUG,"_AndroidRuntime");
    }

    private void initComponent() {
        /**
         * todo 一些第三方组件的初始化
         */
    }
}
