package de.Bethibande.Engine.Rendering;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.Entities.RawModel;
import de.Bethibande.Engine.Rendering.shaders.DefaultShader;
import de.Bethibande.Engine.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

public class DefaultRenderer {

    private DefaultShader shader;

    private RawModel quad;

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
    }

    public void render(List<GameObject2D> objs) {
        shader.start();

        shader.loadLights();

        shader.loadViewMatrix(EngineCore.cam);
        GL30.glBindVertexArray(quad.getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        for(GameObject2D obj : objs) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, obj.getModel().getId());

            shader.loadTransformatiobMatrix(Maths.createTransformationMatrix(obj));

            GL11.glDrawElements(GL11.GL_TRIANGLES, quad.getVertices(), GL11.GL_UNSIGNED_INT, 0);

        }
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        //GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        shader.start();
        shader.loadProjectionMatrix(matrix);
        shader.stop();
    }

}
