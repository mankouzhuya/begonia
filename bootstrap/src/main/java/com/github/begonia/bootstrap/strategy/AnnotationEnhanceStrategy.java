package com.github.begonia.bootstrap.strategy;

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

    public AnnotationEnhanceStrategy(){
        List<String> annotations = new ArrayList<>();
        annotations.add(ANO_RESTCONTROLLER);
        annotations.add(ANO_SERVICE);
        this.annotations = annotations;
    }

    @Override
    public Boolean canProcess(String sourceClassName, ClassPool pool) {
        Boolean canProcess = super.canProcess(sourceClassName,pool);
        if(!canProcess) return false;
        CtClass ctClass = super.getCtClass(sourceClassName,pool);
        return annotations.stream().anyMatch(s -> ctClass.hasAnnotation(s));
    }
}
