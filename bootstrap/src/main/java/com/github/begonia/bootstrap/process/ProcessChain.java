package com.github.begonia.bootstrap.process;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessChain extends AbsProcess {

    private volatile List<Processer> processers;

    private AtomicInteger index;

    public ProcessChain(){
        this.processers = new ArrayList<>();
        this.index = new AtomicInteger(0);
    }

    public Processer addProcesser(Processer process){
        processers.add(process);
        return this;
    }


    @Override
    public CtClass process(ProcessChain processChain, CtClass ctClass) throws NotFoundException, CannotCompileException {
        if(processers.size() == index.get()) return null;
        Processer processer = processers.get(index.getAndAdd(1));
        return processer.process(processChain,ctClass);
    }
}
