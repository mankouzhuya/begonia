package com.github.begonia.bootstrap.process;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * 基于class 头部的注解增强class
 * ***/
public class AnnotationEnhance extends AbsEnhance{

    private List<String> annotations;

    public AnnotationEnhance(List<String> annotations){
        this.annotations = annotations;
    }

    @Override
    public byte[] enhance(String sourceClassName, CtClass cls) throws CannotCompileException, NotFoundException, IOException, ClassNotFoundException {
        annotations.forEach(s ->{
            if(cls.hasAnnotation(s)){
                Arrays.asList(cls.getMethods()).forEach(m ->{
                    if(AccessFlag.PUBLIC == m.getMethodInfo().getAccessFlags() && !METHOD_EQUALS.equals(m.getLongName()) && !METHOD_TOSTRING.equals(m.getLongName())){
                        addTiming(cls,m);
                    }
                });
            }
        });
        return new byte[0];
    }
}
