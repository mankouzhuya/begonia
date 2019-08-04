package com.github.begonia.bootstrap.strategy;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class Register {

    private EnhanceStrategy annotationEnhanceStrategy;

    private EnhanceStrategy classNameEnhanceStrategy;

    public Register() {
        annotationEnhanceStrategy = new AnnotationEnhanceStrategy();
        classNameEnhanceStrategy = new ClassNameEnhanceStrategy();
    }

    public CtClass execute(ClassPool pool, String className, CtClass cls) throws NotFoundException, CannotCompileException {
        if (annotationEnhanceStrategy.canProcess(pool, className, cls)) {
            CtClass ctClass = annotationEnhanceStrategy.getCtClass(cls);
            return annotationEnhanceStrategy.process(ctClass);
        }
        if (classNameEnhanceStrategy.canProcess(pool, className, cls)) {
            CtClass ctClass = classNameEnhanceStrategy.getCtClass(cls);
            return classNameEnhanceStrategy.process(ctClass);
        }
        return cls;
    }


}
