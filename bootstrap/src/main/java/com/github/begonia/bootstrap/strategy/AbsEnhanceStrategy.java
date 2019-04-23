package com.github.begonia.bootstrap.strategy;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbsEnhanceStrategy implements EnhanceStrategy{

    private CtClass ctClass;

    @Override
    public Boolean canProcess(String className, ClassPool pool){
        try {
            ctClass = pool.get(className);
            if(ctClass == null ) return false;
            return true;
        } catch (NotFoundException e) {
            log.error("类不存在:{}",e);
            return false;
        }
    }

    @Override
    public CtClass getCtClass(String sourceClassName, ClassPool pool) {
        return ctClass;
    }
}
