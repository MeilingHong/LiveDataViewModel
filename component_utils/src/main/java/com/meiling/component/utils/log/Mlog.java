package com.meiling.component.utils.log;

import android.util.Log;

/**
 * @Author marisareimu
 * @time 2021-05-19 15:24
 */
public class Mlog {
    private static boolean DEBUG = false;
    private static final int LENGTH_LIMIT = 3000;
    private static String TAG = "_AndroidRuntime";//方面能够同时查看到异常信息

    /**
     * 让日志开关和打印的Tag可配置
     *
     * @param debug
     * @param customTag
     */
    public static void setConfig(boolean debug, String customTag) {
        Mlog.DEBUG = debug;// 指定是否是Debug模式
        Mlog.TAG = customTag;// 指定Log显示的Tag
    }

    public static void e(String msg) {
        if (DEBUG) {
            if (msg != null && msg.length() > 0) {
                if (msg.length() > LENGTH_LIMIT) {
                    int chunkCount = msg.length() / LENGTH_LIMIT;     // integer division
                    for (int i = 0; i <= chunkCount; i++) {
                        int max = LENGTH_LIMIT * (i + 1);
                        if (max >= msg.length()) {
                            Log.e(TAG, msg.substring(LENGTH_LIMIT * i));
                        } else {
                            Log.e(TAG, msg.substring(LENGTH_LIMIT * i, max));
                        }
                    }
                } else {
                    Log.e(TAG, msg.toString());
                }
            }
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            if (msg != null && msg.length() > 0) {
                if (msg.length() > LENGTH_LIMIT) {
                    int chunkCount = msg.length() / LENGTH_LIMIT;     // integer division
                    for (int i = 0; i <= chunkCount; i++) {
                        int max = LENGTH_LIMIT * (i + 1);
                        if (max >= msg.length()) {
                            Log.w(TAG, msg.substring(LENGTH_LIMIT * i));
                        } else {
                            Log.w(TAG, msg.substring(LENGTH_LIMIT * i, max));
                        }
                    }
                } else {
                    Log.w(TAG, msg.toString());
                }
            }
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            if (msg != null && msg.length() > 0) {
                if (msg.length() > LENGTH_LIMIT) {
                    int chunkCount = msg.length() / LENGTH_LIMIT;     // integer division
                    for (int i = 0; i <= chunkCount; i++) {
                        int max = LENGTH_LIMIT * (i + 1);
                        if (max >= msg.length()) {
                            Log.i(TAG, msg.substring(LENGTH_LIMIT * i));
                        } else {
                            Log.i(TAG, msg.substring(LENGTH_LIMIT * i, max));
                        }
                    }
                } else {
                    Log.i(TAG, msg.toString());
                }
            }
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            if (msg != null && msg.length() > 0) {
                if (msg.length() > LENGTH_LIMIT) {
                    int chunkCount = msg.length() / LENGTH_LIMIT;     // integer division
                    for (int i = 0; i <= chunkCount; i++) {
                        int max = LENGTH_LIMIT * (i + 1);
                        if (max >= msg.length()) {
                            Log.d(TAG, msg.substring(LENGTH_LIMIT * i));
                        } else {
                            Log.d(TAG, msg.substring(LENGTH_LIMIT * i, max));
                        }
                    }
                } else {
                    Log.d(TAG, msg.toString());
                }
            }
        }
    }
}
