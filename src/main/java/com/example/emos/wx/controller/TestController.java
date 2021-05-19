package com.example.emos.wx.controller;

import com.example.emos.wx.common.util.R;
import com.example.emos.wx.controller.form.TestSayHelloForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 齐春晖
 * @date Created in 9:44 2021/5/5
 * @description
 */
@RestController//使用Json格式传输数据
@RequestMapping("/test")//分配一个路径
@Api("测试web接口")
public class TestController {
    @ApiOperation("最简单的web测试")
    @PostMapping("/ok")
    public R sayHello(@Valid @RequestBody TestSayHelloForm form){

        return R.ok().put("message","HelloJava!"+form.getName());
    }


}
