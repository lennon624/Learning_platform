package com.xk.sb.common.constants;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class Test {

    @ResponseBody
    @RequestMapping("/hello")
    public Result hello() {
        Result result = new Result();
        result.success("hello SpringBoot");
        return result;
    }
}
