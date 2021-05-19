package com.meiling.component.utils.files;

import android.os.StatFs;

/**
 * @Author marisareimu
 * @time 2021-05-19 16:10
 */
public class SpaceCalculateUtils {
    /**
     * ***************************************************************************************************************************************
     * 计算指定空间大小是：多少GB，多少MB，多少KB，多少B
     */
    private static final long KB = 1024;
    private static final long MB = 1048576;
    private static final long GB = 1073741824L;
    private static final long TB = 1099511627776L;

    public static long[] calculateSpace(long size) {
        long[] sizeResult = new long[]{0, 0, 0, 0};
        if (size <= 0) {
            return sizeResult;
        }
        if (size < KB) {
            sizeResult[0] = size;
            return sizeResult;
        }
        if (size < MB) {
            sizeResult[0] = size % KB;// Byte
            sizeResult[1] = size / KB;// KB
            return sizeResult;
        }
        if (size < GB) {
            sizeResult[0] = size % KB;// Byte
            sizeResult[1] = (size % MB - size % KB) / KB;// KB
            sizeResult[2] = size / MB;// MB
            return sizeResult;
        }
        // size >=GB
        sizeResult[0] = size % KB;// Byte
        sizeResult[1] = (size % MB - size % KB) / KB;// KB
        sizeResult[2] = (size % GB - size % MB) / MB;// MB
        sizeResult[3] = size / GB;// GB
        return sizeResult;
    }

    /**
     * ***************************************************************************************************************************************
     * 计算指定路径的剩余空间和总空间大小
     */

    private static long getAvailableSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        return fileStats.getAvailableBlocksLong() * fileStats.getBlockSizeLong(); // 注意与fileStats.getFreeBlocks()的区别
    }

    private static long getTotalSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        return fileStats.getBlockCountLong() * fileStats.getBlockSizeLong();
    }
    /**
     * ***************************************************************************************************************************************
     */
}
