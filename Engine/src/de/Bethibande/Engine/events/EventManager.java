package de.Bethibande.Engine.events;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private static final List<Listener> listeners = new ArrayList<>();

    @SuppressWarnings("unused")
    public static void registerListener(Listener l) { listeners.add(l); }
    @SuppressWarnings("unused")
    public static void unregisterListener(Listener l) { listeners.remove(l); }


    public static void runEvent(Event e) {
        try {
            for(Listener l : listeners) {
                for(Method m : l.getClass().getDeclaredMethods()) {
                    if(m.getAnnotations() != null) {
                        m.setAccessible(true);
                        for(Annotation a : m.getAnnotations()) {
                            if(a.annotationType().getName().equalsIgnoreCase(EventHandler.class.getName())) {
                                //System.out.println("c");
                                if(m.getParameters() != null) {
                                    if(m.getParameterTypes()[0].getSuperclass().getName().equalsIgnoreCase(Event.class.getName())) {
                                        //System.out.println(m.getParameters()[0].getType().getName() + " " + e.getClass().getTypeName());
                                        try {
                                            m.invoke(l, e);
                                        } catch (InvocationTargetException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
