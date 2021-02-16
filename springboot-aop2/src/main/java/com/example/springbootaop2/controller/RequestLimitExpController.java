package com.example.springbootaop2.controller;

import com.example.springbootaop2.safeLimit.RequestLimit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Title: ,
 *
 * @author yangyuegang
 * @create: 2021/2/4
 */
@Controller
@RequestMapping("safeLimit")
public class RequestLimitExpController {

    @RequestMapping(value = "/limit")
    @RequestLimit(count = 1,time = 6000)
    @ResponseBody
    public Map<String, Object> safeLimitRes(HttpServletRequest request, Integer pagenum, Integer pagesize){
        Map<String, Object> outputMap =new HashMap<String,Object>();
        outputMap.put("success", true);
        outputMap.put("msg", "访问成功。");
        return outputMap;
    }

}
