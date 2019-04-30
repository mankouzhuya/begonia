package com.github.begonia.bootstrap.strategy;

import com.github.begonia.bootstrap.process.ProcessChain;
import com.github.begonia.bootstrap.process.spring.controller.ControllerProcess;
import com.github.begonia.bootstrap.process.spring.service.ServiceProcess;
import javassist.ClassPool;
import javassist.CtClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于class 名字增强class
 * ***/
@Slf4j
public class ClassNameEnhanceStrategy extends AbsEnhanceStrategy{

    public List<String> classNames;

    private ProcessChain processChain;

    public ClassNameEnhanceStrategy(){
        List<String> classNameList = new ArrayList<>();
        classNameList.add("class full name a");
        classNameList.add("class full name b");
        this.classNames = classNameList;
        this.processChain = initChain();
    }

    @Override
    public Boolean canProcess(ClassPool pool,String className,CtClass ctClass) {
        if(!classNames.contains(className)) return false;
        return super.canProcess(pool,className,ctClass);
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
