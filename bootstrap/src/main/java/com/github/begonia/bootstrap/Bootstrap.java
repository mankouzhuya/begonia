package com.github.begonia.bootstrap;

import com.github.begonia.bootstrap.loader.BegoniaLoader;
import com.github.begonia.bootstrap.process.ProcessChain;
import com.github.begonia.bootstrap.process.spring.controller.ControllerProcess;
import com.github.begonia.bootstrap.process.spring.service.ServiceProcess;
import com.github.begonia.bootstrap.strategy.AnnotationEnhanceStrategy;
import com.github.begonia.bootstrap.strategy.ClassNameEnhanceStrategy;
import com.github.begonia.bootstrap.strategy.EnhanceStrategy;
import com.github.begonia.bootstrap.strategy.Register;
import com.github.begonia.bootstrap.trigger.TaskTrigger;
import com.google.common.base.Strings;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Bootstrap {

    public static final String MOD_CURRENT = "debug";

    public static final String MOD_DEBUG = "debug";

    public static final String DEBUG_DUMP_PATH = "C:/debug";

    public static void premain(String arg, Instrumentation instrumentation) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if(log.isDebugEnabled()) log.debug("agent 参数:{}", arg);
        //String path = new File(Bootstrap.class.getResource("/").getPath()).getParent().replace("\\","/")+"/lib/";
        String path = getLibPath();
        if(path == null) return;
        BegoniaLoader.loadJarPath(path);
        TaskTrigger.trigger();

        instrumentation.addTransformer((loader,className,classBeingRedefined,protectionDomain,classfileBuffer)->{
            try {
                if(Strings.isNullOrEmpty(className)) return null;
                //if(log.isDebugEnabled()) log.debug("加载class:{},classLoader是:{}",className, loader != null ? loader.toString() : null);
                ClassPool pool = ClassPool.getDefault();
                //pool.insertClassPath(new ClassClassPath(this.getClass()));
                //pool.insertClassPath(new LoaderClassPath(loader));
                pool.appendClassPath(new LoaderClassPath(loader));

                CtClass cls = pool.makeClass(new ByteArrayInputStream(classfileBuffer));

                if(MOD_CURRENT.equals(MOD_DEBUG)) CtClass.debugDump = DEBUG_DUMP_PATH;
                return new Register().execute(pool,className,cls).toBytecode();
            } catch (Exception e) {
                log.error("初始化失败:{}", e);
            }
            return null;
        });


    }

    public static String getLibPath(){
        List<String> paths = ManagementFactory.getRuntimeMXBean().getInputArguments().stream().filter(s -> s.contains("bootstrap") && s.contains(".jar")).collect(Collectors.toList());
        if(paths == null || paths.size() < 1){
            log.warn("bootstrap jar包路径没找到");
            return null;
        }
        String[] temp = paths.get(0).split(":");
        String[] strs = temp[temp.length - 1].replace("\\","/").split("/");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            if(i == (strs.length -1)){
                stringBuffer.append("lib/");
            }else{
                stringBuffer.append(strs[i]+"/");
            }
        }
        return stringBuffer.toString();
    }



}
