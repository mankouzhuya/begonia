package com.github.begonia.bootstrap.trigger;

import com.github.begonia.core.task.SendTrackeInfoScheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TaskTrigger {

    public static void trigger() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class clazz = Class.forName("com.github.begonia.core.task.SendTrackeInfoScheduler");
        SendTrackeInfoScheduler sendTrackeInfoScheduler = (SendTrackeInfoScheduler) clazz.newInstance();
        Method method = clazz.getMethod("fire");
        method.invoke(sendTrackeInfoScheduler);
    }

}
