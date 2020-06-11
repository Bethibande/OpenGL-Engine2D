package de.Bethibande.Engine.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;

@SuppressWarnings("unused")
public class ArrayUtils {

    public static boolean contains(Object[] array, Object obj) {
        for(Object o : array) {
            if(o.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public static float[] FloatTofloat(Float[] arr) {
        float[] a = new float[arr.length];
        int i = 0;
        while(i < arr.length) {
            a[i] = arr[i];
            i++;
        }
        return a;
    }

    public static int getKeyIndexInLinkedHashMap(LinkedHashMap<?, ?> map, Object key) {
        Iterator<?> it = map.keySet().iterator();
        int i = 0;
        while(it.hasNext()) {
            if(it.next() == key) return i;
            i++;
        }
        return -1;
    }

}
