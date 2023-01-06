package com.github.q16695.managers;

import com.github.q16695.events.Event;
import com.github.q16695.events.EventHandler;
import com.github.q16695.events.TickEvent;
import com.github.q16695.modules.CheckModule;
import com.github.q16695.modules.EmptyModule;
import com.github.q16695.modules.MainListener;
import com.github.q16695.modules.RightModule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class EventManager {
    private static ArrayList<EmptyModule> listeners = new ArrayList<>();
    private static ArrayList<Object> EVENT_BUS = new ArrayList<>();

    public EventManager() {
        listeners.add(new CheckModule());
        listeners.add(new RightModule());
        EVENT_BUS.add(new MainListener());
    }

    public static void register(Object obj) {
        if(!EVENT_BUS.contains(obj)) {
            EVENT_BUS.add(obj);
        }
    }

    public static void unregister(Object obj) {
        EVENT_BUS.remove(obj.getClass());
    }

    /** if a object dont have parameters, default post TickEvent */
    public static void aPost(Event event) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        if(EVENT_BUS.isEmpty()) return;
        for(Object obj : EVENT_BUS) {
            Class<?> clazz = obj.getClass();
            for(Method method : clazz.getMethods()) {
                if(!method.getReturnType().isAssignableFrom(void.class)) continue;
                if(method.getAnnotation(EventHandler.class) != null) {
                    if(method.getParameters().length == 0 && event instanceof TickEvent) {
                        method.invoke(obj);
                    }
                    else if(method.getParameterTypes().length != 0) {
                        String clasz = method.getParameterTypes()[0].toString().substring(6);
                        if(event.getClass().isAssignableFrom(Class.forName(clasz))) {
                            method.invoke(obj, event);
                        }
                    }
                }
            }
        }
    }

    public static void post(Event event) {
        if(listeners.isEmpty()) return;
        for(EmptyModule l : listeners) {
            l.onEvent(event);
        }
        try {
            aPost(event);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}