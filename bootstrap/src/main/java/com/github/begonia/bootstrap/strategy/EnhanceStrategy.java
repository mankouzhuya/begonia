package com.github.begonia.bootstrap.strategy;

import javassist.ClassPool;
import javassist.CtClass;


public interface EnhanceStrategy {

    Boolean canProcess(String sourceClassName,ClassPool pool);

    CtClass getCtClass(String sourceClassName,ClassPool pool);



}
