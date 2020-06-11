package de.Bethibande.Engine.CodeInjection;

import java.io.*;

public class ClassLoader extends java.lang.ClassLoader {

    public ClassLoader(java.lang.ClassLoader parent) {
        super(parent);
    }

    public Class loadClass(String name, File f) {

        try {
            FileInputStream r = new FileInputStream(f);
            byte[] classData = new byte[(int)f.length()];
            r.read(classData);

            return defineClass(name, classData, 0, classData.length);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
