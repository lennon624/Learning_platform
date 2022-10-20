package com.xk.sb.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Auther: wyx
 * @Date: 2019-04-08 10:10
 * @Description:
 */

public class FileUtils {

    /**
     * @param file     文件
     * @param path     文件存放路径
     * @param fileName 原文件名
     * @return
     */
    public static String upload(MultipartFile file, String path, String fileName) {

        String newFileName = FileNameUtils.getFileName(fileName);

        // 生成新的文件名
        String realPath = path + "/" + newFileName;

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}