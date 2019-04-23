package com.github.begonia.bootstrap.process.spring.service;

import com.github.begonia.bootstrap.process.AbsProcess;
import javassist.CtClass;

public class ServiceProcess extends AbsProcess {


    @Override
    public byte[] process(String sourceClassName, CtClass cls) {
        return new byte[0];
    }

}
