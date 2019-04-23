package com.github.begonia.bootstrap.strategy;

import com.github.begonia.bootstrap.process.ProcessChain;
import javassist.ClassPool;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Register {

    private EnhanceStrategy strategy;

    private byte[]  classfileBuffer;

    private Register(){}

    public Register(EnhanceStrategy strategy,byte[]  classfileBuffer){
        this.strategy = strategy;
        this.classfileBuffer = classfileBuffer;
    }

    public byte[] execute(ProcessChain chain,String sourceClassName, ClassPool pool) throws IOException {
        Boolean canProcess = strategy.canProcess(sourceClassName,pool);
        if(!canProcess) return null;
        CtClass cls = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
        return chain.process(sourceClassName,strategy.getCtClass(sourceClassName,pool));
    }

}
