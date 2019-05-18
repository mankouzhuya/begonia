package com.github.begonia.examples.springboot.provider.controller;

import com.github.begonia.cache.DefaultCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author: xujin
 **/
@Controller
@RequestMapping("/hello")
public class HelloController2 {


    @GetMapping("/test4")
    @ResponseBody
    public Object hello4(){
        return DefaultCache.getInstance().getAll();
    }

}
