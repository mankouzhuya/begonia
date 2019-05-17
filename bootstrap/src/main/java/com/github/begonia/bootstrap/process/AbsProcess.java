package com.github.begonia.bootstrap.process;

import com.alibaba.fastjson.JSON;
import com.github.begonia.core.context.MethodNode;
import com.github.begonia.core.context.PostMan;
import com.github.begonia.core.context.TrackContext;
import com.github.begonia.core.fun.Try;
import javassist.*;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;

import java.util.Arrays;
import java.util.stream.Collectors;


public abstract class AbsProcess implements Processer {

    /**
     * 检查方法是否可以增强
     **/
    protected Boolean canEnhanceMethod(CtMethod ctMethod) {
        if (AccessFlag.PUBLIC == ctMethod.getMethodInfo().getAccessFlags() && !METHOD_EQUALS.equals(ctMethod.getLongName()) && !METHOD_TOSTRING.equals(ctMethod.getLongName()))
            return true;
        return false;
    }

    /***
     * 处理一个类中的所有方法
     * **/
    protected CtClass processAllMethod(CtClass cct) {
        Arrays.asList(cct.getMethods()).forEach(Try.of_c(m ->{
            if(canEnhanceMethod(m)) shadow(cct,m);
        }));
        return cct;
    }

    /**
     * 影子方法
     **/
    protected void shadow(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException, NotFoundException {
        String oldMethodName = ctMethod.getName();
        String newMethodName = oldMethodName + "$impl";
        ctMethod.setName(newMethodName);
        CtMethod newCtMethod = CtNewMethod.copy(ctMethod, oldMethodName, ctClass, null);
        for (Object attribute : ctMethod.getMethodInfo().getAttributes()) {
            newCtMethod.getMethodInfo().addAttribute((AttributeInfo) attribute);
        }
        ctMethod.getMethodInfo().removeAttribute(AnnotationsAttribute.visibleTag);
        ctMethod.getMethodInfo().removeAttribute(ParameterAnnotationsAttribute.visibleTag);

        newCtMethod.setBody(enhance(ctClass.getName(),oldMethodName,newMethodName,ctMethod.getReturnType().getName()));
        ctClass.addMethod(newCtMethod);
    }


    protected String enhance( String fullClassName,String oldMethodName,String newMethodName,String type){
        StringBuffer body = new StringBuffer();

        body.append("\n { \n");

        body.append("\n  long start = System.currentTimeMillis(); \n");
        body.append("\n  com.github.begonia.core.context.MethodNode methodNode = null; \n");
        body.append("\n  try { \n");
        body.append("\n  com.github.begonia.core.context.TrackContext context = com.github.begonia.core.context.TrackContext.getTrackContextNotNull(); \n");
        body.append("\n  String methodUid = com.github.begonia.core.context.TrackContext.genUid(); \n");
        body.append("\n  methodNode = new com.github.begonia.core.context.MethodNode(); \n");
        body.append("\n  methodNode.setMethodId(methodUid); \n");
        body.append("\n  if(context.getAnoId() == null || context.getAnoId().equals(\"\")){ \n");
        //body.append("\n  methodNode.setParentMethodId(null); \n");
        body.append("\n  methodNode.setNodeType(com.github.begonia.core.context.MethodNode.NODE_TYPE_START); \n");
        body.append("\n  context.setTrackId(methodUid); \n");
        body.append("\n  }else { \n");
        body.append("\n  methodNode.setParentMethodId(context.getAnoId()); \n");
        body.append("\n  methodNode.setNodeType(com.github.begonia.core.context.MethodNode.NODE_TYPE_NORMAL); \n");
        body.append("\n  } \n");
        body.append("\n  methodNode.setTrackId(context.getTrackId()); \n");

        body.append("\n  methodNode.setFullClassName(\""+fullClassName+"\"); \n");
        body.append("\n  methodNode.setMethodName(\""+oldMethodName+"\"); \n");
        body.append("\n  Object[] parameters = $args; \n");
        body.append("\n  if(parameters != null && parameters.length>0){ \n");
        body.append("\n  java.util.List list = new java.util.ArrayList(); \n");
        body.append("\n  for (int i = 0; i < parameters.length; i++) { \n");
        body.append("\n  list.add(com.alibaba.fastjson.JSON.toJSONString(parameters[i])); \n");
        body.append("\n  } \n");
        body.append("\n  methodNode.setArgs(list); \n");
        body.append("\n  } \n");
        body.append("\n  context.setAnoId(methodUid); \n");
        //返回值类型不同

        if(!"void".equals(type)) body.append(type + " result = ");
        //可以通过$$将传递给拦截器的参数，传递给原来的方法
        body.append(newMethodName + "($$);\n");
        if(!"void".equals(type)) {
            body.append("\n  methodNode.setReturnInfo(com.alibaba.fastjson.JSON.toJSONString(result)); \n");
            body.append("return result;\n");
        }

        body.append("\n } catch (java.lang.Exception e) { \n");

        body.append("\n methodNode.setExcetpionMsg(e.getLocalizedMessage()); \n");
        body.append("\n throw e; \n");

        body.append("\n } finally { \n");

        body.append("\n if(methodNode != null){ \n");
        body.append("\n methodNode.setExcuteTime(System.currentTimeMillis() - start); \n");
       // body.append("\n com.github.begonia.core.context.PostMan.push(methodNode); \n");
        body.append("\n com.github.begonia.core.bus.jvm.event_bus.Bus.post(new com.github.begonia.core.bus.jvm.msg.Msg(com.github.begonia.core.bus.jvm.msg.Msg.MSG_TYPE_METHOD_NODE,methodNode)); \n");
        body.append("\n } \n");//if(methodNode != null){ 结束

        body.append("\n } \n");//try结束

        body.append("\n } \n");//最外层结束

        return body.toString();
    }

    public void test(CtClass ctClass, CtMethod newCtMethod) {
        long start = System.currentTimeMillis();
        MethodNode methodNode = null;
        try {
            TrackContext context = TrackContext.getTrackContextNotNull();
            String methodUid = TrackContext.genUid();
            methodNode = new MethodNode();
            methodNode.setMethodId(methodUid);
            if(context.getAnoId() == null || context.getAnoId().equals("")){
                methodNode.setParentMethodId(null);
                methodNode.setNodeType(MethodNode.NODE_TYPE_START);
                context.setTrackId(methodUid);
            }else {
                methodNode.setParentMethodId(context.getAnoId());
                methodNode.setNodeType(MethodNode.NODE_TYPE_NORMAL);
            }
            methodNode.setTrackId(context.getTrackId());
            methodNode.setFullClassName(ctClass.getName());
            methodNode.setMethodName(newCtMethod.getName());
            Object[] parameters = null;//$args;
            methodNode.setArgs(java.util.Arrays.asList(parameters).stream().map(s -> JSON.toJSONString(s)).collect(Collectors.toList()));


            context.setAnoId(methodUid);

        } catch (Exception e) {
            methodNode.setExcetpionMsg(e.getLocalizedMessage());
            throw e;
        } finally {
            if(methodNode != null){
                methodNode.setExcuteTime(System.currentTimeMillis() - start);
                PostMan.push(methodNode);
            }

        }

    }

}
