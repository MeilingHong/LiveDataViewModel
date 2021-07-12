package com.meiling.livedata.utils;

import android.text.TextUtils;


import com.meiling.component.utils.log.Mlog;
import com.meiling.livedata.base.activity.BaseActivity;

import java.util.ArrayList;

/**
 * Created by marisareimu@126.com on 2020-12-17  16:09
 * project Ubanquan
 */
public class ActivityStackUtil {
    private ArrayList<BaseActivity> activityStack;
    private String lastActivityName = null;// 避免持有引用导致的泄漏问题

    private ActivityStackUtil() {
        activityStack = new ArrayList<>();
    }

    public static ActivityStackUtil getInstance() {
        return Holder.instances;
    }

    public synchronized void saveActivity(BaseActivity activity) {
        if (activityStack != null) {
            if (activityStack.contains(activity)) {
                // 表明已经存过该Activity了，不需要再次存入一个实例
//                activity.finish();// 销毁该多余的Activity【针对多次点击导致页面多次打开的一种处理方式】
                Mlog.w("ActivityStackUtil finish【containsKey】：" + activity.getClass().getName());
                if (!TextUtils.isEmpty(lastActivityName) && lastActivityName.equals(activity.getClass().getName())) {
                    activity.finish();// 销毁该多余的Activity【针对多次点击导致页面多次打开的一种处理方式】
                    Mlog.w("ActivityStackUtil finish【多次重复打开页面】：" + lastActivityName);
                    return;
                } else {
                    activityStack.add(activity);
                    lastActivityName = activity.getClass().getName();
                    Mlog.w("ActivityStackUtil put【重复的Activity添加】：" + activity.getClass().getName());
                }
            } else {
                activityStack.add(activity);
                lastActivityName = activity.getClass().getName();
                Mlog.w("ActivityStackUtil put：" + activity.getClass().getName());
            }
        }
    }

    public synchronized void removeActivity(BaseActivity activity) {
        if (activityStack != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
            Mlog.w("ActivityStackUtil remove：" + activity.getClass().getName());
        }
    }

    public synchronized void removeAllOnLogout(BaseActivity activity) {// 清除除指定Activity之外的其他Activity
        if (activityStack != null && activityStack.size() > 1) {// 当且仅当，进入MainActivity后，栈中的对象超过2【含2】个时，才进行操作
            // 为了避免同步写异常【java.util.ConcurrentModificationException】重新创建一个Key集合，然后通过这个集合遍历
            int size = activityStack.size();
            for (int i = size - 1; i >= 0; i--) {
                // 清除除MainActivity以外的全部Activity
                BaseActivity activityClassName = activityStack.get(i);
                if (activityStack.contains(activityClassName) && // 包含这个对象
                        !activity.equals(activityClassName)) {// 不是指定的这个对象
                    BaseActivity tempActivity = activityClassName;
                    // 如果Activity尚未被销毁掉，则手动进行销毁，然后删除该Activity的引用
                    if (tempActivity != null && !tempActivity.isFinishing()) {
                        Mlog.w("ActivityStackUtil removeAll【finish】：" + tempActivity.getClass().getName());
                        tempActivity.finish();
                    } else {
                        Mlog.w("ActivityStackUtil removeAll【check】：" + tempActivity.getClass().getName());
                    }
                    activityStack.remove(activityClassName);
                } else {
                    Mlog.w("ActivityStackUtil stay【MainActivity】：" + activityClassName);
                }
            }
        }
    }

    private static class Holder {
        private static ActivityStackUtil instances = new ActivityStackUtil();
    }
}
