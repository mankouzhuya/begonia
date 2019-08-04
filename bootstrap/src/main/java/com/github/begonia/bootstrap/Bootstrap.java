package com.github.begonia.bootstrap;

import com.github.begonia.bootstrap.loader.BegoniaClassLoader;
import com.github.begonia.bootstrap.strategy.ClassNameEnhanceStrategy;
import com.github.begonia.bootstrap.strategy.Register;
import com.github.begonia.bootstrap.trigger.TaskTrigger;
import com.github.begonia.communication.Client;
import com.github.begonia.core.config.BootstrapConfig;
import com.google.common.base.Strings;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
public class Bootstrap {

    public static final Boolean MOD_CURRENT = true;

    public static final String DEBUG_DUMP_PATH = "C:/debug";

    public static void premain(String arg, Instrumentation instrumentation) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IOException, ExecutionException, InterruptedException {
        //build agent param
        if (log.isDebugEnabled()) log.debug("agent 参数:{}", arg);
        BootstrapConfig.init(getConfigPath() + "bootstrap.properties", arg);
        //check package scan
        if (!BootstrapConfig.AGENT_MAP.containsKey(BootstrapConfig.PARAM_CLIENT_PACKAGE_SCAN)) {
            log.warn("包扫描路径未配置");
            return;
        }
        String packageScan = BootstrapConfig.AGENT_MAP.get(BootstrapConfig.PARAM_CLIENT_PACKAGE_SCAN).replace(".", "/");
        //load jar
        String path = getLibPath();
        if (path == null) return;
        BegoniaClassLoader.loadJarPath(path);
        //fire task
        TaskTrigger.trigger();
        //regist bus
        //Bus.register(new Eventhandler());
        //transform
        instrumentation.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
            try {
                if (Strings.isNullOrEmpty(className)) return null;
                if (className.startsWith(packageScan) || ClassNameEnhanceStrategy.CLASS_NAME_FEIGN_REQUESTTEMPLATE.contains(className.replace("/","."))){
                    ClassPool pool = ClassPool.getDefault();
                    //pool.insertClassPath(new ClassClassPath(this.getClass()));
                    //pool.insertClassPath(new LoaderClassPath(loader));
                    pool.appendClassPath(new LoaderClassPath(loader));

                    CtClass cls = pool.makeClass(new ByteArrayInputStream(classfileBuffer));

                    if (MOD_CURRENT) CtClass.debugDump = DEBUG_DUMP_PATH;
                    return new Register().execute(pool, className, cls).toBytecode();
                }
            } catch (Exception e) {
                log.error("初始化失败:{}", e);
            }
            return null;
        });
        //start communication module
        Client client = new Client();
        client.start(BootstrapConfig.AGENT_MAP.get(BootstrapConfig.PARAM_SERVER_HOST), Integer.valueOf(BootstrapConfig.AGENT_MAP.get(BootstrapConfig.PARAM_SERVER_PORT)));
    }

    public static String getLibPath() {
        File file = new File(getAgentPath());
        return file.getParent() + File.separator + "lib" + File.separator;

    }

    public static String getConfigPath() {
        File file = new File(getAgentPath());
        return file.getParent() + File.separator + "conf" + File.separator;
    }

    private static String getAgentPath() {
        List<String> paths = ManagementFactory.getRuntimeMXBean().getInputArguments().stream().filter(s -> s.contains("bootstrap") && s.contains(".jar")).collect(Collectors.toList());
        if (paths == null || paths.size() < 1) {
            log.warn("bootstrap jar包路径没找到");
            return null;
        }
        String[] temp = paths.get(0).split("-javaagent:");
        return temp[1];
    }


}
