package com.github.begonia.bootstrap.strategy;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbsEnhanceStrategy implements EnhanceStrategy {

    @Override
    public Boolean canProcess(ClassPool pool, String className, CtClass ctClass) {
        try {
            if (pool.get(className) == null) return false;
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public CtClass getCtClass(CtClass ctClass) {
        return ctClass;
    }
}
