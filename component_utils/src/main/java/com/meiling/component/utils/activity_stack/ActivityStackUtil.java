package com.meiling.component.utils.activity_stack;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @Author marisareimu
 * @time 2021-05-26 16:31
 */
public class ActivityStackUtil {
    // 人为管理Activity的栈
    private static String lastActivityName;
    private List<AppCompatActivity> stack = new ArrayList<>();

    private void stackInit() {
        if (stack == null) {
            stack = new ArrayList<>();
        }
    }

    public void pushIntoStack(AppCompatActivity activity) {
        stackInit();// 避免出现调用时空指针问题
    }

    public void pullOutOfStack(AppCompatActivity activity){

    }
}
