package de.Bethibande.Engine.utils;

import java.util.Calendar;

public class Log {

    public static void log(String message) {
        Calendar c = Calendar.getInstance();
        String time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        System.out.println("[" + time + "] [Thread/" + Thread.currentThread().getName() + "] " + message);
    }

    public static void logError(String message) {
        Calendar c = Calendar.getInstance();
        String time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        System.err.println("[" + time + "] [Thread/" + Thread.currentThread().getName() + "] " + message);
    }

}
