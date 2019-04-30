package com.github.begonia.bootstrap.strategy;

import javassist.ClassPool;
import javassist.CtClass;


public interface EnhanceStrategy {

    Boolean canProcess(ClassPool pool,String className,CtClass cls);

    CtClass getCtClass(CtClass cls);

    CtClass process(CtClass ctClass);



}
