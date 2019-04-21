package com.github.begonia.bootstrap;

import com.github.begonia.bootstrap.loader.BegoniaLoader;
import com.github.begonia.bootstrap.trigger.TaskTrigger;
import com.google.common.base.Strings;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.bytecode.AccessFlag;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.Arrays;

@Slf4j
public class Bootstrap {

    public static final String MOD_CURRENT = "debug";

    public static final String MOD_DEBUG = "debug";

    public static final String DEBUG_DUMP_PATH = "C:/debug";

    public static void premain(String arg, Instrumentation instrumentation) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if(log.isDebugEnabled()) log.debug("agent 参数:{}", arg);
        String path = new File(Bootstrap.class.getResource("/").getPath()).getParent().replace("\\","/")+"/lib/";
        BegoniaLoader.loadJarPath(path);
        TaskTrigger.trigger();

        instrumentation.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                try {
                    if(Strings.isNullOrEmpty(className)) return null;
                    if(log.isDebugEnabled()) log.debug("加载class:{},classLoader是:{}",className, loader != null ? loader.toString() : null);

                    ClassPool pool = ClassPool.getDefault();
                    //pool.insertClassPath(new ClassClassPath(this.getClass()));
                    pool.insertClassPath(new LoaderClassPath(loader));
                    pool.insertClassPath(path);

                    CtClass cls = pool.makeClass(new ByteArrayInputStream(classfileBuffer));

                    if(MOD_CURRENT.equals(MOD_DEBUG)) CtClass.debugDump = DEBUG_DUMP_PATH;


                    return cls.toBytecode();
                } catch (Exception e) {
                    log.error("初始化失败:{}", e);
                }
                return null;
            }
        });

    }



}
