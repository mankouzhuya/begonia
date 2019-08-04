package com.github.begonia.examples.springboot.consumer.feign;

import com.github.begonia.examples.springboot.consumer.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "pay-center")
public interface HelloFeignService {

    /**
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    String hello(@RequestParam(value = "name") String name);


    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    UserDTO hello2(@RequestParam(value = "name") String name, @RequestBody UserDTO userDTO);
}
