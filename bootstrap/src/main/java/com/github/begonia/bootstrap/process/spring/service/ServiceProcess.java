package com.github.begonia.bootstrap.process.spring.service;

import com.github.begonia.bootstrap.process.AbsProcess;
import com.github.begonia.bootstrap.process.ProcessChain;
import javassist.CtClass;

import static com.github.begonia.bootstrap.strategy.AnnotationEnhanceStrategy.ANO_SERVICE;

public class ServiceProcess extends AbsProcess {

    @Override
    public CtClass process(ProcessChain processChain, CtClass ctClass) {
        if(!ctClass.hasAnnotation(ANO_SERVICE)) return processChain.process(processChain,ctClass);

        System.out.println("ServiceProcess 处理了 ");
        return ctClass;
    }
}
