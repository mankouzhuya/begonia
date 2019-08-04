package com.github.begonia.bootstrap.process.spring.service;

import com.github.begonia.bootstrap.process.AbsProcess;
import com.github.begonia.bootstrap.process.ProcessChain;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import static com.github.begonia.bootstrap.strategy.AnnotationEnhanceStrategy.ANO_SERVICE;

public class ServiceProcess extends AbsProcess {

    @Override
    public CtClass process(ProcessChain chain, CtClass ctClass) throws NotFoundException, CannotCompileException {
        if(!ctClass.hasAnnotation(ANO_SERVICE)) return chain.process(chain,ctClass);
        return processAllMethod(ctClass);
    }
}
