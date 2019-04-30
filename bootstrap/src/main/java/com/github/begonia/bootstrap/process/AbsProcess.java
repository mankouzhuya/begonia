package com.github.begonia.bootstrap.process;

import com.alibaba.fastjson.JSON;
import io.vavr.Tuple3;
import javassist.*;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;

import java.util.stream.Collectors;


public abstract class AbsProcess implements Processer {

    /**
     * 检查方法是否可以增强
     * **/
    protected Boolean canEnhanceMethod(CtMethod ctMethod){
        if(AccessFlag.PUBLIC == ctMethod.getMethodInfo().getAccessFlags() && !METHOD_EQUALS.equals(ctMethod.getLongName()) && !METHOD_TOSTRING.equals(ctMethod.getLongName())) return true;
        return false;
    }

    /***
     * 处理一个类中的所有方法
     * **/
    protected CtMethod processAllMethod(CtClass cct){
//        Arrays.asList(cct.getMethods()).forEach(m ->{
//            if(canEnhanceMethod(m)){
//
//            }
//        });
        return null;
    }

    /**
     * 影子方法
     * **/
    protected Tuple3<CtClass,CtMethod,CtMethod> shadow(CtClass ctClass,CtMethod ctMethod) throws CannotCompileException {
        String oldMethodName = ctMethod.getName();
        String newMethodName = oldMethodName + "$impl";
        ctMethod.setName(newMethodName);
        CtMethod newCtMethod = CtNewMethod.copy(ctMethod, oldMethodName, ctClass, null);
        for(Object attribute: ctMethod.getMethodInfo().getAttributes()) {
            newCtMethod.getMethodInfo().addAttribute((AttributeInfo)attribute);
        }
        ctMethod.getMethodInfo().removeAttribute(AnnotationsAttribute.visibleTag);
        ctMethod.getMethodInfo().removeAttribute(ParameterAnnotationsAttribute.visibleTag);
        return new Tuple3<CtClass,CtMethod,CtMethod>(ctClass,ctMethod,newCtMethod);
    }



    protected StringBuffer excute(Tuple3<CtClass,CtMethod,CtMethod> tuple3) throws NotFoundException {
        StringBuffer body = new StringBuffer();
        body.append("{\n long start = System.currentTimeMillis();\n");

        body.append("\ncom.xiehua.agent.context.TrackContext context = com.xiehua.agent.context.TrackContext.getTrackContextNotNull();");
        body.append("\ncom.xiehua.agent.context.MethodNode methodNode = new com.xiehua.agent.context.MethodNode();");

        String returnType = tuple3._2.getReturnType().getName();
        if(!"void".equals(returnType)) body.append(returnType + " result = ");

        //excute target method
        body.append(tuple3._2.getName() + "($$);\n");
        //rt
        body.append("long rt = System.currentTimeMillis()-start;\n");

        if(!"void".equals(returnType)) body.append("return result;\n");



        //tuple3._3.addCatch();


        body.append("}\n");
        return body;
    }

    public void test(CtMethod newCtMethod){
        long start = System.currentTimeMillis();
        com.github.begonia.core.context.TrackContext context = com.github.begonia.core.context.TrackContext.getTrackContextNotNull();
        com.github.begonia.core.context.MethodNode methodNode = new com.github.begonia.core.context.MethodNode();
        try {
            Object[] ob = null;//$args;
            methodNode.setMethodId(java.util.UUID.randomUUID().toString().replace("-",""));
            methodNode.setMethodFullName(newCtMethod.getLongName());
            methodNode.setArgs(java.util.Arrays.asList(ob).stream().map(s -> com.alibaba.fastjson.JSON.toJSONString(s)).collect(Collectors.toList()));



            //TODO something
        }finally {

        }

    }




    protected StringBuffer getStackTraceCode(StringBuffer sb) {
        sb.append("     StackTraceElement[] as = Thread.currentThread().getStackTrace();");
        sb.append("     System.out.println(\"class:\"+as[1].getClassName()+\" method:\"+as[1].getMethodName());");
        sb.append("     StringBuffer temp = new StringBuffer();");
        sb.append("     for (int i = (as.length - 1); i > 0; i--) {");
        sb.append("         temp.append(as[i].getClassName()+\".\"+as[i].getMethodName()+\",\");");
        sb.append("     }");
        sb.append("     System.out.println(\"调用堆栈:\"+temp.toString());");
        return sb;
    }


}
