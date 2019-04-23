package com.github.begonia.bootstrap.process;

import javassist.CtClass;

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
    public byte[] process(String sourceClassName, CtClass cls) {
        if(processers.size() == index.get()) return null;
        Processer processer = processers.get(index.addAndGet(1));
        return processer.process(sourceClassName,cls);
    }

}
