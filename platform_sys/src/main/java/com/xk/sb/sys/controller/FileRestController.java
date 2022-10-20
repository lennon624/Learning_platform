package com.xk.sb.sys.controller;

import com.aliyuncs.utils.StringUtils;
import com.xk.sb.common.tools.handler.NonStaticResourceHttpRequestHandler;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dover Wong
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys/video")
public class FileRestController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 预览视频文件, 支持 byte-range 请求
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "src", paramType = "String", value = "视频资源名", required = true)
    })
    @ApiOperation(value = "根据资源名获取视频流")
    @RequestMapping(method = RequestMethod.GET, value = "/play")
    public void videoPreview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String videoSrc = request.getParameter("src");

        String typeLocal = applicationContext.getEnvironment().getProperty("web.local");

        logger.info("请求视频：" + videoSrc);
        logger.info("获取视频流中...");

        String path = "";
        if ("true".equals(typeLocal)) {
            // 本地的路径
            path = "src/main/resources/files/video/" + videoSrc;
        } else {
            // 服务器上的路径
            path = "/home/static/video/" + videoSrc;

        }

        // 获取路径下的视频文件
        Path filePath = Paths.get(path);
        if (Files.exists(filePath)) {
            logger.info("文件存在");
            String mimeType = Files.probeContentType(filePath);
            if (!StringUtils.isEmpty(mimeType)) {
                logger.info("视频流不为空，正在封装到响应头...");
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
            logger.info("封装完毕");
        } else {
            logger.info("文件不存在");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }
}

