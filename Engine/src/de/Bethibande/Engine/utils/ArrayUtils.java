package de.Bethibande.Engine.utils;

public class ArrayUtils {

    public static boolean contains(Object[] array, Object obj) {
        for(Object o : array) {
            if(o.equals(obj)) {
                return true;
            }
        }
        return false;
    }

}
