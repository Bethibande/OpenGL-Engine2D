package de.Bethibande.Engine.Rendering;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.utils.Log;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.PNGDecoder;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class SpriteLoader {

    public static final HashMap<String, Integer> textures = new HashMap<>();
    public static final HashMap<String, String> loadedFiles = new HashMap<>();

    public static void reloadResourceTexture(String name, String path) {
        Log.log("Reload texture '" + name + "' '" + path + "'!");
        try {
            PNGDecoder decoder = new PNGDecoder(SpriteLoader.class.getResourceAsStream(path));

            //create a byte buffer big enough to store RGBA values
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

            //decode
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.RGBA);

            //flip the buffer so its ready to read
            buffer.flip();

            int id = textures.get(name);

            //bind the texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

            //tell opengl how to unpack bytes
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

            //set the texture parameters, can be GL_LINEAR or GL_NEAREST
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            //upload texture
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            // Generate Mip Map
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            loadedFiles.remove(name);
            loadedFiles.put(name, path);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadTexture(String name, String path) {
        Log.log("Reload texture '" + name + "' '" + path + "'!");
        try {
            PNGDecoder decoder = new PNGDecoder(new FileInputStream(new File(path)));

            //create a byte buffer big enough to store RGBA values
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

            //decode
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.RGBA);

            //flip the buffer so its ready to read
            buffer.flip();

            int id = textures.get(name);

            //bind the texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

            //tell opengl how to unpack bytes
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

            //set the texture parameters, can be GL_LINEAR or GL_NEAREST
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            //upload texture
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            // Generate Mip Map
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            loadedFiles.remove(name);
            loadedFiles.put(name, path);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static int loadTexture(InputStream stream, String name) {
        Log.log("Load texture '" + name + "'!");
        try {
            PNGDecoder decoder = new PNGDecoder(stream);

            //create a byte buffer big enough to store RGBA values
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

            //decode
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.RGBA);

            //flip the buffer so its ready to read
            buffer.flip();

            int id = GL11.glGenTextures();

            //bind the texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

            //tell opengl how to unpack bytes
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

            //set the texture parameters, can be GL_LINEAR or GL_NEAREST
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            //upload texture
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            // Generate Mip Map
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            textures.put(name, id);
            loadedFiles.put(name, null);
            return id;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int loadResourceTexture(String path, String name) {
        Log.log("Load texture '" + path + "'!");
            try {
                PNGDecoder decoder = new PNGDecoder(SpriteLoader.class.getResourceAsStream(path));

                //create a byte buffer big enough to store RGBA values
                ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

                //decode
                decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.RGBA);

                //flip the buffer so its ready to read
                buffer.flip();

                int id = GL11.glGenTextures();

                //bind the texture
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

                //tell opengl how to unpack bytes
                GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

                //set the texture parameters, can be GL_LINEAR or GL_NEAREST
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                //upload texture
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

                // Generate Mip Map
                GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

                textures.put(name, id);
                loadedFiles.put(name, path);
                return id;
            } catch(IOException e) {
                e.printStackTrace();
            }
            return -1;
    }

    public static int loadTexture(String path, String name) {
        try {
            Log.log("Load texture '" + path + "'!");
            PNGDecoder decoder = new PNGDecoder(new FileInputStream(new File(path)));

            //create a byte buffer big enough to store RGBA values
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

            //decode
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.RGBA);

            //flip the buffer so its ready to read
            buffer.flip();

            //create a texture
            int id = GL11.glGenTextures();

            //bind the texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

            //tell opengl how to unpack bytes
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

            //set the texture parameters, can be GL_LINEAR or GL_NEAREST
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            //upload texture
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            // Generate Mip Map
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            textures.put(name, id);
            loadedFiles.put(name, path);
            return id;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void unloadSprite(String name) {
        if(textures.containsKey(name)) {
            GL11.glDeleteTextures(textures.get(name));
            loadedFiles.remove(name);
        }
    }

    public static void cleanUP() {
        for(String s : textures.keySet()) {
            int id = textures.get(s);
            GL11.glDeleteTextures(id);
        }
        loadedFiles.clear();
    }

}
