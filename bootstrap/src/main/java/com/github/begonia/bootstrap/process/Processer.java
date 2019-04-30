package com.github.begonia.bootstrap.process;

import javassist.CtClass;

public interface Processer {

    String ANO_CONTROLLER = "org.springframework.stereotype.Controller";

    String METHOD_EQUALS = "java.lang.Object.equals(java.lang.Object)";

    String METHOD_TOSTRING= "java.lang.Object.toString()";

    CtClass process(ProcessChain chain,CtClass ctClass);

}
