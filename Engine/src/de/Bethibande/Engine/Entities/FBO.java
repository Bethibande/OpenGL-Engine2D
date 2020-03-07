package de.Bethibande.Engine.Entities;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class FBO {

    @Getter
    @Setter
    private int fboID;
    @Getter
    @Setter
    private int colorTexuteID;

    public FBO(int width, int height) {
        int framebufferID = GL30.glGenFramebuffers();
        int colorTextureID = GL11.glGenTextures();
        this.fboID = framebufferID;
        this.colorTexuteID = colorTextureID;
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferID);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER,GL30.GL_COLOR_ATTACHMENT0,GL11.GL_TEXTURE_2D, colorTextureID, 0);
        GL30.glBindFramebuffer(0, 0);
    }

}
