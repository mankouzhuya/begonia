package com.github.begonia.bootstrap.process.feign;

import com.github.begonia.bootstrap.process.ProcessChain;
import com.github.begonia.bootstrap.process.Processer;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import static com.github.begonia.bootstrap.strategy.ClassNameEnhanceStrategy.CLASS_NAME_FEIGN_REQUESTTEMPLATE;
import static com.github.begonia.core.config.BootstrapConfig.*;

public class RequestTemplateProcess implements Processer {

    public static final String E_METHOD_REQUEST = "request";




    private String buildMthodSub() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("org.springframework.web.context.request.RequestAttributes requestAttributes = org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes();\r\n");
        stringBuffer.append("javax.servlet.http.HttpServletRequest req = ((org.springframework.web.context.request.ServletRequestAttributes)requestAttributes).getRequest();\r\n");

        stringBuffer.append("com.github.begonia.core.context.TrackContext context = com.github.begonia.core.context.TrackContext.getTrackContextNotNull(); \r\n");

        //uid 透传
        stringBuffer.append("String reqUid = req.getHeader(\"" + HEAD_REQ_ID + "\");\r\n");
        stringBuffer.append("if(reqUid ==null || reqUid ==\"\"){\r\n");
        stringBuffer.append("reqUid = context.getGid();\r\n");
        stringBuffer.append("}\r\n");
        stringBuffer.append("java.util.List uidList = new java.util.ArrayList();\r\n");
        stringBuffer.append("uidList.add(reqUid);\r\n");
        stringBuffer.append("headers.put(\"" + HEAD_REQ_ID + "\",uidList);\r\n");

        //itemId 取当前系统trackId
        stringBuffer.append("java.util.List itemIdList = new java.util.ArrayList();\r\n");
        stringBuffer.append("itemIdList.add(context.getCurrentSysId());\r\n");
        stringBuffer.append("headers.put(\"" + HEAD_FROM_ID + "\",itemIdList);\r\n");

        //version
        stringBuffer.append("String version = req.getHeader(\"" + HEAD_VERSION + "\");\r\n");
        stringBuffer.append("if(version !=null && version !=\"\"){\r\n");
        stringBuffer.append("java.util.List versionList = new java.util.ArrayList();\r\n");
        stringBuffer.append("versionList.add(version);\r\n");
        stringBuffer.append("headers.put(\"" + HEAD_VERSION + "\",versionList);\r\n");
        stringBuffer.append("}\r\n");

        return stringBuffer.toString();
    }


    @Override
    public CtClass process(ProcessChain chain, CtClass ctClass) throws NotFoundException, CannotCompileException {
        if (!CLASS_NAME_FEIGN_REQUESTTEMPLATE.equals(ctClass.getName())) return chain.process(chain, ctClass);
        CtMethod ctMethod = ctClass.getDeclaredMethod(E_METHOD_REQUEST);
        ctMethod.insertBefore(buildMthodSub());
        return ctClass;
    }
}
