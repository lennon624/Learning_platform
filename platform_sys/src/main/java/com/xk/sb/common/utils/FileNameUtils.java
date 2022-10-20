package com.xk.sb.common.utils;

/**
 * @author Dover Wong
 * @description: 生成新的文件名（利用时间戳防止重名）文件前缀名_当前时间.文件扩展名
 */

public class FileNameUtils {

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 获取文件前缀
     *
     * @param fileName
     * @return
     */
    public static String getPrefix(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     *
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName) {
        return FileNameUtils.getPrefix(fileOriginName) + "_" + System.currentTimeMillis() + FileNameUtils.getSuffix(fileOriginName);
    }
}