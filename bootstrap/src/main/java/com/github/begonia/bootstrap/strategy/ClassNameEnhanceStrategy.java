package com.github.begonia.bootstrap.strategy;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于class 名字增强class
 * ***/
@Slf4j
public class ClassNameEnhanceStrategy extends AbsEnhanceStrategy{

    public List<String> classNames;

    public ClassNameEnhanceStrategy(){
        List<String> classNameList = new ArrayList<>();
        classNameList.add("class full name a");
        classNameList.add("class full name b");
        this.classNames = classNameList;
    }

    @Override
    public Boolean canProcess(String sourceClassName, ClassPool pool) {
        if(!classNames.contains(sourceClassName)) return false;
        return super.canProcess(sourceClassName,pool);
    }
}
