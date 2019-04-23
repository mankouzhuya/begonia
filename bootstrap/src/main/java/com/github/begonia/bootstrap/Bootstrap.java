package com.github.begonia.bootstrap;

import com.github.begonia.bootstrap.loader.BegoniaLoader;
import com.github.begonia.bootstrap.process.ProcessChain;
import com.github.begonia.bootstrap.process.spring.controller.ControllerProcess;
import com.github.begonia.bootstrap.process.spring.service.ServiceProcess;
import com.github.begonia.bootstrap.strategy.AnnotationEnhanceStrategy;
import com.github.begonia.bootstrap.strategy.ClassNameEnhanceStrategy;
import com.github.begonia.bootstrap.strategy.Register;
import com.github.begonia.bootstrap.trigger.TaskTrigger;
import com.google.common.base.Strings;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;

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

        ProcessChain chain = chain();

        instrumentation.addTransformer((loader,className,classBeingRedefined,protectionDomain,classfileBuffer)->{
            try {
                if(Strings.isNullOrEmpty(className)) return null;
                if(log.isDebugEnabled()) log.debug("加载class:{},classLoader是:{}",className, loader != null ? loader.toString() : null);
                ClassPool pool = ClassPool.getDefault();
                //pool.insertClassPath(new ClassClassPath(this.getClass()));
                pool.insertClassPath(new LoaderClassPath(loader));
                pool.insertClassPath(path);
                if(MOD_CURRENT.equals(MOD_DEBUG)) CtClass.debugDump = DEBUG_DUMP_PATH;

                byte[] bytes = new Register(new AnnotationEnhanceStrategy(),classfileBuffer).execute(chain,className,pool);
                if(bytes != null) return bytes;
                return new Register(new ClassNameEnhanceStrategy(),classfileBuffer).execute(chain,className,pool);
            } catch (Exception e) {
                log.error("初始化失败:{}", e);
            }
            return null;
        });


    }

    public static ProcessChain chain(){
        ProcessChain chain = new ProcessChain();
        chain.addProcesser(new ControllerProcess());
        chain.addProcesser(new ServiceProcess());
        return chain;
    }

}
