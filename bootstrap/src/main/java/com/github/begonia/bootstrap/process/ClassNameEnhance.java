package com.github.begonia.bootstrap.process;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;

/**
 * 基于class 名字增强class
 * ***/
public class ClassNameEnhance extends AbsEnhance{

    @Override
    public byte[] enhance(String sourceClassName, CtClass cls) throws CannotCompileException, NotFoundException, IOException, ClassNotFoundException {
        return new byte[0];
    }

}
