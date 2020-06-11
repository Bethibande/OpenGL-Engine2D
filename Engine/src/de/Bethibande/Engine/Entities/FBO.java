package de.Bethibande.Engine.Entities;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

public class FBO {

    @Getter
    @Setter
    private int fboID;
    @Getter
    @Setter
    private int colorTextureID;
    @Getter
    @Setter
    private int depthRenderBufferID;
    @Getter
    private final int width;
    @Getter
    private final int height;

    public FBO(int width, int height) {
        this.width = width;
        this.height = height;

        fboID = EXTFramebufferObject.glGenFramebuffersEXT();
        colorTextureID = GL11.glGenTextures();
        depthRenderBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();

        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboID);

        // initialize color texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT,GL11.GL_TEXTURE_2D, colorTextureID, 0);


        // initialize depth renderbuffer
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferID);
        EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT,EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferID);

        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);

        int fboStatus = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        if (fboStatus != GL30.GL_FRAMEBUFFER_COMPLETE) {
            throw new AssertionError("Could not create FBO: " + fboStatus);
        }
        GL30.glBindFramebuffer(0, 0);
    }

}
