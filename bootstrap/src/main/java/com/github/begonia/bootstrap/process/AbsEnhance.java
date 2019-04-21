package com.github.begonia.bootstrap.process;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;

import java.io.IOException;

public abstract class AbsEnhance implements Enhance{

    protected void addTiming(CtClass cct, CtMethod ctMethod) {
        try {
            String oldMethodName = ctMethod.getName();
            String newMethodName = oldMethodName + "$impl";
            //将旧的方法名称进行重新命名
            ctMethod.setName(newMethodName);
            //方法的副本，采用过滤器的方式
            CtMethod newCtMethod = CtNewMethod.copy(ctMethod, oldMethodName, cct, null);

            for(Object attribute: ctMethod.getMethodInfo().getAttributes()) {
                newCtMethod.getMethodInfo().addAttribute((AttributeInfo)attribute);
            }
            ctMethod.getMethodInfo().removeAttribute(AnnotationsAttribute.visibleTag);
            ctMethod.getMethodInfo().removeAttribute(ParameterAnnotationsAttribute.visibleTag);

            //为该方法添加时间过滤器来计算时间，并判断获取时间的方法是否有返回值
            String type = ctMethod.getReturnType().getName();
            StringBuffer body = new StringBuffer();
            body.append("{\n long start = System.currentTimeMillis();\n");

            body.append("\ncom.xiehua.agent.context.TrackContext context = com.xiehua.agent.context.TrackContext.getTrackContextNotNull();");
            body.append("\ncom.xiehua.agent.context.MethodNode methodNode = new com.xiehua.agent.context.MethodNode();");
            body.append("\nmethodNode.setMethodId(java.util.UUID.randomUUID().toString().replace(\"-\",\"\"));");
            body.append("\nmethodNode.setMethodFullName(\""+ctMethod.getLongName()+"\");");
            body.append("\nmethodNode.setParentMethodId(\"1\");");
            body.append("\ncontext.addMethodNode(methodNode);");
            body.append("\nSystem.out.println(context.toString());");


            getStackTraceCode(body);
            getParaCode(body);

            //返回值类型不同
            if(!"void".equals(type)) {
                body.append(type + " result = ");
            }
            //可以通过$$将传递给拦截器的参数，传递给原来的方法
            body.append(newMethodName + "($$);\n");
            //输出方法运行时间差
            body.append("System.out.println(\"Call to method " + oldMethodName + " took \" + \n (System.currentTimeMillis()-start) + " +  "\" ms.\");\n");
            if(!"void".equals(type)) {
                body.append("org.slf4j.LoggerFactory.getLogger(getClass()).info(\"返回参数:{}\",result);\n");
                body.append("return result;\n");
            }

            body.append("}");

            newCtMethod.setBody(body.toString());

            CtClass etype = ClassPool.getDefault().get("java.io.IOException");
            newCtMethod.addCatch("{ System.out.println($e); throw $e; }", etype);

            cct.addMethod(newCtMethod);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private StringBuffer getStackTraceCode(StringBuffer sb) {
        sb.append("     StackTraceElement[] as = Thread.currentThread().getStackTrace();");
        sb.append("     System.out.println(\"class:\"+as[1].getClassName()+\" method:\"+as[1].getMethodName());");
        sb.append("     StringBuffer temp = new StringBuffer();");
        sb.append("     for (int i = (as.length - 1); i > 0; i--) {");
        sb.append("         temp.append(as[i].getClassName()+\".\"+as[i].getMethodName()+\",\");");
        sb.append("     }");
        sb.append("     System.out.println(\"调用堆栈:\"+temp.toString());");
        return sb;
    }

    /**
     * 方法参数内容显示的嵌入代码
     *
     * @param
     * @return
     * @throws NotFoundException
     */
    private StringBuffer getParaCode(StringBuffer temp) throws NotFoundException {
        temp.append("       Object[] ob = $args; ");
        temp.append("       StringBuffer sb = new StringBuffer();");
        temp.append("       sb.append(\"[\");");
        temp.append("       for (int i = 0; i < ob.length; i++) {");
        temp.append("            sb.append(ob[i]+\",\");");
        temp.append("       }");
        temp.append("       sb.append(\"]\");");
        temp.append("       System.out.println(\"参数:\"+sb);");
        return temp;
    }
}
