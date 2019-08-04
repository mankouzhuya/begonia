package com.github.begonia.bootstrap.strategy;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;


public interface EnhanceStrategy {

    Boolean canProcess(ClassPool pool,String className,CtClass cls);

    CtClass getCtClass(CtClass cls);

    CtClass process(CtClass ctClass) throws NotFoundException, CannotCompileException;



}
