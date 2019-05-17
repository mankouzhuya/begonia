package com.github.begonia.bootstrap.process.spring.controller;

import com.github.begonia.bootstrap.process.AbsProcess;
import com.github.begonia.bootstrap.process.ProcessChain;
import javassist.CtClass;

import static com.github.begonia.bootstrap.strategy.AnnotationEnhanceStrategy.ANO_RESTCONTROLLER;

public class ControllerProcess extends AbsProcess {

    @Override
    public CtClass process(ProcessChain chain, CtClass ctClass) {
        if(!ctClass.hasAnnotation(ANO_RESTCONTROLLER)) return chain.process(chain,ctClass);
        return processAllMethod(ctClass);
    }

}
