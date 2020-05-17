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

    public static float[] FloatTofloat(Float[] arr) {
        float[] a = new float[arr.length];
        int i = 0;
        while(i < arr.length) {
            a[i] = arr[i];
            i++;
        }
        return a;
    }

}
