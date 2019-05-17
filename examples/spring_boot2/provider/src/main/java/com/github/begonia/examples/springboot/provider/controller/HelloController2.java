package com.github.begonia.examples.springboot.provider.controller;

import com.github.begonia.core.cache.DefaultCache;
import com.github.begonia.examples.springboot.provider.dto.UserDTO;
import com.github.begonia.examples.springboot.provider.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
