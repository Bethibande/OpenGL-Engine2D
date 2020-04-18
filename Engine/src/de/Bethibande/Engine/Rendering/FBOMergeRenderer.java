package de.Bethibande.Engine.Rendering;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.FBO;
import de.Bethibande.Engine.Entities.RawModel;
import de.Bethibande.Engine.Rendering.shaders.FBOMergeShader;
import de.Bethibande.Engine.Rendering.shaders.FinalShader;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

public class FBOMergeRenderer {

    // merges all fbos/layers into one fbo that will be rendered to the screen
    // this also fixes some flickering issues
    private FBOMergeShader shader;

    private RawModel quad;

    public FBOMergeRenderer() {
        float[] textureCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };
        float[] vertices = {
                -1.0f, 1.0f,
                -1.0f,-1.0f,
                1.0f,-1.0f,
                1.0f,1.0f
        };

        int[] indices = {
                0,1,3,
                3,1,2
        };
        quad = EngineCore.loader.loadToVAO(vertices, textureCoords, indices);
        shader = new FBOMergeShader();
    }

    public void prepare() {
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, MasterRenderer.getFinalFBO().getFboID());
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public void render(float index, FBO fbo) {
        shader.start();

        shader.setIndex(index);

        GL30.glBindVertexArray(quad.getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getColorTextureID());

        GL11.glDrawElements(GL11.GL_TRIANGLES, quad.getVertices(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        shader.stop();
    }

    /*public void loadProjectionMatrix(Matrix4f mat) {
        shader.start();
        shader.loadProjectioMatrix(mat);
        shader.stop();
    }*/

}
