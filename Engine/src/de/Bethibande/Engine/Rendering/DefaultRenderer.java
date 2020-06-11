package de.Bethibande.Engine.Rendering;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.FBO;
import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.Entities.ObjectComponent;
import de.Bethibande.Engine.Entities.RawModel;
import de.Bethibande.Engine.Rendering.shaders.DefaultShader;
import de.Bethibande.Engine.utils.Maths;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.*;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class DefaultRenderer {

    // this renderer renders the different layers to an fbo
    // it also applies lightning and shading to the layers

    private DefaultShader shader;

    private RawModel quad;

    @Getter
    @Setter
    private boolean wireframe = false;

    public DefaultRenderer() {
        this.shader = new DefaultShader();
        float[] textureCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };
        float[] vertices = {
                -0.5f,0.5f,   //V0
                -0.5f,-0.5f,  //V1
                0.5f,-0.5f,  //V2
                0.5f,0.5f    //V3
        };

        int[] indices = {
                0,1,3,
                3,1,2
        };
        quad = EngineCore.loader.loadToVAO(vertices, textureCoords, indices);
        screenSize = Display.getDesktopDisplayMode();
    }

    public FBO render(List<GameObject2D> objs, FBO f) {
        shader.start();
        //FBO f = new FBO(Display.getWidth(), Display.getHeight());
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, f.getFboID());
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, f.getFboID());
        GL11.glClearColor((float)EngineCore.cfg.clearColor.getRed()/255f, (float)EngineCore.cfg.clearColor.getGreen()/255f, (float)EngineCore.cfg.clearColor.getBlue()/255f, 0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        shader.loadZoom(EngineCore.cam.getZoom());

        if(wireframe) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        }

        //shader.loadLights();
        shader.setZIndex(10);

        shader.loadViewMatrix(EngineCore.cam);
        GL30.glBindVertexArray(quad.getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        shader.loadScreenClip();
        int lastTexture = -1;
        boolean cull = true;
        int i = 0;
        while(i < objs.size()) {
            GameObject2D obj = objs.get(i);
            if(obj.isVisible()) {
                int i2 = 0;
                while(i2 < obj.getComponents().size()) {
                    ObjectComponent oc = obj.getComponents().get(i2);
                    oc.update();
                    i2++;
                }
                if(obj.getCustomModel() != null) {
                    GL30.glBindVertexArray(obj.getCustomModel().getId());
                    GL20.glEnableVertexAttribArray(0);
                    GL20.glEnableVertexAttribArray(1);
                }
                if(obj.getModel().getId() != lastTexture) {
                    GL13.glActiveTexture(GL13.GL_TEXTURE0);
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, obj.getModel().getId());
                    lastTexture = obj.getModel().getId();
                }
                if(obj.isFlipped()) {
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    cull = false;
                }
                if(!cull && !obj.isFlipped()) {
                    GL11.glEnable(GL11.GL_CULL_FACE);
                    cull = true;
                }
                shader.loadTransformatiobMatrix(Maths.createTransformationMatrix(obj));

                if(obj.getCustomModel() == null) {
                    GL11.glDrawElements(GL11.GL_TRIANGLES, quad.getVertices(), GL11.GL_UNSIGNED_INT, 0);
                    GL30.glBindVertexArray(quad.getId());
                } else {
                    GL11.glDrawElements(GL11.GL_TRIANGLES, obj.getCustomModel().getVertices(), GL11.GL_UNSIGNED_INT, 0);
                }

            }
            i++;
        }
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        shader.stop();
        return f;
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GL11.glEnable(GL11.GL_DEPTH_TEST);
        //GL11.glDepthFunc(GL11.GL_LESS);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        //GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    private static DisplayMode screenSize;
    public boolean isOnScreen(GameObject2D obj) {
        Vector2f pos = obj.getPosition();
        Vector2f cPos = EngineCore.cam.getPosition();
        if(pos.x-cPos.x >= 0 && pos.y-cPos.y >= 0) {
            if(obj.getSize().x+pos.x-cPos.x <= screenSize.getWidth() && obj.getSize().y+pos.y-cPos.y <= screenSize.getHeight()) {
                return true;
            }
        }

        return false;
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        shader.start();
        shader.loadProjectionMatrix(matrix);
        shader.stop();
    }

}
