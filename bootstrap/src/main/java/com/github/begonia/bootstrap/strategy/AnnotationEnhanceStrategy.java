package com.github.begonia.bootstrap.strategy;

import com.github.begonia.bootstrap.process.ProcessChain;
import com.github.begonia.bootstrap.process.spring.controller.ControllerProcess;
import com.github.begonia.bootstrap.process.spring.service.ServiceProcess;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 基于class 头部的注解增强class
 * ***/
public class AnnotationEnhanceStrategy extends AbsEnhanceStrategy {

    public static final String ANO_RESTCONTROLLER = "org.springframework.web.bind.annotation.RestController";

    public static final String ANO_SERVICE = "org.springframework.stereotype.Service";

    private List<String> annotations;

    private ProcessChain processChain;

    public AnnotationEnhanceStrategy(){
        List<String> annotations = new ArrayList<>();
        annotations.add(ANO_RESTCONTROLLER);
        annotations.add(ANO_SERVICE);
        this.annotations = annotations;
        this.processChain = initChain();
    }

    @Override
    public Boolean canProcess( ClassPool pool,String sourceClassName,CtClass ctClass) {
        Boolean canProcess = super.canProcess(pool,sourceClassName,ctClass);
        if(!canProcess) return false;
        return annotations.stream().anyMatch(s -> ctClass.hasAnnotation(s));
    }

    @Override
    public CtClass process(CtClass ctClass) {
        return processChain.process(processChain,ctClass);
    }

    public ProcessChain initChain(){
        ProcessChain chain = new ProcessChain();
        chain.addProcesser(new ControllerProcess());
        chain.addProcesser(new ServiceProcess());
        return chain;
    }
}
