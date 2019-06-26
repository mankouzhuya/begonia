package com.github.begonia.bootstrap;

import com.github.begonia.bootstrap.loader.BegoniaClassLoader;
import com.github.begonia.bootstrap.strategy.Register;
import com.github.begonia.bootstrap.trigger.TaskTrigger;
import com.github.begonia.communication.Client;
import com.github.begonia.core.bus.jvm.event_bus.Bus;
import com.github.begonia.core.bus.jvm.event_bus.Eventhandler;
import com.google.common.base.Strings;
import io.vavr.Tuple2;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
public class Bootstrap {

    public static final Boolean MOD_CURRENT = true;

    public static final String DEBUG_DUMP_PATH = "C:/debug";

    public static Map<String, String> AGENT_MAP;//agent 参数

    public static final String AGENT_PARAM_PACKAGE_SCAN = "package_scan";//包扫描

    public static void premain(String arg, Instrumentation instrumentation) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IOException, ExecutionException, InterruptedException {
        //build agent param
        if (log.isDebugEnabled()) log.debug("agent 参数:{}", arg);
        try {
            AGENT_MAP = buildInputParam(arg);
        } catch (Exception e) {
            log.error("agent参数错误", e);
            return;
        }
        //check package scan
        if (!AGENT_MAP.containsKey(AGENT_PARAM_PACKAGE_SCAN)) {
            log.warn("包扫描路径未配置");
            return;
        }
        String packageScan = AGENT_MAP.get(AGENT_PARAM_PACKAGE_SCAN).replace(".", "/");
        //load jar
        String path = getLibPath();
        if (path == null) return;
        BegoniaClassLoader.loadJarPath(path);
        //fire task
        TaskTrigger.trigger();
        //regist bus
        Bus.register(new Eventhandler());
        //transform
        instrumentation.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
            try {
                if (Strings.isNullOrEmpty(className)) return null;
                if (!className.startsWith(packageScan)) return null;
                ClassPool pool = ClassPool.getDefault();
                //pool.insertClassPath(new ClassClassPath(this.getClass()));
                //pool.insertClassPath(new LoaderClassPath(loader));
                pool.appendClassPath(new LoaderClassPath(loader));

                CtClass cls = pool.makeClass(new ByteArrayInputStream(classfileBuffer));

                if (MOD_CURRENT) CtClass.debugDump = DEBUG_DUMP_PATH;
                return new Register().execute(pool, className, cls).toBytecode();
            } catch (Exception e) {
                log.error("初始化失败:{}", e);
            }
            return null;
        });
        //start communication module
        Client client = new Client();
        client.start("localhost", 8888);
    }

    public static String getLibPath() {
        List<String> paths = ManagementFactory.getRuntimeMXBean().getInputArguments().stream().filter(s -> s.contains("bootstrap") && s.contains(".jar")).collect(Collectors.toList());
        if (paths == null || paths.size() < 1) {
            log.warn("bootstrap jar包路径没找到");
            return null;
        }
        String[] temp = paths.get(0).split(":");
        String[] strs = temp[temp.length - 1].replace("\\", "/").split("/");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            if (i == (strs.length - 1)) {
                stringBuffer.append("lib/");
            } else {
                stringBuffer.append(strs[i] + "/");
            }
        }
        return stringBuffer.toString();
    }

    public static Map<String, String> buildInputParam(String arg) {
        if (Strings.isNullOrEmpty(arg) || !arg.contains("&") || !arg.contains("=")) return null;
        return Arrays.asList(arg.split("&")).stream().map(s -> {
            String[] temp = s.split("=");
            Tuple2<String, String> tuple2 = new Tuple2<>(temp[0], temp[1]);
            return tuple2;
        }).collect(Collectors.toMap(s -> s._1, t -> t._2, (x, y) -> y));
    }

}
