package com.github.begonia.examples.springboot.provider.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "order-center")
public interface HelloFeignService2 {

    /**
     *
     * @param
     * @return
     */
    @GetMapping(value = "/private_sleep/{name}")
    String hello(@PathVariable("name") String name);
}
