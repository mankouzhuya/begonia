package com.github.begonia.bootstrap.strategy;

import com.github.begonia.bootstrap.process.ProcessChain;
import com.github.begonia.bootstrap.process.feign.RequestTemplateProcess;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于class 名字增强class
 ***/
@Slf4j
public class ClassNameEnhanceStrategy extends AbsEnhanceStrategy {

    public static final String CLASS_NAME_FEIGN_REQUESTTEMPLATE = "feign.RequestTemplate";

    public static List<String> classNames;

    private ProcessChain processChain;

    static {
        List<String> classNameList = new ArrayList<>();
        classNameList.add(CLASS_NAME_FEIGN_REQUESTTEMPLATE);
        classNames = classNameList;
    }

    public ClassNameEnhanceStrategy() {
        this.processChain = initChain();
    }

    @Override
    public Boolean canProcess(ClassPool pool, String className, CtClass ctClass) {
        Boolean canProcess = super.canProcess(pool, className, ctClass);
        if (!canProcess) return false;
        Boolean flag = classNames.contains(className.replace("/","."));
        return flag;
    }

    @Override
    public CtClass process(CtClass ctClass) throws NotFoundException, CannotCompileException {
        return processChain.process(processChain, ctClass);
    }

    public ProcessChain initChain() {
        ProcessChain chain = new ProcessChain();
        chain.addProcesser(new RequestTemplateProcess());
        return chain;
    }
}
