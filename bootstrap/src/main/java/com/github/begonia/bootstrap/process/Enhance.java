package com.github.begonia.bootstrap.process;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;


public interface Enhance {

    public static final String ANO_CONTROLLER = "org.springframework.stereotype.Controller";

    public static final String METHOD_EQUALS = "java.lang.Object.equals(java.lang.Object)";

    public static final String METHOD_TOSTRING= "java.lang.Object.toString()";

    byte[] enhance(String sourceClassName, CtClass cls) throws CannotCompileException, NotFoundException, IOException, ClassNotFoundException;

}
