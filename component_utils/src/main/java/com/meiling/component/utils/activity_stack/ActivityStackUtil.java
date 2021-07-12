package com.meiling.component.utils.activity_stack;

import android.app.Activity;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理工具类
 *
 * @Author marisareimu
 * @time 2021-05-26 16:31
 */
public class ActivityStackUtil {

    public static ActivityStackUtil getInstance() {
        return Holder.instances;
    }

    private static class Holder {
        private static ActivityStackUtil instances = new ActivityStackUtil();
    }

    // 人为管理Activity的栈
    private volatile String lastActivityName;// 存储上一次加入数组中的Activity对象名称【带路径，避免重名引起的问题】
    private List<Activity> stack = new ArrayList<>();

    private ActivityStackUtil() {
        // 构造函数私有化
        stackInit();
    }

    private void stackInit() {
        if (stack == null) {
            stack = new ArrayList<>();
        }
    }

    /**
     * 将Activity放入数组中进行管理
     * 目的：
     * 1、避免多次重复实例化相同Activity【在Activity栈顶层】
     */
    public synchronized boolean pushIntoStack(Activity activity) {
        if (activity == null) {// 避免添加无效的Activity对象
            return false;
        }
        stackInit();// 避免出现调用时空指针问题【数组空指针】
        if (!stack.isEmpty()) {
            if (stack.contains(activity)) {// 如果数组中包含该对象【相同对象】
                // 【contains表示包含了相同对象，但Intent创建的应该是不同的对象，所以，这个管理还不够完善】
                return pushAction(activity);
            } else if (isContainSameActivity(activity)) {// 不包含相同Activity对象，但有相同类型的实例【】
                return pushAction(activity);
            } else {
                return pushAction(activity);
            }
        } else {
            // 从未添加过
            stack.add(activity);
            lastActivityName = activity.getClass().getName();// 正常赋值
            return true;
        }
    }

    private boolean pushAction(Activity activity) {
        if (activity == null) {// 避免添加无效的Activity对象
            return false;
        }
        int size = stack.size();
        if (!TextUtils.isEmpty(lastActivityName) && lastActivityName.equals(activity.getClass().getName())) {
            // 上次记录的Activity名称赋值过，并且与当前的，名称相同时，忽略该次的添加，并关闭该添加的Activity
            finishActivity(activity);// 关闭该Activity
            return false;
        } else if (size > 0 && stack.get(size - 1) != null &&// 如果不判断Size>0，则可能出现下标越界异常
                stack.get(size - 1).getClass().getName().equals(activity.getClass().getName())) {
            // 上一次添加了相同类型的对象，但名称赋值被跳过，仍然需要忽略
            finishActivity(activity);// 关闭该Activity
            lastActivityName = activity.getClass().getName();// 避免，添加过，但赋值出现了问题；
            return false;
        } else {
            // 相同Activity被添加过
            stack.add(activity);
            lastActivityName = activity.getClass().getName();// 正常赋值
            return true;
        }
    }

    private void finishActivity(Activity activity) {
        if(activity!=null) activity.finish();
    }

    private boolean isContainSameActivity(Activity activity) {
        if (activity == null) {
            return false;
        }
        if (stack == null || stack.isEmpty()) {
            return false;
        } else {
            String tempClassName = activity.getClass().getName();
            for (Activity tempActivity : stack) {
                if (tempActivity != null && tempClassName.equals(tempActivity.getClass().getName())) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 在队列中，移除指定的对象
     */
    public synchronized void pullOutOfStack(Activity activity) {
        if (activity == null) {
            return;
        }
        if (stack == null || stack.isEmpty()) {
            return;
        }
        if (!stack.contains(activity)) {
            return;
        }
        stack.remove(activity);// 由于remove实现中已经进行了循环的比较，所以，不需要在外层额外做一个包含的判断了
    }

    /**
     * 将该指定的Activity加入队列，并保证
     */
    public synchronized boolean pushOnlyOneIntoStack(Activity activity) {
        if (activity == null) {// 避免添加无效的Activity对象
            return false;
        }
        stackInit();// 避免出现调用时空指针问题【数组空指针】
        if (!stack.isEmpty()) {
            // 循环，清除已有的全部数据
            int size = stack.size();
            for (int i = size - 1; i >= 0; i--) {
                if (stack.get(i) != null) {
                    stack.get(i).finish();
                    stack.remove(i);
                }
            }
            // 避免影响该添加，需要重置上一次的值；
            lastActivityName = null;
            return pushAction(activity);
        } else {
            // 从未添加过
            stack.add(activity);
            lastActivityName = activity.getClass().getName();// 正常赋值
            return true;
        }
    }

}
