package com.xk.sb.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传工具类
 *
 * @author Dover Wong
 */
public class UploadFileUtil {
    private static final Logger log = LoggerFactory.getLogger(UploadFileUtil.class);

    private final static String FILE_PATH = "/static/homework/";

    /**
     * 文件上传
     *
     * @param uploadFile
     * @param fileName
     */
    public static Map<String, String> uploadFile(MultipartFile uploadFile, String fileName) {
        Map<String, String> data = new HashMap<>();
        String uploadFileName = null;
        try {
            InputStreamReader read = new InputStreamReader(uploadFile.getInputStream(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            StringBuffer jsonContent = new StringBuffer();
            String content = "";
            while ((content = bufferedReader.readLine()) != null) {
                jsonContent.append(content);
            }
            String filePath = UploadFileUtil.FILE_PATH + File.separator + fileName;
            File date = new File(filePath);
            FileOutputStream fop = null;
            fop = new FileOutputStream(date);
            byte[] contentInBytes = jsonContent.toString().getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
            read.close();
            data.put("success", "true");
            data.put("message", "文件上传成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            data.put("success", "false");
            data.put("message", "文件上传失败");
        }
        return data;
    }
}