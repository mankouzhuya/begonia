package com.github.begonia.core.config;

import com.github.begonia.core.utils.PropsUtil;
import com.google.common.base.Strings;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: 张艳军
 * @date: 2019/8/3 14:40
 * @desc:
 * @version: 1.0
 **/
@Slf4j
public class BootstrapConfig {

    public static final String PARAM_SERVER_HOST = "server.address";//服务端地址

    public static final String PARAM_SERVER_PORT = "server.port";//服务端地址

    public static final String PARAM_CLIENT_PACKAGE_SCAN = "client.package_scan";//客户端包扫描

    public static final String PARAM_CLIENT_CLIENT_NAME = "client.name";//客户端名字

    public static Map<String, String> AGENT_MAP;//agent 参数


    /**
     * others
     * ***/
    public static final String HEAD_REQ_ID = "Request-ID";//global request id,write to request head

    public static final String HEAD_ITERM_ID = "Requst-Iterm-ID";//每个单独请求分配一共req id

    public static final String HEAD_FROM_ID = "Requst-From-ID";//每个单独请求分配一共req id

    public static final String HEAD_VERSION = "version";//version

    public static synchronized void init(String path, String arg) {
        try {
            AGENT_MAP = new PropsUtil(path).loadPropsToMap();
            Map<String, String> map = buildInputParam(arg);
            AGENT_MAP.putAll(map);
        } catch (Exception e) {
            log.error("agent参数错误", e);
            return;
        }
    }

    private static Map<String, String> buildInputParam(String arg) {
        if (Strings.isNullOrEmpty(arg) || !arg.contains("&") || !arg.contains("=")) return null;
        return Arrays.asList(arg.split("&")).stream().map(s -> {
            String[] temp = s.split("=");
            Tuple2<String, String> tuple2 = new Tuple2<>(temp[0], temp[1]);
            return tuple2;
        }).collect(Collectors.toMap(s -> s._1, t -> t._2, (x, y) -> y));
    }
}
