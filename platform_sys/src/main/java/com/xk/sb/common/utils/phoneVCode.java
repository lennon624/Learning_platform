package com.xk.sb.common.utils;


import com.xk.sb.common.constants.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Printable;
import java.util.Random;

@RestController
@RequestMapping("/sys")
public class phoneVCode {

    @RequestMapping(method = RequestMethod.POST, value = "/getVCode")
    public Result loginByPhone(String phone) {

        Result result = new Result();
        int num = 1000 + (int) (Math.random() * (9999 - 1000 + 1));
        result.setData(num);

        return result;

    }


}
