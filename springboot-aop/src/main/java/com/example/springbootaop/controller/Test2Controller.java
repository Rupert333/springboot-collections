package com.example.springbootaop.controller;

import cn.hutool.core.date.DateUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2")
public class Test2Controller {
    
    /**
     * Title: 测试拦截接口,
     * 
     * @Author: yyg
     * @Date: 2021/2/3 下午10:08
     */
    @RequestMapping(value = "/test/{id}",method = RequestMethod.GET)
    public String testMethod(@PathVariable(value = "id")String id){
        return "收到的数据是："+id+"，现在的时间是："+ DateUtil.now();
    }
}
